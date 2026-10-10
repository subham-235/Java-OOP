import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

/**
 * Animated 3D-style landing page built with pure Java Swing.
 * Features: parallax starfield, shaded rotating cube (drag to spin),
 * counter-rotating wireframe, orbiting particle ring, tilting glass cards.
 *
 * Compile: javac Swing3DPage.java
 * Run:     java Swing3DPage
 */
public class Swing3DPage extends JPanel implements ActionListener {

    // ---------- animation state ----------
    private final Timer timer = new Timer(16, this);
    private double time = 0;
    private double angX = 0.5, angY = 0.6, angZ = 0;
    private double velX = 0.004, velY = 0.009;
    private int mouseX = -1000, mouseY = -1000;
    private Point lastDrag;
    private boolean dragging = false;

    // ---------- scene data ----------
    private static final int STAR_COUNT = 260;
    private final double[] sx = new double[STAR_COUNT];
    private final double[] sy = new double[STAR_COUNT];
    private final double[] sz = new double[STAR_COUNT];
    private final Random rnd = new Random(7);

    private static final int RING_POINTS = 140;

    private static class Face {
        double[][] v = new double[4][3];
        double[] n = new double[3];
        float hue;
    }
    private final List<Face> cubeFaces = new ArrayList<>();

    // ---------- UI state ----------
    private final String[] navItems = {"Home", "Features", "Docs", "Contact"};
    private final Rectangle[] navRects = new Rectangle[navItems.length];
    private final float[] navHover = new float[navItems.length];
    private final Rectangle[] cardRects = new Rectangle[3];
    private final float[] cardHover = new float[3];
    private Rectangle buttonRect = new Rectangle();
    private float buttonHover = 0f;

    private final String[][] cards = {
        {"Swing Core", "Lightweight components, painted your way."},
        {"Java2D Magic", "Gradients, transforms and smooth antialiasing."},
        {"Pure 3D Math", "Rotation matrices and perspective projection."}
    };
    private final float[] cardHues = {0.55f, 0.75f, 0.92f};

    public Swing3DPage() {
        setPreferredSize(new Dimension(1100, 680));
        setBackground(Color.BLACK);
        for (int i = 0; i < STAR_COUNT; i++) resetStar(i, true);
        buildCube();

        MouseAdapter ma = new MouseAdapter() {
            @Override public void mouseMoved(MouseEvent e) { mouseX = e.getX(); mouseY = e.getY(); updateCursor(); }
            @Override public void mouseExited(MouseEvent e) { mouseX = mouseY = -1000; }
            @Override public void mousePressed(MouseEvent e) {
                lastDrag = e.getPoint();
                if (buttonRect.contains(e.getPoint())) {
                    JOptionPane.showMessageDialog(Swing3DPage.this,
                        "Welcome to the third dimension!", "Get Started",
                        JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                dragging = true;
            }
            @Override public void mouseReleased(MouseEvent e) { dragging = false; }
            @Override public void mouseDragged(MouseEvent e) {
                mouseX = e.getX(); mouseY = e.getY();
                if (dragging && lastDrag != null) {
                    velY = (e.getX() - lastDrag.x) * 0.004;
                    velX = -(e.getY() - lastDrag.y) * 0.004;
                    lastDrag = e.getPoint();
                }
            }
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
        timer.start();
    }

    private void updateCursor() {
        boolean hand = buttonRect.contains(mouseX, mouseY);
        for (Rectangle r : navRects) if (r != null && r.contains(mouseX, mouseY)) hand = true;
        setCursor(Cursor.getPredefinedCursor(hand ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
    }

    private void resetStar(int i, boolean randomDepth) {
        sx[i] = (rnd.nextDouble() - 0.5) * 2200;
        sy[i] = (rnd.nextDouble() - 0.5) * 1400;
        sz[i] = randomDepth ? rnd.nextDouble() * 1000 + 20 : 1000;
    }

    private void buildCube() {
        float[] hues = {0.58f, 0.66f, 0.74f, 0.82f, 0.90f, 0.50f};
        int idx = 0;
        for (int axis = 0; axis < 3; axis++) {
            for (int sign = -1; sign <= 1; sign += 2) {
                Face f = new Face();
                int u = (axis + 1) % 3, w = (axis + 2) % 3;
                double[][] uv = {{-1, -1}, {1, -1}, {1, 1}, {-1, 1}};
                for (int k = 0; k < 4; k++) {
                    f.v[k][axis] = sign;
                    f.v[k][u] = uv[k][0];
                    f.v[k][w] = uv[k][1];
                }
                f.n[axis] = sign;
                f.hue = hues[idx++];
                cubeFaces.add(f);
            }
        }
    }

    // ---------- 3D math ----------
    private double[] rotate(double x, double y, double z, double ax, double ay, double az) {
        double c = Math.cos(ay), s = Math.sin(ay);
        double x1 = x * c + z * s, z1 = -x * s + z * c;
        c = Math.cos(ax); s = Math.sin(ax);
        double y1 = y * c - z1 * s, z2 = y * s + z1 * c;
        c = Math.cos(az); s = Math.sin(az);
        double x2 = x1 * c - y1 * s, y2 = x1 * s + y1 * c;
        return new double[]{x2, y2, z2};
    }

    private static final double CAM = 520;

    private double[] project(double[] p, double cx, double cy) {
        double f = CAM / (CAM + p[2]);
        return new double[]{cx + p[0] * f, cy + p[1] * f, f};
    }

    // ---------- loop ----------
    @Override public void actionPerformed(ActionEvent e) {
        time += 0.016;
        angX += velX; angY += velY; angZ += 0.002;
        if (!dragging) {
            velX += (0.004 - velX) * 0.03;
            velY += (0.009 - velY) * 0.03;
        }
        for (int i = 0; i < STAR_COUNT; i++) {
            sz[i] -= 2.2;
            if (sz[i] < 20) resetStar(i, false);
        }
        buttonHover += ((buttonRect.contains(mouseX, mouseY) ? 1f : 0f) - buttonHover) * 0.2f;
        for (int i = 0; i < navRects.length; i++) {
            float t = (navRects[i] != null && navRects[i].contains(mouseX, mouseY)) ? 1f : 0f;
            navHover[i] += (t - navHover[i]) * 0.2f;
        }
        for (int i = 0; i < cardRects.length; i++) {
            float t = (cardRects[i] != null && cardRects[i].contains(mouseX, mouseY)) ? 1f : 0f;
            cardHover[i] += (t - cardHover[i]) * 0.15f;
        }
        repaint();
    }

    // ---------- painting ----------
    @Override protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        int w = getWidth(), h = getHeight();

        paintBackground(g, w, h);
        paintStars(g, w, h);
        paintNav(g, w);
        paintHero(g, w, h);
        paintScene(g, (int) (w * 0.72), (int) (h * 0.42));
        paintCards(g, w, h);
        g.dispose();
    }

    private void paintBackground(Graphics2D g, int w, int h) {
        float shift = (float) (Math.sin(time * 0.2) * 0.04);
        Color top = Color.getHSBColor(0.68f + shift, 0.75f, 0.14f);
        Color bot = Color.getHSBColor(0.78f - shift, 0.85f, 0.30f);
        g.setPaint(new GradientPaint(0, 0, top, w, h, bot));
        g.fillRect(0, 0, w, h);

        float r = Math.max(w, h) * 0.55f;
        float gx = (float) (w * 0.72 + Math.cos(time * 0.4) * 40);
        float gy = (float) (h * 0.42 + Math.sin(time * 0.5) * 30);
        g.setPaint(new RadialGradientPaint(gx, gy, r, new float[]{0f, 1f},
            new Color[]{new Color(120, 80, 255, 90), new Color(120, 80, 255, 0)}));
        g.fillRect(0, 0, w, h);
    }

    private void paintStars(Graphics2D g, int w, int h) {
        double cx = w / 2.0 + (mouseX > -500 ? (w / 2.0 - mouseX) * 0.04 : 0);
        double cy = h / 2.0 + (mouseY > -500 ? (h / 2.0 - mouseY) * 0.04 : 0);
        for (int i = 0; i < STAR_COUNT; i++) {
            double f = 420 / sz[i];
            double x = cx + sx[i] * f * 0.5, y = cy + sy[i] * f * 0.5;
            if (x < 0 || x > w || y < 0 || y > h) continue;
            float depth = (float) Math.min(1.0, 1.0 - sz[i] / 1000.0);
            int a = (int) (60 + 190 * depth);
            double size = 0.6 + depth * 2.4;
            g.setColor(new Color(200, 215, 255, Math.min(255, a)));
            g.fill(new Ellipse2D.Double(x - size / 2, y - size / 2, size, size));
        }
    }

    private void paintNav(Graphics2D g, int w) {
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        g.setPaint(new GradientPaint(30, 0, new Color(120, 200, 255), 150, 0, new Color(200, 120, 255)));
        g.drawString("\u2615 SwingFX", 32, 52);

        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        FontMetrics fm = g.getFontMetrics();
        int x = w - 32;
        for (int i = navItems.length - 1; i >= 0; i--) {
            int tw = fm.stringWidth(navItems[i]);
            x -= tw;
            navRects[i] = new Rectangle(x - 8, 28, tw + 16, 34);
            int alpha = (int) (170 + 85 * navHover[i]);
            g.setColor(new Color(235, 240, 255, alpha));
            g.drawString(navItems[i], x, 52);
            g.setColor(new Color(160, 130, 255, (int) (255 * navHover[i])));
            g.fillRoundRect(x, 58, (int) (tw * navHover[i]), 2, 2, 2);
            x -= 34;
        }
    }

    private void paintHero(Graphics2D g, int w, int h) {
        int x = 60, y = (int) (h * 0.30);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 58));
        g.setPaint(Color.WHITE);
        g.drawString("Build in", x, y);
        g.setPaint(new GradientPaint(x, y, new Color(90, 200, 255),
            x + 380, y + 70, new Color(220, 110, 255)));
        g.drawString("Three Dimensions", x, y + 68);

        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 17));
        g.setColor(new Color(200, 210, 235, 210));
        g.drawString("A fully animated 3D-style interface drawn with", x, y + 115);
        g.drawString("nothing but Java Swing and Java2D. Drag the cube!", x, y + 140);

        // button
        int bw = 180, bh = 52, by = y + 175;
        buttonRect = new Rectangle(x, by, bw, bh);
        float pulse = (float) (0.5 + 0.5 * Math.sin(time * 3));
        int glowA = (int) (50 + 70 * buttonHover + 20 * pulse);
        for (int i = 4; i >= 1; i--) {
            g.setColor(new Color(140, 100, 255, glowA / (i + 1)));
            g.fillRoundRect(x - i * 3, by - i * 3, bw + i * 6, bh + i * 6, 30 + i * 3, 30 + i * 3);
        }
        g.setPaint(new GradientPaint(x, by, new Color(90, 160, 255), x + bw, by + bh, new Color(190, 90, 255)));
        g.fillRoundRect(x, by - (int) (2 * buttonHover), bw, bh, 28, 28);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        String t = "Get Started  \u2192";
        g.drawString(t, x + (bw - fm.stringWidth(t)) / 2, by + 32 - (int) (2 * buttonHover));
    }

    private void paintScene(Graphics2D g, int cx, int cy) {
        // ground glow / shadow
        float pulse = (float) (0.85 + 0.15 * Math.sin(time * 2));
        g.setPaint(new RadialGradientPaint(cx, cy + 200, 150 * pulse, new float[]{0f, 1f},
            new Color[]{new Color(130, 90, 255, 110), new Color(130, 90, 255, 0)}));
        g.fill(new Ellipse2D.Double(cx - 170, cy + 175, 340, 50));

        double bob = Math.sin(time * 1.5) * 8;
        double ccy = cy + bob;

        // orbit ring: back half, cube, front half
        List<double[]> ring = new ArrayList<>();
        for (int i = 0; i < RING_POINTS; i++) {
            double a = i * Math.PI * 2 / RING_POINTS + time * 0.8;
            double[] p = rotate(Math.cos(a) * 210, 0, Math.sin(a) * 210, 0.9, time * 0.3, 0.3);
            ring.add(p);
        }
        drawRing(g, ring, cx, ccy, true);

        // outer shaded cube
        drawCube(g, cx, ccy, 105, angX, angY, angZ, true);
        // inner wireframe cube (counter-rotating)
        drawCube(g, cx, ccy, 52, -angX * 1.6, -angY * 1.6, angZ, false);

        drawRing(g, ring, cx, ccy, false);
    }

    private void drawRing(Graphics2D g, List<double[]> ring, int cx, double cy, boolean back) {
        for (int i = 0; i < ring.size(); i++) {
            double[] p = ring.get(i);
            if ((p[2] > 0) != back) continue;
            double[] s = project(p, cx, cy);
            double size = 3 + 5 * s[2] * s[2];
            float hue = (float) ((i / (double) ring.size() + time * 0.1) % 1.0);
            Color c = Color.getHSBColor(0.5f + hue * 0.4f, 0.6f, 1f);
            g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 60));
            g.fill(new Ellipse2D.Double(s[0] - size, s[1] - size, size * 2, size * 2));
            g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 230));
            g.fill(new Ellipse2D.Double(s[0] - size / 2, s[1] - size / 2, size, size));
        }
    }

    private void drawCube(Graphics2D g, double cx, double cy, double size,
                          double ax, double ay, double az, boolean solid) {
        double[] light = {-0.4, -0.7, -0.6};
        double ll = Math.sqrt(light[0] * light[0] + light[1] * light[1] + light[2] * light[2]);
        for (int i = 0; i < 3; i++) light[i] /= ll;

        class Projected { Path2D path; double depth; float hue; double lit; }
        List<Projected> list = new ArrayList<>();

        for (Face f : cubeFaces) {
            double[] n = rotate(f.n[0], f.n[1], f.n[2], ax, ay, az);
            if (solid && n[2] >= 0) continue; // back-face culling
            Projected pr = new Projected();
            pr.path = new Path2D.Double();
            double zsum = 0;
            for (int k = 0; k < 4; k++) {
                double[] r = rotate(f.v[k][0] * size, f.v[k][1] * size, f.v[k][2] * size, ax, ay, az);
                double[] s = project(r, cx, cy);
                if (k == 0) pr.path.moveTo(s[0], s[1]); else pr.path.lineTo(s[0], s[1]);
                zsum += r[2];
            }
            pr.path.closePath();
            pr.depth = zsum / 4;
            pr.hue = f.hue;
            pr.lit = Math.max(0, -(n[0] * light[0] + n[1] * light[1] + n[2] * light[2]));
            list.add(pr);
        }
        list.sort((a, b) -> Double.compare(b.depth, a.depth));

        for (Projected p : list) {
            if (solid) {
                Color base = Color.getHSBColor((p.hue + (float) (time * 0.03)) % 1f, 0.65f,
                    (float) (0.35 + 0.65 * p.lit));
                Color c = new Color(base.getRed(), base.getGreen(), base.getBlue(), 215);
                g.setColor(c);
                g.fill(p.path);
                Rectangle2D b = p.path.getBounds2D();
                g.setPaint(new GradientPaint((float) b.getMinX(), (float) b.getMinY(),
                    new Color(255, 255, 255, (int) (70 * p.lit + 10)),
                    (float) b.getMaxX(), (float) b.getMaxY(), new Color(255, 255, 255, 0)));
                g.fill(p.path);
                g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.setColor(new Color(255, 255, 255, 170));
                g.draw(p.path);
            } else {
                g.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.setColor(new Color(120, 220, 255, 35));
                g.draw(p.path);
                g.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.setColor(new Color(170, 240, 255, 230));
                g.draw(p.path);
            }
        }
    }

    private void paintCards(Graphics2D g, int w, int h) {
        int cw = Math.min(320, (w - 120 - 2 * 24) / 3), ch = 120;
        int total = cw * 3 + 24 * 2;
        int startX = (w - total) / 2, y = h - ch - 40;

        for (int i = 0; i < 3; i++) {
            int x = startX + i * (cw + 24);
            cardRects[i] = new Rectangle(x, y, cw, ch);
            float hv = cardHover[i];
            double cxm = x + cw / 2.0, cym = y + ch / 2.0 - hv * 8;

            // tilt toward mouse
            double dx = Math.max(-1, Math.min(1, (mouseX - cxm) / (cw / 2.0)));
            double dy = Math.max(-1, Math.min(1, (mouseY - cym) / (ch / 2.0)));

            AffineTransform old = g.getTransform();
            AffineTransform at = new AffineTransform();
            at.translate(cxm, cym);
            double sc = 1 + 0.06 * hv;
            at.scale(sc, sc);
            at.shear(dx * 0.08 * hv, dy * 0.05 * hv);
            at.translate(-cxm, -cym);
            g.transform(at);

            // shadow
            for (int s = 6; s >= 1; s--) {
                g.setColor(new Color(0, 0, 0, (int) (14 + 10 * hv)));
                g.fillRoundRect(x - s, y + 8 - s + (int) (hv * 8), cw + 2 * s, ch + 2 * s, 26 + s, 26 + s);
            }
            // glass body
            Color tint = Color.getHSBColor(cardHues[i], 0.6f, 1f);
            g.setPaint(new GradientPaint(x, y, new Color(255, 255, 255, 45 + (int) (25 * hv)),
                x + cw, y + ch, new Color(tint.getRed(), tint.getGreen(), tint.getBlue(), 35)));
            g.fillRoundRect(x, y, cw, ch, 24, 24);
            g.setStroke(new BasicStroke(1.4f));
            g.setPaint(new GradientPaint(x, y, new Color(255, 255, 255, 140),
                x + cw, y + ch, new Color(tint.getRed(), tint.getGreen(), tint.getBlue(), 60)));
            g.drawRoundRect(x, y, cw, ch, 24, 24);

            // icon dot
            g.setPaint(new GradientPaint(x + 22, y + 22, tint, x + 52, y + 52, Color.getHSBColor(cardHues[i] + 0.12f, 0.7f, 1f)));
            g.fill(new Ellipse2D.Double(x + 22, y + 22, 30, 30));

            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
            g.setColor(Color.WHITE);
            g.drawString(cards[i][0], x + 66, y + 44);
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
            g.setColor(new Color(210, 218, 240, 215));
            drawWrapped(g, cards[i][1], x + 22, y + 78, cw - 44);

            g.setTransform(old);
        }
    }

    private void drawWrapped(Graphics2D g, String text, int x, int y, int maxW) {
        FontMetrics fm = g.getFontMetrics();
        StringBuilder line = new StringBuilder();
        for (String word : text.split(" ")) {
            String test = line.length() == 0 ? word : line + " " + word;
            if (fm.stringWidth(test) > maxW) {
                g.drawString(line.toString(), x, y);
                y += fm.getHeight();
                line = new StringBuilder(word);
            } else line = new StringBuilder(test);
        }
        if (line.length() > 0) g.drawString(line.toString(), x, y);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Swing 3D Page");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setContentPane(new Swing3DPage());
            f.pack();
            f.setMinimumSize(new Dimension(900, 600));
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}