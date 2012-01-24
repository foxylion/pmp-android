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

--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_position`
--

CREATE TABLE IF NOT EXISTS `dev_position` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `user` int(8) NOT NULL,
  `latitude` decimal(6,4) NOT NULL,
  `longitude` decimal(7,4) NOT NULL,
  `last_update` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user` (`user`),
  KEY `position` (`latitude`,`longitude`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_query`
--

CREATE TABLE IF NOT EXISTS `dev_query` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `passenger` int(8) NOT NULL,
  `seats` int(8) NOT NULL,
  `destination` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `passenger` (`passenger`),
  KEY `destination` (`destination`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_rating`
--

CREATE TABLE IF NOT EXISTS `dev_rating` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `rater` int(8) NOT NULL,
  `recipient` int(8) NOT NULL,
  `trip` int(8) NOT NULL,
  `rating` int(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `rater` (`rater`),
  KEY `recipient` (`recipient`)
  KEY `trip` (`trip`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_ride`
--

CREATE TABLE IF NOT EXISTS `dev_ride` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `passenger` int(8) NOT NULL,
  `trip` int(8) NOT NULL,
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
  `destination` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `creation` datetime NOT NULL,
  `ending` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `driver` (`driver`),
  KEY `destination` (`destination`)
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

-- TODO: Rework the procedures to fit to current db-layput.

DELIMITER //

DROP PROCEDURE IF EXISTS list_hiker//

CREATE PROCEDURE list_hiker (IN `driverid` int, IN `dist` int)
BEGIN
  DECLARE mylon DOUBLE;
  DECLARE mylat DOUBLE;
  DECLARE lon1 FLOAT;
  DECLARE lon2 FLOAT;
  DECLARE lat1 FLOAT;
  DECLARE lat2 FLOAT;
  DECLARE trip_dest varchar(100);
  DECLARE free_seats INT;
  -- get the original lon, lat and destination for the driverid
  SELECT current_lon, current_lat, destination, avail_seats INTO mylon, mylat, trip_dest, free_seats FROM dev_trip WHERE driver=driverid LIMIT 1;
  -- calculate lon and lat for the rectangle:
  set lon1 = mylon-dist/abs(cos(radians(mylat))*69);
  set lon2 = mylon+dist/abs(cos(radians(mylat))*69);
  set lat1 = mylat-(dist/69);
  set lat2 = mylat+(dist/69);
  -- run the query:
  SELECT `query`.id AS queryid, `query`.passenger AS userid, `user`.username,
    `user`.rating_avg AS rating, `query`.current_lat AS lat, `query`.current_lon AS lon,
    `query`.seats, 1609.344 * 3956 * 2 * ASIN(SQRT( POWER(SIN((mylat -`query`.current_lat) * pi()/180 / 2), 2) + COS(mylat * pi()/180) * COS(`query`.current_lat * pi()/180) *POWER(SIN((mylon -`query`.current_lon) * pi()/180 / 2), 2) )) AS distance
  FROM `dev_query` query
  INNER JOIN `dev_user` user ON `user`.id = passenger
  WHERE `query`.destination=trip_dest AND `query`.passenger != driverid AND
    `query`.seats <= free_seats AND
    `query`.current_lon BETWEEN lon1 AND lon2 AND `query`.current_lat BETWEEN lat1 AND lat2
  HAVING distance < dist
  ORDER BY distance ASC;
END //


DROP PROCEDURE IF EXISTS list_driver //

CREATE PROCEDURE list_driver (IN `passengerid` int,IN `dist` int)
BEGIN
  DECLARE mylon DOUBLE;
  DECLARE mylat DOUBLE;
  DECLARE lon1 FLOAT;
  DECLARE lon2 FLOAT;
  DECLARE lat1 FLOAT;
  DECLARE lat2 FLOAT;
	-- DECLARE trip_dest varchar(100);
  -- get the original lon and lat for the passengerid
  SELECT current_lon, current_lat INTO mylon, mylat FROM dev_query WHERE passenger=passengerid LIMIT 1;
  -- calculate lon and lat for the rectangle:
  set lon1 = mylon-dist/abs(cos(radians(mylat))*69);
  set lon2 = mylon+dist/abs(cos(radians(mylat))*69);
  set lat1 = mylat-(dist/69);
  set lat2 = mylat+(dist/69);
  -- run the query:
  SELECT destination.id as tripid, destination.avail_seats as seats, destination.current_lat as lat, destination.current_lon as lon, destination.destination, `user`.id as driverid, `user`.username, `user`.rating_avg as rating, 1609.344 * 3956 * 2 * ASIN(SQRT( POWER(SIN((origin.current_lat -destination.current_lat) * pi()/180 / 2), 2) + COS(origin.current_lat * pi()/180) * COS(destination.current_lat * pi()/180) *POWER(SIN((origin.current_lon -destination.current_lon) * pi()/180 / 2), 2) )) AS distance
  FROM `dev_query` origin
  JOIN `dev_trip` destination ON destination.destination = origin.destination
	JOIN dev_user `user` ON driver= `user`.id
  WHERE origin.passenger = passengerid AND (ending=0 OR ending > NOW()) AND
		destination.avail_seats >= origin.seats AND
		destination.current_lon BETWEEN lon1 AND lon2 AND destination.current_lat BETWEEN lat1 AND lat2
  HAVING distance < dist
  ORDER BY distance ASC;
END //

DELIMITER ;
