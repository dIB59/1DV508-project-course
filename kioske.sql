-- Create the database
CREATE DATABASE Kioske;

-- Select the database to use
USE Kioske;

-- Create the Items table
CREATE TABLE Items (
    ItemID INT PRIMARY KEY,
    ItemName VARCHAR(255)
);

-- Create the Receipt table
CREATE TABLE Receipt (
    OrderID INT PRIMARY KEY,
    ItemID INT,
    OrderItems VARCHAR(255),
    Address VARCHAR(255),
    Price INT,
    DateTime DATETIME,
    FOREIGN KEY (ItemID) REFERENCES Items(ItemID)
);

