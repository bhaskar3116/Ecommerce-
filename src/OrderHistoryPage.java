import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderHistoryPage extends JDialog {
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(223, 230, 233);
    private static final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private static final Color TEXT_SECONDARY = new Color(127, 140, 141);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    
    public OrderHistoryPage(Frame parent, User user) {
        super(parent, "Your Order History", true);
        setupDialog();
        initializeComponents(user);
    }
    
    private void setupDialog() {
        setSize(900, 700);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void initializeComponents(User user) {
        add(createHeader(), BorderLayout.NORTH);
        
        List<Order> orders = DBManager.getOrderHistory(user);
        
        if (orders.isEmpty()) {
            add(createEmptyState(), BorderLayout.CENTER);
        } else {
            add(createOrdersScrollPane(orders), BorderLayout.CENTER);
        }
        
        add(createFooter(), BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        JLabel titleLabel = new JLabel("Your Order History");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Track and review your past purchases");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 180));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        header.add(titlePanel, BorderLayout.WEST);
        
        return header;
    }
    
    private JPanel createEmptyState() {
        JPanel emptyPanel = new JPanel(new GridBagLayout());
        emptyPanel.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        
        JLabel iconLabel = new JLabel("📦");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 72));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emptyPanel.add(iconLabel, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        JLabel emptyLabel = new JLabel("No Orders Yet");
        emptyLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        emptyLabel.setForeground(TEXT_PRIMARY);
        emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emptyPanel.add(emptyLabel, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        JLabel messageLabel = new JLabel("Your order history will appear here once you make your first purchase");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setForeground(TEXT_SECONDARY);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emptyPanel.add(messageLabel, gbc);
        
        gbc.gridy = 3;
        JButton shopNowBtn = createStyledButton("Start Shopping", SECONDARY_COLOR);
        
        shopNowBtn.addActionListener(e -> dispose());
        
        emptyPanel.add(shopNowBtn, gbc);
        
        return emptyPanel;
    }
    
    private JScrollPane createOrdersScrollPane(List<Order> orders) {
        JPanel ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        ordersPanel.setBackground(BACKGROUND_COLOR);
        ordersPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        ordersPanel.add(createOrdersSummary(orders));
        ordersPanel.add(Box.createVerticalStrut(20));
        
        for (int i = 0; i < orders.size(); i++) {
            ordersPanel.add(createEnhancedOrderPanel(orders.get(i), i + 1));
            if (i < orders.size() - 1) {
                ordersPanel.add(Box.createVerticalStrut(20));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        return scrollPane;
    }
    
    private JPanel createOrdersSummary(List<Order> orders) {
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        summaryPanel.setOpaque(false);
        
        summaryPanel.add(createSummaryCard("Total Orders", String.valueOf(orders.size()), "📋"));
        
        double totalSpent = orders.stream().mapToDouble(Order::getTotalAmount).sum();
        summaryPanel.add(createSummaryCard("Total Spent", String.format("₹%.2f", totalSpent), "💰"));
        
        if (!orders.isEmpty()) {
            Order recentOrder = orders.get(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");
            summaryPanel.add(createSummaryCard("Last Order", recentOrder.getOrderDate().format(formatter), "🕒"));
        }
        
        return summaryPanel;
    }
    
    private JPanel createSummaryCard(String title, String value, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(PRIMARY_COLOR);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(TEXT_SECONDARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel content = new JPanel(new GridLayout(3, 1, 0, 5));
        content.setOpaque(false);
        content.add(iconLabel);
        content.add(valueLabel);
        content.add(titleLabel);
        
        card.add(content, BorderLayout.CENTER);
        return card;
    }
    
    private JPanel createEnhancedOrderPanel(Order order, int orderNumber) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(CARD_COLOR);
        mainPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        
        mainPanel.add(createEnhancedHeader(order, orderNumber), BorderLayout.NORTH);
        mainPanel.add(createItemsSection(order), BorderLayout.CENTER);
        mainPanel.add(createOrderFooter(order), BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createEnhancedHeader(Order order, int orderNumber) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(250, 251, 252));
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JPanel leftInfo = new JPanel(new GridLayout(2, 1, 0, 5));
        leftInfo.setOpaque(false);
        
        JLabel orderLabel = new JLabel("Order #" + order.getOrderId());
        orderLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        orderLabel.setForeground(TEXT_PRIMARY);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a");
        JLabel dateLabel = new JLabel("Placed on " + order.getOrderDate().format(formatter));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateLabel.setForeground(TEXT_SECONDARY);
        
        leftInfo.add(orderLabel);
        leftInfo.add(dateLabel);
        
        JPanel rightInfo = new JPanel(new GridLayout(2, 1, 0, 5));
        rightInfo.setOpaque(false);
        
        JLabel totalLabel = new JLabel("₹" + String.format("%.2f", order.getTotalAmount()));
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(SUCCESS_COLOR);
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JLabel statusLabel = new JLabel("Delivered");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(SUCCESS_COLOR);
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        rightInfo.add(totalLabel);
        rightInfo.add(statusLabel);
        
        header.add(leftInfo, BorderLayout.WEST);
        header.add(rightInfo, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createItemsSection(Order order) {
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(CARD_COLOR);
        itemsPanel.setBorder(new EmptyBorder(0, 20, 15, 20));
        
        JLabel itemsHeader = new JLabel("Items (" + order.getItems().size() + ")");
        itemsHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        itemsHeader.setForeground(TEXT_PRIMARY);
        itemsHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
        itemsPanel.add(itemsHeader);
        
        for (int i = 0; i < order.getItems().size(); i++) {
            itemsPanel.add(createItemRow(order.getItems().get(i)));
            if (i < order.getItems().size() - 1) {
                itemsPanel.add(Box.createVerticalStrut(8));
            }
        }
        
        return itemsPanel;
    }
    
    private JPanel createItemRow(OrderItem item) {
        JPanel itemRow = new JPanel(new BorderLayout());
        itemRow.setOpaque(false);
        itemRow.setBorder(new EmptyBorder(8, 0, 8, 0));
        
        JPanel itemInfo = new JPanel(new GridLayout(2, 1, 0, 2));
        itemInfo.setOpaque(false);
        
        JLabel nameLabel = new JLabel(item.getProductName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(TEXT_PRIMARY);
        
        JLabel qtyLabel = new JLabel("Quantity: " + item.getQuantity());
        qtyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        qtyLabel.setForeground(TEXT_SECONDARY);
        
        itemInfo.add(nameLabel);
        itemInfo.add(qtyLabel);
        
        JLabel priceLabel = new JLabel("₹" + String.format("%.2f", item.getPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(PRIMARY_COLOR);
        
        itemRow.add(itemInfo, BorderLayout.WEST);
        itemRow.add(priceLabel, BorderLayout.EAST);
        
        return itemRow;
    }
    
    private JPanel createOrderFooter(Order order) {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(CARD_COLOR);
        footer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        JButton viewDetailsBtn = createStyledButton("View Details", Color.WHITE, PRIMARY_COLOR);
        JButton reorderBtn = createStyledButton("Reorder", SECONDARY_COLOR);
        
        footer.add(viewDetailsBtn);
        footer.add(Box.createHorizontalStrut(10));
        footer.add(reorderBtn);
        
        return footer;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        return createStyledButton(text, bgColor, Color.WHITE);
    }
    
    // --- UPDATED METHOD ---
    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        
        // --- THIS IS THE FIX ---
        button.setBorderPainted(false);
        
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // For the "View Details" button, we darken the BORDER, not the background
                if (bgColor.equals(Color.WHITE)) {
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR.darker()),
                        new EmptyBorder(7, 15, 7, 15) // Adjust padding for border
                    ));
                } else {
                    button.setBackground(bgColor.darker());
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (bgColor.equals(Color.WHITE)) {
                     button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR),
                        new EmptyBorder(7, 15, 7, 15)
                    ));
                } else {
                    button.setBackground(bgColor);
                }
            }
        });
        
        // --- SPECIAL STYLING FOR "View Details" ---
        if (bgColor.equals(Color.WHITE)) {
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR),
                new EmptyBorder(7, 15, 7, 15)
            ));
        }
        
        return button;
    }
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(BACKGROUND_COLOR);
        footer.setBorder(new EmptyBorder(15, 25, 20, 25));
        
        JButton closeBtn = createStyledButton("Close", new Color(95, 106, 118));
        closeBtn.addActionListener(e -> dispose());
        
        footer.add(closeBtn);
        return footer;
    }
}