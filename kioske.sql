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
ALTER TABLE Product ADD sound LONGBLOB;

CREATE TABLE Order_ProductQuantity_Ingredient (
  order_product_quantity_id INT,
  ingredient_id INT,
  quantity INT,
  PRIMARY KEY (order_product_quantity_id, ingredient_id)
);

INSERT INTO Product (name, description, price, image, image_url, sound, specialLabel, isASide)
VALUES
    ('Rey’s Portion Stew', 'Self-rising bread and stew from Rey’s survival kit on Jakku.', 5.99,
     LOAD_FILE('/var/lib/mysql-files/assets/PolystarchBread-ezgif.com-webp-to-jpg-converter.jpg'), 'assets/PolystarchBread-ezgif.com-webp-to-jpg-converter.jpg',
     LOAD_FILE('/var/lib/mysql-files/assets/Voicy_Tralalero_Tralala_Italian_Brainrot.mp3'), NULL, FALSE),

    ('Roasted Porg Delight', 'Chargrilled Porg, Chewie-style, as seen on Ahch-To.', 8.99,
     LOAD_FILE('/var/lib/mysql-files/assets/disney-galaxys-edge-porg-on-a-stick.jpg'), 'assets/disney-galaxys-edge-porg-on-a-stick.jpg',
     LOAD_FILE('/var/lib/mysql-files/assets/Brr_brr_patapim.mp3'), '🔥 Yodas fav', FALSE),

    ('Crispy Nuna Legs', 'Deep-fried Nuna bird drumsticks – a Coruscant street favorite.', 4.99,
     LOAD_FILE('/var/lib/mysql-files/assets/Roast_nuna-ezgif.com-webp-to-jpg-converter.jpg'), 'assets/Roast_nuna-ezgif.com-webp-to-jpg-converter.jpg',
     LOAD_FILE('/var/lib/mysql-files/assets/tung-tung-shaur.mp3'), NULL, FALSE),

    ('Endorian Tip-Yip Platter', 'Tender poultry served Endor-style with root mash.', 1.99,
     LOAD_FILE('/var/lib/mysql-files/Fried_Endorian_Tip-Yip-SWGE-ezgif.com-webp-to-jpg-converter.jpg'), 'assets/Fried_Endorian_Tip-Yip-SWGE-ezgif.com-webp-to-jpg-converter.jpg',
     LOAD_FILE('/var/lib/mysql-files/assets/bombardilo-crocodilo.mp3'), NULL, TRUE),

    ('Chewie’s Yobacca Noodles', 'Savory, chewy noodles inspired by the mighty Wookiee.', 2.99,
     LOAD_FILE('/var/lib/mysql-files/assets/QEAPO1jv3cTCP4qDlNa3Tt0KkjtJw7dt-yW0xMgPzNo-ezgif.com-webp-to-jpg-converter.jpg'), 'assets/QEAPO1jv3cTCP4qDlNa3Tt0KkjtJw7dt-yW0xMgPzNo-ezgif.com-webp-to-jpg-converter.jpg',
     LOAD_FILE('/var/lib/mysql-files/assets/cocosino-rhino.mp3'), 'Chewbacca recommends', FALSE),

    ('Meiloorun Medley', 'Juicy fruit platter featuring Hera’s favorite – the Meiloorun.', 3.99,
     LOAD_FILE('/var/lib/mysql-files/assets/images (1).jpeg'), 'assets/images (1).jpeg',
     LOAD_FILE('/var/lib/mysql-files/assets/boneca-amvalabu.mp3'), NULL, FALSE),

    ('Nerf Nuggets Supreme', 'Crispy golden nuggets from the finest nerf herds.', 7.99,
     LOAD_FILE('/var/lib/mysql-files/assets/images (2).jpeg'), 'assets/Images_-_2025-05-18T114613.167.jpg',
     LOAD_FILE('/var/lib/mysql-files/assets/ananitto-giraffini.mp3'), '🤑 DEAL OF DESTINY', FALSE),

    ('Dagobah Rootleaf Stew', 'Slow-simmered swampy stew just like Yoda makes.', 4.49,
     LOAD_FILE('/var/lib/mysql-files/assets/image_a11fbf10.jpeg'), 'assets/image_a11fbf10.jpeg',
     LOAD_FILE('/var/lib/mysql-files/assets/panino-destino.mp3'), NULL, FALSE),

    ('Jedi Roast Espresso', 'Bold and swift – a shinobi’s caffeine fix from the Outer Rim.', 2.49,
     LOAD_FILE('/var/lib/mysql-files/assets/images.jpeg'), 'assets/images.jpeg',
     LOAD_FILE('/var/lib/mysql-files/assets/capuccino-asashino.mp3'), NULL, FALSE),

    ('Tob-Tobi Galactic Bites', 'Echoed across systems – the Tob Tobi crunch lives on.', 1.49,
     LOAD_FILE('/var/lib/mysql-files/assets/Tob_Tobi_Tob_Camel-ezgif.com-webp-to-jpg-converter.jpg'), 'assets/Tob_Tobi_Tob_Camel-ezgif.com-webp-to-jpg-converter.jpg',
     LOAD_FILE('/var/lib/mysql-files/assets/tob-tobi.mp3'), NULL, FALSE),

    ('Tripi-Tropi Medallion Cake', 'A dessert fit for a rebel champion – sideways wins included.', 3.49,
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
(1, 1),
(2, 2),
(3,5),
(4, 1),
(5, 2),
(6, 3),
(7, 4),
(8, 1),
(9, 5),
(10, 3),
(11, 2);


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
                                          ('Explosive Bantha Cheese', 0.99),
                                          ('Screaming Tatooine Tomato', 0.89),
                                          ('Dagobah Chaos Leaves', 0.79),
                                          ('Supreme Nerf Beef', 1.49),
                                          ('Ancient Empire Croquette', 0.69),
                                          ('Fizzbubble Orb', 0.59),
                                          ('Lava Crystal Ice', 0.39),
                                          ('Mystic Meiloorun Chunk', 0.99),
                                          ('Dramatic Mustafar Pasta', 1.29),
                                          ('Grandma Jedi Brew', 0.89),
                                          ('Peaceful Monastery Tea', 0.69),
                                          ('CocoBoom Sweet Essence', 1.09),
                                          ('Bread of Destiny', 0.89),
                                          ('Mozzarella of the Porgs', 0.99),
                                          ('Sugar of Galactic Love', 0.59),
                                          ('Shaurian Spice Mix', 0.79),
                                          ('Tralalala Sauce of Fate', 0.89),
                                          ('Crocoboom Crunch Flakes', 1.19);

-- Rey’s Portion Stew
INSERT INTO Product_ingredients VALUES
                                    (1, 17), -- Tralalala Sauce of Fate
                                    (1, 4);  -- Supreme Nerf Beef

-- Roasted Porg Delight
INSERT INTO Product_ingredients VALUES
                                    (2, 14), -- Mozzarella of the Porgs
                                    (2, 1),  -- Explosive Bantha Cheese
                                    (2, 2);  -- Screaming Tatooine Tomato

-- Crispy Nuna Legs
INSERT INTO Product_ingredients VALUES
                                    (3, 16), -- Shaurian Spice Mix
                                    (3, 3);  -- Dagobah Chaos Leaves

-- Endorian Tip-Yip Platter
INSERT INTO Product_ingredients VALUES
                                    (4, 18), -- Crocoboom Crunch Flakes
                                    (4, 6);  -- Fizzbubble Orb

-- Chewie’s Yobacca Noodles
INSERT INTO Product_ingredients VALUES
                                    (5, 4),  -- Supreme Nerf Beef
                                    (5, 5);  -- Ancient Empire Croquette

-- Meiloorun Medley
INSERT INTO Product_ingredients VALUES
                                    (6, 15), -- Sugar of Galactic Love
                                    (6, 12); -- CocoBoom Sweet Essence

-- Nerf Nuggets Supreme
INSERT INTO Product_ingredients VALUES
                                    (7, 8),  -- Mystic Meiloorun Chunk
                                    (7, 9);  -- Dramatic Mustafar Pasta

-- Dagobah Rootleaf Stew
INSERT INTO Product_ingredients VALUES
                                    (8, 13), -- Bread of Destiny
                                    (8, 4);  -- Supreme Nerf Beef

-- Jedi Roast Espresso
INSERT INTO Product_ingredients VALUES
    (9, 10); -- Grandma Jedi Brew

-- Tob-Tobi Galactic Bites
INSERT INTO Product_ingredients VALUES
    (10, 11); -- Peaceful Monastery Tea

-- Tripi-Tropi Medallion Cake
INSERT INTO Product_ingredients VALUES
                                    (11, 1),  -- Explosive Bantha Cheese
                                    (11, 8);  -- Mystic Meiloorun Chunk


ALTER TABLE Orders
    ADD COLUMN coupon_code VARCHAR(255),
    ADD COLUMN is_member BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN member_id INT,
    ADD COLUMN is_receipt BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN type ENUM('EAT_IN', 'TAKEAWAY', 'DELIVERY') DEFAULT 'EAT_IN',
    ADD COLUMN is_paid BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD FOREIGN KEY (coupon_code) REFERENCES Coupons(code);


CREATE TABLE IF NOT EXISTS restaurant_settings (
  id INT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  address TEXT,
  contact TEXT,
  logo LONGBLOB
);

INSERT INTO restaurant_settings (id, name, address, contact, logo)
VALUES (1, 'My Restaurant', 'Welcome to our place!', '6942069420',LOAD_FILE('/var/lib/mysql-files/assets/tralalero-tralala.jpg'));

-- Blue Milk
INSERT INTO Product (name, description, price, image, image_url, sound, specialLabel, isASide)
VALUES ('Blue Milk', 'A refreshing dairy drink from Tatooine made from Bantha milk.', 3.50,
        LOAD_FILE('/var/lib/mysql-files/assets/blue_milk.jpg'), 'assets/blue_milk.jpg',
        NULL, NULL, FALSE);
INSERT INTO Product_Tags (product_id, tag_id) VALUES (LAST_INSERT_ID(), 4);

-- Spotchka
INSERT INTO Product (name, description, price, image, image_url, sound, specialLabel, isASide)
VALUES ('Spotchka', 'A glowing blue alcoholic beverage made from alien creatures.', 4.75,
        LOAD_FILE('/var/lib/mysql-files/assets/spotchka.jpg'), 'assets/spotchka.jpg',
        NULL, NULL, FALSE);
INSERT INTO Product_Tags (product_id, tag_id) VALUES (LAST_INSERT_ID(), 4);

-- Jawa Juice
INSERT INTO Product (name, description, price, image, image_url, sound, specialLabel, isASide)
VALUES ('Jawa Juice', 'Popular diner drink made from fermented bantha hides.', 2.99,
        LOAD_FILE('/var/lib/mysql-files/assets/jawa_juice.jpg'), 'assets/jawa_juice.jpg',
        NULL, NULL, FALSE);
INSERT INTO Product_Tags (product_id, tag_id) VALUES (LAST_INSERT_ID(), 4);

-- Meiloorun Juice
INSERT INTO Product (name, description, price, image, image_url, sound, specialLabel, isASide)
VALUES ('Meiloorun Juice', 'A sweet and tangy juice made from rare Meiloorun fruits.', 4.25,
        LOAD_FILE('/var/lib/mysql-files/assets/meiloorun_juice.jpg'), 'assets/meiloorun_juice.jpg',
        NULL, NULL, FALSE);
INSERT INTO Product_Tags (product_id, tag_id) VALUES (LAST_INSERT_ID(), 4);

-- Tatooine Sunset
INSERT INTO Product (name, description, price, image, image_url, sound, specialLabel, isASide)
VALUES ('Tatooine Sunset', 'A citrusy mocktail evoking the twin suns of Tatooine.', 3.75,
        LOAD_FILE('/var/lib/mysql-files/assets/tatooine_sunset.jpg'), 'assets/tatooine_sunset.jpg',
        NULL, NULL, FALSE);
INSERT INTO Product_Tags (product_id, tag_id) VALUES (LAST_INSERT_ID(), 4);
