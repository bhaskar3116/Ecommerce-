import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminLogin extends JDialog {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    private JTextField usernameField;
    private JPasswordField passwordField;

    public AdminLogin() {
        setTitle("Admin Login");
        setSize(400, 320); // Increased height for better spacing
        setLocationRelativeTo(null); // Center the dialog
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Use a modern panel as the main container
        JPanel mainPanel = ModernUIUtils.createModernPanel(ModernUIUtils.SECONDARY_COLOR);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(25, 35, 25, 35));

        // --- UI Components using ModernUIUtils ---

        // 1. Title Label
        JLabel titleLabel = ModernUIUtils.createModernLabel("Administrator Access", new Font("Segoe UI", Font.BOLD, 22), ModernUIUtils.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 2. Username Field with placeholder
        usernameField = ModernUIUtils.createModernTextField("Enter admin username");
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // 3. Password Field with placeholder
        passwordField = ModernUIUtils.createModernPasswordField("Enter admin password");
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // 4. Modern Login Button
        JButton loginButton = ModernUIUtils.createModernButton("Login Securely", ModernUIUtils.PRIMARY_COLOR, Color.WHITE);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMinimumSize(new Dimension(0, 45));

        // --- Action Logic ---
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                dispose();
                new AdminDashboard(); 
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText(""); // Clear password field on failure
            }
        });
        
        // --- Assemble the Panel ---
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(25)); // Spacing
        mainPanel.add(usernameField);
        mainPanel.add(Box.createVerticalStrut(15)); // Spacing
        mainPanel.add(passwordField);
        mainPanel.add(Box.createVerticalStrut(25)); // Spacing
        mainPanel.add(loginButton);

        setContentPane(mainPanel);
        setVisible(true);
    }
}