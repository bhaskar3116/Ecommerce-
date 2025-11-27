import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.Timer;

public class ShopApp {
    private List<Product> allProducts = new ArrayList<>();
    private List<Product> filteredProducts;
    private JPanel productListPanel;
    private final Cart cart;
    public final JFrame frame;
    private CartPage cartPage;
    private JTextField searchField;
    private JComboBox<String> sortComboBox;
    private JLabel resultCountLabel;
    private ButtonGroup categoryButtonGroup;
    private final User currentUser;

    public void addNewProduct(Product product) {
        this.allProducts.add(product);
        applyFilters();
    }

    public ShopApp(User user) {
        this.currentUser = user;
        frame = new JFrame("ShopEasy - Welcome, " + user.getUsername());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        
        cart = user.getCart();
        
        loadProductsAndSetupUI(user); 
    }
    
    private void setupMainLayout(User user) {
        frame.setLayout(new BorderLayout());
        
        frame.setJMenuBar(createMenuBar());
        frame.add(createHeaderPanel(user), BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(createFilterPanel(), BorderLayout.NORTH);
        contentPanel.add(createProductsContainer(), BorderLayout.CENTER);
        
        frame.add(contentPanel, BorderLayout.CENTER);
        
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu adminMenu = new JMenu("Admin");
        JMenuItem adminLoginItem = new JMenuItem("Admin Login");
        adminLoginItem.addActionListener(e -> new AdminLogin());
        adminMenu.add(adminLoginItem);
        menuBar.add(adminMenu);
        return menuBar;
    }

    
    private JPanel createHeaderPanel(User user) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 37, 41));
        headerPanel.setBorder(new EmptyBorder(12, 20, 12, 20));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);
        JLabel logoIcon = new JLabel("🛒");
        logoIcon.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        JLabel logoText = new JLabel("ShopEasy");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoText.setForeground(Color.WHITE);
        logoText.setBorder(new EmptyBorder(0, 8, 0, 0));
        leftPanel.add(logoIcon);
        leftPanel.add(logoText);
        
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerPanel.setOpaque(false);
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setPreferredSize(new Dimension(320, 32));
        
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218)),
            new EmptyBorder(6, 10, 6, 10)
        ));
        searchField.setBackground(Color.WHITE);
        
        searchField.setForeground(Color.GRAY);
        searchField.setText("Search products...");
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Search products...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Search products...");
                }
            }
        });
        
        searchField.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) { if (e.getKeyCode() == KeyEvent.VK_ENTER) applyFilters(); }
            public void keyReleased(KeyEvent e) {
                Timer timer = new Timer(300, ev -> applyFilters());
                timer.setRepeats(false);
                timer.start();
            }
        });
        
        JButton searchButton = new JButton("🔍");
        searchButton.addActionListener(e -> applyFilters());
        searchButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchButton.setBackground(new Color(108, 117, 125));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.setPreferredSize(new Dimension(35, 32));
        
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                searchButton.setBackground(new Color(90, 98, 104));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                searchButton.setBackground(new Color(108, 117, 125));
            }
        });
        
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        centerPanel.add(searchPanel);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightPanel.setOpaque(false);
        
        JButton ordersButton = new JButton("<html>Returns<br><b>& Orders</b></html>");
        ordersButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ordersButton.setForeground(Color.WHITE);
        ordersButton.setOpaque(false);
        ordersButton.setContentAreaFilled(false);
        ordersButton.setBorderPainted(false);
        ordersButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ordersButton.addActionListener(e -> new OrderHistoryPage(frame, currentUser));
        
        JButton cartButton = new JButton("🛒 View Cart");
        cartButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cartButton.setForeground(Color.WHITE);
        cartButton.setBackground(new Color(40, 167, 69));
        cartButton.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
        cartButton.setFocusPainted(false);
        cartButton.setBorderPainted(false); 
        cartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cartButton.addActionListener(e -> cartPage = new CartPage(cart, this));
        
        cartButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                cartButton.setBackground(new Color(34, 142, 111));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                cartButton.setBackground(new Color(40, 167, 69));
            }
        });
        
        rightPanel.add(ordersButton);
        rightPanel.add(cartButton);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(centerPanel, BorderLayout.CENTER);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }

    private JPanel createFilterPanel() {
        JPanel filterContainer = new JPanel(new BorderLayout());
        filterContainer.setBackground(Color.WHITE);
        filterContainer.setBorder(new EmptyBorder(12, 20, 12, 20));
        
        resultCountLabel = new JLabel();
        resultCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resultCountLabel.setForeground(new Color(108, 117, 125));
        
        // --- FIX 1: Use WrapLayout for the buttons ---
        JPanel filterButtonsPanel = new JPanel(new WrapLayout(FlowLayout.CENTER, 8, 0));
        filterButtonsPanel.setOpaque(false);
        
        String[] categories = {"All", "Mens", "Womens", "Shoes", "Electronics", "Health"};
        categoryButtonGroup = new ButtonGroup();
        
        for (int i = 0; i < categories.length; i++) {
            String category = categories[i];
            JToggleButton filterBtn = createFilterButton(category, i == 0);
            categoryButtonGroup.add(filterBtn);
            filterButtonsPanel.add(filterBtn);
            filterBtn.addActionListener(e -> filterProducts(category));
        }
        
        JPanel rightSortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightSortPanel.setOpaque(false);
        
        JLabel sortLabel = new JLabel("Sort by:");
        sortLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sortLabel.setForeground(new Color(73, 80, 87));
        
        sortComboBox = new JComboBox<>(new String[]{
            "Default", "Price: Low to High", "Price: High to Low", "Name: A to Z", "Name: Z to A"
        });
        sortComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sortComboBox.setPreferredSize(new Dimension(140, 28));
        sortComboBox.addActionListener(e -> applyFilters());
        
        rightSortPanel.add(sortLabel);
        rightSortPanel.add(sortComboBox);
        
        filterContainer.add(resultCountLabel, BorderLayout.WEST);
        filterContainer.add(filterButtonsPanel, BorderLayout.CENTER);
        filterContainer.add(rightSortPanel, BorderLayout.EAST);
        
        return filterContainer;
    }
    
    private JToggleButton createFilterButton(String text, boolean selected) {
        JToggleButton button = new JToggleButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(85, 30));
        
        // --- FIX 2: Add setOpaque(true) to prevent button from disappearing ---
        button.setOpaque(true);
        
        button.setBorderPainted(false); 
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); 
        
        if (selected) {
            button.setSelected(true);
            button.setBackground(new Color(40, 167, 69));
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(new Color(73, 80, 87));
        }
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!button.isSelected()) button.setBackground(new Color(248, 249, 250));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.isSelected()) button.setBackground(Color.WHITE);
            }
        });
        
        button.addItemListener(e -> {
            if (button.isSelected()) {
                button.setBackground(new Color(40, 167, 69));
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(Color.WHITE);
                button.setForeground(new Color(73, 80, 87));
            }
        });
        
        return button;
    }
    
    private JPanel createProductsContainer() {
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(new Color(248, 249, 250));
        
        productListPanel = new JPanel();
        productListPanel.setLayout(new GridLayout(0, 4, 20, 20));
        productListPanel.setBackground(new Color(248, 249, 250));
        productListPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(productListPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainContainer.add(scrollPane, BorderLayout.CENTER);
        return mainContainer;
    }
    
    private void filterProducts(String category) {
        applyFilters();
    }
    
    private void applyFilters() {
        if (allProducts.isEmpty()) return;
        
        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.equals("search products...")) {
            searchText = "";
        }
        String selectedSort = (String) sortComboBox.getSelectedItem();
        
        String selectedCategory = "All";
        for (Enumeration<AbstractButton> buttons = categoryButtonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                selectedCategory = button.getText();
                break;
            }
        }

        Stream<Product> productStream = allProducts.stream();

        final String searchTextForFilter = searchText;
        productStream = productStream.filter(p ->
            p.getName().toLowerCase().contains(searchTextForFilter)
        );

        final String categoryForFilter = selectedCategory;

        if (!"All".equalsIgnoreCase(categoryForFilter)) {
            productStream = productStream.filter(p ->
                // --- FIX 3: Add .trim() to handle whitespace in database data ---
                p.getCategory() != null && p.getCategory().trim().equalsIgnoreCase(categoryForFilter)
            );
        }

        filteredProducts = productStream.collect(Collectors.toList());
        
        sortProducts(selectedSort);
        updateProductDisplay();
        updateResultCount(selectedCategory);
    }

    private void sortProducts(String sortOption) {
        switch (sortOption) {
            case "Price: Low to High": filteredProducts.sort(Comparator.comparingDouble(Product::getPrice)); break;
            case "Price: High to Low": filteredProducts.sort(Comparator.comparingDouble(Product::getPrice).reversed()); break;
            case "Name: A to Z": filteredProducts.sort(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER)); break;
            case "Name: Z to A": filteredProducts.sort(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER).reversed()); break;
            default: filteredProducts.sort(Comparator.comparingInt(Product::getId)); break;
        }
    }

    private void updateProductDisplay() {
        productListPanel.removeAll();
        if (filteredProducts.isEmpty()) {
            productListPanel.setLayout(new BorderLayout());
            JLabel noResultsLabel = new JLabel("😥 No products found.", SwingConstants.CENTER);
            noResultsLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            noResultsLabel.setForeground(Color.GRAY);
            productListPanel.add(noResultsLabel, BorderLayout.CENTER);
        } else {
            productListPanel.setLayout(new GridLayout(0, 4, 20, 20)); // Reset layout
            for (Product product : filteredProducts) {
                productListPanel.add(new ProductPanel(product, cart, this::updateAllProductPanels));
            }
        }
        productListPanel.revalidate();
        productListPanel.repaint();
    }
    
    public void updateAllProductPanels() {
        for (Component comp : productListPanel.getComponents()) {
            if (comp instanceof ProductPanel) {
                ((ProductPanel) comp).updateButtonState();
            }
        }
        if (cartPage != null && cartPage.isVisible()) {
            cartPage.updateCartDisplay();
        }
    }

    private void updateResultCount(String category) {
        int count = filteredProducts.size();
        String categoryText = "All".equalsIgnoreCase(category) ? "all products" : "in " + category;
        resultCountLabel.setText("Showing " + count + " product(s) " + categoryText);
    }
    
    public void onCartPageClosed() {
        updateAllProductPanels();
    }
    
    public void focusOnProduct(Product productToFind) {
        for (Component comp : productListPanel.getComponents()) {
            if (comp instanceof ProductPanel) {
                ProductPanel panel = (ProductPanel) comp;
                if (panel.getProductId() == productToFind.getId()) {
                    Rectangle bounds = panel.getBounds();
                    productListPanel.scrollRectToVisible(bounds);
                    panel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
                    Timer timer = new Timer(2000, e -> panel.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1)));
                    timer.setRepeats(false);
                    timer.start();
                    break;
                }
            }
        }
    }
    
    
    public void refreshProductsFromDB() {
        SwingWorker<List<Product>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Product> doInBackground() throws Exception {
                return DBManager.getAllProducts();
            }

            @Override
            protected void done() {
                try {
                    allProducts = get();
                    applyFilters();
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Failed to refresh products from the database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
    
    public void simulatePayment() {
        Runnable paymentTask = () -> {
            try {
                Thread.sleep(2500); 
                
                SwingUtilities.invokeLater(() -> {
                    boolean success = Math.random() < 0.9; 
                    
                    if (success) {
                        Order newOrder = new Order(cart.getItems(), cart.getTotal());
                        boolean saved = DBManager.saveOrder(currentUser, newOrder);
                        
                        if (!saved) {
                           JOptionPane.showMessageDialog(frame, "Critical Error: Payment succeeded but order could not be saved.", "Save Error", JOptionPane.ERROR_MESSAGE);
                           return;
                        }
                        
                        PaymentSuccessDialog successDialog = new PaymentSuccessDialog(
                            frame,
                            newOrder.getOrderId(), 
                            newOrder.getTotalAmount() 
                        );

                        successDialog.showWithAutoClose(4);

                        cart.clearCart();
                        updateAllProductPanels();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Payment failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        
        LoadingDialog.showWithTask(frame, "Payment Processing", "Processing your payment...", paymentTask);
    }
    
    
    private void loadProductsAndSetupUI(User user) {
        JDialog loadingDialog = new JDialog(frame, "Loading Products...", true);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        loadingDialog.add(new JLabel("Fetching product data from database, please wait..."), BorderLayout.NORTH);
        loadingDialog.add(progressBar, BorderLayout.CENTER);
        loadingDialog.pack();
        loadingDialog.setLocationRelativeTo(null);
        
        SwingWorker<List<Product>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Product> doInBackground() throws Exception {
                return DBManager.getAllProducts();
            }

            @Override
            protected void done() {
                loadingDialog.dispose();
                try {
                    allProducts = get();
                    setupMainLayout(user);
                    applyFilters();
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Application failed to load product data.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
        loadingDialog.setVisible(true);
    }
}