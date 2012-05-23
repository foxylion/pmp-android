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
 * A profile event stores information about the state of the user's profile
 * at a given timestamp
 *
 * @author Patrick Strobel
 * @version 4.0.0
 * @package infoapp
 * @subpackage events
 */
class ProfileEvent extends Event {

    const CALL = "c";
    const SMS = "s";
    const INCOMING = "i";
    const OUTGOING = "o";

    /** @var char */
    private $event;
    /** @var char */
    private $direction;
    /** @var String */
    private $city;

    /**
     * Creates a new connection event
     * @param int $id           The event's ID
     * @param long $timestamp   Point in time when this event occured
     * @param char $event
     * @param char $direction
     * @param String $city
     * @throws InvalidArgumentException
     */
    public function __construct($id, $timestamp, $event, $direction, $city) {

        parent::__construct($id, $timestamp);

        if ($event != ProfileEvent::CALL && $event != ProfileEvent::SMS) {
            throw new InvalidArgumentException("\"event\" is not a valid character");
        }
        if ($direction != ProfileEvent::INCOMING && $direction != ProfileEvent::OUTGOING) {
            throw new InvalidArgumentException("\"direction\" is not a valid character");
        }

        // Escape city if it's value is given and check the value
        if (!$city == null) {
            $city = Database::secureInput($city);

            if (!General::isValidCity($city)) {
                throw new InvalidArgumentException("\"city\" is not a valid city name");
            }
        }

        $this->event = $event;
        $this->direction = $direction;
        $this->city = $city;
    }

    /**
     * City, at which the device has been connected
     * @return String   Name of the city
     */
    public function getCity() {
        return $this->city;
    }

    /**
     * Direction, in which the event occurred
     * @return char Equivalent to one of the given constants (INCOMING or OUTGOING)
     */
    public function getDirection() {
        return $this->direction;
    }

    /**
     * Event that has been occurred
     * @return char Equivalent to one of the given constants (CALL or SMS)
     */
    public function getEvent() {
        return $this->event;
    }
}

?>
