-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.5.34-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             8.0.0.4396
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
-- Dumping data for table expensetracker.brand: ~34 rows (approximately)
DELETE FROM `brand`;
/*!40000 ALTER TABLE `brand` DISABLE KEYS */;
INSERT INTO `brand` (`BrandId`, `BrandName`, `ProductId`) VALUES
	(21, 'Aircel Bill', 10),
	(22, 'Airtel Bill', 10),
	(29, 'Loose', 17),
	(30, 'Udhayam', 17),
	(31, 'Barley', 16),
	(34, 'Vaseline', 25),
	(35, 'Dove', 26),
	(36, 'Eveready', 30),
	(37, 'Eveready', 31),
	(39, 'Mommy Poko', 33),
	(40, 'Huggies', 34),
	(42, 'Himalaya', 25),
	(43, 'Mommas Baby', 36),
	(44, 'SpiceTex', 37),
	(46, 'Aachi', 20),
	(47, 'Aachi', 21),
	(49, 'Godrej', 69),
	(55, 'The Hindu', 73),
	(56, 'DTH', 71),
	(58, 'Udhayam', 6),
	(59, 'Loose', 6),
	(60, 'Gold Winner', 77),
	(61, 'Idhayam', 78),
	(63, 'Orange', 79),
	(64, 'Blue', 80),
	(65, 'Prathana', 81),
	(66, 'Deepam', 82),
	(67, 'BSNL', 84),
	(68, 'Orange', 85),
	(70, 'TestBrand', 36),
	(71, 'ZZ', 36),
	(72, 'India Gate', 117),
	(73, 'Rin', 26),
	(74, 'Bingo', 121);
/*!40000 ALTER TABLE `brand` ENABLE KEYS */;

-- Dumping data for table expensetracker.category: ~11 rows (approximately)
DELETE FROM `category`;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` (`CategoryId`, `CategoryName`) VALUES
	(10, 'Child care'),
	(36, 'Clothing'),
	(6, 'Communication'),
	(7, 'Extras'),
	(4, 'Fixed Expense'),
	(1, 'Grocery'),
	(28, 'Knowledge'),
	(15, 'Medical Expense'),
	(3, 'Outside Food'),
	(9, 'Stationery'),
	(2, 'Transport');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;

-- Dumping data for table expensetracker.months: ~12 rows (approximately)
DELETE FROM `months`;
/*!40000 ALTER TABLE `months` DISABLE KEYS */;
INSERT INTO `months` (`id`, `name`) VALUES
	(1, 'January'),
	(2, 'Febraury'),
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

-- Dumping data for table expensetracker.product: ~83 rows (approximately)
DELETE FROM `product`;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` (`ProductId`, `ProductName`, `SubCategoryId`) VALUES
	(2, 'Apple', 5),
	(6, 'Batter', 6),
	(7, 'Rent', 12),
	(8, 'Housekeeping', 12),
	(10, 'Phone Recharge', 14),
	(12, 'Insurance', 8),
	(14, 'Zinger Burger', 11),
	(15, '5 pc Hot Wings', 11),
	(16, 'Oats', 1),
	(17, 'Toor Dal', 2),
	(18, 'Muruku', 10),
	(19, 'Bread', 10),
	(20, 'Chicken Masala', 7),
	(21, 'Mutton Masala', 7),
	(23, 'Cloth clips', 17),
	(24, 'Groom stick', 16),
	(25, 'Moisturizing Lotion', 15),
	(26, 'Soap', 15),
	(28, 'Kesar Halwa', 10),
	(29, 'Milk Bikis', 10),
	(30, 'AA Battery', 17),
	(31, 'AAA Battery', 17),
	(32, 'Fuel', 9),
	(33, 'Wipes', 20),
	(34, 'Diaper', 20),
	(36, 'Tooth Brush', 20),
	(37, 'Jetty', 23),
	(42, 'Sapota', 5),
	(43, 'Banana', 5),
	(44, 'Sweet Pumpkin', 4),
	(45, 'Puthina Leaves', 4),
	(46, 'Green Leafy', 4),
	(47, 'Mint Leaves', 4),
	(49, 'Potato', 4),
	(50, 'Green chillies', 4),
	(51, 'Carrot', 4),
	(53, 'Green Peas', 4),
	(54, 'Butter Beans', 4),
	(55, 'Soya Beans', 4),
	(56, 'Cabbage', 4),
	(57, 'Brocolli', 4),
	(58, 'Cauliflower', 4),
	(59, 'Avarai', 4),
	(60, 'Radish', 4),
	(61, 'Elephant Yam', 4),
	(62, 'Small Onion', 4),
	(63, 'Big Onion', 4),
	(64, 'Drum Stick', 4),
	(65, 'Coconut', 4),
	(66, 'Tablets  or Injection n Doctor Fees', 18),
	(67, 'Injection n Doctor Fees', 19),
	(68, 'Insurance', 9),
	(69, 'Knife', 34),
	(71, 'Cable', 27),
	(72, 'Fuel', 8),
	(73, 'Paper', 27),
	(74, 'Papaya', 5),
	(76, 'Ladies Finger', 4),
	(77, 'Sunflower Oil', 28),
	(78, 'Gingely Oil', 28),
	(79, 'Aavin Milk', 29),
	(80, 'Aavin Milk', 30),
	(81, 'Agarbathi', 31),
	(82, 'Oil', 31),
	(83, 'Septic Tank Cleaning', 34),
	(84, 'Internet', 27),
	(85, 'Aavin Milk', 35),
	(86, 'Toll Booth', 8),
	(87, 'Books', 37),
	(88, 'Books', 38),
	(112, 'Utensils', 43),
	(113, 'Plastic items', 43),
	(114, 'Wooden Items', 43),
	(115, 'Dress', 45),
	(116, 'Dress', 44),
	(117, 'Basmathi Rice', 46),
	(118, 'Ponni Rice', 46),
	(119, 'Rice', 34),
	(120, 'Briyani Masala', 34),
	(121, 'Chips', 10),
	(122, 'Tomato', 4);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;

-- Dumping data for table expensetracker.purchaseorder: ~41 rows (approximately)
DELETE FROM `purchaseorder`;
/*!40000 ALTER TABLE `purchaseorder` DISABLE KEYS */;
INSERT INTO `purchaseorder` (`OrderDate`, `Price`, `Quantity`, `CategoryId`, `ShopId`, `BrandId`, `OrderId`, `ProductId`, `SubCategoryId`, `Comments`) VALUES
	('2014-01-01', 500, 1, 4, NULL, NULL, 3, 8, 12, NULL),
	('2014-01-01', 13500, 1, 4, NULL, NULL, 4, 7, 12, NULL),
	('2014-01-10', 1986, 1, 2, 7, 54, 6, 72, 8, NULL),
	('2014-01-01', 1068, 1, 4, NULL, 67, 9, 84, 27, NULL),
	('2014-01-06', 543, 31, 4, 12, 63, 10, 79, 29, NULL),
	('2014-01-09', 100, 1, 3, 13, NULL, 12, 28, 10, NULL),
	('2014-01-09', 40, 4, 3, 14, 69, 16, 29, 10, NULL),
	('2014-01-09', 60, 6, 9, 14, 36, 17, 30, 17, NULL),
	('2014-01-09', 44, 4, 9, 14, 37, 19, 31, 17, NULL),
	('2014-01-09', 50, 1, 7, 14, 38, 20, 69, 34, NULL),
	('2014-01-08', 127, 1, 3, 9, NULL, 21, 14, 11, NULL),
	('2014-01-08', 120, 1, 3, 9, NULL, 22, 15, 11, NULL),
	('2014-01-13', 130.75, 200, 9, 15, 34, 23, 25, 15, NULL),
	('2014-01-13', 79.75, 52, 10, 15, 39, 24, 33, 20, NULL),
	('2014-01-13', 85.5, 1, 10, 15, 43, 25, 36, 20, NULL),
	('2014-01-13', 110, 3, 10, 15, 44, 26, 37, 23, NULL),
	('2014-01-19', 100, 31, 4, 12, 68, 27, 85, 35, NULL),
	('2014-01-06', 500, 1, 7, 16, NULL, 28, 83, 34, NULL),
	('2014-01-10', 19, 0, 1, 1, NULL, 29, 42, 5, NULL),
	('2014-01-19', 2130, 28, 2, 8, NULL, 30, 72, 8, NULL),
	('2014-01-19', 800, 2, 2, 16, NULL, 31, 86, 8, NULL),
	('2014-01-10', 110, 1, 9, 14, NULL, 32, 24, 16, NULL),
	('2014-01-09', 35, 15, 9, 14, NULL, 33, 23, 17, NULL),
	('2014-01-01', 160, 1, 4, 16, 56, 34, 71, 27, NULL),
	('2014-01-15', 670, 1, 28, 17, NULL, 35, 87, 37, NULL),
	('2014-01-21', 320, 1, 9, 19, NULL, 36, 112, 43, 'Stainless steel Idly pot'),
	('2014-01-21', 45, 1, 9, 19, NULL, 37, 113, 43, 'Bowl'),
	('2014-01-21', 25, 1, 9, 19, NULL, 39, 114, 43, 'Wood grinder stick'),
	('2014-01-21', 40, 1, 9, 19, NULL, 40, 113, 43, 'Modak maker'),
	('2014-01-22', 25, 6, 36, 16, NULL, 41, 115, 45, NULL),
	('2014-01-14', 95, 1, 7, 20, NULL, 42, 119, 34, 'India gate rice for briyani preparation'),
	('2014-01-14', 5, 1, 7, 20, NULL, 43, 120, 34, 'Aachi briyani masala'),
	('2014-01-21', 258, 1, 6, 17, 21, 44, 10, 14, NULL),
	('2014-01-20', 20, 4, 9, 4, 73, 45, 26, 15, NULL),
	('2014-01-20', 10, 1, 9, 4, 73, 46, 26, 15, NULL),
	('2014-01-20', 5, 1, 3, 12, 74, 47, 121, 10, NULL),
	('2014-01-20', 19, 0, 1, 4, NULL, 48, 43, 5, NULL),
	('2014-01-20', 15, 1, 1, 4, NULL, 49, 46, 4, NULL),
	('2014-01-20', 5, 1, 1, 4, NULL, 50, 47, 4, NULL),
	('2014-01-20', 3, 0, 1, 4, NULL, 51, 122, 4, NULL);
/*!40000 ALTER TABLE `purchaseorder` ENABLE KEYS */;

-- Dumping data for table expensetracker.shop: ~20 rows (approximately)
DELETE FROM `shop`;
/*!40000 ALTER TABLE `shop` DISABLE KEYS */;
INSERT INTO `shop` (`ShopId`, `ShopName`) VALUES
	(12, 'Aavin Milk Booth'),
	(13, 'Adyar Anandha Bhavan'),
	(2, 'Corner Shop'),
	(10, 'Hot Chips'),
	(15, 'Kannan Departmental Stores'),
	(9, 'KFC'),
	(20, 'Madurai Grocery Shop'),
	(8, 'Madurai Petrol Shop'),
	(3, 'Main road shop'),
	(6, 'More'),
	(16, 'Not Applicable'),
	(17, 'Online'),
	(1, 'Ottanchathiram'),
	(7, 'Palavakkam Petrol Shop'),
	(14, 'Salam Department Store'),
	(11, 'Seena Vanna'),
	(5, 'Spencer Daily'),
	(18, 'TestShop'),
	(4, 'Thangam stores'),
	(19, 'Vijayalakshmi Stores');
/*!40000 ALTER TABLE `shop` ENABLE KEYS */;

-- Dumping data for table expensetracker.subcategory: ~36 rows (approximately)
DELETE FROM `subcategory`;
/*!40000 ALTER TABLE `subcategory` DISABLE KEYS */;
INSERT INTO `subcategory` (`SubcategoryId`, `SubcategoryName`, `CategoryId`) VALUES
	(1, 'Cereals', 1),
	(2, 'Pulses', 1),
	(3, 'Flour Items and Powder', 1),
	(4, 'Vegetables', 1),
	(5, 'Fruits', 1),
	(6, 'Batter', 1),
	(7, 'Spices', 1),
	(8, 'Car', 2),
	(9, 'Bike', 2),
	(10, 'Snacks', 3),
	(11, 'Food', 3),
	(12, 'Stay', 4),
	(14, 'Phone', 6),
	(15, 'Bath n Sanitary Ware', 9),
	(16, 'Home Cleaning', 9),
	(17, 'Others', 9),
	(18, 'Illness', 15),
	(19, 'Vaccine', 15),
	(20, 'Bath, Skin and Health Care', 10),
	(21, 'Toys and Books', 10),
	(23, 'Kidswear', 10),
	(27, 'Knowledge', 4),
	(28, 'Oil', 1),
	(29, 'Milk', 4),
	(30, 'Milk', 1),
	(31, 'Pooja Items', 9),
	(32, 'Repair', 7),
	(33, 'Donation and gifts', 7),
	(34, 'Others', 7),
	(35, 'Milk Delivery', 4),
	(37, 'Techincal', 28),
	(38, 'Non Techinical', 28),
	(43, 'Kitchenware', 9),
	(44, 'New Dress', 36),
	(45, 'Iorning', 36),
	(46, 'Rice', 1);
/*!40000 ALTER TABLE `subcategory` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
