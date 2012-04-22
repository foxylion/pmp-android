-- phpMyAdmin SQL Dump
-- version 3.2.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 22. April 2012 um 17:43
-- Server Version: 5.1.41
-- PHP-Version: 5.3.1

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `infoapp`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_awake`
--

CREATE TABLE IF NOT EXISTS `dev_awake` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `device` binary(16) NOT NULL,
  `event_id` int(8) NOT NULL,
  `timestamp` bigint(19) NOT NULL,
  `awake` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `EVENT` (`device`,`event_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_battery`
--

CREATE TABLE IF NOT EXISTS `dev_battery` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `device` binary(16) NOT NULL,
  `event_id` int(8) NOT NULL,
  `timestamp` bigint(19) NOT NULL,
  `level` tinyint(3) NOT NULL,
  `plugged` char(1) COLLATE utf8_unicode_ci NOT NULL,
  `present` tinyint(1) NOT NULL,
  `status` char(1) COLLATE utf8_unicode_ci NOT NULL,
  `temperature` float(3,1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `EVENT` (`device`,`event_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_battery_prop`
--

CREATE TABLE IF NOT EXISTS `dev_battery_prop` (
  `device` binary(16) NOT NULL,
  `technology` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `health` tinyint(3) NOT NULL,
  PRIMARY KEY (`device`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_connection`
--

CREATE TABLE IF NOT EXISTS `dev_connection` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `device` binary(16) NOT NULL,
  `event_id` int(8) NOT NULL,
  `timestampt` bigint(19) NOT NULL,
  `connected` tinyint(1) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `city` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `EVENT` (`device`,`event_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=2 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_connection_cellular`
--

CREATE TABLE IF NOT EXISTS `dev_connection_cellular` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `device` binary(16) NOT NULL,
  `event_id` int(8) NOT NULL,
  `timestamp` bigint(19) NOT NULL,
  `roaming` tinyint(1) NOT NULL,
  `airplane` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `EVENT` (`device`,`event_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_connection_prop`
--

CREATE TABLE IF NOT EXISTS `dev_connection_prop` (
  `device` binary(16) NOT NULL,
  `wifi` smallint(5) NOT NULL,
  `bluetooth` smallint(5) NOT NULL,
  `provider` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `signal` tinyint(3) NOT NULL,
  PRIMARY KEY (`device`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_device_prop`
--

CREATE TABLE IF NOT EXISTS `dev_device_prop` (
  `device` binary(16) NOT NULL,
  `manufacturer` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `api` tinyint(3) NOT NULL,
  `kernel` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `model` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `ui` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `display_x` smallint(5) NOT NULL,
  `display_y` smallint(5) NOT NULL,
  `cpu` smallint(5) NOT NULL,
  `memory_internal` smallint(5) NOT NULL,
  `memory_internal_free` smallint(5) NOT NULL,
  `memory_external` smallint(5) NOT NULL,
  `memory_external_free` smallint(5) NOT NULL,
  `camera` float(3,1) NOT NULL,
  `sensors` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `runtime` float(10,2) NOT NULL,
  PRIMARY KEY (`device`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_last_event_ids`
--

CREATE TABLE IF NOT EXISTS `dev_last_event_ids` (
  `device` binary(16) NOT NULL,
  `connection` int(10) NOT NULL,
  `connection_cellular` int(10) NOT NULL,
  `battery` int(10) NOT NULL,
  `awake` int(10) NOT NULL,
  `screen` int(10) NOT NULL,
  `profile` int(10) NOT NULL,
  PRIMARY KEY (`device`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_profile`
--

CREATE TABLE IF NOT EXISTS `dev_profile` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `device` binary(16) NOT NULL,
  `event_id` int(8) NOT NULL,
  `timestamp` bigint(19) NOT NULL,
  `event` char(1) COLLATE utf8_unicode_ci NOT NULL,
  `direction` char(1) COLLATE utf8_unicode_ci NOT NULL,
  `city` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `EVENT` (`device`,`event_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_profile_prop`
--

CREATE TABLE IF NOT EXISTS `dev_profile_prop` (
  `device` binary(16) NOT NULL,
  `ring` char(1) COLLATE utf8_unicode_ci NOT NULL,
  `contacts` smallint(5) NOT NULL,
  `apps` smallint(5) NOT NULL,
  PRIMARY KEY (`device`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dev_screen`
--

CREATE TABLE IF NOT EXISTS `dev_screen` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `device` binary(16) NOT NULL,
  `event_id` int(8) NOT NULL,
  `timestamp` bigint(19) NOT NULL,
  `screen` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `EVENT` (`device`,`event_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
