/*
Navicat MySQL Data Transfer

Source Server         : vhike localhost
Source Server Version : 50516
Source Host           : localhost:3306
Source Database       : pmp_vhike

Target Server Type    : MYSQL
Target Server Version : 50516
File Encoding         : 65001

Date: 2012-07-11 02:35:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `dev_message`
-- ----------------------------
DROP TABLE IF EXISTS `dev_message`;
CREATE TABLE `dev_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` int(11) NOT NULL,
  `recipient` int(11) NOT NULL,
  `trip` int(11) DEFAULT NULL,
  `query` int(11) DEFAULT NULL,
  `time` datetime NOT NULL,
  `unread` tinyint(1) NOT NULL DEFAULT '1',
  `message` varchar(1000) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of dev_message
-- ----------------------------
INSERT INTO `dev_message` VALUES ('1', '3', '2', '401', null, '2012-06-08 04:00:08', '1', 'Hi, you\'re hot');
INSERT INTO `dev_message` VALUES ('2', '2', '3', '401', null, '2012-06-08 04:08:29', '1', 'Thanks!');
INSERT INTO `dev_message` VALUES ('3', '3', '2', '401', null, '0000-00-00 00:00:00', '1', 'Can I go with you?');
INSERT INTO `dev_message` VALUES ('4', '3', '2', '401', null, '0000-00-00 00:00:00', '1', 'please?');

-- ----------------------------
-- Table structure for `dev_observation`
-- ----------------------------
DROP TABLE IF EXISTS `dev_observation`;
CREATE TABLE `dev_observation` (
  `user_id` int(11) NOT NULL,
  `obs_nr` varchar(11) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of dev_observation
-- ----------------------------
INSERT INTO `dev_observation` VALUES ('33', '328040610');
INSERT INTO `dev_observation` VALUES ('32', '25754654');

-- ----------------------------
-- Table structure for `dev_offer`
-- ----------------------------
DROP TABLE IF EXISTS `dev_offer`;
CREATE TABLE `dev_offer` (
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
) ENGINE=MyISAM AUTO_INCREMENT=66 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of dev_offer
-- ----------------------------
INSERT INTO `dev_offer` VALUES ('65', '420', '106', '5', '33', '32', '2012-06-11 10:40:55', 'I WANT TO TAKE YOU WITH ME!');
INSERT INTO `dev_offer` VALUES ('64', '419', '105', '0', '3', '2', '2012-06-11 10:23:30', 'Hi, can I go for a ride?');

-- ----------------------------
-- Table structure for `dev_position`
-- ----------------------------
DROP TABLE IF EXISTS `dev_position`;
CREATE TABLE `dev_position` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `user` int(8) NOT NULL,
  `latitude` decimal(10,8) NOT NULL,
  `longitude` decimal(11,8) NOT NULL,
  `last_update` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user` (`user`),
  KEY `position` (`latitude`,`longitude`)
) ENGINE=MyISAM AUTO_INCREMENT=70 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of dev_position
-- ----------------------------
INSERT INTO `dev_position` VALUES ('69', '32', '37.41126000', '-122.08352000', '2012-06-11 10:40:52');
INSERT INTO `dev_position` VALUES ('68', '33', '37.42235600', '-122.08372000', '2012-06-11 10:45:22');
INSERT INTO `dev_position` VALUES ('67', '34', '37.44251300', '-122.08349000', '2012-06-11 09:39:15');
INSERT INTO `dev_position` VALUES ('66', '3', '48.78317300', '9.18105100', '2012-06-11 10:21:52');
INSERT INTO `dev_position` VALUES ('64', '5', '37.41226600', '-122.07040400', '2012-06-04 13:57:11');
INSERT INTO `dev_position` VALUES ('65', '2', '0.00000000', '0.00000000', '2012-06-08 01:26:33');
INSERT INTO `dev_position` VALUES ('62', '8', '37.41213200', '-122.08391600', '2012-06-06 14:07:40');
INSERT INTO `dev_position` VALUES ('61', '1', '37.42222600', '-122.08376000', '2012-06-10 22:07:58');

-- ----------------------------
-- Table structure for `dev_query`
-- ----------------------------
DROP TABLE IF EXISTS `dev_query`;
CREATE TABLE `dev_query` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `passenger` int(8) NOT NULL,
  `seats` int(8) NOT NULL,
  `departure` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `destination` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `date` date NOT NULL DEFAULT '0000-00-00',
  `tolerance` tinyint(4) NOT NULL DEFAULT '0',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `passenger` (`passenger`),
  KEY `pos_destination` (`destination`)
) ENGINE=MyISAM AUTO_INCREMENT=107 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of dev_query
-- ----------------------------
INSERT INTO `dev_query` VALUES ('105', '3', '2', null, 'Berlin', '0000-00-00', '0', null);

-- ----------------------------
-- Table structure for `dev_rating`
-- ----------------------------
DROP TABLE IF EXISTS `dev_rating`;
CREATE TABLE `dev_rating` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `rater` int(8) NOT NULL,
  `recipient` int(8) NOT NULL,
  `trip` int(8) NOT NULL,
  `rating` int(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `rater` (`rater`),
  KEY `recipient` (`recipient`),
  KEY `trip` (`trip`)
) ENGINE=MyISAM AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of dev_rating
-- ----------------------------
INSERT INTO `dev_rating` VALUES ('32', '33', '32', '420', '5');

-- ----------------------------
-- Table structure for `dev_ride`
-- ----------------------------
DROP TABLE IF EXISTS `dev_ride`;
CREATE TABLE `dev_ride` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `passenger` int(8) NOT NULL,
  `trip` int(8) NOT NULL,
  `picked_up` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `foreign_key` (`passenger`,`trip`)
) ENGINE=MyISAM AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of dev_ride
-- ----------------------------
INSERT INTO `dev_ride` VALUES ('25', '32', '420', '1');

-- ----------------------------
-- Table structure for `dev_trip`
-- ----------------------------
DROP TABLE IF EXISTS `dev_trip`;
CREATE TABLE `dev_trip` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `driver` int(8) NOT NULL,
  `avail_seats` int(8) NOT NULL,
  `departure` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `destination` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `creation` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `started` tinyint(1) NOT NULL COMMENT 'whether the trip has started',
  `ending` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `driver` (`driver`),
  KEY `pos_destination` (`destination`)
) ENGINE=MyISAM AUTO_INCREMENT=429 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of dev_trip
-- ----------------------------
INSERT INTO `dev_trip` VALUES ('422', '2', '1', null, ';Berlin;', '2012-07-10 07:10:01', null, '0', '0000-00-00 00:00:00');
INSERT INTO `dev_trip` VALUES ('428', '2', '1', null, ';MÃ¼nchen;', '2012-07-09 07:40:00', null, '0', '0000-00-00 00:00:00');
INSERT INTO `dev_trip` VALUES ('421', '2', '1', null, ';Berlin;', '2012-06-12 12:02:00', null, '0', '0000-00-00 00:00:00');
INSERT INTO `dev_trip` VALUES ('420', '33', '0', null, ';Berlin;', '2012-06-11 10:37:31', null, '1', '2012-06-11 10:45:24');
INSERT INTO `dev_trip` VALUES ('419', '2', '2', null, ';Berlin;', '2012-06-12 10:02:00', null, '0', '0000-00-00 00:00:00');

-- ----------------------------
-- Table structure for `dev_user`
-- ----------------------------
DROP TABLE IF EXISTS `dev_user`;
CREATE TABLE `dev_user` (
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
) ENGINE=MyISAM AUTO_INCREMENT=35 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of dev_user
-- ----------------------------
INSERT INTO `dev_user` VALUES ('1', 'demo', 'c06db68e819be6ec3d26c6038d8e8d1f', 'some@mail.de', 'Patrick', 'Strobel', '0151', 'Ein Test-Konto', '2011-12-29 14:52:34', '0', '0', '0', '0', '1');
INSERT INTO `dev_user` VALUES ('5', 'demo3', 'c06db68e819be6ec3d26c6038d8e8d1f', 'test@mail.de', 'test', 'user', '0123456', '', '2012-01-02 18:08:49', '1', '1', '1', '1', '1');
INSERT INTO `dev_user` VALUES ('8', 'demo2', 'c06db68e819be6ec3d26c6038d8e8d1f', 'some1@mail.de', 'mail', 'man', '0123456789', 'test user', '2012-01-12 12:48:17', '0', '0', '0', '0', '1');
INSERT INTO `dev_user` VALUES ('34', 'Anonymous', 'c06db68e819be6ec3d26c6038d8e8d1f', 'anonymous@gmail.com', 'Der', 'Anonyme', '1928374650', '', '2012-06-10 20:58:29', '0', '0', '0', '0', '1');
INSERT INTO `dev_user` VALUES ('32', 'Alex', 'c06db68e819be6ec3d26c6038d8e8d1f', 'p.mp.for.android@googlemail.com', 'Der', 'Mitfahrer', '0123456789', '', '2012-06-10 20:55:42', '1', '1', '1', '1', '1');
INSERT INTO `dev_user` VALUES ('3', 'hiker', 'c06db68e819be6ec3d26c6038d8e8d1f', 'hiker@mail.com', 'Test', 'Hiker', '0175', 'I\'m a test hiker', '2012-06-01 01:26:37', '1', '1', '1', '1', '1');
INSERT INTO `dev_user` VALUES ('2', 'driver', 'c06db68e819be6ec3d26c6038d8e8d1f', 'driver@mail.com', 'Test', 'Driver', '0151', 'I\'m a test driver', '2012-06-01 01:26:37', '0', '0', '0', '0', '1');
INSERT INTO `dev_user` VALUES ('33', 'Andre', 'c06db68e819be6ec3d26c6038d8e8d1f', 'pmp.for.android@googlemail.com', 'Der', 'Fahrer', '9876543210', '', '2012-06-10 20:56:37', '1', '1', '1', '1', '1');

-- ----------------------------
-- Table structure for `dev_verification`
-- ----------------------------
DROP TABLE IF EXISTS `dev_verification`;
CREATE TABLE `dev_verification` (
  `user` int(11) NOT NULL,
  `key` varchar(32) CHARACTER SET ascii NOT NULL,
  PRIMARY KEY (`user`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of dev_verification
-- ----------------------------
INSERT INTO `dev_verification` VALUES ('4', 'uoojdiw06astrij90vuysd3m8n8w0348');
INSERT INTO `dev_verification` VALUES ('5', 'rbnxbk8hfsxg9n36s8ygbg0qbpa73hnh');
INSERT INTO `dev_verification` VALUES ('8', 'rtbc3lsfr735lbomob8woz0ewtdtuih2');
INSERT INTO `dev_verification` VALUES ('9', 'azeqpbq7f0qz3r13n101akz5xhwiulxt');
INSERT INTO `dev_verification` VALUES ('10', 'e4bvj8vnof1p3l9wq83syj9a1q9n7xz0');
INSERT INTO `dev_verification` VALUES ('15', 'j2uybz0fhxabm151infcmnao5bwp48ur');
INSERT INTO `dev_verification` VALUES ('26', 'j705pv2b1d2s19vfhqlqgg4eel45ca5k');
INSERT INTO `dev_verification` VALUES ('32', 'vlhe7x0wfckhyr5hcfaxxh6cu5dcuzmf');
INSERT INTO `dev_verification` VALUES ('33', 'ktgcbujwsxil8nbwh7kva73v563g1zqf');
INSERT INTO `dev_verification` VALUES ('34', 'gdueha1eyqibtr0y6bey0ainzinm5oiu');

-- ----------------------------
-- Function structure for `user_rating`
-- ----------------------------
DROP FUNCTION IF EXISTS `user_rating`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `user_rating`(`user_id` int) RETURNS float
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
END
;;
DELIMITER ;
