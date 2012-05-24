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
use infoapp\General;

if (!defined("INCLUDE")) {
    exit;
}

/**
 * Abstract base class for all events that might be used by the webservices.
 *
 * @author Patrick Strobel
 * @version 4.1.0
 * @package infoapp
 * @subpackage events
 */
abstract class Event {

    /** @var int */
    private $id;
    /** @var long */
    private $timestamp;

    public function __construct($id, $timestamp) {
        if (!General::isValidId($id)) {
            throw new InvalidArgumentException("ID is invalid");
        }
        if (!General::isValidTimestamp($timestamp)) {
            throw new InvalidArgumentException("Timestamp is invalid");
        }

        $this->id = $id;
        $this->timestamp = $timestamp;
    }

    /**
     * Gets the event's ID
     * @return int  The ID
     */
    public function getId() {
        return $this->id;
    }

    /**
     * Sets the event's ID
     * @param int $id   The ID
     * @throws InvalidArgumentException Thrown, if the given ID is no integer or out of range
     */
    public function setId($id) {
        if (!General::isValidId($id)) {
            throw new InvalidArgumentException("ID is invalid");
        }

        $this->id = $id;
    }

    /**
     * Returns the time this event occurred
     * @return long  Timestamp in ms accuracy
     */
    public function getTimestamp() {
        return $this->timestamp;
    }

}

?>
