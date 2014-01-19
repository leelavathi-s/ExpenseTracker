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
-- Dumping data for table expensetracker.brand: ~28 rows (approximately)
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
	(67, 'BSNL', 84);
/*!40000 ALTER TABLE `brand` ENABLE KEYS */;

-- Dumping data for table expensetracker.category: ~9 rows (approximately)
DELETE FROM `category`;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` (`CategoryId`, `CategoryName`) VALUES
	(1, 'Grocery'),
	(2, 'Transport'),
	(3, 'Outside Food'),
	(4, 'Fixed Expense'),
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

-- Dumping data for table expensetracker.product: ~68 rows (approximately)
DELETE FROM `product`;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` (`ProductId`, `SubCategoryId`, `ProductName`) VALUES
	(2, 5, 'Apple'),
	(6, 6, 'Batter'),
	(7, 12, 'Rent'),
	(8, 12, 'Housekeeping'),
	(10, 14, 'Phone Recharge'),
	(12, 8, 'Insurance'),
	(14, 11, 'Zinger Burger'),
	(15, 11, '5 pc Hot Wings'),
	(16, 1, 'Oats'),
	(17, 2, 'Toor Dal'),
	(18, 10, 'Muruku'),
	(19, 10, 'Bread'),
	(20, 7, 'Chicken Masala'),
	(21, 7, 'Mutton Masala'),
	(23, 17, 'Cloth clips'),
	(24, 16, 'Groom stick'),
	(25, 15, 'Moisturizing Lotion'),
	(26, 15, 'Soap'),
	(28, 10, 'Kesar Halwa'),
	(29, 10, 'Milk Bikis'),
	(30, 17, 'AA Battery'),
	(31, 17, 'AAA Battery'),
	(32, 9, 'Fuel'),
	(33, 20, 'Wipes'),
	(34, 20, 'Diaper'),
	(36, 20, ' Tooth Brush'),
	(37, 23, 'Jetty'),
	(42, 5, 'Sapota'),
	(43, 5, 'Banana'),
	(44, 4, 'Sweet Pumpkin'),
	(45, 4, 'Puthina Leaves'),
	(46, 4, 'Green Leafy'),
	(47, 4, 'Mint Leaves'),
	(48, 4, 'Paper'),
	(49, 4, 'Potato'),
	(50, 4, 'Green chillies'),
	(51, 4, 'Carrot'),
	(52, 4, 'Paper'),
	(53, 4, 'Green Peas'),
	(54, 4, 'Butter Beans'),
	(55, 4, 'Soya Beans'),
	(56, 4, 'Cabbage'),
	(57, 4, 'Brocolli'),
	(58, 4, 'Cauliflower'),
	(59, 4, 'Avarai'),
	(60, 4, 'Radish'),
	(61, 4, 'Elephant Yam'),
	(62, 4, 'Small Onion'),
	(63, 4, 'Big Onion'),
	(64, 4, 'Drum Stick'),
	(65, 4, 'Coconut'),
	(66, 18, 'Tablets  or Injection n Doctor Fees'),
	(67, 19, 'Injection n Doctor Fees'),
	(68, 9, 'Insurance'),
	(69, 17, 'Knife'),
	(71, 27, 'Cable'),
	(72, 8, 'Fuel'),
	(73, 27, 'Paper'),
	(74, 5, 'Papaya'),
	(76, 4, 'Ladies Finger'),
	(77, 28, 'Sunflower Oil'),
	(78, 28, 'Gingely Oil'),
	(79, 29, 'Aavin Milk'),
	(80, 30, 'Aavin Milk'),
	(81, 31, 'Agarbathi'),
	(82, 31, 'Oil'),
	(83, 34, 'Septic Tank Cleaning'),
	(84, 27, 'Internet');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;

-- Dumping data for table expensetracker.purchaseorder: ~16 rows (approximately)
DELETE FROM `purchaseorder`;
/*!40000 ALTER TABLE `purchaseorder` DISABLE KEYS */;
INSERT INTO `purchaseorder` (`OrderId`, `Quantity`, `SubCategoryId`, `ProductId`, `Price`, `ShopId`, `BrandId`, `CategoryId`, `OrderDate`) VALUES
	(3, 1, 12, 8, 500, NULL, NULL, 4, '2014-01-01'),
	(4, 1, 12, 7, 13500, NULL, NULL, 4, '2014-01-01'),
	(6, 1, 8, 72, 1986, 7, 54, 2, '2014-01-10'),
	(9, 1, 27, 84, 1000, NULL, 67, 4, '2014-01-01'),
	(10, 31, 29, 79, 543, 12, 63, 4, '2014-01-06'),
	(12, 1, 10, 28, 100, 13, NULL, 3, '2014-01-09'),
	(16, 4, 10, 29, 40, 14, NULL, 3, '2014-01-09'),
	(17, 6, 17, 30, 60, 14, 36, 9, '2014-01-09'),
	(19, 4, 17, 31, 44, 14, 37, 9, '2014-01-09'),
	(20, 1, 17, 69, 50, 14, 38, 9, '2014-01-09'),
	(21, 1, 11, 14, 127, 9, NULL, 3, '2014-01-08'),
	(22, 1, 11, 15, 120, 9, NULL, 3, '2014-01-08'),
	(23, 200, 15, 25, 130.75, 15, 34, 9, '2014-01-13'),
	(24, 52, 20, 33, 79.75, 15, 39, 10, '2014-01-13'),
	(25, 1, 20, 36, 85.5, 15, 43, 10, '2014-01-13'),
	(26, 3, 23, 37, 110, 15, 44, 10, '2014-01-13');
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

-- Dumping data for table expensetracker.subcategory: ~29 rows (approximately)
DELETE FROM `subcategory`;
/*!40000 ALTER TABLE `subcategory` DISABLE KEYS */;
INSERT INTO `subcategory` (`SubCategoryId`, `SubCategoryName`, `CategoryId`) VALUES
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
	(34, 'Others', 7);
/*!40000 ALTER TABLE `subcategory` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
