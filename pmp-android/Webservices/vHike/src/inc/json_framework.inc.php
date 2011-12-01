<?php
/**
 * This file includes files and classes used by all pages.
 * It also sets the HTTP header to the valid JSON-header, connects to the database-server and 
 * opens the database.
 */
if (!defined("INCLUDE")) {
    exit;
}

// Set JSON-Header
header("Content-type: application/json");

// Load config-file
require ("./../inc/config.inc.php");

// Load class-files
require ("./../inc/class/database.class.php");
require ("./../inc/class/general.class.php");
require ("./../inc/class/json.class.php");
require ("./../inc/class/user.class.php");

// Connect to database
try {
    Database::getInstance()->connect();
} catch (DatabaseException $de) {
    Json::printError("CANNOT_CONNECT_TO_DATABASE", $de->__toString());
}
?>
