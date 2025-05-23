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


ALTER TABLE Product ADD sound LONGBLOB;

INSERT INTO Product (name, description, price, image, image_url, sound, specialLabel, isASide)
VALUES
    ('TRALALERO TRALALA', 'Sings arias that summon thunder ⚡🎤 Mamma mia, he *vibrato*-slaps!', 5.99,
     LOAD_FILE('/var/lib/mysql-files/assets/tralalero-tralala.jpg'), 'assets/tralalero-tralala.jpg',
     LOAD_FILE('/var/lib/mysql-files/assets/Voicy_Tralalero_Tralala_Italian_Brainrot.mp3'), NULL, FALSE),

    ('BRR BRR PATAPIM', 'He enters. He toots. Reality shakes 🧀💥 *PATAPIM BRRR!!*', 8.99,
     LOAD_FILE('/var/lib/mysql-files/assets/Brr_brr_patapim.jpg'), 'assets/Brr_brr_patapim.jpg',
     LOAD_FILE('/var/lib/mysql-files/assets/Brr_brr_patapim.mp3'), '🔥 HOT HOT HOT', FALSE),

    ('TUNG TUNG TUNG TUNG SHAUR', 'Tung-powered tank from the dunes 💨 *TUNG* x4 = obliteration.', 4.99,
     LOAD_FILE('/var/lib/mysql-files/assets/artworks-Ugyi6VK6utMMpLzA-iOuDVA-t500x500.jpeg'), 'assets/artworks-Ugyi6VK6utMMpLzA-iOuDVA-t500x500.jpeg',
     LOAD_FILE('/var/lib/mysql-files/assets/tung-tung-shaur.mp3'), NULL, FALSE),

    ('BOMBARDILO CROCODILO', 'Explodes into battle with toothy chaos 🐊💣 *BOOMbar-dilo!*', 1.99,
     LOAD_FILE('/var/lib/mysql-files/assets/img_4257.jpg'), 'assets/img_4257.jpg',
     LOAD_FILE('/var/lib/mysql-files/assets/bombardilo-crocodilo.mp3'), NULL, TRUE),

    ('COCOSINO RHINO', 'Charges with coconut-powered rage 🌴💢 — BONK goes the rival!', 2.99,
     LOAD_FILE('/var/lib/mysql-files/assets/GTA5-2025-05-07-17-55-51_269-e1746625550739.png'), 'assets/GTA5-2025-05-07-17-55-51_269-e1746625550739.png',
     LOAD_FILE('/var/lib/mysql-files/assets/cocosino-rhino.mp3'), NULL, FALSE),

    ('BONECA AMVALABU', 'Skeleton doll with chaotic energy 😵‍💫💀 – it giggles in Morse code.', 3.99,
     LOAD_FILE('/var/lib/mysql-files/assets/boneca-ambalabu-ezgif.com-webp-to-jpg-converter.jpg'), 'assets/boneca-ambalabu-ezgif.com-webp-to-jpg-converter.jpg',
     LOAD_FILE('/var/lib/mysql-files/assets/boneca-amvalabu.mp3'), NULL, FALSE),

    ('ANANITTO GIRAFFINI', 'Neck-a so long it pierces the *cheese stratosphere* 🧀🦒', 7.99,
     LOAD_FILE('/var/lib/mysql-files/assets/Images_-_2025-05-18T114613.167.jpg'), 'assets/Images_-_2025-05-18T114613.167.jpg',
     LOAD_FILE('/var/lib/mysql-files/assets/ananitto-giraffini.mp3'), '🤑 DEAL OF DESTINY', FALSE),

    ('PANINO DEL DESTINO', 'Still a panino, but destiny awaits inside... probably Tob Tobi.', 4.49,
     LOAD_FILE('/var/lib/mysql-files/assets/sandwich.jpg'), 'assets/sandwich.jpg',
     LOAD_FILE('/var/lib/mysql-files/assets/panino-destino.mp3'), NULL, FALSE),

    ('CAPUCCINO ASASHINO', 'Caffeinated shinobi ☕🥷 Disappears between sips. *Zoom!*', 2.49,
     LOAD_FILE('/var/lib/mysql-files/assets/images.jpeg'), 'assets/images.jpeg',
     LOAD_FILE('/var/lib/mysql-files/assets/capuccino-asashino.mp3'), NULL, FALSE),

    ('TOB TOBI TOB TOB TOBI TOB', 'Too many Tobis. Echoes of barks in the void 🐾🌀 *BARK²*.', 1.49,
     LOAD_FILE('/var/lib/mysql-files/assets/Tob_Tobi_Tob_Camel-ezgif.com-webp-to-jpg-converter.jpg'), 'assets/Tob_Tobi_Tob_Camel-ezgif.com-webp-to-jpg-converter.jpg',
     LOAD_FILE('/var/lib/mysql-files/assets/tob-tobi.mp3'), NULL, FALSE),

    ('TRIPY TROPHY', 'Won ALL the imaginary Olympics 🏆🦓 – even the sideways ones.', 3.49,
     LOAD_FILE('/var/lib/mysql-files/assets/lets-settle-the-debate-who-is-tripi-tropi-v0-t4elw6moq0ue1.jpg'), 'assets/lets-settle-the-debate-who-is-tripi-tropi-v0-t4elw6moq0ue1.jpg',
     LOAD_FILE('/var/lib/mysql-files/assets/tripy-trophy.mp3'), NULL, FALSE);

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

INSERT INTO Ingredients (name, price) VALUES
                                          ('Formaggio Esplosivo', 0.99),
                                          ('Pomodoro Urlante', 0.89),
                                          ('Foglie del Caos', 0.79),
                                          ('Beef-a del Supremo', 1.49),
                                          ('Crocchetta dell\'Antico Imperio', 0.69),
                                          ('Bolla Frizzantina', 0.59),
                                          ('Ghiaccio del Vulcano', 0.39),
                                          ('Melone Mistico', 0.99),
                                          ('Pasta Drammatica', 1.29),
                                          ('Caffè della Nonna', 0.89),
                                          ('Tè della Pace Interna', 0.69),
                                          ('Dolcezza di Coccoboom', 1.09),
                                          ('Pane della Destinazione', 0.89),
                                          ('Mozzarella Patapim', 0.99),
                                          ('Zucchero dell’Amore', 0.59),
                                          ('Shaur Spice Mix', 0.79),
                                          ('Salsa Tralalala', 0.89),
                                          ('Croco Boom Crunch', 1.19);

-- Tralalero Tralala
INSERT INTO Product_ingredients VALUES
                                    (1, 17), -- Salsa Tralalala
                                    (1, 4);  -- Beef-a del Supremo

-- Brr Brr Patapim
INSERT INTO Product_ingredients VALUES
                                    (2, 14), -- Mozzarella Patapim
                                    (2, 1),  -- Formaggio Esplosivo
                                    (2, 2);  -- Pomodoro Urlante

-- Tung Tung Tung Tung Shaur
INSERT INTO Product_ingredients VALUES
                                    (3, 16), -- Shaur Spice Mix
                                    (3, 3);  -- Foglie del Caos

-- Bombardilo Crocodilo
INSERT INTO Product_ingredients VALUES
                                    (4, 18), -- Croco Boom Crunch
                                    (4, 6);  -- Bolla Frizzantina

-- Cocosino Rhino
INSERT INTO Product_ingredients VALUES
                                    (5, 4),  -- Beef-a del Supremo
                                    (5, 5);  -- Crocchetta dell'Antico Imperio

-- Boneca Amvalabu
INSERT INTO Product_ingredients VALUES
                                    (6, 15), -- Zucchero dell’Amore
                                    (6, 12); -- Dolcezza di Coccoboom

-- Ananitto Giraffini
INSERT INTO Product_ingredients VALUES
                                    (7, 8),  -- Melone Mistico
                                    (7, 9);  -- Pasta Drammatica

-- Panino del Destino
INSERT INTO Product_ingredients VALUES
                                    (8, 13), -- Pane della Destinazione
                                    (8, 4);  -- Beef-a del Supremo

-- Capuccino Asashino
INSERT INTO Product_ingredients VALUES
    (9, 10); -- Caffè della Nonna

-- Tob Tobi Tob Tob Tobi Tob
INSERT INTO Product_ingredients VALUES
    (10, 11); -- Tè della Pace Interna

-- Tripy Trophy
INSERT INTO Product_ingredients VALUES
                                    (11, 1),  -- Formaggio Esplosivo
                                    (11, 8);  -- Melone Mistico


ALTER TABLE Orders
    ADD COLUMN coupon_code VARCHAR(255),
    ADD COLUMN is_member BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN member_id INT,
    ADD COLUMN is_receipt BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN type ENUM('EAT_IN', 'TAKEAWAY', 'DELIVERY') DEFAULT 'EAT_IN',
    ADD COLUMN is_paid BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD FOREIGN KEY (coupon_code) REFERENCES Coupons(code);
