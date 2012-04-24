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
 * Connection events stores information about the state of the connection
 * at a given timestamp
 *
 * @author Patrick Strobel
 * @version 1.0.1
 */
class ConnectionEvent extends Event {
    
    const BLUETOOTH = 'b';
    const WIFI = 'w';
    
    private $medium;
    private $connected;
    private $enabled;
    private $city;
    

    /**
     * Creates a new connection event
     * @param boolean $connected
     * @param boolean $enabled
     * @param String $city
     * @throws InvalidArgumentException
     */
    public function __construct($id, $timestamp, $medium, $connected, $enabled, $city) {
        
        parent::__construct($id, $timestamp);
        
        if (!is_string($medium) || $medium != ConnectionEvent::BLUETOOTH && $medium != ConnectionEvent::WIFI) {
            throw new InvalidArgumentException("\"medium\" is not a valid character");
        }        
        if (!is_bool($connected)) {
            throw new InvalidArgumentException("\"connected\" is no boolean");
        }
        if (!is_bool($enabled)) {
            throw new InvalidArgumentException("\"enabled\" is no boolean");
        }
        
        $this->medium = (string)$medium;
        $this->connected = (bool)$connected;
        $this->enabled = (bool)$enabled;
        $this->city = (string)$city;
    }
    
    /**
     * Connection status
     * @return boolean True, if device is connected
     */
    public function isConnected() {
        return $this->connected;
    }
    
    public function isEnabled() {
        return $this->enabled;
    }
    
    public function getCity() {
        return $this->city;
    }
}

?>
