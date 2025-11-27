import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

public class DBManager {

    // --- CONFIGURE THIS ---
    private static final String DB_URL = "jdbc:mysql://localhost:3306/shopeasy_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "cvrce"; // Your Password
    // ----------------------

    /**
     * Establishes a connection to the database.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Did you add it to your project's build path?");
            e.printStackTrace();
            throw new SQLException("JDBC Driver not found", e);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    /**
     * Main method to test the connection.
     */
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("✅ Connection to shopeasy_db successful!");
            } else {
                System.out.println("❌ Failed to make connection.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Connection Failed!");
            e.printStackTrace();
        }
    }

    // =========================================================================
    // PRODUCT METHODS
    // =========================================================================
    
    /**
     * Fetches all products from the 'products' table in the database.
     */
    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products;";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getString("imagePath"),
                    rs.getString("category")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching products from database:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Could not load product data from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return products;
    }
    
    /**
     * Inserts a new product into the database.
     * The product's ID is ignored as the database will auto-generate it.
     *
     * @param product The Product object to save.
     * @return true if the insertion was successful, false otherwise.
     */
    public static boolean addProduct(Product product) {
        String sql = "INSERT INTO products (name, price, imagePath, category) VALUES (?, ?, ?, ?);";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setString(3, product.getImagePath());
            stmt.setString(4, product.getCategory());
            
            int rowsAffected = stmt.executeUpdate();
            
            // executeUpdate() returns the number of rows affected. 
            // If it's > 0, the insert was successful.
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding product to database:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: Could not save the product.", "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // =========================================================================
    // USER & AUTHENTICATION METHODS
    // =========================================================================

    /**
     * Registers a new user in the database.
     * Handles salt generation and password hashing.
     * @return true if successful, false if username is taken.
     */
    public static boolean registerUser(String username, String password) {
        String checkUserSql = "SELECT COUNT(*) FROM users WHERE username = ?;";
        
        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkUserSql)) {

            checkStmt.setString(1, username);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // Username is already taken
                }
            }

            String salt = generateSalt();
            String hash = hashPassword(password, salt);

            String insertSql = "INSERT INTO users (username, salt, hash) VALUES (?, ?, ?);";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, salt);
                insertStmt.setString(3, hash);
                insertStmt.executeUpdate();
                return true; // Registration successful
            }

        } catch (SQLException e) {
            System.err.println("Database error during registration: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Authenticates a user against the database.
     * @return a User object (now with database ID) if successful, null otherwise.
     */
    public static User authenticateUser(String username, String password) {
        String sql = "SELECT id, salt, hash FROM users WHERE username = ?;";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) { // User was found
                    String dbSalt = rs.getString("salt");
                    String dbHash = rs.getString("hash");
                    
                    String hashedInput = hashPassword(password, dbSalt);
                    
                    if (hashedInput.equals(dbHash)) {
                        int userId = rs.getInt("id"); 
                        return new User(userId, username, dbSalt, dbHash);
                    }
                }
                return null; // User not found or password incorrect
            }
        } catch (SQLException e) {
            System.err.println("Database error during authentication: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // --- Security helper methods (copied from User.java) ---
    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    private static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt.getBytes());
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not hash password", e);
        }
    }

    // =========================================================================
    // ORDER METHODS (ALL NEW)
    // =========================================================================

    /**
     * Saves a completed order to the database. This is a transaction.
     * @return true if the save was successful.
     */
    public static boolean saveOrder(User user, Order order) {
        String insertOrderSql = "INSERT INTO orders (user_id, order_date, total_price, order_id_string) VALUES (?, ?, ?, ?);";
        String insertItemSql = "INSERT INTO order_items (order_id, product_id, quantity, price_per_item) VALUES (?, ?, ?, ?);";
        
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false); 
            
            int dbOrderId; 
            
            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, user.getId()); 
                orderStmt.setTimestamp(2, java.sql.Timestamp.valueOf(order.getOrderDate()));
                orderStmt.setDouble(3, order.getTotalAmount());
                orderStmt.setString(4, order.getOrderId());
                
                orderStmt.executeUpdate();
                
                try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        dbOrderId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            }
            
            try (PreparedStatement itemStmt = conn.prepareStatement(insertItemSql)) {
                for (OrderItem item : order.getItems()) {
                    itemStmt.setInt(1, dbOrderId);
                    itemStmt.setInt(2, item.getProductId()); 
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setDouble(4, item.getPrice());
                    itemStmt.addBatch(); 
                }
                itemStmt.executeBatch(); 
            }
            
            conn.commit(); 
            return true;

        } catch (SQLException e) {
            System.err.println("Transaction failed. Rolling back changes.");
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); 
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); 
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Loads all order history for a given user from the database.
     */
    public static List<Order> getOrderHistory(User user) {
        List<Order> orderHistory = new ArrayList<>();
        String orderSql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC;";
        String itemSql = "SELECT oi.*, p.name " +
                         "FROM order_items oi " +
                         "JOIN products p ON oi.product_id = p.id " +
                         "WHERE oi.order_id = ?;";

        try (Connection conn = getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(orderSql)) {
             
            orderStmt.setInt(1, user.getId());
            
            try (ResultSet orderRs = orderStmt.executeQuery()) {
                while (orderRs.next()) {
                    String orderIdStr = orderRs.getString("order_id_string");
                    LocalDateTime orderDate = orderRs.getTimestamp("order_date").toLocalDateTime();
                    double totalAmount = orderRs.getDouble("total_price");
                    int dbOrderId = orderRs.getInt("id"); 

                    List<OrderItem> itemsForThisOrder = new ArrayList<>();

                    try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                        itemStmt.setInt(1, dbOrderId);
                        try (ResultSet itemRs = itemStmt.executeQuery()) {
                            while (itemRs.next()) {
                                itemsForThisOrder.add(new OrderItem(
                                    itemRs.getInt("product_id"),
                                    itemRs.getString("p.name"), 
                                    itemRs.getInt("quantity"),
                                    itemRs.getDouble("price_per_item")
                                ));
                            }
                        }
                    }
                    
                    orderHistory.add(new Order(orderIdStr, orderDate, totalAmount, itemsForThisOrder));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading order history:");
            e.printStackTrace();
        }
        return orderHistory;
    }

    /**
     * Deletes all orders from the database that are older than 7 days.
     */
    public static void cleanupOldOrders() {
        String sql = "DELETE FROM orders WHERE order_date < NOW() - INTERVAL 7 DAY;";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Cleanup successful: Deleted " + rowsDeleted + " old order(s).");
            } else {
                System.out.println("Order cleanup ran: No old orders to delete.");
            }
             
        } catch (SQLException e) {
            System.err.println("Error cleaning up old orders:");
            e.printStackTrace();
        }
    }
}