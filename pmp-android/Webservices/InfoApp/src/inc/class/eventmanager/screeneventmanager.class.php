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

/**
 * Gives access to screen events<br />
 * <b>Warning:</b> This class should not be to instantiated directly as there is
 * no type or value check in the constructor. Use {@see Device} to get an instance
 * instead.
 * @author Patrick Strobel
 * @version 4.0.1
 */
class ScreenEventManager extends EventManager {

    protected function isEventTypeValid($event) {
        return $event instanceof ScreenEvent;
    }

    /**
     *
     * @param ScreenEvent[] $events
     */
    protected function writeBack($events) {
        $db = Database::getInstance();

        // Add events
        $lastId = 0;
        foreach ($events as $event) {
            $db->query("INSERT INTO `" . DB_PREFIX . "_screen` (
                            `device`,
                            `event_id`,
                            `timestamp`,
                            `screen`
                        ) VALUES (
                            x'" . $this->deviceId . "',
                            " . $event->getId() . ",
                            " . $event->getTimestamp() . ",
                            " . (int) $event->isDisplayOn() . "
                        )");
            $lastId = $event->getId();
        }

        $this->updateOrInsertLastIdEntry("screen", $lastId);
    }

    public function getEventsInterval($fromTs, $toTs) {
        if (!is_numeric($fromTs) || !is_numeric($toTs)) {
            throw new InvalidArgumentException("At least one timestamp is not numeric");
        }

        $db = Database::getInstance();

        $res = $db->query("SELECT * FROM `" . DB_PREFIX . "_screen`
                           WHERE `device` = x'" . $this->deviceId . "'
                           AND `timestamp` >= $fromTs
                           AND `timestamp` <= $toTs
                           ORDER BY `event_id` ASC");

        $events = array();
        while (($row = $db->fetch($res)) != null) {
            $events[] = new ScreenEvent($row["event_id"], $row["timestamp"], (bool) $row["screen"]);
        }

        return $events;
    }

    public function getLastId() {
        return $this->queryLastId("screen");
    }

}

?>
