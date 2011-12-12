<?php
if (!defined("INCLUDE")) {
    exit;
}

class InputException extends Exception {}

class General {

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
     * Generates a random string.
     * Usefull for greating passwords or activation strings  
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
