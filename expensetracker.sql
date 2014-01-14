-- --------------------------------------------------------
-- Host:                         localhost
-- Server version:               5.5.34-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win32
-- HeidiSQL Version:             8.0.0.4396
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
-- Dumping data for table expensetracker.brand: ~36 rows (approximately)
DELETE FROM `brand`;
/*!40000 ALTER TABLE `brand` DISABLE KEYS */;
INSERT INTO `brand` (`BrandId`, `BrandName`, `ProductId`) VALUES
	(1, 'Green Leafy', 1),
	(2, 'Apple', 2),
	(3, 'Banana', 2),
	(4, 'Beetroot', 1),
	(5, 'Garlic', 1),
	(6, 'Coconut', 1),
	(7, 'Potato', 1),
	(8, 'Tomato', 1),
	(9, 'Sweet Pumpkin', 1),
	(10, 'Chow chow', 1),
	(11, 'Puthina Leaves', 1),
	(12, 'Mint Leaves', 1),
	(13, 'Sapota', 2),
	(14, 'Car Petrol', 4),
	(15, 'Bike Petrol', 4),
	(16, 'Aavin', 6),
	(21, 'Aircel', 10),
	(22, 'Airtel', 10),
	(23, 'Car Insurance', 12),
	(24, 'Bike Insurance', 12),
	(25, 'Green Chillies', 1),
	(29, 'Toor Dal', 17),
	(30, 'New Item 7', 17),
	(31, 'Oats', 16),
	(32, 'Green Peas', 1),
	(33, 'Drumstick', 1),
	(34, 'Vaseline', 25),
	(35, 'Dove', 26),
	(36, 'Eveready', 30),
	(37, 'Eveready', 31),
	(38, 'Godrej', 32),
	(39, 'Mommy Poko', 33),
	(40, 'Huggies', 34),
	(42, 'Himalaya', 25),
	(43, 'Mommas Baby', 36),
	(44, 'SpiceTex', 37);
/*!40000 ALTER TABLE `brand` ENABLE KEYS */;

-- Dumping data for table expensetracker.category: ~9 rows (approximately)
DELETE FROM `category`;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` (`CategoryId`, `CategoryName`) VALUES
	(1, 'Grocery'),
	(2, 'Transport'),
	(3, 'Outside Food'),
	(4, 'Lodging'),
	(6, 'Communication'),
	(7, 'Extras'),
	(9, 'Stationery'),
	(10, 'Child care'),
	(15, 'Medical Expense');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;

-- Dumping data for table expensetracker.months: ~12 rows (approximately)
DELETE FROM `months`;
/*!40000 ALTER TABLE `months` DISABLE KEYS */;
INSERT INTO `months` (`Id`, `Name`) VALUES
	(1, 'January'),
	(2, 'Febuary'),
	(3, 'March'),
	(4, 'April'),
	(5, 'May'),
	(6, 'June'),
	(7, 'July'),
	(8, 'August'),
	(9, 'September'),
	(10, 'October'),
	(11, 'November'),
	(12, 'December');
/*!40000 ALTER TABLE `months` ENABLE KEYS */;

-- Dumping data for table expensetracker.product: ~31 rows (approximately)
DELETE FROM `product`;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` (`ProductId`, `SubCategoryId`, `ProductName`) VALUES
	(1, 4, 'Vegetables'),
	(2, 5, 'Fruits'),
	(4, 8, 'Fuel'),
	(6, 6, 'Milk'),
	(7, 12, 'Rent'),
	(8, 12, 'Housekeeping'),
	(10, 14, 'Phone Recharge'),
	(11, 13, 'Broadband'),
	(12, 8, 'Insurance'),
	(13, 24, 'Cable'),
	(14, 11, 'Zinger Burger'),
	(15, 11, '5 pc Hot Wings'),
	(16, 1, 'Oats'),
	(17, 2, 'Pulses'),
	(18, 10, 'Muruku'),
	(19, 10, 'Bread'),
	(20, 7, 'Septic Tank Cleaning'),
	(21, 7, 'Spices'),
	(23, 17, 'Cloth clips'),
	(24, 16, 'Groom stick'),
	(25, 15, 'Moisturizing Lotion'),
	(26, 15, 'Soap'),
	(28, 10, 'Kesar Halwa'),
	(29, 10, 'Milk Bikis'),
	(30, 17, 'AA Battery'),
	(31, 17, 'AAA Battery'),
	(32, 9, 'Knife'),
	(33, 20, 'Wipes'),
	(34, 20, 'Diaper'),
	(36, 20, ' Tooth Brush'),
	(37, 23, 'Jetty');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;

-- Dumping data for table expensetracker.purchaseorder: ~16 rows (approximately)
DELETE FROM `purchaseorder`;
/*!40000 ALTER TABLE `purchaseorder` DISABLE KEYS */;
INSERT INTO `purchaseorder` (`OrderId`, `Quantity`, `ProductId`, `Price`, `ShopId`, `BrandId`, `CategoryId`, `OrderDate`) VALUES
	(3, 1, 8, 500, NULL, NULL, 4, '2014-01-01'),
	(4, 1, 7, 13500, NULL, NULL, 4, '2014-01-01'),
	(6, 1, 4, 1986, 7, 14, 2, '2014-01-10'),
	(9, 1, 11, 1000, NULL, NULL, 6, '2014-01-01'),
	(10, 31, 6, 543, 12, 16, 1, '2014-01-06'),
	(12, 0, 28, 100, 1, NULL, 3, '2014-01-09'),
	(16, 4, 29, 40, 14, NULL, 3, '2014-01-09'),
	(17, 6, 30, 60, 14, 36, 9, '2014-01-09'),
	(19, 4, 31, 44, 14, 37, 9, '2014-01-09'),
	(20, 1, 32, 50, 14, 38, 9, '2014-01-09'),
	(21, 1, 14, 127, 9, NULL, 3, '2014-01-08'),
	(22, 1, 15, 120, 9, NULL, 3, '2014-01-08'),
	(23, 200, 25, 130.75, 15, 34, 9, '2014-01-13'),
	(24, 52, 33, 79.75, 15, 39, 10, '2014-01-13'),
	(25, 1, 36, 85.5, 15, 43, 10, '2014-01-13'),
	(26, 3, 37, 110, 15, 44, 10, '2014-01-13');
/*!40000 ALTER TABLE `purchaseorder` ENABLE KEYS */;

-- Dumping data for table expensetracker.shop: ~15 rows (approximately)
DELETE FROM `shop`;
/*!40000 ALTER TABLE `shop` DISABLE KEYS */;
INSERT INTO `shop` (`ShopId`, `ShopName`) VALUES
	(1, 'Ottanchathiram'),
	(2, 'Corner Shop'),
	(3, 'Main road shop'),
	(4, 'Thangam stores'),
	(5, 'Spencer Daily'),
	(6, 'More'),
	(7, 'Palavakkam Petrol Shop'),
	(8, 'Madurai Petrol Shop'),
	(9, 'KFC'),
	(10, 'Hot Chips'),
	(11, 'Seena Vanna'),
	(12, 'Aavin Milk Booth'),
	(13, 'Adyar Anandha Bhavan'),
	(14, 'Salam Department Store'),
	(15, 'Kannan Departmental Stores');
/*!40000 ALTER TABLE `shop` ENABLE KEYS */;

-- Dumping data for table expensetracker.subcategory: ~25 rows (approximately)
DELETE FROM `subcategory`;
/*!40000 ALTER TABLE `subcategory` DISABLE KEYS */;
INSERT INTO `subcategory` (`SubCategoryId`, `SubCategoryName`, `CategoryId`) VALUES
	(1, 'Cereals', 1),
	(2, 'Pulses', 1),
	(3, 'Flour Items', 1),
	(4, 'Vegetables', 1),
	(5, 'Fruits', 1),
	(6, 'Milk', 1),
	(7, 'Spices', 1),
	(8, 'Car', 2),
	(9, 'Bike', 2),
	(10, 'Snacks', 3),
	(11, 'Food', 3),
	(12, 'Stay', 4),
	(13, 'Internet', 6),
	(14, 'Phone', 6),
	(15, 'Bath n Sanitary Ware', 9),
	(16, 'Home Cleaning', 9),
	(17, 'Others', 9),
	(18, 'Illness', 15),
	(19, 'Vaccine', 15),
	(20, 'Bath, Skin and Health Care', 10),
	(21, 'Toys and Books', 10),
	(23, 'Kidswear', 10),
	(24, 'Cable', 4),
	(25, 'Paper', 4);
/*!40000 ALTER TABLE `subcategory` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
