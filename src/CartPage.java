import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;

public class CartPage extends JDialog {
    private final Cart cart;
    private final ShopApp shopApp;
    private JPanel itemsPanel;
    private JLabel itemCountLabel;
    private JLabel subtotalValueLabel, taxValueLabel, shippingValueLabel, discountValueLabel, totalValueLabel;
    private JLabel freeShippingInfoLabel;
    private JButton checkoutButton;

    public CartPage(Cart cart, ShopApp shopApp) {
        super(shopApp.frame, "Your Shopping Cart", true);
        this.cart = cart;
        this.shopApp = shopApp;
        
        setSize(900, 700);
        setLocationRelativeTo(shopApp.frame);
        setLayout(new BorderLayout());
        setResizable(true);

        setupUI();
        updateCartDisplay();
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                shopApp.onCartPageClosed();
            }
        });
        
        setVisible(true);
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(248, 249, 250));
        
        JPanel headerPanel = createHeaderPanel();
        JPanel contentPanel = new JPanel(new BorderLayout(20, 0));
        contentPanel.setOpaque(false);
        JPanel itemsContainer = createItemsContainer();
        JPanel summaryPanel = createSummaryPanel();
        
        contentPanel.add(itemsContainer, BorderLayout.CENTER);
        contentPanel.add(summaryPanel, BorderLayout.EAST);
        
        JPanel footerPanel = createFooterPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("🛒 My Cart");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 37, 41));
        
        itemCountLabel = new JLabel("0 items");
        itemCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        itemCountLabel.setForeground(new Color(108, 117, 125));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(itemCountLabel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createItemsContainer() {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        
        itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(Color.WHITE);
        itemsPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JScrollPane itemsScrollPane = new JScrollPane(itemsPanel);
        itemsScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218)), 
                "Items in your cart",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(73, 80, 87)
            ),
            new EmptyBorder(5, 5, 5, 5)
        ));
        itemsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        itemsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        itemsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        container.add(itemsScrollPane, BorderLayout.CENTER);
        return container;
    }
    
    private JPanel createSummaryPanel() {
        JPanel summaryContainer = new JPanel(new BorderLayout());
        summaryContainer.setPreferredSize(new Dimension(320, 0));
        summaryContainer.setMinimumSize(new Dimension(320, 0));
        summaryContainer.setBackground(Color.WHITE);
        summaryContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218)),
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(206, 212, 218)), 
                    "Order Summary",
                    javax.swing.border.TitledBorder.LEFT,
                    javax.swing.border.TitledBorder.TOP,
                    new Font("Segoe UI", Font.BOLD, 14),
                    new Color(73, 80, 87)
                )
            ),
            new EmptyBorder(15, 20, 20, 20)
        ));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        JPanel costPanel = createCostBreakdownPanel();
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(206, 212, 218));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        JPanel totalPanel = createTotalPanel();
        
        freeShippingInfoLabel = new JLabel(" ", SwingConstants.CENTER);
        freeShippingInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        freeShippingInfoLabel.setForeground(new Color(108, 117, 125));
        freeShippingInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        freeShippingInfoLabel.setBorder(new EmptyBorder(10, 0, 15, 0));
        
        checkoutButton = createCheckoutButton();
        
        contentPanel.add(costPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(separator);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(totalPanel);
        contentPanel.add(freeShippingInfoLabel);
        contentPanel.add(checkoutButton);
        contentPanel.add(Box.createVerticalGlue());
        
        summaryContainer.add(contentPanel, BorderLayout.CENTER);
        return summaryContainer;
    }
    
    private JPanel createCostBreakdownPanel() {
        JPanel costPanel = new JPanel(new GridBagLayout());
        costPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        subtotalValueLabel = new JLabel("₹0.00");
        taxValueLabel = new JLabel("₹0.00");
        shippingValueLabel = new JLabel("₹0.00");
        discountValueLabel = new JLabel("₹0.00");
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 14);
        subtotalValueLabel.setFont(valueFont);
        taxValueLabel.setFont(valueFont);
        shippingValueLabel.setFont(valueFont);
        discountValueLabel.setFont(valueFont);
        addCostRow(costPanel, gbc, 0, "Subtotal:", subtotalValueLabel);
        addCostRow(costPanel, gbc, 1, "Tax (18% GST):", taxValueLabel);
        addCostRow(costPanel, gbc, 2, "Shipping:", shippingValueLabel);
        addCostRow(costPanel, gbc, 3, "Discount:", discountValueLabel);
        return costPanel;
    }
    
    private void addCostRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JLabel valueLabel) {
        gbc.gridy = row;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 0; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1.0;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(73, 80, 87));
        panel.add(label, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0.0;
        valueLabel.setForeground(new Color(73, 80, 87));
        panel.add(valueLabel, gbc);
    }
    
    private JPanel createTotalPanel() {
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setOpaque(false);
        totalPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
        JLabel totalLabelText = new JLabel("Total:");
        totalLabelText.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabelText.setForeground(new Color(33, 37, 41));
        totalValueLabel = new JLabel("₹0.00");
        totalValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalValueLabel.setForeground(new Color(40, 167, 69));
        totalPanel.add(totalLabelText, BorderLayout.WEST);
        totalPanel.add(totalValueLabel, BorderLayout.EAST);
        return totalPanel;
    }
    
    // --- UPDATED METHOD ---
    private JButton createCheckoutButton() {
        checkoutButton = new JButton("Proceed to Checkout");
        checkoutButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFocusPainted(false);
        
        // --- FIX FOR CHECKOUT BUTTON ---
        checkoutButton.setBorderPainted(false);
        checkoutButton.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        
        checkoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkoutButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        // ** ACTION LISTENER ADDED HERE **
        checkoutButton.addActionListener(e -> {
            dispose();
            shopApp.simulatePayment();
        });
        
        checkoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (checkoutButton.isEnabled()) checkoutButton.setBackground(new Color(34, 142, 111));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (checkoutButton.isEnabled()) checkoutButton.setBackground(new Color(40, 167, 69));
            }
        });
        return checkoutButton;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        JButton backButton = new JButton("← Back to Shopping");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setForeground(new Color(73, 80, 87));
        backButton.setBackground(new Color(248, 249, 250));
        backButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218)),
            new EmptyBorder(8, 16, 8, 16)
        ));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> dispose());
        footerPanel.add(backButton);
        return footerPanel;
    }

    public void updateCartDisplay() {
        itemsPanel.removeAll();
        List<Cart.CartItem> currentItems = cart.getItems();
        if (currentItems.isEmpty()) {
            showEmptyCart();
        } else {
            showCartItems(currentItems);
        }
        updateTotals();
        revalidate();
        repaint();
    }
    
    private void showEmptyCart() {
        itemsPanel.setLayout(new BorderLayout());
        JPanel emptyPanel = new JPanel();
        emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
        emptyPanel.setOpaque(false);
        JLabel emptyIcon = new JLabel("🛒", SwingConstants.CENTER);
        emptyIcon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel emptyLabel = new JLabel("Your cart is empty", SwingConstants.CENTER);
        emptyLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        emptyLabel.setForeground(new Color(108, 117, 125));
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel emptySubLabel = new JLabel("Add some products to get started!", SwingConstants.CENTER);
        emptySubLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emptySubLabel.setForeground(new Color(134, 142, 150));
        emptySubLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emptyPanel.add(Box.createVerticalGlue());
        emptyPanel.add(emptyIcon);
        emptyPanel.add(Box.createVerticalStrut(10));
        emptyPanel.add(emptyLabel);
        emptyPanel.add(Box.createVerticalStrut(5));
        emptyPanel.add(emptySubLabel);
        emptyPanel.add(Box.createVerticalGlue());
        itemsPanel.add(emptyPanel, BorderLayout.CENTER);
    }
    
    private void showCartItems(List<Cart.CartItem> currentItems) {
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < currentItems.size(); i++) {
            itemsPanel.add(createCartItemPanel(currentItems.get(i)));
            if (i < currentItems.size() - 1) itemsPanel.add(Box.createVerticalStrut(10));
        }
        itemsPanel.add(Box.createVerticalGlue());
    }
    
    private void updateTotals() {
        cart.updateTotals();
        itemCountLabel.setText(cart.getTotalItemCount() + " items");
        subtotalValueLabel.setText(String.format("₹%.2f", cart.getSubtotal()));
        taxValueLabel.setText(String.format("₹%.2f", cart.getTaxAmount()));
        shippingValueLabel.setText(String.format("₹%.2f", cart.getShippingCost()));
        discountValueLabel.setText(String.format("- ₹%.2f", cart.getDiscount()));
        totalValueLabel.setText(String.format("₹%.2f", cart.getTotal()));
        updateFreeShippingInfo();
        updateCheckoutButton();
    }
    
    private void updateFreeShippingInfo() {
        if (cart.isFreeShippingEligible() && !cart.getItems().isEmpty()) {
            freeShippingInfoLabel.setText("🚚 You've qualified for FREE shipping!");
            freeShippingInfoLabel.setForeground(new Color(40, 167, 69));
        } else if (!cart.getItems().isEmpty()) {
            double amountNeeded = cart.getAmountForFreeShipping();
            freeShippingInfoLabel.setText(String.format("Add ₹%.2f more for FREE shipping", amountNeeded));
            freeShippingInfoLabel.setForeground(new Color(255, 193, 7));
        } else {
            freeShippingInfoLabel.setText(" ");
        }
    }
    
    private void updateCheckoutButton() {
        boolean hasItems = !cart.getItems().isEmpty();
        checkoutButton.setEnabled(hasItems);
        checkoutButton.setBackground(hasItems ? new Color(40, 167, 69) : new Color(206, 212, 218));
        checkoutButton.setForeground(hasItems ? Color.WHITE : new Color(108, 117, 125));
    }
    
    private JPanel createCartItemPanel(Cart.CartItem item) {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        Product product = item.getProduct();
        panel.add(createImagePanel(product, item), BorderLayout.WEST);
        panel.add(createProductInfoPanel(product, item), BorderLayout.CENTER);
        panel.add(createControlsPanel(item), BorderLayout.EAST);
        return panel;
    }
    
    private JPanel createImagePanel(Product product, Cart.CartItem item) {
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        imagePanel.setPreferredSize(new Dimension(60, 60));
        JLabel imageLabel = new JLabel("...", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(60, 60));
        imageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        imageLabel.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230)));
        imageLabel.setBackground(new Color(248, 249, 250));
        imageLabel.setOpaque(true);
        loadCartItemImage(product.getImagePath(), imageLabel);
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                shopApp.focusOnProduct(product);
            }
        });
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        return imagePanel;
    }
    
    private JPanel createProductInfoPanel(Product product, Cart.CartItem item) {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(new Color(33, 37, 41));
        JLabel priceLabel = new JLabel(String.format("₹%.2f each", product.getPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        priceLabel.setForeground(new Color(108, 117, 125));
        JLabel totalLabel = new JLabel(String.format("Subtotal: ₹%.2f", product.getPrice() * item.getQuantity()));
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        totalLabel.setForeground(new Color(40, 167, 69));
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(totalLabel);
        return infoPanel;
    }
    
    // --- UPDATED METHOD ---
    private JPanel createControlsPanel(Cart.CartItem item) {
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        controlsPanel.setOpaque(false);
        controlsPanel.setPreferredSize(new Dimension(180, 60));
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        quantityPanel.setOpaque(false);
        quantityPanel.setBorder(BorderFactory.createLineBorder(new Color(206, 212, 218)));
        JButton minusBtn = createQuantityButton("-");
        minusBtn.addActionListener(e -> {
            cart.updateItemQuantity(item.getItemKey(), item.getQuantity() - 1);
            updateCartDisplay();
            shopApp.updateAllProductPanels();
        });
        JLabel quantityLabel = new JLabel(String.valueOf(item.getQuantity()));
        quantityLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        quantityLabel.setPreferredSize(new Dimension(30, 25));
        quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton plusBtn = createQuantityButton("+");
        plusBtn.addActionListener(e -> {
            cart.updateItemQuantity(item.getItemKey(), item.getQuantity() + 1);
            updateCartDisplay();
            shopApp.updateAllProductPanels();
        });
        quantityPanel.add(minusBtn);
        quantityPanel.add(quantityLabel);
        quantityPanel.add(plusBtn);
        
        JButton removeBtn = new JButton("🗑️");
        removeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        removeBtn.setPreferredSize(new Dimension(35, 28));
        removeBtn.setToolTipText("Remove from cart");
        
        // --- FIX FOR REMOVE BUTTON ---
        // Styled to be a flat red button with a white icon
        removeBtn.setForeground(Color.WHITE);
        removeBtn.setBackground(new Color(220, 53, 69));
        removeBtn.setBorder(null);
        removeBtn.setBorderPainted(false);
        
        removeBtn.setFocusPainted(false);
        removeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeBtn.addActionListener(e -> {
            cart.removeItem(item.getItemKey());
            updateCartDisplay();
            shopApp.updateAllProductPanels();
        });
        controlsPanel.add(quantityPanel);
        controlsPanel.add(Box.createHorizontalStrut(10));
        controlsPanel.add(removeBtn);
        return controlsPanel;
    }
    
    // --- UPDATED METHOD ---
    private JButton createQuantityButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(28, 25));
        button.setForeground(new Color(73, 80, 87));
        button.setBackground(new Color(248, 249, 250));
        button.setBorder(null);
        button.setFocusPainted(false);
        
        // --- FIX FOR QUANTITY BUTTONS ---
        button.setBorderPainted(false);
        
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { button.setBackground(new Color(233, 236, 239)); }
            @Override
            public void mouseExited(MouseEvent e) { button.setBackground(new Color(248, 249, 250)); }
        });
        return button;
    }

    private void loadCartItemImage(String imageUrl, JLabel imageLabel) {
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                URL url = new URL(imageUrl);
                ImageIcon icon = new ImageIcon(url);
                Image scaledImage = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
            @Override
            protected void done() {
                try {
                    imageLabel.setText(null);
                    imageLabel.setIcon(get());
                } catch (Exception e) {
                    imageLabel.setText("N/A");
                    imageLabel.setForeground(new Color(108, 117, 125));
                }
            }
        };
        worker.execute();
    }
}