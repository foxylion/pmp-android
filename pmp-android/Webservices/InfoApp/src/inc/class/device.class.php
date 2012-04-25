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

/**
 * This class handles the access to all classes used to manage the stored information
 *
 * @author Patrick Strobel
 * @version 1.3.0
 */
class Device {

    private $deviceId;

    /** @var Device */
    private static $instance = null;

    private $awakeMgr = null;
    private $batteryMgr = null;
    private $cellularConnectionMgr = null;
    private $connectionMgr = null;
    private $screenMgr = null;

    private function __construct($deviceId) {
        $this->deviceId = $deviceId;
    }

    /**
     * Returns the singelton-instance of this class
     * @param String $deviceId The device's ID thats represented by this object
     * @return Device The instance
     * @throws InvalidArgumentException Thrown, if this method has already created
     *              an instance having another device ID than given as argument or
     *              if the given ID is no valid MD5-Hash
     */
    public static function getInstance($deviceId) {

        if (!General::isValidDeviceId($deviceId)) {
            throw new InvalidArgumentException("The given device ID is no valid MD5-Hash");
        }

        if (self::$instance == null) {
            self::$instance = new Device($deviceId);
        }

        if (self::$instance->deviceId != $deviceId) {
            throw new InvalidArgumentException("The given device ID does not match the ID of the stored instance");
        }
        return self::$instance;
    }

    /**
     * Gets the manager for awake events
     * @return AwakeEventManager The manager
     */
    public function getAwakeEventManager() {
        if ($this->awakeMgr == null) {
            $this->awakeMgr = new AwakeEventManager($this->deviceId);
        }
        return $this->awakeMgr;
    }

    /**
     * Gets the manager for battery events
     * @return BatteryEventManager The manager
     */
    public function getBatteryEventManager() {
        if ($this->batteryMgr == null) {
            $this->batteryMgr = new BatteryEventManager($this->deviceId);
        }
        return $this->batteryMgr;
    }

    /**
     * Gets the manager for cellular connection events
     * @return ConnectionEventManager The manager
     */
    public function getCellularConnectionEventManager() {
        if ($this->cellularConnectionMgr == null) {
            $this->cellularConnectionMgr = new CellularConnectionEventManager($this->deviceId);
        }
        return $this->cellularConnectionMgr;
    }

    /**
     * Gets the manager for bluetooth and wifi connection events
     * @return ConnectionEventManager The manager
     */
    public function getConnectionEventManager() {
        if ($this->connectionMgr == null) {
            $this->connectionMgr = new ConnectionEventManager($this->deviceId);
        }
        return $this->connectionMgr;
    }

    /**
     * Gets the manager for screen events
     * @return ScreenEventManager The manager
     */
    public function getScreenEventManager() {
        if ($this->screenMgr == null) {
            $this->screenMgr = new ScreenEventManager($this->deviceId);
        }
        return $this->screenMgr;
    }


}

?>
