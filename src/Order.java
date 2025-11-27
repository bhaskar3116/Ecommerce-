import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private final String orderId;
    private final LocalDateTime orderDate;
    private final List<OrderItem> items;
    private final double totalAmount;

    // This constructor is used when creating a NEW order from the cart
    public Order(List<Cart.CartItem> cartItems, double totalAmount) {
        this.orderId = "ORD-" + System.currentTimeMillis();
        this.orderDate = LocalDateTime.now();
        this.totalAmount = totalAmount;
        this.items = cartItems.stream()
                .map(OrderItem::new) // Uses the OrderItem(CartItem) constructor
                .collect(java.util.stream.Collectors.toList());
    }

    // --- NEW CONSTRUCTOR ---
    // This is used when loading an EXISTING order from the database
    public Order(String orderId, LocalDateTime orderDate, double totalAmount, List<OrderItem> items) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.items = items;
    }

    // Getters
    public String getOrderId() { return orderId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public List<OrderItem> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
}