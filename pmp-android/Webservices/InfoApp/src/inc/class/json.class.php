<?php

if (!defined("INCLUDE")) {
    exit;
}

/**
 * Provides general functionality used for JSON output
 * @author  Patrick Strobel
 * @version 1.0.0
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
     * Prints a JSON-error-message showing that a input-data is invalid
     *
     * @param boolean $exit When set to true, the script will be stopped
     */
    public static function printInvalidParameterError($message = '', $exit = true) {
        if ($message == '') {
            self::printError("invalid_parameter", "At least one POST-Parameter is invalid", $exit);
        } else {
            self::printError("invalid_parameter", "Invalid argument(s): $message", $exit);
        }
    }

    /**
     * Prints a JSON-error-message showing that a input-data is invalid
     *
     * @param boolean $exit When set to true, the script will be stopped
     */
    public static function printInvalidIdError($exit = true) {
        self::printError("invalid_id", "At least one event-ID is invalid. All IDs have to be in an ascending order and used on-time only", $exit);
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
     * Converty a JSON object into an array
     * 
     * @param String $input JSON Object
     * @return Object Read data  
     */
    public static function jsonToArry($input) {
        return json_decode($input);
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