-- Table for Product
CREATE TABLE Product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    image_url VARCHAR(500)
);

-- Table for Order
CREATE TABLE Orders (
    id INT AUTO_INCREMENT PRIMARY KEY
);

-- Table for ProductQuantity (associates products with orders and quantities)
CREATE TABLE Order_ProductQuantity (
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity >= 0),
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES Orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Product(id)
);

INSERT INTO Product (name, description, price, image_url) VALUES
('Burger', 'A delicious beef burger', 5.99, 'burger.jpg'),
('Pizza', 'A cheesy pizza with toppings', 8.99, 'pizza.jpg'),
('Salad', 'A fresh garden salad', 4.99, 'salad.jpg'),
('Soda', 'A refreshing soda drink', 1.99, 'soda.jpg'),
('Fries', 'Crispy french fries', 2.99, 'fries.jpg'),
('Ice Cream', 'A scoop of ice cream', 3.99, 'ice_cream.jpg'),
('Pasta', 'Creamy pasta with sauce', 7.99, 'pasta.jpg'),
('Sandwich', 'A tasty sandwich with fillings', 4.49, 'sandwich.jpg'),
('Coffee', 'A hot cup of coffee', 2.49, 'coffee.jpg'),
('Tea', 'A soothing cup of tea', 1.49, 'tea.jpg'),
('Cake', 'A slice of chocolate cake', 3.49, 'cake.jpg');
