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
 * Provides general functionality used for JSON output
 * @author  Patrick Strobel
 * @version 1.2.1
 */
class Json {

    /**
     * Prints a default JSON-error-message
     *
     * @param String   $error	 Typ of the error. Has to be one of the values
     *                            defined in the design document (e.g. "NOT_LOGGED_IN")
     * @param String   $msg	   Message to describe the error in more detail
     * @param bool     $exit	  If set to true, this function will close the database
     *                            connection and stops the execution of the script
     *                            after the message has been printed
     */
    public static function printError($error, $msg, $exit = true) {
        $json = array('successful' => false,
            'error' => $error,
            'msg' => $msg);
        echo self::arrayToJson($json);

        // Exit script if required
        if ($exit) {
            Database::getInstance()->disconnect();
            exit();
        }
    }

    /**
     * Prints a JSON-error-message for given DatabaseException
     *
     * @param DatabaseException $exception
     * @param boolean		   $exit When set to true, the script will be stopped
     */
    public static function printDatabaseError($exception, $exit = true) {
        self::printError('internal_database_error', $exception->__toString(), $exit);
    }

    /**
     * Prints a JSON-error-message showing that an input-data is invalid
     *
     * @param InvalidArgumentException $exception
     * @param boolean $exit When set to true, the script will be stopped
     */
    public static function printInvalidParameterError($exception, $exit = true) {
        self::printError("invalid_parameter", $exception->getMessage(), $exit);
    }

    /**
     * Prints a JSON-error-message showing that an ID is used twice
     *
     * @param InvalidEventIdError $exception
     * @param boolean $exit When set to true, the script will be stopped
     */
    public static function printInvalidEventIdError($exception, $exit = true) {
        self::printError("invalid_event_id", $exception->getMessage(), $exit);
    }

    /**
     * Prints a JSON-error-message showing that the events are not ordered properly
     *
     * @param InvalidOrderException $exception
     * @param boolean $exit When set to true, the script will be stopped
     */
    public static function printInvalidEventOrderError($exception, $exit = true) {
        self::printError("invalid_event_order", $exception->getMessage(), $exit);
    }

    /**
     * Converts an array to a JSON string
     *
     * @param array $array Array to convert
     *
     * @return String Generated JSON string
     */
    public static function arrayToJson($array) {
        $json = json_encode($array);

        // Format JSON string if formation is enabled
        if (FORMAT_JSON) {
            $json = self::indent($json);
        }

        return $json;
    }

    /**
     * Prints a given array as JSON object
     * @param array $array Array to print
     */
    public static function printAsJson($array) {
        echo self::arrayToJson($array);
    }

    /**
     * Converty a JSON object into an array
     *
     * @param String $input JSON Object
     * @return Object Read data
     * @throws InvalidArgumentException Thrown, if input is not a JSON object
     */
    public static function jsonToArry($input) {
        $object = json_decode($input);
        if (!is_object($object)) {
            throw new InvalidArgumentException("The given string is not a valid JSON-Object: ".$input);
        }
        return $object;
    }

    /**
     * Parses the JSON data parameter and returns the read event array
     * @param String $input The raw JSON object as string
     * @return Object[] The parsed event array
     */
    public static function getEventArray($input) {
        $data = JSON::jsonToArry($_POST["data"]);

        if (!property_exists($data, "events") || !is_array($data->events)) {
            throw new InvalidArgumentException("Property \"events\" missing in JSON parameter or not an array");
        }

        return $data->events;
    }

    /**
     * This function is copied from http://recursive-design.com/blog/2008/03/11/format-json-with-php/
     * Indents a flat JSON string to make it more human-readable.
     *
     * @param string $json The original JSON string to process.
     *
     * @return string Indented version of the original JSON string.
     */
    private static function indent($json) {

        $result = '';
        $pos = 0;
        $strLen = strlen($json);
        $indentStr = '  ';
        $newLine = "\n";
        $prevChar = '';
        $outOfQuotes = true;

        for ($i = 0; $i <= $strLen; $i++) {

// Grab the next character in the string.
            $char = substr($json, $i, 1);

// Are we inside a quoted string?
            if ($char == '"' && $prevChar != '\\') {
                $outOfQuotes = !$outOfQuotes;

// If this character is the end of an element,
// output a new line and indent the next line.
            } else if (($char == '}' || $char == ']') && $outOfQuotes) {
                $result .= $newLine;
                $pos--;
                for ($j = 0; $j < $pos; $j++) {
                    $result .= $indentStr;
                }
            }

// Add the character to the result string.
            $result .= $char;

// If the last character was the beginning of an element,
// output a new line and indent the next line.
            if (($char == ',' || $char == '{' || $char == '[') && $outOfQuotes) {
                $result .= $newLine;
                if ($char == '{' || $char == '[') {
                    $pos++;
                }

                for ($j = 0; $j < $pos; $j++) {
                    $result .= $indentStr;
                }
            }

            $prevChar = $char;
        }

        return $result;
    }

}

?>