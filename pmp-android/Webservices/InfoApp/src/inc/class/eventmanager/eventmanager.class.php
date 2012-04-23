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

if (!defined("INCLUDE")) {
    exit;
}

class InvalidOrderException extends Exception {
    
}

class IdInUseException extends Exception {
    
}

/**
 * Abstract base class for all event managers that might be used by the webservices.
 * 
 * @author Patrick Strobel
 * @version 1.0.0 
 */
abstract class EventManager {

    /**
     * MD5-Hash used to identify the device
     * @var String 
     */
    private $deviceId;

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

            if (!$event instanceof Event) {
                throw new InvalidArgumentException("At least one event is not a object of the base class \"Event\"");
            }

            if ($lastId >= $event->getId()) {
                throw new InvalidOrderException("The order of the event's IDs is invalid");
            }

            if ($lastTimestamp > $event->getTimestamp) {
                throw new InvalidOrderException("The order of the event's timestampts is invalid");
            }

            $lastId = $event->getId();
            $lastTimestamp = $event->getTimestamp;
        }

        // Check if ID is already in use
        if ($events[0]->getId() <= $this->getLastId()) {
            throw new IdInUseException("At least the first event's ID is already in use");
        }
        
        // Write data into db
        $this->writeBack($events);
        
    }

    /**
     * Write data into db-table.
     * This function can rely on a correct event-order as this will be checked
     * before this method is called
     * @param Event[] $events Events to add to the table 
     */
    protected abstract function writeBack($events);
    
    /**
     * Get the ID of the event that has been added to the db the last time
     * @return The last ID 
     */
    protected abstract function getLastId();
}

?>
