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

use infoapp\Database;
use infoapp\Device;
use infoapp\Json;
use infoapp\events\BatteryEvent;
use infoapp\exceptions\DatabaseException;
use infoapp\exceptions\InvalidOrderException;

define("INCLUDE", true);
require("./../inc/json_framework.inc.php");
try {
    $device = Device::getInstance($_POST["device"]);

    // Parse JSON parameter
    $rawEvents = JSON::getEventArray($_POST["data"]);

    // Build event array
    $events = array();

    foreach ($rawEvents as $event) {
        // Check the JSON object's format
        if (!is_object($event)) {
            throw new InvalidArgumentException("The event array contains a non-object element");
        }
        if (!property_exists($event, "timestamp") || !property_exists($event, "level") ||
                !property_exists($event, "voltage") || !property_exists($event, "plugged") ||
                !property_exists($event, "present") || !property_exists($event, "status") ||
                !property_exists($event, "temperature")) {
            throw new InvalidArgumentException("At least one required attribute is missing in one or more events");
        }

        // Build event object and add it to the event array
        $events[] = new BatteryEvent(1, $event->timestamp, $event->level, $event->voltage,
                $event->plugged, $event->present, $event->status, $event->temperature);

    }

    // Write events into table
    $device->getBatteryEventManager()->addEvents($events);

    Json::printAsJson(array('successful' => true));

} catch (InvalidArgumentException $iae) {
    Json::printInvalidParameterError($iae);
} catch (InvalidOrderException $ioe) {
    Json::printInvalidEventOrderError($ioe);
} catch (DatabaseException $de) {
    Json::printDatabaseError($de);
}

Database::getInstance()->disconnect();
?>
