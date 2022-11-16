CREATE TABLE IF NOT EXISTS `product`(
    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(255),
    `category` varchar(255),
    `price` float(53),
    `stock_amount` int(255)
);