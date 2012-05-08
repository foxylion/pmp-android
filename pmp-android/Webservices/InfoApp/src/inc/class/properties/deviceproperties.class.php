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
 * Stores information about a device and allows to update or insert a new device information set
 * @author Patrick Strobel
 * @version 4.0.1
 */
class DeviceProperties extends Properties {

    /** @var String */
    private $manufacturer = "unknown";

    /** @var short */
    private $api = 0;

    /** @var String */
    private $kernel = "0.0";

    /** @var String */
    private $model = "unknown";

    /** @var String */
    private $ui = "unknown";

    /** @var short[] */
    private $display = array("x" => 0, "y" => 0);

    /** @var short */
    private $cpu = 0;

    /** @var int */
    private $memoryInt = 0;

    /** @var int */
    private $memoryIntFree = 0;

    /** @var int */
    private $memoryExt = 0;

    /** @var int */
    private $memoryExtFree = 0;

    /** @var float */
    private $camera = 0.0;

    /** @var String[] */
    private $sensors = array();

    /** @var float */
    private $runtime = 0.0;

    /**
     * Load the data for a given device from the db and returns a instance of
     * this class storing this information<br />
     * <b>Warning:</b> This method should not be usd as there is
     * no type or value check for the argument. Use {@see Device} to get an instance
     * instead.
     * @param String $deviceId The device's MD5-Hash this EventManager will be bound to
     * @return DeviceProperties   Instance of this class storing the loaded information
     */
    public static function load($deviceId) {
        $instance = new DeviceProperties($deviceId);

        $db = Database::getInstance();

        $row = $db->fetch($db->query("SELECT * FROM `" . DB_PREFIX . "_device_prop`
                                      WHERE device = x'" . $deviceId . "'"));

        if ($db->getAffectedRows() > 0) {
            $instance->manufacturer = $row["manufacturer"];
            $instance->api = $row["api"];
            $instance->kernel = $row["kernel"];
            $instance->model = $row["model"];
            $instance->ui = $row["ui"];
            $instance->display["x"] = $row["display_x"];
            $instance->display["y"] = $row["display_y"];
            $instance->cpu = $row["cpu"];
            $instance->memoryInt = $row["memory_internal"];
            $instance->memoryIntFree = $row["memory_internal_free"];
            $instance->memoryExt = $row["memory_external"];
            $instance->memoryExtFree = $row["memory_external_free"];
            $instance->camera = $row["camera"];
            $instance->sensors = explode(",", $row["sensors"]);
            $instance->runtime = $row["runtime"];
        }

        return $instance;
    }

    public function writeBack() {
        $db = Database::getInstance();

        // Build sensors string
        $sensorString = "";
        $first = true;

        foreach ($this->sensors as $sensor) {
            if ($first) {
                $first = false;
            } else {
                $sensorString .= ",";
            }
            $sensorString .= $sensor;
        }

        // getAffectedRows() may also return 0 if the entry already exists but
        // the user wants to save the same data again. So we have to do
        // a query first to distinguish between "UPDATE" or "INSERT"
        $row = $db->fetch($db->query("SELECT * FROM `" . DB_PREFIX . "_device_prop`
                                      WHERE device = x'" . $this->deviceId . "'"));
        if (isset($row["device"])) {
            // Update the entry if it exists
            $db->query("UPDATE `" . DB_PREFIX . "_device_prop`
                    SET
                        `manufacturer`          = \"" . $this->manufacturer . "\",
                        `api`                   = " . $this->api . ",
                        `kernel`                = \"" . $this->kernel . "\",
                        `model`                 = \"" . $this->model . "\",
                        `ui`                    = \"" . $this->ui . "\",
                        `display_x`             = " . $this->display["x"] . ",
                        `display_y`             = " . $this->display["y"] . ",
                        `cpu`                   = " . $this->cpu . ",
                        `memory_internal`       = " . $this->memoryInt . ",
                        `memory_internal_free`  = " . $this->memoryIntFree . ",
                        `memory_external`       = " . $this->memoryExt . ",
                        `memory_external_free`  = " . $this->memoryExtFree . ",
                        `camera`                = " . $this->camera . ",
                        `sensors`               = \"" . $sensorString . "\",
                        `runtime`               = " . $this->runtime . "
                    WHERE `device` = x'" . $this->deviceId . "'");
        } else {
            // If no entry has been updated, we have to create one
            $db->query("INSERT INTO `" . DB_PREFIX . "_device_prop` (
                            `device`,
                            `manufacturer`,
                            `api`,
                            `kernel`,
                            `model`,
                            `ui`,
                            `display_x`,
                            `display_y`,
                            `cpu`,
                            `memory_internal`,
                            `memory_internal_free`,
                            `memory_external`,
                            `memory_external_free`,
                            `camera`,
                            `sensors`,
                            `runtime`
                        ) VALUES (
                            x'" . $this->deviceId . "',
                            \"" . $this->manufacturer . "\",
                            " . $this->api . ",
                            \"" . $this->kernel . "\",
                            \"" . $this->model . "\",
                            \"" . $this->ui . "\",
                            " . $this->display["x"] . ",
                            " . $this->display["y"] . ",
                            " . $this->cpu . ",
                            " . $this->memoryInt . ",
                            " . $this->memoryIntFree . ",
                            " . $this->memoryExt . ",
                            " . $this->memoryExtFree . ",
                            " . $this->camera . ",
                            \"" . $sensorString . "\",
                            " . $this->runtime . "
                        )");
        }
    }

    /**
     * Gets the smartphone's manufacturer
     * @return String   The name of the manufacturer
     */
    public function getManufacturer() {
        return $this->manufacturer;
    }

    /**
     * Sets the smartphone's manufacturer
     * @param String $man   The name of the manufacturer
     * @throws InvalidArgumentException Thrown, if the argument is no string or has an invalid length
     */
    public function setManufacturer($man) {
        $man = Database::secureInput($man);

        if (!is_string($man) || strlen($man) < 3 || strlen($man) > 100) {
            throw new InvalidArgumentException("\"manufacturer\" is no string or its length is invalid");
        }

        $this->manufacturer = $man;
    }

    /**
     * Gets the API-level supported by the device
     * @return int  The API-level
     */
    public function getApiLevel() {
        return $this->api;
    }

    /**
     * Sets the API-level supported by the device
     * @param int $level    The API-level
     * @throws InvalidArgumentException Thrown, if the argument is no integer or out of range
     */
    public function setApiLevel($level) {
        if (!is_int($level) || $level <= 0 || $level > 99) {
            throw new InvalidArgumentException("\"level\" is no integer or its value is invalid");
        }
        $this->api = $level;
    }

    /**
     * Gets the version of the linux-kernel used by the device
     * @return String   The kernel version
     */
    public function getKernelVersion() {
        return $this->kernel;
    }

    /**
     * Sets the version of the linux-kernel used by the device
     * @param String $version   The kernel version
     * @throws InvalidArgumentException Thrown, if the argument is no string or has an invalid length
     */
    public function setKernelVersion($version) {
        $version = Database::secureInput($version);

        if (!is_string($version) || strlen($version) < 3 || strlen($version) > 10) {
            throw new InvalidArgumentException("\"kernel\" is no string or its length is invalid");
        }

        $this->kernel = $version;
    }

    /**
     * Gets the smartphone's model
     * @return String   The name of the model
     */
    public function getModel() {
        return $this->model;
    }

    /**
     * Sets the smartphone's model
     * @param String $model The name of the model
     * @throws InvalidArgumentException Thrown, if the argument is no string or has an invalid length
     */
    public function setModel($model) {
        $model = Database::secureInput($model);

        if (!is_string($model) || strlen($model) < 3 || strlen($model) > 99) {
            throw new InvalidArgumentException("\"model\" is no string or its length is invalid");
        }

        $this->model = $model;
    }

    /**
     * Gets the name of the UI used by the smartphone's manufacturer
     * @return String   The name of the UI
     */
    public function getUi() {
        return $this->ui;
    }

    /**
     * Sets the name of the UI used by the smartphone's manufacturer
     * @param String $ui    The name of the UI
     * @throws InvalidArgumentException Thrown, if the argument is no string or has an invalid length
     */
    public function setUi($ui) {
        $ui = Database::secureInput($ui);

        if (!is_string($ui) || strlen($ui) < 3 || strlen($ui) > 99) {
            throw new InvalidArgumentException("\"ui\" is no string or its length is invalid");
        }

        $this->ui = $ui;
    }

    /**
     * Gets the displays resoultion in X and Y direction
     * @return short[]  The resolution. Resolution is stored in keys X and Y, respectively
     */
    public function getDisplayResoultion() {
        return $this->display;
    }

    /**
     * Sets the displays resolution in X and Y direction
     * @param short $x  The X-resolution
     * @param short $y  The Y-resolution
     * @throws InvalidArgumentException Thrown, if one argument is no integer or out of range
     */
    public function setDisplayResolution($x, $y) {
        if (!is_int($x) || $x < 0 || $x > 30000) {
            throw new InvalidArgumentException("\"displayx\" is no integer or its value is invalid");
        }
        if (!is_int($y) || $y < 0 || $y > 30000) {
            throw new InvalidArgumentException("\"displayy\" is no integer or its value is invalid");
        }

        $this->display["x"] = $x;
        $this->display["y"] = $y;
    }

    /**
     * Gets the CPU's frequency
     * @return short    The frequency in MHz
     */
    public function getCpuFrequency() {
        return $this->cpu;
    }

    /**
     * Sets the CPU's frequency
     * @param short $freq   The frequency in MHz
     * @throws InvalidArgumentException Thrown, if the argument is no integer or out of range
     */
    public function setCpuFrequency($freq) {
        if (!is_int($freq) || $freq < 0 || $freq > 30000) {
            throw new InvalidArgumentException("\"cpufrequency\" is no integer or its value is invalid");
        }

        $this->cpu = $freq;
    }

    /**
     * Gets the size of the internal memory
     * @return int  The internal memory's size in MB
     */
    public function getInternalMemory() {
        return $this->memoryInt;
    }

    /**
     * Sets the size of the internal memory
     * @param int $memory   The internal memory's size in MB
     * @throws InvalidArgumentException Thrown, if the argument is no integer or out of range
     */
    public function setInternalMemory($memory) {
        if (!is_int($memory) || $memory < 0 || $memory > 30000) {
            throw new InvalidArgumentException("\"interalmemory\" is no integer or its value is invalid");
        }

        $this->memoryInt = $memory;
    }

    /**
     * Gets the free space size of the internal memory
     * @return int  The free space of the internal memory in MB
     */
    public function getInternalMemoryFree() {
        return $this->memoryIntFree;
    }

    /**
     * Sets the free space size of the internal memory
     * @param int $memory   The free space of the internal memory in MB
     * @throws InvalidArgumentException Thrown, if the argument is no integer or out of range
     */
    public function setInternalMemoryFree($memory) {
        if (!is_int($memory) || $memory < 0 || $memory > 30000) {
            throw new InvalidArgumentException("\"freeinternalmemory\" is no integer or its value is invalid");
        }

        $this->memoryIntFree = $memory;
    }

    /**
     * Gets the size of the external memory
     * @return int  The external memory's size in MB
     */
    public function getExternalMemory() {
        return $this->memoryExt;
    }

    /**
     * Sets the size of the external memory
     * @param int $memory   The external memory's size in MB
     * @throws InvalidArgumentException Thrown, if the argument is no integer or out of range
     */
    public function setExternalMemory($memory) {
        if (!is_int($memory) || $memory < 0 || $memory > 30000) {
            throw new InvalidArgumentException("\"externalmemory\" is no integer or its value is invalid");
        }

        $this->memoryExt = $memory;
    }

    /**
     * Gets the free space size of the internal memory
     * @return int  The free space of the external memory in MB
     */
    public function getInternalExternalFree() {
        return $this->memoryExtFree;
    }

    /**
     * Sets the free space size of the external memory
     * @param int $memory   The free space of the external memory in MB
     * @throws InvalidArgumentException Thrown, if the argument is no integer or out of range
     */
    public function setExternalMemoryFree($memory) {
        if (!is_int($memory) || $memory < 0 || $memory > 30000) {
            throw new InvalidArgumentException("\"freeexternalmemory\" is no integer or its value is invalid");
        }

        $this->memoryExtFree = $memory;
    }

    /**
     * Gets the resolution of the internal camera
     * @return float    The camera's resolution
     */
    public function getCameraResolution() {
        return $this->camera;
    }

    /**
     * Sets the resolution of the internal camera
     * @param float $res    The camera's resolution
     * @throws InvalidArgumentException Thrown, if the argument is no integer or out of range
     */
    public function setCameraResolution($res) {
        if (!is_float($res) || $res < 0 || $res >= 100) {
            throw new InvalidArgumentException("\"resolution\" is no float or its value is invalid");
        }

        $this->camera = $res;
    }

    /**
     * Gets all sensors supported by this device
     * @return String[] Supported sensors
     */
    public function getSensors() {
        return $this->sensors;
    }

    /**
     * Sets all sensors supported by this device
     * @param String[] $sensors Supported sensors
     * @throws InvalidArgumentException Thrown, if the argument is no string-array
     */
    public function setSensors($sensors) {
        if (!is_array($sensors)) {
            throw new InvalidArgumentException("\"sensors\" is no array");
        }

        foreach ($sensors as $sensor) {
            if (!is_string($sensor)) {
                throw new InvalidArgumentException("\"sensors\" contains a element that is no string");
            }
        }

        $this->sensors = $sensors;
    }

    /**
     * Gets the total runtime of the device
     * @return float    The device's runtime
     */
    public function getRuntime() {
        return $this->runtime;
    }

    public function setRuntime($time) {
        if (!is_float($time) || $time < 0 || $time >= 100000000) {
            throw new InvalidArgumentException("\"runtime\" is no float or its value is invalid");
        }

        $this->runtime = $time;
    }
}
?>
