import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterPage extends JFrame {

    public RegisterPage() {
        setTitle("Create a New Account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 500); // Increased height for better spacing
        setLocationRelativeTo(null);
        setResizable(false);

        // --- Main Panel ---
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        // --- Header ---
        JLabel titleLabel = new JLabel("Create Your Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));

        // --- Form Panel ---
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Border fieldBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 12, 8, 12)
        );

        // --- Username ---
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(labelFont);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField userField = new JTextField();
        userField.setBorder(fieldBorder);
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userField.setAlignmentX(Component.LEFT_ALIGNMENT);
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, userField.getPreferredSize().height));

        // --- Password ---
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(labelFont);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPasswordField passField = new JPasswordField();
        passField.setBorder(fieldBorder);
        
        JPanel passwordPanel = createPasswordPanel(passField);
        passwordPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // --- Confirm Password ---
        JLabel confirmPassLabel = new JLabel("Confirm Password");
        confirmPassLabel.setFont(labelFont);
        confirmPassLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPasswordField confirmPassField = new JPasswordField();
        confirmPassField.setBorder(fieldBorder);
        
        JPanel confirmPasswordPanel = createPasswordPanel(confirmPassField);
        confirmPasswordPanel.setAlignmentX(Component.LEFT_ALIGNMENT);


        formPanel.add(userLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(userField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordPanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(confirmPassLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(confirmPasswordPanel);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        JButton registerButton = createStyledButton("Create Account", new Color(0, 123, 255), Color.WHITE);
        JButton backToLoginButton = createStyledButton("Back to Login", new Color(108, 117, 125), Color.WHITE);
        
        buttonPanel.add(registerButton);
        buttonPanel.add(backToLoginButton);
        
        // --- Assemble Main Panel ---
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // --- Final Setup ---
        add(mainPanel);
        getRootPane().setDefaultButton(registerButton);

        registerButton.addActionListener(e -> {
            String username = userField.getText();
            String pass1 = new String(passField.getPassword());
            String pass2 = new String(confirmPassField.getPassword());

            if (username.trim().isEmpty() || pass1.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!pass1.equals(pass2)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (UserManager.register(username, pass1)) {
                JOptionPane.showMessageDialog(this, "Registration successful! Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                new LoginPage();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username '" + username + "' is already taken.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backToLoginButton.addActionListener(e -> {
            new LoginPage();
            dispose();
        });

        setVisible(true);
    }

    private JPanel createPasswordPanel(JPasswordField passwordField) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JToggleButton showHideButton = new JToggleButton("👁️");
        showHideButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        showHideButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        showHideButton.setFocusPainted(false);
        showHideButton.setContentAreaFilled(false);
        showHideButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        showHideButton.addActionListener(e -> {
            if (showHideButton.isSelected()) {
                passwordField.setEchoChar((char) 0); // Show password
            } else {
                passwordField.setEchoChar('•'); // Hide password
            }
        });

        panel.add(passwordField, BorderLayout.CENTER);
        panel.add(showHideButton, BorderLayout.EAST);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, passwordField.getPreferredSize().height));
        
        return panel;
    }

    // --- UPDATED METHOD ---
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        
        // --- THIS IS THE FIX ---
        // We set the padding with an EmptyBorder, but also tell
        // the button not to paint the default L&F border.
        button.setBorder(new EmptyBorder(12, 0, 12, 0));
        button.setBorderPainted(false);
        // --- END OF FIX ---
        
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

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