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
 * Stores statistical information about the connections
 * @author Patrick Strobel
 * @version 4.0.0
 */
class ConnectionPropertiesStat extends PropertiesStat {
    /** @var float */
    private $bluetoothAvg;
    /** @var float */
    private $wifiAvg;
    /** @var String[] */
    private $providerDist;
    /** @var float[] */
    private $signalAvg;
    /** @var float */
    private $roamingPerc;

    public function __construct($bluetooh, $wifi, $provider, $roaming, $signal) {
        $this->bluetoothAvg = $bluetooh;
        $this->wifiAvg = $wifi;
        $this->providerDist = $provider;
        $this->roamingPerc = $roaming;
        $this->signalAvg = $signal;
    }

    /**
     * Gets the average of bluetooth connection
     * @return float Average
     */
    public function getBluetoothAvg() {
        return $this->bluetoothAvg;
    }

    /**
     * Gets the average of wifi connection
     * @return float Average
     */
    public function getWifiAvg() {
        return $this->wifiAvg;
    }

    /**
     * Gets information about the provider distribution
     * @return int[] The providers's name is stored in the array's key and
     *                  the counted value in the value
     */
    public function getProviderDist() {
        return $this->providerDist;
    }

    /**
     * Gets the percentage of active roaming connection
     * @return float Percentage (between 0 and 100)
     */
    public function getRoamingPerc() {
        return $this->roamingPerc;
    }

    /**
     * Gets information about the signal strength
     * @return int[] The provider's name is stored in the array's key and
     *                  the average value in the value
     */
    public function getSignalAvg() {
        return $this->signalAvg;
    }
}

/**
 * Stores information about the device's connection and allows to update or insert a new device information set
 * @author Patrick Strobel
 * @version 4.2.0
 */
class ConnectionProperties extends Properties {

    /** @var short */
    private $wifi = 0;

    /** @var short */
    private $bluetooth = 0;

    /** @var String */
    private $provider = "unknown";

    /** @var boolean */
    private $roaming = false;

    /** @var short */
    private $signal = 0;

    public static function load($deviceId) {
        $instance = new ConnectionProperties($deviceId);

        $db = Database::getInstance();

        $row = $db->fetch($db->query("SELECT * FROM `" . DB_PREFIX . "_connection_prop`
                                      WHERE device = x'" . $deviceId . "'"));

        if ($db->getAffectedRows() > 0) {
            $instance->wifi = $row["wifi"];
            $instance->bluetooth = $row["bluetooth"];
            $instance->provider = $row["provider"];
            $instance->roaming = $row["roaming"];
            $instance->signal = $row["signal"];
        }

        return $instance;
    }

    public function writeBack() {
        $db = Database::getInstance();

        // getAffectedRows() may also return 0 if the entry already exists but
        // the user wants to save the same data again. So we have to do
        // a query first to distinguish between "UPDATE" or "INSERT"
        $row = $db->fetch($db->query("SELECT * FROM `" . DB_PREFIX . "_connection_prop`
                                      WHERE device = x'" . $this->deviceId . "'"));
        if (isset($row["device"])) {
            // Update the entry if it exists
            $db->query("UPDATE `" . DB_PREFIX . "_connection_prop`
                    SET
                        `wifi`      = " . $this->wifi . ",
                        `bluetooth` = " . $this->bluetooth . ",
                        `provider`  = \"" . $this->provider . "\",
                        `roaming`   = " . (int) $this->roaming . ",
                        `signal`    = " . $this->signal . "
                    WHERE `device` = x'" . $this->deviceId . "'");
        } else {
            // If no entry has been updated, we have to create one
            $db->query("INSERT INTO `" . DB_PREFIX . "_connection_prop` (
                            `device`,
                            `wifi`,
                            `bluetooth`,
                            `provider`,
                            `roaming`,
                            `signal`
                        ) VALUES (
                            x'" . $this->deviceId . "',
                            " . $this->wifi . ",
                            " . $this->bluetooth . ",
                            \"" . $this->provider . "\",
                            " . (int) $this->roaming . ",
                            " . $this->signal . "
                        )");
        }
    }

    /**
     * Gets the number of wifi-networks the device has been connected to
     * @return short    Number of wifis
     */
    public function getWifiCount() {
        return $this->wifi;
    }

    /**
     * Sets the number of wifi-networks the device has been connected to
     * @param short $count  Number of networks
     * @throws InvalidArgumentException Thrown, if the argument is no integer or out of range
     */
    public function setWifiCount($count) {
        if (!is_int($count) || $count < 0 || $count > 30000) {
            throw new InvalidArgumentException("\"wificount\" is no integer or its value is invalid");
        }
        $this->wifi = $count;
    }

    /**
     * Gets the number of bluetooth device the device has paired with
     * @return short    Number of bluetooth devices
     */
    public function getBluetoothCount() {
        return $this->bluetooth;
    }

    /**
     * Sets the number of bluetooth device the device has paired with
     * @param short $count  Number of bluetooth devices
     * @throws InvalidArgumentException Thrown, if the argument is no integer or out of range
     */
    public function setBluetoothCount($count) {
        if (!is_int($count) || $count < 0 || $count > 30000) {
            throw new InvalidArgumentException("\"bluetoothcount\" is no integer or its value is invalid");
        }
        $this->bluetooth = $count;
    }

    /**
     * Gets the network provider's name
     * @return String   Name of the provider
     */
    public function getProvider() {
        return $this->provider;
    }

    /**
     * Gets the roaming status
     * @return boolean  True, if roaming is active
     */
    public function getRoaming() {
        return $this->roaming;
    }
    /**
     * Sets the provider's name
     * @param String $name  Number of the provider
     * @throws InvalidArgumentException Thrown, if the argument is no string or has an invalid length
     */
    public function setProvider($name) {
        $name = Database::secureInput($name);

        if (!is_string($name) || strlen($name) < 2 || strlen($name) > 100) {
            throw new InvalidArgumentException("\"provider\" is no string or its length is invalid");
        }

        $this->provider = $name;
    }

    /**
     * Sets the roaming status
     * @param boolen $roaming   True, if roaming is active
     * @throws InvalidArgumentException Thrown, if the argument is no boolean
     */
    public function setRoaming($roaming) {
        if (!is_bool($roaming)) {
            throw new InvalidArgumentException("\"roaming\" is no boolean");
        }
        $this->roaming = $roaming;
    }

    /**
     * Gets the signal's strength
     * @return short    Signal strength in percent.
     */
    public function getSignalStrength() {
        return $this->signal;
    }

    /**
     * Sets the signal's strength
     * @param short $strength   Signal strength in percent
     * @throws InvalidArgumentException Thrown, if argument is no integer or no percentage value
     */
    public function setSignalStrength($strength) {
        if (!General::isPercentageInt($strength)) {
            throw new InvalidArgumentException("\"signalstrength\" is no integer or its value is invalid");
        }
        $this->signal = $strength;
    }

    public static function getStatistic() {
        $db = Database::getInstance();

        // Connections
        $row = $db->fetch($db->query("SELECT
                                        COUNT(`device`) AS 'count',
                                        AVG(`bluetooth`) AS 'bluetoothAvg',
                                        AVG(`wifi`) AS 'wifiAvg'
                                      FROM `" . DB_PREFIX . "_connection_prop`"));

        $entries = $row["count"];
        $bluetooth = $row["bluetoothAvg"];
        $wifi = $row["wifiAvg"];

        // Roaming
        $row = $db->fetch($db->query("SELECT COUNT(`roaming`) AS 'roamingCount'
                                      FROM `" . DB_PREFIX . "_connection_prop`
                                      WHERE `roaming` = 1
                                      GROUP BY `roaming`"));
        $roaming = $row["roamingCount"] / $entries * 100;

        // Provider and signal
        $result = $db->query("SELECT COUNT(`device`) AS 'count', `provider`, AVG(`signal`) AS 'signalAvg'
                              FROM `" . DB_PREFIX . "_connection_prop`
                              GROUP BY `provider`");

        $providers = array();
        $signalAvg = array();

        while (($row = $db->fetch($result)) != null) {
            $provider = $row["provider"];
            $providers[$provider] = $row["count"];
            $signalAvg[$provider] = $row["signalAvg"];
        }

        return new ConnectionPropertiesStat($bluetooth, $wifi, $providers, $roaming, $signalAvg);

    }

}

?>
