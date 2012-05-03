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

class Chart {

    public static function getJsDataObject($array) {
        $object = "[\n";

        $first = true;

        // Put event data into JS-object
        foreach ($array as $key => $value) {
            if ($first) {
                $first = false;
            } else {
                $object .= ",\n";
            }
            $object .= "['" . $key . "', " . $value . "]";
        }

        $object .= "\n]";
        return $object;
    }

    /**
     * Converts a timestamp given in MS into a formated date/time string
     * @param String $format    Format to which the date/time should be converted
     * @param long $millis      Timestamp in MS that needs to be converted
     * @return String           Formated date/time
     */
    public static function timeMillisToString($format, $millis) {
        // Convert millis to seconds
        $sec = $millis / 1000;
        return date($format, $sec);
    }


}

?>
