-- phpMyAdmin SQL Dump
-- version 3.3.8.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 03. Dezember 2011 um 18:06
-- Server Version: 5.0.51
-- PHP-Version: 5.2.6-1+lenny13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `vhike`
--
CREATE DATABASE `vhike` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `vhike`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_offer`
--

CREATE TABLE IF NOT EXISTS `dev_offer` (
  `id` int(8) NOT NULL auto_increment,
  `driver` int(8) NOT NULL,
  `query` int(8) NOT NULL,
  `message` text character set utf8 collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Daten für Tabelle `dev_offer`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_query`
--

CREATE TABLE IF NOT EXISTS `dev_query` (
  `id` int(8) NOT NULL auto_increment,
  `passenger` int(8) NOT NULL,
  `seats` int(8) NOT NULL,
  `current_lat` decimal(6,4) NOT NULL,
  `current_lon` decimal(7,4) NOT NULL,
  `destination` varchar(100) character set utf8 collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Daten für Tabelle `dev_query`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_ride`
--

CREATE TABLE IF NOT EXISTS `dev_ride` (
  `id` int(8) NOT NULL auto_increment,
  `passenger` int(8) NOT NULL,
  `trip` int(8) NOT NULL,
  `driver_rated` tinyint(1) NOT NULL,
  `passenger_rated` tinyint(1) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Daten für Tabelle `dev_ride`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_trip`
--

CREATE TABLE IF NOT EXISTS `dev_trip` (
  `id` int(8) NOT NULL,
  `driver` int(8) NOT NULL,
  `avail_seats` int(8) NOT NULL,
  `current_lat` decimal(6,4) NOT NULL,
  `current_lon` decimal(7,4) NOT NULL,
  `destination` varchar(100) NOT NULL,
  `creation` date NOT NULL,
  `ending` date NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `driver` (`driver`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `dev_trip`
--


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_user`
--

CREATE TABLE IF NOT EXISTS `dev_user` (
  `id` int(8) NOT NULL auto_increment,
  `username` varchar(100) character set utf8 collate utf8_unicode_ci NOT NULL,
  `password` varchar(100) character set utf8 collate utf8_unicode_ci NOT NULL,
  `email` varchar(100) character set utf8 collate utf8_unicode_ci NOT NULL,
  `firstname` varchar(100) character set utf8 collate utf8_unicode_ci NOT NULL,
  `lastname` varchar(100) character set utf8 collate utf8_unicode_ci NOT NULL,
  `tel` varchar(16) character set utf8 collate utf8_unicode_ci NOT NULL,
  `description` text character set utf8 collate utf8_unicode_ci NOT NULL,
  `regdate` datetime NOT NULL,
  `email_public` tinyint(1) NOT NULL default '0',
  `firstname_public` tinyint(1) NOT NULL default '0',
  `lastname_public` tinyint(1) NOT NULL default '0',
  `tel_public` tinyint(1) NOT NULL default '0',
  `rating_avg` float NOT NULL default '0',
  `rating_num` int(5) NOT NULL default '0',
  `activated` tinyint(1) NOT NULL default '1',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Daten für Tabelle `dev_user`
--

INSERT INTO `dev_user` (`id`, `username`, `password`, `email`, `firstname`, `lastname`, `tel`, `description`, `regdate`, `email_public`, `firstname_public`, `lastname_public`, `tel_public`, `rating_avg`, `rating_num`, `activated`) VALUES
(1, 'demo', '098f6bcd4621d373cade4e832627b4f6', 'patrick.strobel.swt@gmail.com', 'max', 'mustermann', '0121-11111111', 'blabla', '2011-12-01 12:27:23', 0, 1, 0, 1, 0, 0, 1);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_verification`
--

CREATE TABLE IF NOT EXISTS `dev_verification` (
  `user` int(11) NOT NULL,
  `key` varchar(32) character set ascii NOT NULL,
  KEY `user` (`user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `dev_verification`
--

INSERT INTO `dev_verification` (`user`, `key`) VALUES
(1, '5rr6bx62lzx1h3isb7axenlkfempt7tn');

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