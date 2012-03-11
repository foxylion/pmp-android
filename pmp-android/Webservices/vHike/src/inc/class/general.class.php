<?php
if (!defined("INCLUDE")) {
	exit;
}

class InputException extends Exception {
}

/**
 * Encapsulates generic functions used by many parts of the application
 * @author  Dang Huynh, Patrick Strobel
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
	 *
	 * @param String $input Input
	 *
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
	 * NOTICE: If you want to check an ID from $_POST, user validateId($input) instead to avoid PHP warning
	 *
	 * @param String $post_key The key of the variable in $_POST that need to be checked
	 *
	 * @return boolean True, if parameter is a valid id
	 * @deprecated
	 */
	public static function validId($post_key) {
		return (isset($post_key) && is_numeric($post_key) && $post_key > 0);
	}

	/**
	 * Checks if the given parameter is a valid id
	 *
	 * @param String $post_key Input post key
	 *
	 * @return boolean  True, if parameter is a valid id
	 */
	public static function validateId($post_key) {
		return (isset($_POST[$post_key]) && is_numeric($_POST[$post_key]) && $_POST[$post_key] > 0);
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
	 * Checks if the given parameter is a valid latitude
	 *
	 * @param String $input The input
	 *
	 * @return boolean  True, if the parameter is a floating-point number and a valid latitude
	 */
//	public static function validLatitude($input) {
//		return (isset($input) && is_numeric($input) && $input >= -90 && $input <= 90);
//	}

	/**
	 * Checks in $_POST if the given parameter is a valid latitude value
	 *
	 * @param String $key Name of the input to be checked in $_POST
	 *
	 * @return boolean  True, if the parameter is a floating-point number and a valid latitude
	 */
	public static function validateLatitude($key) {
		return (isset($_POST[$key]) && is_numeric($_POST[$key]) && $_POST[$key] >= -90 && $_POST[$key] <= 90);
	}

	/**
	 * Checks if the given parameter is a valid longitude
	 *
	 * @param String $input The input
	 *
	 * @return boolean  True, if the parameter is a floating-point number and a valid longitude
	 */
//	public static function validLongitude($input) {
//		return (isset($input) && is_numeric($input) && $input >= -180 && $input <= 180);
//	}

	/**
	 * Checks in $_POST if the given parameter is a valid longitude value
	 *
	 * @param String $key Name of the input to be checked in $_POST
	 *
	 * @return boolean  True, if the parameter is a floating-point number and a valid longitude
	 */
	public static function validateLongitude($key) {
		return (isset($_POST[$key]) && is_numeric($_POST[$key]) && $_POST[$key] >= -180 && $_POST[$key] <= 180);
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
		mt_srand((double)microtime() * 1000000);
		$string = null;
		while (strlen($string) < $length) {
			$string .= substr($chars, mt_rand(0, strlen($chars) - 1), 1);
		}
		return $string;
	}

}

// EOF general.class.php