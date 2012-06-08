<?php
/**
 * This file includes files and classes used by all pages.
 * It also sets the HTTP header to the valid JSON-header, connects to the database-server and
 * opens the database.
 */
if (!defined('INCLUDE')) {
	exit;
}

// Set JSON-Header
header('Content-type: application/json');
// Load config file
require ('config.inc.php');

// Load class-files
require ('class/database.class.php');
require ('class/general.class.php');
require ('class/json.class.php');
require ('class/offer.class.php');
require ('class/query.class.php');
require ('class/rating.class.php');
require ('class/ride.class.php');
require ('class/session.class.php');
require ('class/trip.class.php');
require ('class/user.class.php');
require ('class/observation.class.php');

// Connect to database
try {
	Database::getInstance()->connect();
} catch (DatabaseException $de) {
	Json::printError("cannot_connect_to_database", $de->__toString());
} catch (Exception $e) {
	echo $e->getTraceAsString();
}

// EOF