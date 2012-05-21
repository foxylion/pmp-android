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

namespace infoapp\properties;

use InvalidArgumentException;
use infoapp\Database;
use infoapp\General;

if (!defined("INCLUDE")) {
    exit;
}

/**
 * Stores statistical information about the users' profile
 * @author Patrick Strobel
 * @version 4.0.0
 */
class ProfilePropertiesStat extends PropertiesStat {

    /** @var char[] */
    private $ringDist;

    /** @var float */
    private $contactsAvg;

    /** @var float */
    private $appsAvg;

    public function __construct($rings, $contacts, $apps) {
        $this->ringDist = $rings;
        $this->contactsAvg = $contacts;
        $this->appsAvg = $apps;
    }

    /**
     * Gets the average of contacts the user have in their address book
     * @return float Average
     */
    public function getContactsAvg() {
        return $this->contactsAvg;
    }

    /**
     * Gets the average of apps the user have in their address book
     * @return float Average
     */
    public function getAppsAvg() {
        return $this->appsAvg;
    }

    /**
     * Gets information about the ring type distribution
     * @return char[] The ring type is stored in the array's key and
     *                  the counted value in the value
     */
    public function getRingDist() {
        return $this->ringDist;
    }

}

/**
 * Stores information about the user's profile and allows to update or insert a new profile information set
 * @author Patrick Strobel
 * @version 4.0.0
 */
class ProfileProperties extends Properties {

    const BOTH = "b";
    const RING = "r";
    const SILENT = "s";
    const VIBRATION = "v";

    /** @var char */
    private $ring = ProfileProperties::BOTH;

    /** @var short */
    private $contacts = 0;

    /** @var short */
    private $apps = 0;

    public static function load($deviceId) {
        $instance = new ProfileProperties($deviceId);

        $db = Database::getInstance();

        $row = $db->fetch($db->query("SELECT * FROM `" . DB_PREFIX . "_profile_prop`
                                      WHERE device = x'" . $deviceId . "'"));

        if ($db->getAffectedRows() > 0) {
            $instance->ring = $row["ring"];
            $instance->contacts = $row["contacts"];
            $instance->apps = $row["apps"];
        }

        return $instance;
    }

    public function writeBack() {
        $db = Database::getInstance();

        // getAffectedRows() may also return 0 if the entry already exists but
        // the user wants to save the same data again. So we have to do
        // a query first to distinguish between "UPDATE" or "INSERT"
        $row = $db->fetch($db->query("SELECT * FROM `" . DB_PREFIX . "_profile_prop`
                                      WHERE device = x'" . $this->deviceId . "'"));
        if (isset($row["device"])) {
            // Update the entry if it exists
            $db->query("UPDATE `" . DB_PREFIX . "_profile_prop`
                    SET
                        `ring`      = \"" . $this->ring . "\",
                        `contacts`  = " . $this->contacts . ",
                        `apps`      = " . $this->apps . "
                    WHERE `device` = x'" . $this->deviceId . "'");
        } else {
            // If no entry has been updated, we have to create one
            $db->query("INSERT INTO `" . DB_PREFIX . "_profile_prop` (
                            `device`,
                            `ring`,
                            `contacts`,
                            `apps`
                        ) VALUES (
                            x'" . $this->deviceId . "',
                            \"" . $this->ring . "\",
                            " . $this->contacts . ",
                            " . $this->apps . "
                        )");
        }
    }

    /**
     * Gets the number of apps that have been installed onto the device
     * @return short    Number of apps
     */
    public function getAppsCount() {
        return $this->apps;
    }

    /**
     * Sets the number of apps that have been installed onto the device
     * @param short $count  Number of apps
     * @throws InvalidArgumentException Thrown, if the argument is no integer or out of range
     */
    public function setAppsCount($count) {
        if (!is_int($count) || $count < 0 || $count > 30000) {
            throw new InvalidArgumentException("\"apps\" is no integer or its value is invalid");
        }
        $this->apps = $count;
    }

    /**
     * Gets the number of contacts the user has inserted in his/her address book
     * @return short    Number of contacts
     */
    public function getContactsCount() {
        return $this->contacts;
    }

    /**
     * Sets the number of contacts the user has insterted in his/her address book
     * @param short $count  Number of contacts
     * @throws InvalidArgumentException Thrown, if the argument is no integer or out of range
     */
    public function setContactsCount($count) {
        if (!is_int($count) || $count < 0 || $count > 30000) {
            throw new InvalidArgumentException("\"contacts\" is no integer or its value is invalid");
        }
        $this->contacts = $count;
    }

    /**
     * Gets the ring notification type
     * @return char   Ring-type (see constants)
     */
    public function getRingType() {
        return $this->ring;
    }

    /**
     * Sets the ring notifiaction type
     * @param char $ring  Ring-type (one of the constants)
     * @throws InvalidArgumentException Thrown, if the argument is no char or is invalid
     */
    public function setRingType($ring) {
        if ($ring != ProfileProperties::BOTH && $ring != ProfileProperties::RING &&
                $ring != ProfileProperties::SILENT && $ring != ProfileProperties::VIBRATION) {
            throw new InvalidArgumentException("\"ring\" is no character or its value is invalid");
        }

        $this->ring = $ring;
    }

    public static function getStatistic() {
        $db = Database::getInstance();

        // Connections
        $row = $db->fetch($db->query("SELECT
                                        COUNT(`device`) AS 'count',
                                        AVG(`contacts`) AS 'contactsAvg',
                                        AVG(`apps`) AS 'appsAvg'
                                      FROM `" . DB_PREFIX . "_profile_prop`"));

        $contacts = $row["contactsAvg"];
        $apps = $row["appsAvg"];

        // Ring type
        $result = $db->query("SELECT COUNT(`ring`) AS 'count', `ring`
                              FROM `" . DB_PREFIX . "_profile_prop`
                              GROUP BY `ring`");

        $rings = array();

        while (($row = $db->fetch($result)) != null) {
            $rings[$row["ring"]] = $row["count"];
        }

        return new ProfilePropertiesStat($rings, $contacts, $apps);
    }

}

?>
