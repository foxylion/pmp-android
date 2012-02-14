-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 10, 2012 at 01:08 PM
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
DROP PROCEDURE IF EXISTS `list_driver`$$
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

DROP PROCEDURE IF EXISTS `list_hiker`$$
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

DROP TABLE IF EXISTS `dev_offer`;
CREATE TABLE IF NOT EXISTS `dev_offer` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `trip` int(8) NOT NULL,
  `query` int(8) NOT NULL,
  `status` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `sender` int(8) NOT NULL DEFAULT '-1',
  `recipient` int(8) NOT NULL DEFAULT '-1',
  `message` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `trip` (`trip`),
  KEY `query` (`query`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=64 ;

--
-- Dumping data for table `dev_offer`
--

INSERT INTO `dev_offer` (`id`, `trip`, `query`, `status`, `sender`, `recipient`, `message`) VALUES
(62, 319, 168, 8, 3, 4, 'Hello');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
