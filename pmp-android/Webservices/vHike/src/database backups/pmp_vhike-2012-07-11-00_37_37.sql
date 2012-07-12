-- phpMyAdmin SQL Dump
-- version 3.4.11deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 11, 2012 at 12:37 AM
-- Server version: 5.5.24
-- PHP Version: 5.4.4-2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `pmp_vhike`
--

DELIMITER $$
--
-- Procedures
--
$$

CREATE DEFINER=`pmp_vhike`@`%` PROCEDURE `list_hiker`(IN `driverid` int, IN `dist` int)
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
		WHERE trip_dest LIKE CONCAT('%;',`query`.destination,';%')
			AND `query`.passenger != driverid AND `query`.seats <= free_seats 
			AND pos.longitude BETWEEN lon1 AND lon2 AND pos.latitude BETWEEN lat1 AND lat2
		HAVING distance < dist
		ORDER BY distance ASC;
	END IF;
END$$

--
-- Functions
--
CREATE DEFINER=`pmp_vhike`@`%` FUNCTION `user_rating`(`user_id` int) RETURNS float
BEGIN
	DECLARE result FLOAT;
	
	SELECT AVG(rating) INTO result
	FROM dev_rating
	WHERE recipient =`user_id`;

	IF(result IS NULL) THEN
		RETURN 0;
	ELSE
		RETURN result;
	END IF;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `dev_message`
--

CREATE TABLE IF NOT EXISTS `dev_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` int(11) NOT NULL,
  `recipient` int(11) NOT NULL,
  `trip` int(11) DEFAULT NULL,
  `query` int(11) DEFAULT NULL,
  `time` datetime NOT NULL,
  `unread` tinyint(1) NOT NULL DEFAULT '1',
  `message` varchar(1000) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `dev_message`
--

INSERT INTO `dev_message` (`id`, `sender`, `recipient`, `trip`, `query`, `time`, `unread`, `message`) VALUES
(1, 3, 2, 401, NULL, '2012-06-08 04:00:08', 1, 'Hi, you''re hot'),
(2, 2, 3, 401, NULL, '2012-06-08 04:08:29', 1, 'Thanks!'),
(3, 3, 2, 401, NULL, '0000-00-00 00:00:00', 1, 'Can I go with you?'),
(4, 3, 2, 401, NULL, '0000-00-00 00:00:00', 1, 'please?');

-- --------------------------------------------------------

--
-- Table structure for table `dev_observation`
--

CREATE TABLE IF NOT EXISTS `dev_observation` (
  `user_id` int(11) NOT NULL,
  `obs_nr` varchar(11) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dev_observation`
--

INSERT INTO `dev_observation` (`user_id`, `obs_nr`) VALUES
(33, '328040610'),
(32, '25754654');

-- --------------------------------------------------------

--
-- Table structure for table `dev_offer`
--

CREATE TABLE IF NOT EXISTS `dev_offer` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `trip` int(8) NOT NULL,
  `query` int(8) NOT NULL,
  `status` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `sender` int(8) NOT NULL DEFAULT '-1',
  `recipient` int(8) NOT NULL DEFAULT '-1',
  `time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `message` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `trip` (`trip`),
  KEY `query` (`query`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=66 ;

--
-- Dumping data for table `dev_offer`
--

INSERT INTO `dev_offer` (`id`, `trip`, `query`, `status`, `sender`, `recipient`, `time`, `message`) VALUES
(65, 420, 106, 5, 33, 32, '2012-06-11 10:40:55', 'I WANT TO TAKE YOU WITH ME!'),
(64, 419, 105, 0, 3, 2, '2012-06-11 10:23:30', 'Hi, can I go for a ride?');

-- --------------------------------------------------------

--
-- Table structure for table `dev_position`
--

CREATE TABLE IF NOT EXISTS `dev_position` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `user` int(8) NOT NULL,
  `latitude` decimal(10,8) NOT NULL,
  `longitude` decimal(11,8) NOT NULL,
  `last_update` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user` (`user`),
  KEY `position` (`latitude`,`longitude`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=70 ;

--
-- Dumping data for table `dev_position`
--

INSERT INTO `dev_position` (`id`, `user`, `latitude`, `longitude`, `last_update`) VALUES
(69, 32, 37.41126000, -122.08352000, '2012-06-11 10:40:52'),
(68, 33, 37.42235600, -122.08372000, '2012-06-11 10:45:22'),
(67, 34, 37.44251300, -122.08349000, '2012-06-11 09:39:15'),
(66, 3, 48.78317300, 9.18105100, '2012-06-11 10:21:52'),
(64, 5, 37.41226600, -122.07040400, '2012-06-04 13:57:11'),
(65, 2, 37.42210800, -122.08382400, '2012-07-11 00:33:28'),
(62, 8, 37.41213200, -122.08391600, '2012-06-06 14:07:40'),
(61, 1, 37.42222600, -122.08376000, '2012-06-10 22:07:58');

-- --------------------------------------------------------

--
-- Table structure for table `dev_query`
--

CREATE TABLE IF NOT EXISTS `dev_query` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `passenger` int(8) NOT NULL,
  `seats` int(8) NOT NULL,
  `destination` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `date` date NOT NULL DEFAULT '0000-00-00',
  `tolerance` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `passenger` (`passenger`),
  KEY `pos_destination` (`destination`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=107 ;

--
-- Dumping data for table `dev_query`
--

INSERT INTO `dev_query` (`id`, `passenger`, `seats`, `destination`, `date`, `tolerance`) VALUES
(105, 3, 2, 'Berlin', '0000-00-00', 0);

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
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=33 ;

--
-- Dumping data for table `dev_rating`
--

INSERT INTO `dev_rating` (`id`, `rater`, `recipient`, `trip`, `rating`) VALUES
(32, 33, 32, 420, 5);

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
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=26 ;

--
-- Dumping data for table `dev_ride`
--

INSERT INTO `dev_ride` (`id`, `passenger`, `trip`, `picked_up`) VALUES
(25, 32, 420, 1);

-- --------------------------------------------------------

--
-- Table structure for table `dev_trip`
--

CREATE TABLE IF NOT EXISTS `dev_trip` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `driver` int(8) NOT NULL,
  `avail_seats` int(8) NOT NULL,
  `destination` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `creation` datetime NOT NULL,
  `started` tinyint(1) NOT NULL COMMENT 'whether the trip has started',
  `ending` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `driver` (`driver`),
  KEY `pos_destination` (`destination`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=439 ;

--
-- Dumping data for table `dev_trip`
--

INSERT INTO `dev_trip` (`id`, `driver`, `avail_seats`, `destination`, `creation`, `started`, `ending`) VALUES
(435, 2, 1, ';Stuttgart;', '2012-07-11 01:07:01', 0, '0000-00-00 00:00:00'),
(436, 2, 1, ';Berlin;', '2012-07-11 01:13:01', 0, '0000-00-00 00:00:00'),
(437, 2, 1, ';MÃ¼nchen;', '2012-07-11 01:19:01', 0, '0000-00-00 00:00:00'),
(438, 2, 1, ';Berlin;', '2012-07-11 00:32:36', 1, '2012-07-11 00:33:31'),
(434, 2, 1, ';Berlin;', '2012-07-11 01:04:01', 0, '0000-00-00 00:00:00'),
(420, 33, 0, ';Berlin;', '2012-06-11 10:37:31', 1, '2012-06-11 10:45:24'),
(419, 2, 2, ';Berlin;', '2012-06-12 10:02:00', 0, '0000-00-00 00:00:00');

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
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=35 ;

--
-- Dumping data for table `dev_user`
--

INSERT INTO `dev_user` (`id`, `username`, `password`, `email`, `firstname`, `lastname`, `tel`, `description`, `regdate`, `email_public`, `firstname_public`, `lastname_public`, `tel_public`, `activated`) VALUES
(1, 'demo', 'c06db68e819be6ec3d26c6038d8e8d1f', 'some@mail.de', 'Patrick', 'Strobel', '0151', 'Ein Test-Konto', '2011-12-29 14:52:34', 0, 0, 0, 0, 1),
(5, 'demo3', 'c06db68e819be6ec3d26c6038d8e8d1f', 'test@mail.de', 'test', 'user', '0123456', '', '2012-01-02 18:08:49', 1, 1, 1, 1, 1),
(8, 'demo2', 'c06db68e819be6ec3d26c6038d8e8d1f', 'some1@mail.de', 'mail', 'man', '0123456789', 'test user', '2012-01-12 12:48:17', 0, 0, 0, 0, 1),
(34, 'Anonymous', 'c06db68e819be6ec3d26c6038d8e8d1f', 'anonymous@gmail.com', 'Der', 'Anonyme', '1928374650', '', '2012-06-10 20:58:29', 0, 0, 0, 0, 1),
(32, 'Alex', 'c06db68e819be6ec3d26c6038d8e8d1f', 'p.mp.for.android@googlemail.com', 'Der', 'Mitfahrer', '0123456789', '', '2012-06-10 20:55:42', 1, 1, 1, 1, 1),
(3, 'hiker', 'c06db68e819be6ec3d26c6038d8e8d1f', 'hiker@mail.com', 'Test', 'Hiker', '0175', 'I''m a test hiker', '2012-06-01 01:26:37', 1, 1, 1, 1, 1),
(2, 'driver', 'c06db68e819be6ec3d26c6038d8e8d1f', 'driver@mail.com', 'Test', 'Driver', '0151', 'I''m a test driver', '2012-06-01 01:26:37', 0, 0, 0, 0, 1),
(33, 'Andre', 'c06db68e819be6ec3d26c6038d8e8d1f', 'pmp.for.android@googlemail.com', 'Der', 'Fahrer', '9876543210', '', '2012-06-10 20:56:37', 1, 1, 1, 1, 1);

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
(10, 'e4bvj8vnof1p3l9wq83syj9a1q9n7xz0'),
(15, 'j2uybz0fhxabm151infcmnao5bwp48ur'),
(26, 'j705pv2b1d2s19vfhqlqgg4eel45ca5k'),
(32, 'vlhe7x0wfckhyr5hcfaxxh6cu5dcuzmf'),
(33, 'ktgcbujwsxil8nbwh7kva73v563g1zqf'),
(34, 'gdueha1eyqibtr0y6bey0ainzinm5oiu');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
