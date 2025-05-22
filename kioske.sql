-- Table for Product
CREATE TABLE IF NOT EXISTS Product
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description TEXT           NOT NULL,
    price       DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    image_url   VARCHAR(500),
    image       LONGBLOB,
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
CREATE TABLE Order_ProductQuantity (
  id INT AUTO_INCREMENT PRIMARY KEY,
  order_id INT,
  product_id INT,
  quantity INT
);

CREATE TABLE Order_ProductQuantity_Ingredient (
  order_product_quantity_id INT,
  ingredient_id INT,
  quantity INT,
  PRIMARY KEY (order_product_quantity_id, ingredient_id)
);

INSERT INTO Product (name, description, price, image, image_url, specialLabel, isASide)
VALUES
    ('TRALALERO TRALALA', 'Sings arias that summon thunder ⚡🎤 Mamma mia, he *vibrato*-slaps!', 5.99, LOAD_FILE('/var/lib/mysql-files/assets/burger.jpg'), 'assets/burger.jpg', NULL, FALSE),

    ('BRR BRR PATAPIM', 'He enters. He toots. Reality shakes 🧀💥 *PATAPIM BRRR!!*', 8.99, LOAD_FILE('/var/lib/mysql-files/assets/pizza.jpg'), 'assets/pizza.jpg', '🔥 HOT HOT HOT', FALSE),

    ('TUNG TUNG TUNG TUNG SHAUR', 'Tung-powered tank from the dunes 💨 *TUNG* x4 = obliteration.', 4.99, LOAD_FILE('/var/lib/mysql-files/assets/salad.jpg'), 'assets/salad.jpg', NULL, FALSE),

    ('BOMBARDILO CROCODILO', 'Explodes into battle with toothy chaos 🐊💣 *BOOMbar-dilo!*', 1.99, LOAD_FILE('/var/lib/mysql-files/assets/soda.jpg'), 'assets/soda.jpg', NULL, TRUE),

    ('COCOSINO RHINO', 'Charges with coconut-powered rage 🌴💢 — BONK goes the rival!', 2.99, LOAD_FILE('/var/lib/mysql-files/assets/fries.jpg'), 'assets/fries.jpg', NULL, FALSE),

    ('BONECA AMVALABU', 'Skeleton doll with chaotic energy 😵‍💫💀 – it giggles in Morse code.', 3.99, LOAD_FILE('/var/lib/mysql-files/assets/ice_cream.jpg'), 'assets/ice_cream.jpg', NULL, FALSE),

    ('ANANITTO GIRAFFINI', 'Neck-a so long it pierces the *cheese stratosphere* 🧀🦒', 7.99, LOAD_FILE('/var/lib/mysql-files/assets/pasta.jpg'), 'assets/pasta.jpg', '🤑 DEAL OF DESTINY', FALSE),

    ('PANINO DEL DESTINO', 'Still a panino, but destiny awaits inside... probably Tob Tobi.', 4.49, LOAD_FILE('/var/lib/mysql-files/assets/sandwich.jpg'), 'assets/sandwich.jpg', NULL, FALSE),

    ('CAPUCCINO ASASHINO', 'Caffeinated shinobi ☕🥷 Disappears between sips. *Zoom!*', 2.49, LOAD_FILE('/var/lib/mysql-files/assets/coffee.jpg'), 'assets/coffee.jpg', NULL, FALSE),

    ('TOB TOBI TOB TOB TOBI TOB', 'Too many Tobis. Echoes of barks in the void 🐾🌀 *BARK²*.', 1.49, LOAD_FILE('/var/lib/mysql-files/assets/tea.jpg'), 'assets/tea.jpg', NULL, FALSE),

    ('TRIPY TROPHY', 'Won ALL the imaginary Olympics 🏆🦓 – even the sideways ones.', 3.49, LOAD_FILE('/var/lib/mysql-files/assets/cake.jpg'), 'assets/cake.jpg', NULL, FALSE);


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
    code        VARCHAR(255)   NOT NULL UNIQUE,
    discount    INT NOT NULL CHECK (discount >= 0),
    PRIMARY KEY (code)
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

CREATE TABLE IF NOT EXISTS Ingredients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0)
);

CREATE TABLE IF NOT EXISTS Product_ingredients (
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

INSERT INTO Product_ingredients (product_id, ingredients_id)
VALUES
(1, 1),
(1, 2),  
(1, 3);

ALTER TABLE Orders
    ADD COLUMN coupon_code VARCHAR(255),
    ADD COLUMN is_member BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN member_id INT,
    ADD COLUMN is_receipt BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN type ENUM('EAT_IN', 'TAKEAWAY', 'DELIVERY') DEFAULT 'EAT_IN',
    ADD COLUMN is_paid BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD FOREIGN KEY (coupon_code) REFERENCES Coupons(code);
