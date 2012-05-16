<?php
/**
 * This file includes files and classes used by all pages.
 * It also connects to the database-server and opens the database.
 */
if (!defined('INCLUDE')) {
    exit;
}

// Load config file
require ('./../inc/config.inc.php');

// Load class-files
require ('./../inc/class/database.class.php');
require ('./../inc/class/general.class.php');
require ('./../inc/class/json.class.php');
require ('./../inc/class/offer.class.php');
require ('./../inc/class/query.class.php');
require ('./../inc/class/rating.class.php');
require ('./../inc/class/ride.class.php');
require ('./../inc/class/session.class.php');
require ('./../inc/class/trip.class.php');
require ('./../inc/class/user.class.php');
require ('./../inc/class/observation.class.php');

// Connect to database
try {
    Database::getInstance()->connect();
} catch (DatabaseException $de) {
    Json::printError('cannot_connect_to_database', $de->__toString());
}

// EOF