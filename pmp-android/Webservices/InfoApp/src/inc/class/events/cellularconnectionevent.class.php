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
 * A cellular connection event stores information about the state of the cellular connection
 * at a given timestamp
 *
 * @author Patrick Strobel
 * @version 1.0.0
 */
class CellularConnectionEvent extends Event {

    private $roaming;
    private $airplane;

    /**
     * Creates a new cellular connection event
     * @param boolean $connected
     * @param boolean $enabled
     * @param String $city
     * @throws InvalidArgumentException
     */
    public function __construct($id, $timestamp, $roaming, $airplane) {
        
        parent::__construct($id, $timestamp);
        
        if (!is_bool($roaming)) {
            throw new InvalidArgumentException("\"roaming\" is no boolean");
        }
        if (!is_bool($airplane)) {
            throw new InvalidArgumentException("\"airplane\" is no boolean");
        }
        
        $this->roaming = $roaming;
        $this->airplane = $airplane;
    }
    
    /**
     * Airplane mode status
     * @return boolean  True, if airplane mode is enabled 
     */
    public function isAirplane() {
        return $this->airplane;
    }
    
    /**
     * Roaming status
     * @return boolean  True, if roaming in active 
     */
    public function isRoaming() {
        return $this->roaming;
    }

}

?>