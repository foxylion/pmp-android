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

/**
 * Gives access to bluetooth or WiFi events 
 * @author Patrick Strobel
 * @version 1.0.0
 */
class ConnectionEventManager extends EventManager {

    public function __construct($deviceId) {
        parent::__construct($deviceId);
    }

    public function getLastId() {
        $db = Database::getInstance();

        $row = $db->fetch($db->query("SELECT `connection` FROM `" . DB_PREFIX . "_last_event_ids` 
                                      WHERE device = x'" . $this->deviceId . "'"));

        if ($db->getAffectedRows() <= 0) {
            return 0;
        }

        return (int) $row["connection"];
    }

    /**
     * @param ConnectionEvent[] $events 
     */
    protected function writeBack($events) {
        $db = Database::getInstance();

        // Add events
        $lastId = 0;
        foreach ($events as $event) {
            $db->query("INSERT INTO `" . DB_PREFIX . "_connection` (
                            `device`, 
                            `event_id`, 
                            `timestamp`, 
                            `connected`, 
                            `enabled`, 
                            `city`
                        ) VALUES (
                            x'" . $this->deviceId . "', 
                            " . $event->getId() . ", 
                            " . $event->getTimestamp() . ",
                            " . (int)$event->isConnected() . ",
                            " . (int)$event->isEnabled() . ",
                            \"" . $event->getCity() . "\"
                        )");
            $lastId = $event->getId();
        }
        
        $this->updateOrInsertLastIdEntry("connection", $lastId);
    }

    protected function isEventTypeValid($event) {
        return $event instanceof ConnectionEvent;
    }

}

?>
