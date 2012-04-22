<?php

if (!defined("INCLUDE")) {
    exit;
}

class InputException extends Exception {
    
}

/**
 * Encapsulates generic functions used by many parts of the application
 * @author  Dang Huynh, Patrick Strobel
 * @version 1.0.0
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
     * Checks if the given parameter is a valid id
     *
     * @param String $input Input
     *
     * @return boolean  True, if parameter is a valid id
     */
    public static function isValidId($input) {
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