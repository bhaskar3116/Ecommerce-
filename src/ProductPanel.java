import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class ProductPanel extends JPanel {
    private final Product product;
    private final Cart cart;
    private final Runnable globalRefreshCallback;

    private JPanel buttonContainer; // final removed
    private JLabel quantityLabel;   // final removed

    public ProductPanel(Product product, Cart cart, Runnable globalRefreshCallback) {
        this.product = product;
        this.cart = cart;
        this.globalRefreshCallback = globalRefreshCallback;

        setupPanel();
        createComponents();
        updateButtonState();
    }
    
    private void setupPanel() {
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(16, 16, 16, 16)
        ));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(260, 420));
        setMinimumSize(new Dimension(260, 420));
        setMaximumSize(new Dimension(260, 420));
        
        // Add subtle hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    new EmptyBorder(16, 16, 16, 16)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                    new EmptyBorder(16, 16, 16, 16)
                ));
            }
        });
    }
    
    private void createComponents() {
        // Image Panel
        JPanel imagePanel = createImagePanel();
        
        // Product Details Panel
        JPanel detailsPanel = createDetailsPanel();
        
        // Button Panel
        buttonContainer = createButtonPanel();
        
        // Assembly
        add(imagePanel, BorderLayout.NORTH);
        add(detailsPanel, BorderLayout.CENTER);
        add(buttonContainer, BorderLayout.SOUTH);
    }
    
    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        imagePanel.setPreferredSize(new Dimension(220, 220));
        
        JLabel imageLabel = new JLabel("Loading...");
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        imageLabel.setForeground(new Color(120, 120, 120));
        imageLabel.setPreferredSize(new Dimension(220, 220));
        imageLabel.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240), 1));
        imageLabel.setBackground(new Color(250, 250, 250));
        imageLabel.setOpaque(true);
        
        loadAndSetImage(product.getImagePath(), imageLabel);
        
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        return imagePanel;
    }
    
    private JPanel createDetailsPanel() {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        detailsPanel.setBorder(new EmptyBorder(12, 0, 12, 0));
        
        // Product Name
        JLabel nameLabel = new JLabel("<html><div style='text-align:center;'>" + 
            product.getName() + "</div></html>");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nameLabel.setForeground(new Color(33, 37, 41));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Spacing
        Component spacing = Box.createVerticalStrut(8);
        
        // Price
        JLabel priceLabel = new JLabel(String.format("₹%.2f", product.getPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        priceLabel.setForeground(new Color(40, 167, 69));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        detailsPanel.add(nameLabel);
        detailsPanel.add(spacing);
        detailsPanel.add(priceLabel);
        detailsPanel.add(Box.createVerticalGlue());
        
        return detailsPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new CardLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(0, 45));
        
        // Add to Cart Button
        JButton addToCartBtn = createAddToCartButton();
        
        // Quantity Stepper Panel
        JPanel quantityStepperPanel = createQuantityStepperPanel();
        
        buttonPanel.add(addToCartBtn, "BUTTON");
        buttonPanel.add(quantityStepperPanel, "STEPPER");
        
        return buttonPanel;
    }
    
    private JButton createAddToCartButton() {
        JButton addToCartBtn = new JButton("Add to Cart");
        addToCartBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addToCartBtn.setForeground(Color.WHITE);
        addToCartBtn.setBackground(new Color(40, 167, 69));
        addToCartBtn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        addToCartBtn.setFocusPainted(false);
        
        // --- FIX FOR "ADD TO CART" BUTTON ---
        addToCartBtn.setBorderPainted(false);
        
        addToCartBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addToCartBtn.setPreferredSize(new Dimension(0, 45));
        
        // Hover effect
        addToCartBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addToCartBtn.setBackground(new Color(34, 142, 111));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                addToCartBtn.setBackground(new Color(40, 167, 69));
            }
        });
        
        addToCartBtn.addActionListener(e -> {
            cart.addItem(product, 1, null, null);
            if (globalRefreshCallback != null) globalRefreshCallback.run();
        });
        
        return addToCartBtn;
    }
    
    private JPanel createQuantityStepperPanel() {
        JPanel stepperPanel = new JPanel(new BorderLayout());
        stepperPanel.setOpaque(false);
        stepperPanel.setPreferredSize(new Dimension(0, 45));
        stepperPanel.setBorder(BorderFactory.createLineBorder(new Color(206, 212, 218), 1));
        stepperPanel.setBackground(Color.WHITE);
        
        // Minus Button
        JButton minusBtn = createStepperButton("-");
        minusBtn.addActionListener(e -> {
            Cart.CartItem item = cart.findItemByKey(product.getId() + "_na_na");
            if (item != null) {
                cart.updateItemQuantity(item.getItemKey(), item.getQuantity() - 1);
                if (globalRefreshCallback != null) globalRefreshCallback.run();
            }
        });
        
        // Quantity Label
        quantityLabel = new JLabel("1");
        quantityLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        quantityLabel.setForeground(new Color(33, 37, 41));
        quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        quantityLabel.setBackground(Color.WHITE);
        quantityLabel.setOpaque(true);
        quantityLabel.setPreferredSize(new Dimension(60, 43));
        
        // Plus Button
        JButton plusBtn = createStepperButton("+");
        plusBtn.addActionListener(e -> {
            cart.addItem(product, 1, null, null);
            if (globalRefreshCallback != null) globalRefreshCallback.run();
        });
        
        stepperPanel.add(minusBtn, BorderLayout.WEST);
        stepperPanel.add(quantityLabel, BorderLayout.CENTER);
        stepperPanel.add(plusBtn, BorderLayout.EAST);
        
        return stepperPanel;
    }
    
    private JButton createStepperButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setForeground(new Color(73, 80, 87));
        button.setBackground(new Color(248, 249, 250));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        
        // --- FIX FOR STEPPER BUTTONS ---
        button.setBorderPainted(false);
        
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(45, 43));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(233, 236, 239));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(248, 249, 250)); // fixed here
            }
        });
        
        return button;
    }
    
    public int getProductId() {
        return this.product.getId();
    }

    public void updateButtonState() {
        CardLayout cl = (CardLayout) (buttonContainer.getLayout());
        String itemKey = product.getId() + "_na_na";
        Cart.CartItem itemInCart = cart.findItemByKey(itemKey);
        
        if (itemInCart != null) {
            quantityLabel.setText(String.valueOf(itemInCart.getQuantity()));
            cl.show(buttonContainer, "STEPPER");
        } else {
            cl.show(buttonContainer, "BUTTON");
        }
    }

    private void loadAndSetImage(String imageUrl, JLabel imageLabel) {
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                URL url = new URL(imageUrl);
                ImageIcon icon = new ImageIcon(url);
                Image scaledImage = icon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
            
            @Override
            protected void done() {
                try {
                    ImageIcon scaledIcon = get();
                    imageLabel.setText(null);
                    imageLabel.setIcon(scaledIcon);
                } catch (Exception e) {
                    imageLabel.setText("Image not available");
                    imageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                    imageLabel.setForeground(new Color(150, 150, 150));
                }
            }
        };
        worker.execute();
    }
}