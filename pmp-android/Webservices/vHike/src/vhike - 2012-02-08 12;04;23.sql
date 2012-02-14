-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 08, 2012 at 12:04 PM
-- Server version: 5.5.16
-- PHP Version: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `vhike`
--

DELIMITER $$
--
-- Procedures
--
CREATE PROCEDURE `list_driver`(IN `passengerid` int,IN `dist` int)
BEGIN
  DECLARE mylon FLOAT;
  DECLARE mylat FLOAT;
  DECLARE lon1 FLOAT;
  DECLARE lon2 FLOAT;
  DECLARE lat1 FLOAT;
  DECLARE lat2 FLOAT;
	-- DECLARE trip_dest varchar(100);
  -- get the original lon and lat for the passengerid
  SELECT longitude, latitude INTO mylon, mylat FROM dev_position WHERE user=passengerid LIMIT 1;
	IF mylon IS NOT NULL AND mylat IS NOT NULL THEN
		-- calculate lon and lat for the rectangle:
		set lon1 = mylon-dist/abs(cos(radians(mylat))*69);
		set lon2 = mylon+dist/abs(cos(radians(mylat))*69);
		set lat1 = mylat-(dist/69);
		set lat2 = mylat+(dist/69);
		-- run the query:
		SELECT destination.id as tripid, destination.avail_seats as seats,
			pos.latitude as lat, pos.longitude as lon, destination.destination,
			`user`.id as driverid, `user`.username,
			(SELECT AVG(rating) FROM dev_rating WHERE driverid = recipient) AS rating,
			(SELECT COUNT(rating) FROM dev_rating WHERE driverid = recipient) AS rating_num,    
			1609.344 * 3956 * 2 * ASIN(SQRT( POWER(SIN((mylat -pos.latitude) * pi()/180 / 2), 2) + COS(mylat * pi()/180) * COS(pos.latitude * pi()/180) *POWER(SIN((mylon -pos.longitude) * pi()/180 / 2), 2) )) AS distance
		FROM `dev_query` origin
		JOIN `dev_trip` destination ON destination.destination = origin.destination
		JOIN dev_position pos ON user=driver
		JOIN dev_user `user` ON driver= `user`.id
		WHERE origin.passenger = passengerid AND ending=0 AND
			destination.avail_seats >= origin.seats AND
			pos.longitude BETWEEN lon1 AND lon2 AND pos.latitude BETWEEN lat1 AND lat2
		HAVING distance < dist
		ORDER BY distance ASC;
	END IF;
END$$

CREATE PROCEDURE `list_hiker`(IN `driverid` int, IN `dist` int)
BEGIN
  DECLARE mylon FLOAT;
  DECLARE mylat FLOAT;
  DECLARE lon1 FLOAT;
  DECLARE lon2 FLOAT;
  DECLARE lat1 FLOAT;
  DECLARE lat2 FLOAT;
  DECLARE trip_dest varchar(100);
  DECLARE free_seats INT;
  -- get the original lon, lat and destination for the driverid
  SELECT destination, avail_seats INTO trip_dest, free_seats FROM dev_trip WHERE driver=driverid AND ending=0 LIMIT 1; -- and did not start
	SELECT longitude, latitude INTO mylon, mylat FROM dev_position WHERE user=driverid;
	IF mylon IS NOT NULL AND mylat IS NOT NULL THEN
		-- calculate lon and lat for the rectangle:
		set lon1 = mylon-dist/abs(cos(radians(mylat))*69);
		set lon2 = mylon+dist/abs(cos(radians(mylat))*69);
		set lat1 = mylat-(dist/69);
		set lat2 = mylat+(dist/69);
		-- run the query:
		SELECT `query`.id AS queryid, `query`.passenger AS userid, `user`.username,
			(SELECT AVG(rating) FROM dev_rating WHERE userid = recipient) AS rating,
			(SELECT COUNT(rating) FROM dev_rating WHERE userid = recipient) AS rating_num,    
			pos.latitude AS lat, pos.longitude AS lon, `query`.seats,
			1609.344 * 3956 * 2 * ASIN(SQRT( POWER(SIN((mylat -pos.latitude) * pi()/180 / 2), 2) + COS(mylat * pi()/180) * COS(pos.latitude * pi()/180) *POWER(SIN((mylon -pos.longitude) * pi()/180 / 2), 2) )) AS distance
		FROM `dev_query` query
		INNER JOIN `dev_user` user ON `user`.id = passenger
		INNER JOIN dev_position pos ON user=passenger
		WHERE `query`.destination=trip_dest AND `query`.passenger != driverid AND
			`query`.seats <= free_seats AND
			pos.longitude BETWEEN lon1 AND lon2 AND pos.latitude BETWEEN lat1 AND lat2
		HAVING distance < dist
		ORDER BY distance ASC;
	END IF;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `dev_offer`
--

CREATE TABLE IF NOT EXISTS `dev_offer` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `trip` int(8) NOT NULL,
  `query` int(8) NOT NULL,
  `message` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `trip` (`trip`),
  KEY `query` (`query`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=64 ;

--
-- Dumping data for table `dev_offer`
--

INSERT INTO `dev_offer` (`id`, `trip`, `query`, `message`) VALUES
(62, 319, 164, 'Hello'),
(63, 319, 165, 'Hello');

-- --------------------------------------------------------

--
-- Table structure for table `dev_position`
--

CREATE TABLE IF NOT EXISTS `dev_position` (
  `user` int(8) NOT NULL,
  `latitude` decimal(6,4) NOT NULL,
  `longitude` decimal(7,4) NOT NULL,
  `last_update` datetime NOT NULL,
  PRIMARY KEY (`user`),
  UNIQUE KEY `user` (`user`),
  KEY `position` (`latitude`,`longitude`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `dev_position`
--

INSERT INTO `dev_position` (`user`, `latitude`, `longitude`, `last_update`) VALUES
(8, 48.6825, 9.1838, '2012-02-04 23:57:09'),
(1, 48.7824, 9.1836, '2012-02-05 03:25:54');

-- --------------------------------------------------------

--
-- Table structure for table `dev_query`
--

CREATE TABLE IF NOT EXISTS `dev_query` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `passenger` int(8) NOT NULL,
  `seats` int(8) NOT NULL,
  `current_lat` decimal(6,4) NOT NULL,
  `current_lon` decimal(7,4) NOT NULL,
  `destination` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `passenger` (`passenger`),
  KEY `pos_destination` (`current_lat`,`current_lon`,`destination`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=166 ;

--
-- Dumping data for table `dev_query`
--

INSERT INTO `dev_query` (`id`, `passenger`, `seats`, `current_lat`, `current_lon`, `destination`) VALUES
(164, 8, 1, 0.0000, 0.0000, 'Berlin'),
(165, 4, 2, 0.0000, 0.0000, 'Berlin');

-- --------------------------------------------------------

--
-- Table structure for table `dev_rating`
--

CREATE TABLE IF NOT EXISTS `dev_rating` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `rater` int(8) NOT NULL,
  `recipient` int(8) NOT NULL,
  `trip` int(8) NOT NULL,
  `rating` int(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `rater` (`rater`),
  KEY `recipient` (`recipient`),
  KEY `trip` (`trip`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=7 ;

--
-- Dumping data for table `dev_rating`
--

INSERT INTO `dev_rating` (`id`, `rater`, `recipient`, `trip`, `rating`) VALUES
(3, 4, 3, 152, 4),
(4, 1, 3, 155, 3),
(5, 3, 1, 155, 2),
(6, 3, 4, 152, 1);

-- --------------------------------------------------------

--
-- Table structure for table `dev_ride`
--

CREATE TABLE IF NOT EXISTS `dev_ride` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `passenger` int(8) NOT NULL,
  `trip` int(8) NOT NULL,
  `picked_up` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `foreign_key` (`passenger`,`trip`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=19 ;

--
-- Dumping data for table `dev_ride`
--

INSERT INTO `dev_ride` (`id`, `passenger`, `trip`, `picked_up`) VALUES
(17, 8, 319, 0);

-- --------------------------------------------------------

--
-- Table structure for table `dev_trip`
--

CREATE TABLE IF NOT EXISTS `dev_trip` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `driver` int(8) NOT NULL,
  `avail_seats` int(8) NOT NULL,
  `current_lat` decimal(6,4) NOT NULL,
  `current_lon` decimal(7,4) NOT NULL,
  `destination` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `creation` datetime NOT NULL,
  `started` tinyint(1) NOT NULL COMMENT 'whether the trip has started',
  `ending` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `driver` (`driver`),
  KEY `pos_destination` (`current_lat`,`current_lon`,`destination`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=320 ;

--
-- Dumping data for table `dev_trip`
--

INSERT INTO `dev_trip` (`id`, `driver`, `avail_seats`, `current_lat`, `current_lon`, `destination`, `creation`, `started`, `ending`) VALUES
(318, 3, 1, 0.0000, 0.0000, 'Berlin', '2012-02-03 14:41:24', 0, '0000-00-00 00:00:00'),
(316, 4, 1, 0.0000, 0.0000, 'Berlin', '2012-02-03 14:38:38', 0, '0000-00-00 00:00:00'),
(317, 3, 1, 0.0000, 0.0000, 'Berlin', '2012-02-03 14:40:39', 0, '2012-02-03 14:41:22'),
(319, 1, 3, 0.0000, 0.0000, 'Berlin', '2012-02-03 14:46:25', 0, '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `dev_user`
--

CREATE TABLE IF NOT EXISTS `dev_user` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `firstname` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `lastname` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `tel` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  `description` text COLLATE utf8_unicode_ci NOT NULL,
  `regdate` datetime NOT NULL,
  `email_public` tinyint(1) NOT NULL DEFAULT '0',
  `firstname_public` tinyint(1) NOT NULL DEFAULT '0',
  `lastname_public` tinyint(1) NOT NULL DEFAULT '0',
  `tel_public` tinyint(1) NOT NULL DEFAULT '0',
  `activated` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=15 ;

--
-- Dumping data for table `dev_user`
--

INSERT INTO `dev_user` (`id`, `username`, `password`, `email`, `firstname`, `lastname`, `tel`, `description`, `regdate`, `email_public`, `firstname_public`, `lastname_public`, `tel_public`, `activated`) VALUES
(1, 'demo', 'c06db68e819be6ec3d26c6038d8e8d1f', 'patrick.strobel.swt@gmail.com', 'Patrick', 'Strobel', '0151', 'Ein Test-Konto', '2011-12-29 14:52:34', 0, 0, 0, 0, 1),
(3, 'demo2', 'c06db68e819be6ec3d26c6038d8e8d1f', 'nguyen.andres@gmail.com', 'demo2', 'test', '0123456789', '', '2011-12-29 19:28:28', 1, 1, 1, 1, 1),
(4, 'demo3', 'c06db68e819be6ec3d26c6038d8e8d1f', 'test@mail.de', 'test', 'user', '1312359579', '', '2012-01-02 18:08:49', 1, 1, 1, 1, 1),
(5, 'hallo', '56a87baa07ac821581088d1b9cf88d0d', 'vwfrfgw@abc.com', 'abc', 'adfgdvf', '1627263738', '', '2012-01-10 14:05:32', 0, 0, 0, 0, 0),
(6, 'foxylion', 'e99a18c428cb38d5f260853678922e03', 'mail@jakobjarosch.de', 'Jakob', 'Jarosch', '015771535748', '', '2012-01-11 12:34:20', 1, 1, 1, 1, 1),
(7, 'marcus', '214aaf2c9a8510d948555ee25cb38397', 'marqus.ve@googlemail.com', 'Marcus', 'Vetter', '0123456789', '', '2012-01-11 12:45:18', 1, 1, 1, 1, 1),
(8, 'demo4', 'c06db68e819be6ec3d26c6038d8e8d1f', 'mail@mail.com', 'mail', 'man', '0123456789', 'test user', '2012-01-12 12:48:17', 1, 1, 1, 1, 1),
(9, 'demo6', 'c06db68e819be6ec3d26c6038d8e8d1f', 'mail@mailman.com', 'mail', 'man', '0123456789', 'test user', '2012-01-12 12:48:40', 1, 1, 1, 1, 1),
(10, 'demo5', 'c06db68e819be6ec3d26c6038d8e8d1f', 'mailman@mailman.com', 'mail', 'man', '0123456789', 'test user', '2012-01-12 12:48:54', 1, 1, 1, 1, 1),
(11, 'demo7', 'c06db68e819be6ec3d26c6038d8e8d1f', 'mail@mailman.com', 'mail', 'man', '0123456789', 'test user', '2012-01-12 12:48:40', 1, 1, 1, 1, 1),
(12, 'demo8', 'c06db68e819be6ec3d26c6038d8e8d1f', 'mail@mailman.com', 'mail', 'man', '0123456789', 'test user', '2012-01-12 12:48:40', 1, 1, 1, 1, 1),
(13, 'demo9', 'c06db68e819be6ec3d26c6038d8e8d1f', 'mail@mailman.com', 'mail', 'man', '0123456789', 'test user', '2012-01-12 12:48:40', 1, 1, 1, 1, 1),
(14, 'abnahme1', 'c06db68e819be6ec3d26c6038d8e8d1f', 'abnahme@abnahme.com', 'abnahme', 'iteration 2', '0123456789', 'User für die Abnahme', '2012-01-12 12:48:40', 1, 1, 1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `dev_verification`
--

CREATE TABLE IF NOT EXISTS `dev_verification` (
  `user` int(11) NOT NULL,
  `key` varchar(32) CHARACTER SET ascii NOT NULL,
  PRIMARY KEY (`user`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `dev_verification`
--

INSERT INTO `dev_verification` (`user`, `key`) VALUES
(4, 'uoojdiw06astrij90vuysd3m8n8w0348'),
(5, 'rbnxbk8hfsxg9n36s8ygbg0qbpa73hnh'),
(8, 'rtbc3lsfr735lbomob8woz0ewtdtuih2'),
(9, 'azeqpbq7f0qz3r13n101akz5xhwiulxt'),
(10, 'e4bvj8vnof1p3l9wq83syj9a1q9n7xz0');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
