# ShopEasy - ECommerce Cart Application

ShopEasy is a Java Swing-based eCommerce application that provides a user-friendly interface for browsing products, managing a shopping cart, and processing orders. It features user authentication, product filtering, search functionality, and order history tracking.

## Features

- **User Authentication**: Secure login and registration system with password hashing and salting.
- **Product Browsing**: Display products in a grid layout with images, names, prices, and categories.
- **Search and Filtering**: Real-time search by product name and filtering by categories (All, Mens, Womens, Shoes, Electronics, Health).
- **Sorting Options**: Sort products by price (low to high, high to low) or name (A to Z, Z to A).
- **Shopping Cart**: Add/remove items, view cart contents, and calculate totals.
- **Order Processing**: Simulate payment processing and save orders to the database.
- **Order History**: View past orders with details of purchased items.
- **Admin Panel**: Separate admin login for managing products (add new products).
- **Database Integration**: MySQL database for storing users, products, and orders.
- **Responsive UI**: Modern, responsive design using Java Swing with custom styling.

## Prerequisites

Before running the application, ensure you have the following installed:

- **Java Development Kit (JDK)**: Version 8 or higher
- **MySQL Server**: For the database backend
- **MySQL Connector/J**: JDBC driver for Java-MySQL connectivity
- **Eclipse IDE** or any Java IDE (recommended for development)

## Installation

1. **Clone or Download the Project**:
   - Download the project files to your local machine.

2. **Set Up the Database**:
   - Install and start MySQL Server.
   - Create a database named `shopeasy_db`.
   - Run the following SQL scripts to create the necessary tables:

     ```sql
     CREATE TABLE users (
         id INT AUTO_INCREMENT PRIMARY KEY,
         username VARCHAR(50) UNIQUE NOT NULL,
         salt VARCHAR(255) NOT NULL,
         hash VARCHAR(255) NOT NULL
     );

     CREATE TABLE products (
         id INT AUTO_INCREMENT PRIMARY KEY,
         name VARCHAR(100) NOT NULL,
         price DECIMAL(10, 2) NOT NULL,
         imagePath VARCHAR(255),
         category VARCHAR(50)
     );

     CREATE TABLE orders (
         id INT AUTO_INCREMENT PRIMARY KEY,
         user_id INT NOT NULL,
         order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
         total_price DECIMAL(10, 2) NOT NULL,
         order_id_string VARCHAR(50) UNIQUE NOT NULL,
         FOREIGN KEY (user_id) REFERENCES users(id)
     );

     CREATE TABLE order_items (
         id INT AUTO_INCREMENT PRIMARY KEY,
         order_id INT NOT NULL,
         product_id INT NOT NULL,
         quantity INT NOT NULL,
         price_per_item DECIMAL(10, 2) NOT NULL,
         FOREIGN KEY (order_id) REFERENCES orders(id),
         FOREIGN KEY (product_id) REFERENCES products(id)
     );
     ```

   - Insert some sample products into the `products` table:

     ```sql
     INSERT INTO products (name, price, imagePath, category) VALUES
     ('T-Shirt', 19.99, 'images/tshirt.jpg', 'Mens'),
     ('Dress', 49.99, 'images/dress.jpg', 'Womens'),
     ('Sneakers', 79.99, 'images/sneakers.jpg', 'Shoes'),
     ('Laptop', 999.99, 'images/laptop.jpg', 'Electronics'),
     ('Vitamins', 29.99, 'images/vitamins.jpg', 'Health');
     ```

3. **Configure Database Connection**:
   - Open `src/DBManager.java`.
   - Update the database connection details:
     ```java
     private static final String DB_URL = "jdbc:mysql://localhost:3306/shopeasy_db";
     private static final String DB_USER = "your_mysql_username";
     private static final String DB_PASSWORD = "your_mysql_password";
     ```

4. **Add MySQL Connector/J to Project**:
   - Download the MySQL Connector/J JAR file (e.g., `mysql-connector-java-8.0.33.jar`).
   - Add it to your project's build path in Eclipse:
     - Right-click on the project > Properties > Java Build Path > Libraries > Add External JARs.
   - Alternatively, place the JAR in the `lib/` folder and ensure it's included in the classpath.

5. **Compile and Run**:
   - In Eclipse, right-click on the project and select "Run As" > "Java Application".
   - Select `Main` as the main class to run.

## Usage

1. **Launch the Application**:
   - Run the `Main` class to start the application.
   - Select your role: "Login as User" or "Login as Admin".

2. **User Registration/Login**:
   - If new, register with a username and password.
   - Login with existing credentials.

3. **Browse Products**:
   - View products in a grid layout.
   - Use the search bar for real-time filtering.
   - Apply category filters and sorting options.

4. **Manage Cart**:
   - Click "Add to Cart" on product panels.
   - View cart by clicking "🛒 View Cart".
   - Adjust quantities or remove items in the cart page.

5. **Checkout and Payment**:
   - Proceed to checkout from the cart page.
   - Simulate payment (90% success rate for demo).
   - View order confirmation upon successful payment.

6. **Order History**:
   - Click "Returns & Orders" to view past purchases.

7. **Admin Functions**:
   - Login as admin to access the admin dashboard.
   - Add new products with name, price, image path, and category.

## Project Structure

```
ecommerce-cart/
├── src/
│   ├── AddProductForm.java      # Form for adding new products (Admin)
│   ├── AdminDashboard.java      # Admin dashboard for product management
│   ├── AdminLogin.java          # Admin login page
│   ├── Cart.java                # Shopping cart model
│   ├── CartPage.java            # Cart view and management
│   ├── DBManager.java           # Database connection and operations
│   ├── LoadingDialog.java       # Loading dialog for async operations
│   ├── LoginPage.java           # User login page
│   ├── Main.java                # Application entry point
│   ├── ModernUIUtils.java       # UI utility classes
│   ├── Order.java               # Order model
│   ├── OrderHistoryPage.java    # Order history view
│   ├── OrderItem.java           # Order item model
│   ├── PaymentSuccessDialog.java # Payment success dialog
│   ├── Product.java             # Product model
│   ├── ProductPanel.java        # Product display panel
│   ├── RegisterPage.java        # User registration page
│   ├── RoundedBorder.java       # Custom border for UI elements
│   ├── ShopApp.java             # Main shopping application UI
│   ├── User.java                # User model
│   ├── UserManager.java         # User management utilities
│   ├── WrapLayout.java          # Custom layout manager
│   └── myui/                    # Additional UI components
├── bin/                         # Compiled class files
├── lib/
│   └── json-20250517.jar        # JSON library (if used)
├── .classpath                   # Eclipse classpath file
├── .project                     # Eclipse project file
└── README.md                    # This file
```

## Technologies Used

- **Java**: Core programming language
- **Java Swing**: GUI framework
- **MySQL**: Database management system
- **JDBC**: Java Database Connectivity
- **Eclipse**: IDE for development

## Contributing

1. Fork the repository.
2. Create a new branch for your feature.
3. Make your changes and test thoroughly.
4. Submit a pull request with a clear description of your changes.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Project Screenshots

### 1. Role Selection Screen
![alt text](<SS/Screenshot 2025-11-27 212427.png>)
*The initial screen where users select their role (User or Admin).*

### 2. User Login Page
![alt text](<SS/Screenshot 2025-11-27 212838.png>)
*Login page for existing users to access the shopping application.*

### 3. User Registration Page
![alt text](<SS/Screenshot 2025-11-27 212809.png>)
*Registration form for new users to create an account.*

### 4. Main Shopping Interface
![alt text](<SS/Screenshot 2025-11-27 213022.png>)
*The main product browsing interface with search, filters, and product grid.*

### 5. Product Details and Cart Addition
![alt text](<SS/Screenshot 2025-11-27 213145.png>)
*Detailed view of a product with add to cart functionality.*

### 6. Shopping Cart Page
![alt text](<SS/Screenshot 2025-11-27 213327.png>)
*Cart page showing selected items, quantities, and total price.*

### 7. Checkout and Payment Simulation
![alt text](<SS/Screenshot 2025-11-27 213426.png>)
*Checkout process with payment simulation.*

### 8. Payment Success Dialog
![alt text](<SS/Screenshot 2025-11-27 213355.png>)
*Confirmation dialog after successful payment.*

### 9. Order History Page
![alt text](<SS/Screenshot 2025-11-27 213511.png>)
*Page displaying user's past orders and purchase details.*

### 10. Admin Login Page
![alt text](<SS/Screenshot 2025-11-27 212443.png>)
*Login page for administrators.*

### 11. Admin Dashboard
![alt text](<SS/Screenshot 2025-11-27 212450.png>)
*Admin interface for managing products and viewing system information.*

### 12. Add Product Form (Admin)
![alt text](<SS/Screenshot 2025-11-27 212640.png>)
*Form for administrators to add new products to the catalog.*

*Note: Replace the placeholder image paths with actual screenshots of your running application. Capture screenshots at various resolutions to showcase the responsive design.*
