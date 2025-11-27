import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;
    private double subtotal;
    private double taxAmount;
    private double shippingCost;
    private double discount;
    private double total;
    private final double freeShippingThreshold;

    public Cart() {
        this.items = new ArrayList<>();
        this.freeShippingThreshold = 500.0;
        updateTotals();
    }

    public Cart(String cartId, String userId) {
        this();
    }

    public void addItem(Product product, int quantity, String selectedSize, String selectedColor) {
        String itemKey = generateItemKey(String.valueOf(product.getId()), selectedSize, selectedColor);
        CartItem existingItem = findItemByKey(itemKey);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            items.add(new CartItem(product, quantity, selectedSize, selectedColor));
        }
        updateTotals();
    }

    public void updateItemQuantity(String itemKey, int newQuantity) {
        CartItem item = findItemByKey(itemKey);
        if (item != null) {
            if (newQuantity <= 0) {
                removeItem(itemKey);
            } else {
                item.setQuantity(newQuantity);
                updateTotals();
            }
        }
    }

    public void removeItem(String itemKey) {
        items.removeIf(item -> item.getItemKey().equals(itemKey));
        updateTotals();
    }

    public void updateTotals() {
        this.subtotal = items.stream().mapToDouble(CartItem::getSubtotal).sum();
        this.taxAmount = this.subtotal * 0.18; // 18% GST
        this.shippingCost = calculateShippingCost();
        this.total = this.subtotal + this.taxAmount + this.shippingCost - this.discount;
    }
    
    private double calculateShippingCost() {
        if (items.isEmpty()) {
            return 0.0;
        }
        return subtotal >= freeShippingThreshold ? 0.0 : 50.0;
    }

    public double getAmountForFreeShipping() {
        boolean isEligible = subtotal >= freeShippingThreshold;
        return isEligible ? 0.0 : freeShippingThreshold - subtotal;
    }
    
    public boolean isFreeShippingEligible() {
        return subtotal >= freeShippingThreshold;
    }

    public int getTotalItemCount() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }
    
    public void clearCart() {
        items.clear();
        updateTotals();
    }

    private String generateItemKey(String productId, String size, String color) {
        return productId + "_" + (size != null ? size : "na") + "_" + (color != null ? color : "na");
    }

    // ** THIS IS THE CORRECTED LINE **
    public CartItem findItemByKey(String itemKey) {
        for(CartItem item : items) {
            if(item.getItemKey().equals(itemKey)) {
                return item;
            }
        }
        return null;
    }

    // Getters
    public List<CartItem> getItems() { return items; }
    public double getSubtotal() { return subtotal; }
    public double getTaxAmount() { return taxAmount; }
    public double getShippingCost() { return shippingCost; }
    public double getDiscount() { return discount; }
    public double getTotal() { return total; }
    
    // Inner class for CartItem
    public static class CartItem {
        private final Product product;
        private int quantity;
        private final String itemKey;

        public CartItem(Product product, int quantity, String selectedSize, String selectedColor) {
            this.product = product;
            this.quantity = quantity;
            this.itemKey = product.getId() + "_" + (selectedSize != null ? selectedSize : "na") + "_" + (selectedColor != null ? selectedColor : "na");
        }

        public double getSubtotal() { return product.getPrice() * quantity; }
        public Product getProduct() { return product; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public String getItemKey() { return itemKey; }
    }
}