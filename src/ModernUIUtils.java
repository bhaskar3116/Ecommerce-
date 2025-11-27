import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Utility class for creating modern UI components with consistent styling
 */
public class ModernUIUtils {
    
    // Modern color scheme
    public static final Color PRIMARY_COLOR = new Color(37, 99, 235);
    public static final Color SECONDARY_COLOR = new Color(249, 250, 251);
    public static final Color ACCENT_COLOR = new Color(16, 185, 129);
    public static final Color DANGER_COLOR = new Color(239, 68, 68);
    public static final Color TEXT_COLOR = new Color(31, 41, 55);
    public static final Color BORDER_COLOR = new Color(229, 231, 235);
    
    /**
     * Creates a modern styled button with a default hover effect (darker).
     */
    public static JButton createModernButton(String text, Color backgroundColor, Color textColor) {
        return createModernButton(text, backgroundColor, textColor, backgroundColor.darker());
    }

    /**
     * Creates a modern styled button with a specified hover effect.
     */
    public static JButton createModernButton(String text, Color backgroundColor, Color textColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        
        // --- THIS IS THE FIX ---
        // This tells the button to use our background color
        // instead of the default system look and feel's background.
        button.setBorderPainted(false);
        // --- END OF FIX ---

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }
    
    /**
     * Creates a modern styled text field.
     */
    public static JTextField createModernTextField(String placeholder) {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(10, 15, 10, 15)
        ));
        textField.setBackground(Color.WHITE);
        
        if (placeholder != null && !placeholder.isEmpty()) {
            textField.setForeground(Color.GRAY);
            textField.setText(placeholder);
            
            textField.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (textField.getText().equals(placeholder)) {
                        textField.setText("");
                        textField.setForeground(TEXT_COLOR);
                    }
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (textField.getText().isEmpty()) {
                        textField.setForeground(Color.GRAY);
                        textField.setText(placeholder);
                    }
                }
            });
        }
        
        return textField;
    }

    /**
     * Creates a modern styled password field with placeholder text.
     */
    public static JPasswordField createModernPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(10, 15, 10, 15)
        ));
        passwordField.setBackground(Color.WHITE);

        if (placeholder != null && !placeholder.isEmpty()) {
            passwordField.setForeground(Color.GRAY);
            passwordField.setEchoChar((char) 0); // Show placeholder text
            passwordField.setText(placeholder);
            
            passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                        passwordField.setText("");
                        passwordField.setEchoChar('•'); // Mask text with dots
                        passwordField.setForeground(TEXT_COLOR);
                    }
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                        passwordField.setForeground(Color.GRAY);
                        passwordField.setEchoChar((char) 0); // Show placeholder
                        passwordField.setText(placeholder);
                    }
                }
            });
        }
        return passwordField;
    }
    
    /**
     * Creates a modern styled label.
     */
    public static JLabel createModernLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        if (font != null) label.setFont(font);
        if (color != null) label.setForeground(color);
        return label;
    }
    
    /**
     * Creates a modern styled panel with sharp corners.
     */
    public static JPanel createModernPanel(Color backgroundColor) {
        JPanel panel = new JPanel();
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        return panel;
    }

    /**
     * Creates a modern styled panel with rounded corners.
     */
    public static JPanel createRoundedPanel(Color backgroundColor, int cornerRadius) {
        JPanel panel = new JPanel();
        panel.setBackground(backgroundColor);
        panel.setOpaque(false); // Required for custom border painting
        panel.setBorder(new RoundedBorder(cornerRadius, BORDER_COLOR, 1)); 
        return panel;
    }
    
    /**
     * Creates a standard separator line with a modern color.
     */
    public static JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(BORDER_COLOR);
        separator.setBackground(Color.WHITE);
        return separator;
    }
}