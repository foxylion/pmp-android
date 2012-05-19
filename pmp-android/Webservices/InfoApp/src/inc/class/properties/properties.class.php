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

if (!defined("INCLUDE")) {
    exit;
}

/**
 * Abstract base class for all properties that might be updated or set by webservices.<br />
 * <b>Warning:</b> Sub classes should not be to instantiated directly as there is
 * no type or value check in the constructor. Use {@see Device} to get an instance
 * instead.
 * @author Patrick Strobel
 * @version 4.1.0
 */
abstract class Properties {

    /**
     * MD5-Hash used to identify the device
     * @var String
     */
    protected $deviceId;

    /**
     * Creates a new EventManager.
     * @param String $deviceId The device's MD5-Hash this EventManager will be bound to
     */
    protected function __construct($deviceId) {
        $this->deviceId = $deviceId;
    }

    /**
     * Load the data from the db and returns a instance of
     * a sub class of {@see Property} storing this information<br />
     * <b>Warning:</b> This method should not be used as there is
     * no type or value check for the argument. Use {@see Device} to get an instance
     * instead.
     * @param String $deviceId The device's MD5-Hash this EventManager will be bound to
     * @return Properties   Instance of this class storing the loaded information
     */
    public abstract static function load($deviceId);

    /**
     * Writes the data back into the corresponding db-table.
     * This function can rely on attruted having the right type as this will be checked
     * by all set-methods
     */
    public abstract function writeBack();

    /**
     * Gets statistical information about the data stored in the db
     */
    public static abstract function getStatistic();
}
?>
