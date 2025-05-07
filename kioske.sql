-- Table for Product
CREATE TABLE IF NOT EXISTS Product
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description TEXT           NOT NULL,
    price       DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    image_url   VARCHAR(500),
    specialLabel       VARCHAR(255),
    isASide     BOOLEAN NOT NULl DEFAULT FALSE
);

-- Table for Order
CREATE TABLE IF NOT EXISTS Orders
(
    id INT AUTO_INCREMENT PRIMARY KEY
);

-- Table for ProductQuantity (associates products with orders and quantities)
CREATE TABLE IF NOT EXISTS Order_ProductQuantity
(
    order_id   INT NOT NULL,
    product_id INT NOT NULL,
    quantity   INT NOT NULL CHECK (quantity >= 0),
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES Orders (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Product (id)
);

INSERT INTO Product (name, description, price, image_url, specialLabel, isASide)
VALUES ('Burger', 'A delicious beef burger', 5.99, 'assets/burger.jpg', NULL, FALSE),
       ('Pizza', 'A cheesy pizza with toppings', 8.99, 'pizza.jpg', 'Hot', FALSE),
       ('Salad', 'A fresh garden salad', 4.99, 'salad.jpg', NULL, FALSE),
       ('Soda', 'A refreshing soda drink', 1.99, 'soda.jpg', NULL, TRUE),
       ('Fries', 'Crispy french fries', 2.99, 'fries.jpg', NULL, FALSE),
       ('Ice Cream', 'A scoop of ice cream', 3.99, 'ice_cream.jpg', NULL, FALSE),
       ('Pasta', 'Creamy pasta with sauce', 7.99, 'pasta.jpg', 'Deal', FALSE),
       ('Sandwich', 'A tasty sandwich with fillings', 4.49, 'sandwich.jpg', NULL, FALSE),
       ('Coffee', 'A hot cup of coffee', 2.49, 'coffee.jpg', NULL, FALSE),
       ('Tea', 'A soothing cup of tea', 1.49, 'tea.jpg', NULL, FALSE),
       ('Cake', 'A slice of chocolate cake', 3.49, 'cake.jpg', NULL, FALSE);

CREATE TABLE IF NOT EXISTS Admin
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

INSERT INTO Admin (username, password) VALUES
('root', 'root'),
('manager', 'manager');

-- Table for ProductCategory (associates products with categories)
CREATE TABLE IF NOT EXISTS Tags
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- Table for Product_Tag (associates products with tags)
CREATE TABLE IF NOT EXISTS Product_Tags
(
    product_id INT NOT NULL,
    tag_id     INT NOT NULL,
    PRIMARY KEY (product_id, tag_id),
    FOREIGN KEY (product_id) REFERENCES Product (id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES Tags (id) ON DELETE CASCADE
);

-- Sample data for Product_Tag
INSERT INTO Tags (name) VALUES
('Starters'),
('Main Course'),
('Desserts'),
('Beverages'),
('Snacks');

INSERT INTO Product_Tags (product_id, tag_id) VALUES
(1, 1), -- Burger
(2, 2); -- Pizza

CREATE TABLE IF NOT EXISTS Coupons
(
    code        VARCHAR(255)   NOT NULL,
    discount    INT NOT NULL CHECK (discount >= 0)
);

INSERT INTO Coupons (code, discount) VALUES
('code10', 10),
('code5', 5),
('code50', 50);
