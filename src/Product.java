public class Product {
    private final int id;
    private final String name;
    private final double price;
    private final String imagePath;
    private final String category;
    private int stockQuantity;

    public Product(int id, String name, double price, String imagePath, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.category = category;
        this.stockQuantity = 10; // Default stock
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImagePath() { return imagePath; }
    public String getCategory() { return category; }
    public int getStockQuantity() { return stockQuantity; }
    public boolean isInStock() { return stockQuantity > 0; }

    // Setter
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
