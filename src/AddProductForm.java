import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AddProductForm extends JDialog {

    private JTextField nameField;
    private JTextField priceField;
    private JTextField imagePathField;
    private JComboBox<String> categoryComboBox;

    public AddProductForm() {
        setTitle("Add New Product");
        setMinimumSize(new Dimension(450, 350));
        setLocationRelativeTo(null);
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(ModernUIUtils.SECONDARY_COLOR);

        JLabel titleLabel = ModernUIUtils.createModernLabel("New Product Details", new Font("Segoe UI", Font.BOLD, 20), ModernUIUtils.TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.2;
        fieldsPanel.add(ModernUIUtils.createModernLabel("Product Name:", new Font("Segoe UI", Font.PLAIN, 14), ModernUIUtils.TEXT_COLOR), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.8;
        nameField = ModernUIUtils.createModernTextField("e.g., Wireless Headphones");
        fieldsPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        fieldsPanel.add(ModernUIUtils.createModernLabel("Price (₹):", new Font("Segoe UI", Font.PLAIN, 14), ModernUIUtils.TEXT_COLOR), gbc);
        
        gbc.gridx = 1;
        priceField = ModernUIUtils.createModernTextField("e.g., 1999.00");
        fieldsPanel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        fieldsPanel.add(ModernUIUtils.createModernLabel("Image Path:", new Font("Segoe UI", Font.PLAIN, 14), ModernUIUtils.TEXT_COLOR), gbc);
        
        gbc.gridx = 1;
        imagePathField = ModernUIUtils.createModernTextField("e.g., https://m.media-amazon.com/images/I/some_image.jpg");
        fieldsPanel.add(imagePathField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        fieldsPanel.add(ModernUIUtils.createModernLabel("Category:", new Font("Segoe UI", Font.PLAIN, 14), ModernUIUtils.TEXT_COLOR), gbc);
        
        gbc.gridx = 1;
        String[] categories = {"All", "Mens", "Womens", "Shoes", "Electronics", "Health"};
        categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fieldsPanel.add(categoryComboBox, gbc);
        
        mainPanel.add(fieldsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton saveButton = ModernUIUtils.createModernButton("Save Product", ModernUIUtils.ACCENT_COLOR, Color.WHITE);
        saveButton.addActionListener(e -> saveProduct());
        
        JButton cancelButton = ModernUIUtils.createModernButton("Cancel", ModernUIUtils.DANGER_COLOR, Color.WHITE);
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        
        pack();
        setVisible(true);
    }

    /**
     * This method now saves the product to the MySQL database.
     */
    private void saveProduct() {
        String name = nameField.getText().trim();
        String imagePath = imagePathField.getText().trim();
        String category = (String) categoryComboBox.getSelectedItem();
        double price;

        // --- 1. Validate the form data ---
        if (name.isEmpty() || name.startsWith("e.g.,")) {
            JOptionPane.showMessageDialog(this, "Product name cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            price = Double.parseDouble(priceField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for the price.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- 2. Create the Product object ---
        // The ID is 0 because the database will auto-generate the real ID.
        Product newProduct = new Product(0, name, price, imagePath, category);

        // --- 3. Call the DBManager to save the product ---
        boolean success = DBManager.addProduct(newProduct);

        if (success) {
            JOptionPane.showMessageDialog(this, "Product saved to database successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // --- 4. Refresh the main shop window to show the new product ---
            if (Main.shopInstance != null) {
                Main.shopInstance.refreshProductsFromDB();
            }
            
            dispose(); // Close this dialog
        } else {
            // The error message is already shown by DBManager, but we can add another if needed.
            // JOptionPane.showMessageDialog(this, "Failed to save product.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}