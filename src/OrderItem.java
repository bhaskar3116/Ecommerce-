public class OrderItem {
    private final int productId; // <-- NEW FIELD
    private final String productName;
    private final int quantity;
    private final double price;

    // Updated constructor for creating from a CartItem
    public OrderItem(Cart.CartItem cartItem) {
        this.productId = cartItem.getProduct().getId(); // <-- ADDED THIS
        this.productName = cartItem.getProduct().getName();
        this.quantity = cartItem.getQuantity();
        this.price = cartItem.getProduct().getPrice();
    }

    // --- NEW CONSTRUCTOR ---
    // This is used when loading an item FROM the database
    public OrderItem(int productId, String productName, int quantity, double price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters
    public int getProductId() { return productId; } // <-- NEW GETTER
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}