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
 * Stores information about the device's battery and allows to update or insert a new device information set
 * @author Patrick Strobel
 * @version 1.0.0
 */
class BatteryProperties extends Properties {

    /** @var String */
    private $technology = "unknown";

    /** @var short */
    private $health = 0;

    public static function load($deviceId) {
        $instance = new BatteryProperties($deviceId);

        $db = Database::getInstance();

        $row = $db->fetch($db->query("SELECT * FROM `" . DB_PREFIX . "_battery_prop`
                                      WHERE device = x'" . $deviceId . "'"));

        if ($db->getAffectedRows() > 0) {
            $instance->technology = $row["technology"];
            $instance->health = $row["health"];
        }

        return $instance;
    }

    public function writeBack() {
        $db = Database::getInstance();

        // getAffectedRows() may also return 0 if the entry already exists but
        // the user wants to save the same data again. So we have to do
        // a query first to distinguish between "UPDATE" or "INSERT"
        $row = $db->fetch($db->query("SELECT * FROM `" . DB_PREFIX . "_battery_prop`
                                      WHERE device = x'" . $this->deviceId . "'"));
        if (isset($row["device"])) {
            // Update the entry if it exists
            $db->query("UPDATE `" . DB_PREFIX . "_battery_prop`
                    SET
                        `technology`    = \"" . $this->technology . "\",
                        `health`        = " . $this->health . "
                    WHERE `device` = x'" . $this->deviceId . "'");
        } else {
            // If no entry has been updated, we have to create one
            $db->query("INSERT INTO `" . DB_PREFIX . "_battery_prop` (
                            `device`,
                            `technology`,
                            `health`
                        ) VALUES (
                            x'" . $this->deviceId . "',
                            \"" . $this->technology . "\",
                            " . $this->health . "
                        )");
        }
    }

    /**
     * Gets the technology that is beeing used for the device's battery
     * @return String   Battery technology
     */
    public function getTechnology() {
        return $this->technology;
    }

    /**
     * Sets the technology that is beeing used for the device's battery
     * @param String $tech  Battery technology
     * @throws InvalidArgumentException Thrown, if the argument is no string or has an invalid length
     */
    public function setTechnology($tech) {
        $tech = Database::secureInput($tech);

        if (!is_string($tech) || strlen($tech) < 3 || strlen($tech) > 10) {
            throw new InvalidArgumentException("\"technology\" is no string or its length is invalid");
        }

        $this->technology = $tech;
    }

    /**
     * Gets the battery's health
     * @return short    Battery's health in percent
     */
    public function getHealth() {
        return $this->health;
    }

    /**
     * Sets the battery's health
     * @param short $health Battery's health in percent
     * @throws InvalidArgumentException Thrown, if argument is no integer or no percentage value
     */
    public function setHealth($health) {
        if (!General::isPercentageInt($health)) {
            throw new InvalidArgumentException("\"health\" is no integer or its value is invalid");
        }

        $this->health = $health;
    }

}

?>
