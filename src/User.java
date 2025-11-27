import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class User {
    private int id; // Database ID. Default is 0 for newly registering users.
    private final String username;
    private final String hashedPassword;
    private final String salt;
    private final Cart cart;
    private final List<Order> orderHistory; // Stores past orders

    // Constructor for new user registration
    public User(String username, String password) {
        this.username = username;
        this.salt = generateSalt();
        this.hashedPassword = hashPassword(password, salt);
        this.cart = new Cart("cart_" + username, username);
        this.orderHistory = new ArrayList<>(); // Initialize the order list
    }
    
    // Constructor for loading users FROM THE DATABASE
    public User(int id, String username, String salt, String hashedPassword) {
        this.id = id; // Set the database ID
        this.username = username;
        this.salt = salt;
        this.hashedPassword = hashedPassword;
        this.cart = new Cart("cart_" + username, username);
        this.orderHistory = new ArrayList<>(); // Initialize the order list
    }

    /**
     * Adds a completed order to this user's history.
     * @param order The order to add.
     */
    public void addOrderToHistory(Order order) {
        this.orderHistory.add(order);
    }

    // --- Getters ---
    public int getId() { return id; } // <-- NEW GETTER
    public String getUsername() { return username; }
    public String getSalt() { return salt; }
    public String getHashedPassword() { return hashedPassword; }
    public Cart getCart() { return cart; }
    public List<Order> getOrderHistory() { return orderHistory; }

    // --- Security Methods ---
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    private String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt.getBytes());
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not hash password", e);
        }
    }

    public boolean checkPassword(String input) {
        String hashedInput = hashPassword(input, this.salt);
        return hashedInput != null && hashedInput.equals(this.hashedPassword);
    }
}