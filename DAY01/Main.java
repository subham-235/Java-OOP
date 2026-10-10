
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Random;

public class Main extends JFrame {

    public Main() {
        setTitle("✨ Dream World");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new DreamPanel());
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}

class DreamPanel extends JPanel implements ActionListener {

    private final Timer timer;
    private final Random random = new Random();

    private final int COUNT = 90;
    private final double[] x = new double[COUNT];
    private final double[] y = new double[COUNT];
    private final double[] speed = new double[COUNT];
    private final int[] radius = new int[COUNT];

    private double angle = 0;
    private int hue = 0;

    private final JButton explore = new JButton("EXPLORE ✦");
    private final JButton exit = new JButton("EXIT");

    DreamPanel() {
        setLayout(null);

        for (int i = 0; i < COUNT; i++) {
            x[i] = random.nextInt(900);
            y[i] = random.nextInt(600);
            speed[i] = 0.5 + random.nextDouble() * 2;
            radius[i] = 2 + random.nextInt(4);
        }

        styleButton(explore);
        styleButton(exit);

        explore.setBounds(325, 405, 140, 45);
        exit.setBounds(480, 405, 100, 45);

        explore.addActionListener(e ->
            JOptionPane.showMessageDialog(
                this,
                "Welcome to your Dream World! ✨",
                "A Little Magic",
                JOptionPane.INFORMATION_MESSAGE
            )
        );

        exit.addActionListener(e ->
            System.exit(0)
        );

        add(explore);
        add(exit);

        timer = new Timer(30, this);
        timer.start();
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 45, 130));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(
            new Color(190, 140, 255), 2
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        angle += 0.035;
        hue = (hue + 1) % 360;

        for (int i = 0; i < COUNT; i++) {
            y[i] -= speed[i];
            x[i] += Math.sin(angle + i) * 0.7;

            if (y[i] < -10) {
                y[i] = getHeight() + 10;
                x[i] = random.nextInt(Math.max(1, getWidth()));
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        int w = getWidth();
        int h = getHeight();

        // Beautiful gradient background
        GradientPaint background = new GradientPaint(
            0, 0, new Color(12, 8, 40),
            w, h, new Color(55, 15, 100)
        );

        g2.setPaint(background);
        g2.fillRect(0, 0, w, h);

        // Glowing moon
        int moonX = w / 2 - 45;
        int moonY = 75;

        for (int i = 5; i >= 1; i--) {
            g2.setColor(new Color(170, 120, 255, 12 * i));
            g2.fillOval(
                moonX - i * 12, moonY - i * 12,
                90 + i * 24, 90 + i * 24
            );
        }

        g2.setColor(new Color(245, 235, 255));
        g2.fillOval(moonX, moonY, 90, 90);

        // Animated stars
        for (int i = 0; i < COUNT; i++) {
            int alpha = 100 + (int) (
                155 * Math.abs(Math.sin(angle * 2 + i))
            );

            g2.setColor(new Color(210, 190, 255, alpha));
            g2.fillOval(
                (int) x[i], (int) y[i],
                radius[i], radius[i]
            );
        }

        // Animated decorative rings
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(
            160, 100, 255, 100
        ));

        g2.drawOval(
            w / 2 - 200, 160,
            400, 180
        );

        Graphics2D ring = (Graphics2D) g2.create();
        ring.rotate(angle, w / 2.0, 250);

        ring.setColor(new Color(255, 150, 240, 180));
        ring.drawOval(w / 2 - 200, 160, 400, 180);

        ring.setColor(new Color(120, 220, 255, 220));
        ring.fillOval(w / 2 + 180, 245, 12, 12);
        ring.dispose();

        // Animated title
        float pulse = (float) (
            0.65 + 0.35 * Math.sin(angle * 2)
        );

        g2.setFont(new Font("Serif", Font.BOLD, 54));

        String title = "DREAM WORLD";
        FontMetrics fm = g2.getFontMetrics();
        int titleX = (w - fm.stringWidth(title)) / 2;
        int titleY = 305;

        g2.setColor(new Color(
            160, 100, 255,
            (int) (100 * pulse)
        ));

        g2.drawString(title, titleX - 3, titleY - 3);

        g2.setColor(new Color(
            245, 230, 255
        ));

        g2.drawString(title, titleX, titleY);

        // Subtitle
        g2.setFont(new Font("SansSerif", Font.PLAIN, 17));
        String subtitle = "Where imagination becomes reality";
        fm = g2.getFontMetrics();

        g2.setColor(new Color(210, 195, 245));
        g2.drawString(
            subtitle,
            (w - fm.stringWidth(subtitle)) / 2,
            350
        );

        // Footer
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2.setColor(new Color(180, 160, 220));

        String footer = "✦  MADE WITH JAVA SWING  ✦";
        fm = g2.getFontMetrics();

        g2.drawString(
            footer,
            (w - fm.stringWidth(footer)) / 2,
            520
        );

        g2.dispose();
    }
}

