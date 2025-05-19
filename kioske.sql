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
    id INT AUTO_INCREMENT PRIMARY KEY,
    feedback INT NOT NULL
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
       ('Pizza', 'A cheesy pizza with toppings', 8.99, 'assets/pizza.jpg', 'Hot', FALSE),
       ('Salad', 'A fresh garden salad', 4.99, 'assets/salad.jpg', NULL, FALSE),
       ('Soda', 'A refreshing soda drink', 1.99, 'assets/soda.jpg', NULL, TRUE),
       ('Fries', 'Crispy french fries', 2.99, 'assets/fries.jpg', NULL, FALSE),
       ('Ice Cream', 'A scoop of ice cream', 3.99, 'assets/ice_cream.jpg', NULL, FALSE),
       ('Pasta', 'Creamy pasta with sauce', 7.99, 'assets/pasta.jpg', 'Deal', FALSE),
       ('Sandwich', 'A tasty sandwich with fillings', 4.49, 'assets/sandwich.jpg', NULL, FALSE),
       ('Coffee', 'A hot cup of coffee', 2.49, 'assets/coffee.jpg', NULL, FALSE),
       ('Tea', 'A soothing cup of tea', 1.49, 'assets/tea.jpg', NULL, FALSE),
       ('Cake', 'A slice of chocolate cake', 3.49, 'assets/cake.jpg', NULL, FALSE);

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

CREATE TABLE IF NOT EXISTS Members
(
    personal_number INT NOT NULL,
    points INT NOT NULL
);

INSERT INTO Members (personal_number, points) VALUES
('12345', '0'),
('6969','0');

CREATE TABLE Campaign (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    type ENUM('DISCOUNT', 'BUNDLE', 'BUY_ONE_GET_ONE') NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    image_url VARCHAR(1024) NOT NULL
);

INSERT INTO Campaign (name, description, type, start_date, end_date, image_url)
VALUES
('Summer Sale', 'Get ready for summer with our special discounts!', 'DISCOUNT', '2025-05-01', '2025-08-31', 'assets/campaign1.jpg'),
('Winter Wonderland', 'Warm up with our winter specials!', 'BUNDLE', '2025-12-01', '2025-08-28', 'assets/campaign2.jpg'),
('Buy One Get One Free', 'Buy one item and get another one free!', 'BUY_ONE_GET_ONE', '2025-05-01', '2025-11-30', 'assets/campaign3.jpg');

CREATE TABLE IF NOT EXISTS Translations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    original_text_hash VARCHAR(64) NOT NULL, -- Using VARCHAR for hash
    original_text TEXT,
    source_lang VARCHAR(10) NOT NULL,
    target_lang VARCHAR(10) NOT NULL,
    translated_text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (original_text_hash, source_lang, target_lang)
);

CREATE IF NOT EXISTS Ingredients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
);

CREATE IF NOT EXISTS Product_Indgredients (
    product_id INT NOT NULL,
    ingredients_id INT NOT NULL,
    PRIMARY KEY (product_id, ingredients_id),
    FOREIGN KEY (product_id) REFERENCES Product(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredients_id) REFERENCES Ingredients(id) ON DELETE CASCADE

);

INSERT INTO Ingredients (name, price)
VALUES 

('Lettuce', 0.50),
('Tomato', 0.60),
('Cheese', 0.75);
