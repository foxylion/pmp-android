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
 * Stores information about the device's connection and allows to update or insert a new device information set
 * @author Patrick Strobel
 * @version 1.0.0
 */
class ConnectionProperties extends Properties {

    /** @var short */
    private $wifi = 0;

    /** @var short */
    private $bluetooth = 0;

    /** @var String */
    private $provider = "unknown";

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
                        `signal`    = " . $this->signal . "
                    WHERE `device` = x'" . $this->deviceId . "'");
        } else {
            // If no entry has been updated, we have to create one
            $db->query("INSERT INTO `" . DB_PREFIX . "_connection_prop` (
                            `device`,
                            `wifi`,
                            `bluetooth`,
                            `provider`,
                            `signal`
                        ) VALUES (
                            x'" . $this->deviceId . "',
                            " . $this->wifi . ",
                            " . $this->bluetooth . ",
                            \"" . $this->provider . "\",
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
     * @return String   Name of the provicer
     */
    public function getProvider() {
        return $this->provider;
    }

    /**
     * Sets the provider's name
     * @param String $name  Number of the provider
     * @throws InvalidArgumentException Thrown, if the argument is no string or has an invalid length
     */
    public function setProvider($name) {
        $name = Database::secureInput($name);

        if (!is_string($name) || strlen($name) < 3 || strlen($name) > 100) {
            throw new InvalidArgumentException("\"provider\" is no string or its length is invalid");
        }

        $this->provider = $name;
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

}

?>
