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
 * An awake event stores information about the state of the device
 * at a given timestamp
 *
 * @author Patrick Strobel
 * @version 4.0.0
 */

class AwakeEvent extends Event {

    /** @var boolean */
    private $awake;

    /**
     * Creates a new awake event
     * @param int $id           The event's ID
     * @param long $timestamp   Point in time when this event occurred
     * @param boolean $awake    Indicates if the device is active (true) or in standby (false)
     * @throws InvalidArgumentException
     */
    public function __construct($id, $timestamp, $awake) {

        parent::__construct($id, $timestamp);

        if (!is_bool($awake)) {
            throw new InvalidArgumentException("\"awake\" is no boolean");
        }

        $this->awake = $awake;
    }

    /**
     * Gets the device's awake status
     * @return boolean  True, if device is active
     */
    public function isAwake() {
        return $this->awake;
    }
}
?>
