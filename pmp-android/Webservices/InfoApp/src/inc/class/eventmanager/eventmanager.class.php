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

if (!defined("INCLUDE")) {
    exit;
}

class InvalidOrderException extends Exception {

}

class IdInUseException extends Exception {

}

/**
 * Abstract base class for all event managers that might be used by the webservices.<br />
 * <b>Warning:</b> Sub classes should not be to instantiated directly as there is
 * no type or value check in the constructor. Use {@see Device} to get an instance
 * instead.
 * @author Patrick Strobel
 * @version 4.0.2
 */
abstract class EventManager {

    /**
     * MD5-Hash used to identify the device
     * @var String
     */
    protected $deviceId;

    /**
     * Creates a new EventManager.
     * @param String $deviceId The device's MD5-Hash this EventManager will be bound to
     */
    public function __construct($deviceId) {
        $this->deviceId = $deviceId;
    }

    /**
     * Adds events to the event-table.
     * Before events are added, their order will be checked. That is:
     * <ul>
     *  <li>if no ID is used twice and not in use by another entry in the database</li>
     *  <li>for all events A and B: A.timesamp <= B.timesampt <=> A.id < B.id</li>
     * </ul>
     * @param Event[] $events Events to check
     * @throws InvalidOrderException Thrown, if the ID or timestamp order is violated
     * @throws IdInUseException Thrown, if at least on ID is already used in the table
     */
    public function addEvents($events) {
        if (count($events) <= 0) {
            return;
        }

        $lastId = -1;
        $lastTimestamp = -1;

        // Check ID and timestamp order
        for ($i = 0; $i < count($events); $i++) {
            $event = $events[$i];

            if (!$this->isEventTypeValid($event)) {
                throw new InvalidArgumentException("At least one event is not a object of the proper sub-class \"Event\"");
            }

            if ($lastId >= $event->getId()) {
                throw new InvalidOrderException("The order of the event's IDs is invalid or IDs are used twice");
            }

            if ($lastTimestamp > $event->getTimestamp()) {
                throw new InvalidOrderException("The order of the event's timestampts is invalid");
            }

            $lastId = $event->getId();
            $lastTimestamp = $event->getTimestamp();
        }

        // Check if ID is already in use
        if ($events[0]->getId() <= $this->getLastId()) {
            throw new IdInUseException("At least the first event's ID is already in use");
        }

        // Write data into db
        $this->writeBack($events);
    }

    /**
     * Gets all available events for a one-day-period
     * @param long $startTimestamp Start timestamp
     * @return Events[] The loaded events
     */
    public function getEventsOneDay($startTs) {
        return $this->getEventsInterval($startTs, $startTs + 86400000);
    }

    /**
     * Gets all available events for a multiple-day-period
     * @param long $startTimestamp Start timestamp
     * @param int $days   Number of days
     * @return Events[] The loaded events
     */
    public function getEventsMultDays($startTs, $days) {
        return $this->getEventsInterval($startTs, $startTs + $days * 86400000);
    }

    /**
     * Gets all available events stored in the DB for the given interval
     * @param long  $fromTs Start timestamp in MS
     * @param long  $toTs   End timestamp in MS
     * @return Events[] The loaded events
     */
    public abstract function getEventsInterval($fromTs, $toTs);

    /**
     * Gets the ID of the event that has been added to the db the last time
     * @return The last ID
     */
    public abstract function getLastId();

    protected function queryLastId($field) {
        $db = Database::getInstance();

        $row = $db->fetch($db->query("SELECT `" . $field . "` FROM `" . DB_PREFIX . "_last_event_ids`
                                      WHERE device = x'" . $this->deviceId . "'"));

        if ($db->getAffectedRows() <= 0) {
            return 0;
        }

        return (int) $row[$field];
    }

    /**
     * Writes the data back into the corresponding db-table.
     * This function can rely on a correct event order as this will be checked
     * before this method is beeing called
     * @param Event[] $events Events that should add to the table
     */
    protected abstract function writeBack($events);

    /**
     * Checks if the event-type is correct
     * @return True, if the event's type is valid
     */
    protected abstract function isEventTypeValid($event);

    /**
     * Updates the entry in the "last_event_ids" table or creates a new entry
     * if there is no one for the given device ID
     * @param String $field Field whose value shall be updated
     * @param int $value The field's new value
     */
    protected function updateOrInsertLastIdEntry($field, $value) {
        $db = Database::getInstance();

        // Update last ID-field
        $db->query("UPDATE `" . DB_PREFIX . "_last_event_ids`
                    SET `" . $field . "` = " . $value . "
                    WHERE `device` = x'" . $this->deviceId . "'");

        // If no entry has been updated, we have to create one
        if ($db->getAffectedRows() <= 0) {
            $db->query("INSERT INTO `" . DB_PREFIX . "_last_event_ids` (
                            `device`,
                            `$field`
                        ) VALUES (
                            x'" . $this->deviceId . "',
                            " . $value . "
                        )");
        }
    }
}
?>
