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
 *
 * @author Patrick Strobel
 * @version 4.1.0
 * @package infoapp
 * @subpackage properties
 */
class ConnectionPropertiesStat extends PropertiesStat {
    /** @var float */
    private $bluetoothAvg;
    /** @var float */
    private $wifiAvg;
    /** @var String[] */
    private $providerDist;
    /** @var String[] */
    private $networkDist;
    /** @var float */
    private $roamingPerc;

    public function __construct($bluetooh, $wifi, $provider, $roaming, $network) {
        $this->bluetoothAvg = $bluetooh;
        $this->wifiAvg = $wifi;
        $this->providerDist = $provider;
        $this->roamingPerc = $roaming;
        $this->networkDist = $network;
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
     * Gets information about the network type distribution
     * @return int[] The type's name is stored in the array's key and
     *                  the counted value in the value
     */
    public function getNetworkDist() {
        return $this->networkDist;
    }
}

/**
 * Stores information about the device's connection and allows to update or insert a new device information set
 *
 * @author Patrick Strobel
 * @version 4.3.0
 * @package infoapp
 * @subpackage properties
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

    const NETWORK_TYPE_UNKNOWN = 'un';
    const NETWORK_TYPE_GPRS = 'gp';
    Const NETWORK_TYPE_EDGE = 'ed';
    Const NETWORK_TYPE_UMTS = 'um';
    Const NETWORK_TYPE_HSDPA = 'hd';
    Const NETWORK_TYPE_HSUPA = 'hu';
    Const NETWORK_TYPE_HSPA = 'hs';
    Const NETWORK_TYPE_CDMA = 'cd';
    Const NETWORK_TYPE_EVDO_0 = 'e0';
    Const NETWORK_TYPE_EVDO_A = 'ea';
    Const NETWORK_TYPE_EVDO_B = 'eb';
    Const NETWORK_TYPE_1xRTT = '1r';
    Const NETWORK_TYPE_IDEN = 'id';
    Const NETWORK_TYPE_LTE = 'lt';
    Const NETWORK_TYPE_EHRPD = 'eh';
    Const NETWORK_TYPE_HSPAP = 'hp';

    /** @var String */
    private $network = 0;

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
            $instance->network = $row["network"];
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
                        `network`   = \"" . $this->network . "\"
                    WHERE `device` = x'" . $this->deviceId . "'");
        } else {
            // If no entry has been updated, we have to create one
            $db->query("INSERT INTO `" . DB_PREFIX . "_connection_prop` (
                            `device`,
                            `wifi`,
                            `bluetooth`,
                            `provider`,
                            `roaming`,
                            `network`
                        ) VALUES (
                            x'" . $this->deviceId . "',
                            " . $this->wifi . ",
                            " . $this->bluetooth . ",
                            \"" . $this->provider . "\",
                            " . (int) $this->roaming . ",
                            \"" . $this->network . "\"
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
     * Gets the network type
     * @return String The network type
     */
    public function getNetworkType() {
        return $this->signal;
    }

    /**
     * Sets the network type
     * @param String $network   Network type (one of the NETWORK_TYPE_... constants)
     * @throws InvalidArgumentException Thrown, if argument is not valid
     */
    public function setNetworkType($network) {
        if ($network != self::NETWORK_TYPE_1xRTT && $network != self::NETWORK_TYPE_CDMA &&
                $network != self::NETWORK_TYPE_EDGE && $network != self::NETWORK_TYPE_EHRPD &&
                $network != self::NETWORK_TYPE_EVDO_0 && $network != self::NETWORK_TYPE_EVDO_A &&
                $network != self::NETWORK_TYPE_EVDO_B && $network != self::NETWORK_TYPE_GPRS &&
                $network != self::NETWORK_TYPE_HSDPA && $network != self::NETWORK_TYPE_HSPA &&
                $network != self::NETWORK_TYPE_HSPAP && $network != self::NETWORK_TYPE_HSUPA &&
                $network != self::NETWORK_TYPE_IDEN && $network != self::NETWORK_TYPE_LTE &&
                $network != self::NETWORK_TYPE_UMTS && $network != self::NETWORK_TYPE_UNKNOWN) {
            throw new InvalidArgumentException("\"network\" is no valid character");
        }
        $this->network = $network;
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
        $result = $db->query("SELECT COUNT(`device`) AS 'count', `provider`
                              FROM `" . DB_PREFIX . "_connection_prop`
                              GROUP BY `provider`");

        $providers = array();

        while (($row = $db->fetch($result)) != null) {
            $providers[$row["provider"]] = $row["count"];
        }

        // Network distribution
        $result = $db->query("SELECT COUNT(`device`) AS 'count', `network`
                              FROM `" . DB_PREFIX . "_connection_prop`
                              GROUP BY `provider`");
        $networks = array();

        while (($row = $db->fetch($result)) != null) {
            $networks[$row["network"]] = $row["count"];
        }

        return new ConnectionPropertiesStat($bluetooth, $wifi, $providers, $roaming, $networks);

    }

}

?>
