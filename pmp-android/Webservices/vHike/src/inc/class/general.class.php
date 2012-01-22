<?php
if (!defined("INCLUDE")) {
    exit;
}

class InputException extends Exception {}

/**
 * Encapsulates generic functions used by many parts of the application
 * @author Dang Huynh, Patrick Strobel 
 * @version 1.0.1
 */
class General {
    
    /**
     * String used inside a regular expression. This matches a single
     * letter, that might be used in any european language (e.g. a-z, á, Ê)
     */
    const REG_INTCHARS = "A-Za-zÄäÖößÜüÁÀÂáàâÉÈÊéèêÍÌÎíìîÓÒÔóòôÚÙÛúùûÇç";

    /**
     * Checks if the length of an input string is valid 
     * @param String $input Input
     * @return boolean  True, if length is valid
     */
    public static function validLength($input) {
        $length = strlen($input);
        
        if ($length > 2 && $length <= 100) {
            return true;
        } else {
            return false;
        }
        
    }
    
    /**
     * Checks if the given parameter is a valid id
     * @param String $input Input
     * @return boolean  True, if parameter is avalid id 
     */
    public static function validId($input) {
        return (isset($input) && is_numeric($input) && $input > 0);
    }

    /**
     * Checks if the given parameter is a valid coordinate
     * @param String $input Input
     * @return boolean  True, if parameter is a valid coordinate 
     * @deprecated Please use <code>validLatitude()</code> or <code>validLongitude()</code>
     *              as latitude and longitude have a different range
     */
    public static function validCoordinate($input) {
        return (isset($input) && is_numeric($input) && $input>=-180 && $input<=180);
    }
    
    /**
     * Checks if the given parameter is a valid latitude
     * @param String $input The input
     * @return boolean  True, if the parameter is a floating-point number and a valid latitude 
     */
    public static function validLatitude($input) {
        return (isset($input) && is_numeric($input) && $input >= -90 && $input <= 90);
    }
    
    /**
     * Checks if the given parameter is a valid longitude
     * @param String $input The input
     * @return boolean  True, if the parameter is a floating-point number and a valid longitude 
     */
    public static function validLongitude($input) {
        return (isset($input) && is_numeric($input) && $input >= -180 && $input <= 180);
    }
    
    /**
     * Generates a random string.
     * Useful for creating passwords or activation strings
     *   
     * @param    $length   Length of the generated string
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
