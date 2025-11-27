import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class PaymentSuccessDialog extends JDialog {

    // Changed parameter from BigDecimal to double for compatibility
    public PaymentSuccessDialog(JFrame parent, String orderId, double total) {
        super(parent, true); // keep it modal
        setUndecorated(true);
        setSize(400, 200);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel icon = new JLabel("✔", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI", Font.BOLD, 48));
        icon.setForeground(new Color(16, 185, 129));

        JLabel title = new JLabel("Payment Successful!", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(16, 185, 129));

        JLabel order = new JLabel("Order ID: " + orderId, SwingConstants.CENTER);
        order.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Convert double to BigDecimal for proper formatting
        BigDecimal totalDecimal = BigDecimal.valueOf(total);
        JLabel amount = new JLabel(
            "Total: Rs " + totalDecimal.setScale(2, RoundingMode.HALF_UP).toPlainString(),
            SwingConstants.CENTER
        );
        amount.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JPanel center = new JPanel(new GridLayout(3, 1, 6, 6));
        center.setOpaque(false);
        center.add(title);
        center.add(order);
        center.add(amount);

        panel.add(icon, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        setContentPane(panel);
    }

    /** Auto-close after X seconds (no visible progress/timer). */
    public void showWithAutoClose(int seconds) {
        Timer t = new Timer(seconds * 1000, e -> dispose());
        t.setRepeats(false);
        t.start();
        setVisible(true);
    }
}