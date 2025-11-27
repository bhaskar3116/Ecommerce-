import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPage extends JFrame {

    public LoginPage() {
        setTitle("ShopEasy Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- Main Panel ---
        JPanel mainPanel = new JPanel(new BorderLayout());
        // --- UPDATED: Set background to match Main.java for consistency ---
        mainPanel.setBackground(Color.WHITE); 
        mainPanel.setBorder(new EmptyBorder(30, 50, 40, 50));

        // --- Header Panel ---
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel iconLabel = new JLabel("👤", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Welcome to ShopEasy", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(10, 0, 30, 0));

        headerPanel.add(iconLabel);
        headerPanel.add(titleLabel);

        // --- Form Panel ---
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Border fieldBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 12, 8, 12)
        );

        // Username components
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(labelFont);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField userField = new JTextField();
        userField.setBorder(fieldBorder);
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userField.setAlignmentX(Component.LEFT_ALIGNMENT);
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, userField.getPreferredSize().height));

        // --- Password components (UPDATED) ---
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(labelFont);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPasswordField passField = new JPasswordField();
        passField.setBorder(fieldBorder);
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, passField.getPreferredSize().height));
        
        // ** NEW: Show/Hide Password Button **
        JToggleButton showHideButton = new JToggleButton("👁️");
        showHideButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        showHideButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        showHideButton.setFocusPainted(false);
        showHideButton.setContentAreaFilled(false);
        showHideButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Action listener to toggle password visibility
        showHideButton.addActionListener(e -> {
            if (showHideButton.isSelected()) {
                passField.setEchoChar((char) 0); // Show password
            } else {
                passField.setEchoChar('•'); // Hide password
            }
        });

        // Panel to hold password field and show/hide button together
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);
        passwordPanel.add(passField, BorderLayout.CENTER);
        passwordPanel.add(showHideButton, BorderLayout.EAST);
        passwordPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, passField.getPreferredSize().height));
        

        formPanel.add(userLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(userField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordPanel); // Add the combined password panel


        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        // --- UPDATED: Using the same modern colors from Main.java ---
        JButton loginButton = createStyledButton("Login", new Color(0, 123, 255), Color.WHITE);
        JButton registerButton = createStyledButton("Register", new Color(108, 117, 125), Color.WHITE);
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // --- Assemble Main Panel ---
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Final Setup ---
        add(mainPanel);
        getRootPane().setDefaultButton(loginButton);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            authenticate(username, password);
        });

        registerButton.addActionListener(e -> {
            new RegisterPage();
            dispose();
        });

        setVisible(true);
    }

    private void authenticate(String username, String password) {
        User authenticatedUser = UserManager.authenticate(username, password);
        if (authenticatedUser != null) {
            dispose();
            Main.launchApp(authenticatedUser); // --- Use the Main.launchApp method ---
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper method to create styled buttons
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 0, 12, 0));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- THIS IS THE FIX ---
        // This line tells the button not to draw the system's L&F border,
        // which allows our setBackground() color to be visible.
        button.setBorderPainted(false);
        // --- END OF FIX ---

        Color hoverColor = bgColor.darker();
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }
}