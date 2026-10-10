import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {
        new Addition();
    }
}

class Addition extends JFrame implements ActionListener {

    JTextField t1, t2;
    JButton b1;
    JLabel l1, l2, l3;

    public Addition() {

        t1 = new JTextField(10);
        l1 = new JLabel("Enter First Number");

        t2 = new JTextField(10);
        l2 = new JLabel("Enter Second Number");

        b1 = new JButton("Add");
        l3 = new JLabel("Result");

        setLayout(new FlowLayout());

        add(l1);
        add(t1);
        add(l2);
        add(t2);
        add(b1);
        add(l3);

        b1.addActionListener(this);

        setSize(400, 200);
        setTitle("Addition Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            int num1 = Integer.parseInt(t1.getText());
            int num2 = Integer.parseInt(t2.getText());

            int res = num1 + num2;

            l3.setText("Result is " + res);
        } catch (NumberFormatException ex) {
            l3.setText("Please enter valid integers!");
        }
    }
}