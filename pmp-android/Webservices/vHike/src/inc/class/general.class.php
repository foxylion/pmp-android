<?php
if (!defined("INCLUDE")) {
    exit;
}

class InputException extends Exception {}

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
    
    public static function validId($input) {
        return (isset($input) && is_numeric($input) && $input > 0);
    }

    public static function validCoordinate($input) {
        return (isset($input) && is_numeric($input) && $input>=-180 && $input<=180);
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
