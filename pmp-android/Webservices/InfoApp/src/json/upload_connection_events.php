<?php

/*
 * Copyright 2012 pmp-android development team
 * Project: PMP
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
        if (!property_exists($event, "id") || !property_exists($event, "timestamp") ||
                !property_exists($event, "medium") || !property_exists($event, "connected") ||
                !property_exists($event, "enabled") || !property_exists($event, "city")) {
            throw new InvalidArgumentException("At least one required attribute is missing in one or more events");
        }

        // Build event object and add it to the event array
        $events[] = new ConnectionEvent($event->id, $event->timestamp, $event->medium,
                $event->connected, $event->enabled, $event->city);

    }

    // Write events into table
    $device->getConnectionEventManager()->addEvents($events);

    Json::printAsJson(array('successful' => true));

} catch (InvalidArgumentException $iae) {
    Json::printInvalidParameterError($iae);
} catch (IdInUseException $iiue) {
    Json::printInvalidEventIdError($iiue);
} catch (InvalidOrderException $ioe) {
    Json::printInvalidEventOrderError($ioe);
} catch (DatabaseException $de) {
    Json::printDatabaseError($de);
}

Database::getInstance()->disconnect();
?>
