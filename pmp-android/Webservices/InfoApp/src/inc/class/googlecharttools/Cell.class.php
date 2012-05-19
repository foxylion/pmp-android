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

namespace infoapp\googlecharttools;

if (!defined("INCLUDE")) {
    exit;
}

/**
 * A Cell stores the data from one cell of the data table.
 * Various cells are grouped together in a Row
 *
 * @author Patrick Strobel
 */
class Cell {

    /** @var String */
    private $value;

    /** @var String */
    private $formatted;

    /** @var String */
    private $poperty;

    public function __construct($value, $formatted = null, $property = null) {
        $this->value = $value;
        $this->formatted = $formatted;
        $this->property = $property;
    }

    public function getValue() {
        return $this->value;
    }

    public function getJsonString($type) {
        $string = "{\"v\": ";
        if ($type == "datetime") {
            $string .= "new Date(" . $this->value . ")";
        } elseif ($type == "string") {
            $string .= "\"" . $this->value . "\"";
        } else {
            $string .= $this->value;
        }


        if ($this->formatted != null) {
            $string .= ", \"" . $this->formatted . "\"";
        }

        if ($this->poperty != null) {
            $string .= ", \"" . $this->poperty . "\"";
        }


        $string .= "}";
        return $string;
    }

}

?>
