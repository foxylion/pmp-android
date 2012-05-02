<?php

/*
 * Copyright 2012 pmp-android development team
 * Project: InfoApp-Webservice
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This file includes files and classes used by all pages.
 * It also sets the HTTP header to the valid JSON-header, connects to the database-server and
 * opens the database.
 */
if (!defined("INCLUDE")) {
    exit;
}

// Automatically escape strings in HTTP-Paramters if "magic_quotes_gpc" is enabled in php.ini
// Fallback if .htaccess is ignored
// Based on http://www.php.net/manual/de/security.magicquotes.disabling.php#91585
if (get_magic_quotes_gpc()) {
    function stripslashes_gpc(&$value)
    {
        $value = stripslashes($value);
    }
    array_walk_recursive($_GET, 'stripslashes_gpc');
    array_walk_recursive($_POST, 'stripslashes_gpc');
    array_walk_recursive($_COOKIE, 'stripslashes_gpc');
    array_walk_recursive($_REQUEST, 'stripslashes_gpc');
}

// Set JSON-Header
header("Content-type: application/json");

// Load config-file
require ("./../inc/config.inc.php");

// Load class-files
require ("./../inc/class/database.class.php");
require ("./../inc/class/general.class.php");
require ("./../inc/class/json.class.php");
require ("./../inc/class/device.class.php");

require ("./../inc/class/eventmanager/eventmanager.class.php");
require ("./../inc/class/eventmanager/awakeeventmanager.class.php");
require ("./../inc/class/eventmanager/batteryeventmanager.class.php");
require ("./../inc/class/eventmanager/cellularconnectioneventmanager.class.php");
require ("./../inc/class/eventmanager/connectioneventmanager.class.php");
require ("./../inc/class/eventmanager/screeneventmanager.class.php");

require ("./../inc/class/events/event.class.php");
require ("./../inc/class/events/awakeevent.class.php");
require ("./../inc/class/events/batteryevent.class.php");
require ("./../inc/class/events/cellularconnectionevent.class.php");
require ("./../inc/class/events/connectionevent.class.php");
require ("./../inc/class/events/screenevent.class.php");

require ("./../inc/class/properties/properties.class.php");
require ("./../inc/class/properties/batteryproperties.class.php");
require ("./../inc/class/properties/connectionproperties.class.php");
require ("./../inc/class/properties/deviceproperties.class.php");

// Connect to database
try {
    Database::getInstance()->connect();
} catch (DatabaseException $de) {
    Json::printDatabaseError($de);
}
?>