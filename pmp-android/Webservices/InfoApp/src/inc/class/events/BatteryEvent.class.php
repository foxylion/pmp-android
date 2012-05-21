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

namespace infoapp\events;

use InvalidArgumentException;
use infoapp\Database;
use infoapp\General;

if (!defined("INCLUDE")) {
    exit;
}

/**
 * A battery event stores information about the state of the battery
 * at a given timestamp
 *
 * @author Patrick Strobel
 * @version 4.1.0
 */

class BatteryEvent extends Event {

    const AC_ADAPTER = "a";
    const USB_ADAPTER = "u";
    const ADAPTER_NOT_PLUGGED = "n";

    const CHARGING = "c";
    const DISCHARGING = "d";
    const FULL = "f";
    const NOT_CHARGING = "n";
    const UNKNOWN = "u";

    /** @var int */
    private $level;
    /** @var int */
    private $voltage;
    /** @var char */
    private $plugged;
    /** @var boolean */
    private $present;
    /** @var char */
    private $status;
    /** @var float */
    private $temperature;

    /**
     * Creates a new battery event
     * @param int $id           The event's ID
     * @param long $timestamp   Point in time when this event occurred
     * @param int $level        The battery's level
     * @param int $voltage      The battery's voltage in mV (between 3000 and 4000)
     * @param char $plugged     "a" if AC-adapter or "u" if USB-adapter is plugged, otherwise "n"
     * @param boolean $present  True, if the battery is installed in the device
     * @param char $status      Charging status (c, d, f, n or u)
     * @param float $temperature    The battery's temperature
     */
    public function __construct($id, $timestamp, $level, $voltage, $plugged, $present, $status, $temperature) {

        parent::__construct($id, $timestamp);

        if (!General::isPercentageInt($level)) {
            throw new InvalidArgumentException("\"level\" is no integer or has no percentage value");
        }
        if (!General::isPercentageInt($voltage) && $voltage < 3000 || $voltage > 4000) {
            throw new InvalidArgumentException("\"voltage\" is no integer or out of range");
        }
        if (!is_string($plugged) ||
                $plugged != BatteryEvent::AC_ADAPTER && $plugged != BatteryEvent::USB_ADAPTER &&
                $plugged != BatteryEvent::ADAPTER_NOT_PLUGGED) {
            throw new InvalidArgumentException("\"plugged\" is not a valid character");
        }
        if (!is_bool($present)) {
            throw new InvalidArgumentException("\"present\" is no boolean");
        }
        if (!is_string($status) ||
                $status != BatteryEvent::CHARGING && $status != BatteryEvent::DISCHARGING &&
                $status != BatteryEvent::FULL && $status != BatteryEvent::NOT_CHARGING &&
                $status != BatteryEvent::UNKNOWN) {
            throw new InvalidArgumentException("\"status\" is not a valid character");
        }
        if (!(is_float($temperature) || is_int($temperature)) || $temperature < -100 || $temperature > 100) {
            throw new InvalidArgumentException("\"temperature\" is no floating point value or out of range");
        }

        $this->level = $level;
        $this->voltage = $voltage;
        $this->plugged = $plugged;
        $this->present = $present;
        $this->status = $status;
        $this->temperature = $temperature;
    }

    /**
     * Gets the battery's level
     * @return int  Level in percent
     */
    public function getLevel() {
        return $this->level;
    }

    public function getVoltage() {
        return $this->voltage;
    }

    /**
     * Gets the ac-adapter status.
     * @return char Adapter status
     * @see BatteryEvent::AC_ADAPTER
     * @see BatteryEvent::ADAPTER_NOT_PLUGGED
     * @see BatteryEvent::USB_ADAPTER
     */
    public function getPlugged() {
        return $this->plugged;
    }

    /**
     * Gets the installation status of the battery
     * @return boolean  True, if the battery is installed in the device
     */
    public function isPresent() {
        return $this->present;
    }

    /**
     * Gets the charging status
     * @return char Charging status
     * @see BatteryEvent::CHARGING
     * @see BatteryEvent::DISCHARGING
     * @see BatteryEvent::FULL
     * @see BatteryEvent::NOT_CHARGING
     * @see BatteryEvent::UNKNOWN
     */
    public function getStatus() {
        return $this->status;
    }

    /**
     * Gets the battery's temperature
     * @return float    Temperature
     */
    public function getTemperature() {
        return $this->temperature;
    }
}
?>
