-- phpMyAdmin SQL Dump
-- version 3.2.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 29. Dezember 2011 um 14:33
-- Server Version: 5.1.41
-- PHP-Version: 5.3.1

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `vhike`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_offer`
--

CREATE TABLE IF NOT EXISTS `dev_offer` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `trip` int(8) NOT NULL,
  `query` int(8) NOT NULL,
  `message` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `trip` (`trip`),
  KEY `query` (`query`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_query`
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
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_ride`
--

CREATE TABLE IF NOT EXISTS `dev_ride` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `passenger` int(8) NOT NULL,
  `trip` int(8) NOT NULL,
  `driver_rated` tinyint(1) NOT NULL DEFAULT '0',
  `passenger_rated` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `foreign_key` (`passenger`,`trip`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_trip`
--

CREATE TABLE IF NOT EXISTS `dev_trip` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `driver` int(8) NOT NULL,
  `avail_seats` int(8) NOT NULL,
  `current_lat` decimal(6,4) NOT NULL,
  `current_lon` decimal(7,4) NOT NULL,
  `destination` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `creation` datetime NOT NULL,
  `ending` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `driver` (`driver`),
  KEY `pos_destination` (`current_lat`,`current_lon`,`destination`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_user`
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
  `rating_avg` float NOT NULL DEFAULT '0',
  `rating_num` int(5) NOT NULL DEFAULT '0',
  `activated` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_verification`
--

CREATE TABLE IF NOT EXISTS `dev_verification` (
  `user` int(11) NOT NULL,
  `key` varchar(32) CHARACTER SET ascii NOT NULL,
  PRIMARY KEY (`user`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


-- The original version of this procedure was written by Alexander Rubin
-- http://www.scribd.com/doc/2569355/Geo-Distance-Search-with-MySQL

DELIMITER //

DROP PROCEDURE IF EXISTS list_hiker//

CREATE PROCEDURE list_hiker (IN driverid int, IN dist int)
BEGIN
  DECLARE mylon DOUBLE;
  DECLARE mylat DOUBLE;
  DECLARE lon1 FLOAT;
  DECLARE lon2 FLOAT;
  DECLARE lat1 FLOAT;
  DECLARE lat2 FLOAT;
  -- get the original lon and lat for the driverid
  SELECT current_lon, current_lat INTO mylon, mylat FROM `dev_trip` WHERE `driver`=driverid LIMIT 1;
  -- calculate lon and lat for the rectangle:
  set lon1 = mylon-dist/abs(cos(radians(mylat))*69);
  set lon2 = mylon+dist/abs(cos(radians(mylat))*69);
  set lat1 = mylat-(dist/69);
  set lat2 = mylat+(dist/69);
  -- run the query:
  SELECT destination.*, 1609.344 * 3956 * 2 * ASIN(SQRT( POWER(SIN((origin.current_lat -destination.current_lat) * pi()/180 / 2), 2) + COS(origin.current_lat * pi()/180) * COS(destination.current_lat * pi()/180) *POWER(SIN((origin.current_lon -destination.current_lon) * pi()/180 / 2), 2) )) AS distance
  FROM `dev_query` destination, `dev_trip` origin
  WHERE origin.avail_seats>=destination.seats AND destination.current_lon BETWEEN lon1 AND lon2 AND destination.current_lat BETWEEN lat1 AND lat2
  HAVING distance < dist
  ORDER BY distance ASC;
END
//

DELIMITER ;