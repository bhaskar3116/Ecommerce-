import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Main {
    // This static variable will hold the running instance of the ShopApp
    public static ShopApp shopInstance;

    public static void main(String[] args) {
        // --- NEW: Set System Look and Feel ---
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            UserManager.initialize();
            
            // --- ADDED THIS LINE ---
            // This runs the cleanup task every time the app starts.
            DBManager.cleanupOldOrders();
            // --- END ADD ---
            
            showRoleSelector();
        });
    }

    private static void showRoleSelector() {
        JFrame frame = new JFrame("Select Role");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 300); // Slightly larger for better spacing
        frame.setLocationRelativeTo(null);
        frame.setResizable(false); 

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 50, 40, 50)); 
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Select Your Role");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); 
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(50, 50, 50));

        JButton userBtn = new JButton("Login as User");
        JButton adminBtn = new JButton("Login as Admin");

        Color userColor = new Color(0, 123, 255); 
        Color userHover = new Color(0, 105, 217);
        Color adminColor = new Color(108, 117, 125);
        Color adminHover = new Color(88, 97, 105); 

        styleButton(userBtn, userColor, userHover);
        styleButton(adminBtn, adminColor, adminHover);

        userBtn.addActionListener(e -> {
            frame.dispose();
            new LoginPage();
        });

        adminBtn.addActionListener(e -> {
            frame.dispose();
            new AdminLogin(); 
        });

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(userBtn);
        panel.add(Box.createVerticalStrut(15));
        panel.add(adminBtn);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    private static void styleButton(JButton button, Color baseColor, Color hoverColor) {
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14)); 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        Dimension size = new Dimension(220, 45); 
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setMinimumSize(size);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(baseColor);
            }
        });
    }

    public static void launchApp(User user) {
        if (shopInstance == null) {
            shopInstance = new ShopApp(user);
        }
        shopInstance.frame.setVisible(true);
    }
}