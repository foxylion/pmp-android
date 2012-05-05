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
 * Encapsulates generic functions used by many parts of the application
 * @author  Dang Huynh, Patrick Strobel
 * @version 4.4.0
 */
class General {
    /**
     * String used inside a regular expression. This matches a single
     * letter, that might be used in any european language (e.g. a-z, Ã¡, ÃŠ)
     */

    const REG_INTCHARS = "A-Za-zÃ„Ã¤Ã–Ã¶ÃŸÃœÃ¼Ã�Ã€Ã‚Ã¡Ã Ã¢Ã‰ÃˆÃŠÃ©Ã¨ÃªÃ�ÃŒÃŽÃ­Ã¬Ã®Ã“Ã’Ã”Ã³Ã²Ã´ÃšÃ™Ã›ÃºÃ¹Ã»Ã‡Ã§";

    /**
     * Checks if the length of an input string is valid
     *
     * @param String $input Input
     *
     * @return boolean  True, if length is valid
     */
    public static function validLength($input) {
        $length = strlen($input);

        if ($length > 2 && $length <= 200) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the given value is a valid id
     *
     * @param String $input Input
     *
     * @return boolean  True, if argument is a valid id
     */
    public static function isValidId($id) {
        return (is_numeric($id) && $id > 0);
    }

    public static function isValidTimestamp($timestamp) {
        return (is_numeric($timestamp) && $timestamp > 0);
    }

    /**
     * Checks a given input if it represents a percentage value
     * @param any $value    Input
     * @return boolean  True, if argument is an integer and beteween 0 and 100
     */
    public static function isPercentageInt($value) {
        return is_int($value) && $value >= 0 && $value <= 100;
    }

    /**
     * Checks if the given input is a valid city name
     * @param String $input Input
     * @return boolean  True, if argument is avalid city name
     */
    public static function isValidCity($input) {
        $length = strlen($input);
        return $length >= 3 && $length <= 100;
    }

    /**
     * Checks if the given value is a valid device ID.
     * That is, if the given value is a MD5 hash
     * @param String $device Input value
     * @return type
     */
    public static function isValidDeviceId($device) {
        return !empty($device) && preg_match('/^[a-fA-F0-9]{32}$/', $device);
    }

    /**
     * Checks if the given parameter is a valid id
     *
     * @param String $input Input
     *
     * @return boolean  True, if parameter is a valid id
     */
    public static function isValidIdParameter($input) {
        return (isset($_POST[$input]) && is_numeric($_POST[$input]) && $_POST[$input] > 0);
    }

    /**
     * Checks is the given string is a boolean
     *
     * @static
     *
     * @param string $string The input string
     *
     * @return boolean TRUE, if the input string is "true", "false" (case ignored) or "0", "1"
     */
    public static function isBoolean($string) {
        return $string == 0 OR strcasecmp($string, "false") OR strcasecmp($string, "0") OR
                $string == 1 OR strcasecmp($string, "true") OR strcasecmp($string, "1");
    }

    /**
     * Generates a random string.
     * Useful for creating passwords or activation strings
     *
     * @param	$length   Length of the generated string
     *
     * @return   Generated string
     */
    public static function randomString($length) {
        $chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        mt_srand((double) microtime() * 1000000);
        $string = null;
        while (strlen($string) < $length) {
            $string .= substr($chars, mt_rand(0, strlen($chars) - 1), 1);
        }
        return $string;
    }

}

?>