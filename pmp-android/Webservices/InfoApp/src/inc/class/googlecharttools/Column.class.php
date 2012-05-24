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
 * A Column stores the definition of one column of the data table.
 * For each column of the data table, a Column has to be defined
 *
 * @author Patrick Strobel
 * @version 4.1.0
 * @package infoapp
 * @subpackage googlecharttools
 */
class Column {

        /** @var String */
        private $type;
        /** @var String */
        private $id;
        /** @var String */
        private $label;
        /** @var String */
        private $pattern;
        /** @var String */
        private $property;

        /**
         * Creates a new columns.
         * @see https://google-developers.appspot.com/chart/interactive/docs/reference#dataparam for details
         * @param String $type
         * @param String $id
         * @param String $label
         * @param String $pattern
         * @param String $p
         */
        public function __construct($type, $id = null, $label = null, $pattern = null, $property = null) {
            $this->type = $type;
            $this->id = $id;
            $this->label = $label;
            $this->pattern = $pattern;
            $this->property = $property;
        }

        public function getType() {
            return $this->type;
        }

        public function getId() {
            return $this->id;
        }

        public function getLabel() {
            return $this->label;
        }

        public function getProperty() {
            return $this->property;
        }

        public function toJsonString() {
            $string = "{\"type\": \"" . $this->type . "\"";
            if ($this->id != null) {
                $string .= ", \"id\": \"" . $this->id . "\"";
            }

            if ($this->label != null) {
                $string .= ", \"label\": \"" . $this->label . "\"";
            }

            if ($this->pattern != null) {
                $string .= ", \"pattern\": \"" . $this->pattern . "\"";
            }

            if ($this->property != null) {
                $string .= ", \"p\": " . $this->property;
            }
            $string .= "}";

            return $string;
        }
}
?>
