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
 * @version 1.0.0 
 */
class Device {

    private $deviceId;

    /** @var Device */
    private static $instance = null;
    private $bluetoothMgr = null;
    private $wifiMgr = null;

    private function __construct($deviceId) {
        $this->deviceId = $deviceId;
    }

    /**
     * Returns the singelton-instance of this class
     * @param String $deviceId The device's ID thats represented by this object
     * @return Device The instance
     * @throws InvalidArgumentException Thrown, if this method has already created 
     *              an instance having another device ID than given as argument
     */
    public static function getInstance($deviceId) {
        if (self::$instance == null) {
            self::$instance = new Device($deviceId);
        }

        if (self::$instance->deviceId != $deviceId) {
            throw new InvalidArgumentException("The given device id does not match the id of the stored instance");
        }
        return self::$instance;
    }

    /**
     * Gets the manager for bluetooth connection events
     * @return ConnectionEventManager The manager 
     */
    public function getBluetoothConnectionManager() {
        if ($this->bluetoothMgr == null) {
            $this->bluetoothMgr = new ConnectionEventManager($this->deviceId, ConnectionEventManager::BLUETOOTH);
        }
        return $this->bluetoothMgr;
    }

    /**
     * Gets the manager for bluetooth connection events
     * @return ConnectionEventManager The manager 
     */
    public function getWifiConnectionManager() {
        if ($this->wifiMgr == null) {
            $this->wifiMgr = new ConnectionEventManager($this->deviceId, ConnectionEventManager::WIFI);
        }
        return $this->wifiMgr;
    }

}

?>
