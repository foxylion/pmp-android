<?php
/**
 * Retrieves the position of an user
 *
 * GET  Parameters:
 * - sid Session ID
 *
 * POST Parameters:<br/>
 * - user_id (optional)
 *
 * @return string A JSON response
 * - <b>error</b> Error string if available
 * - <b>msg</b> Error message if available
 * - <b>successful</b> boolean
 * <pre>
 * "position":{
 *     "latitude":"0.00000000",
 *     "longitude":"0.00000000",
 *     "last_update":"2012-03-11 02:35:59"
 * }
 * </pre>
 *
 * @todo Check relationship between users
 * @author  Dang Huynh
 * @version 1.2
 * @package vhike.services
 */
/**
 * @ignore
 */
define('INCLUDE', true);
require ('./../inc/json_framework.inc.php');

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
	$user = Session::getInstance()->getLoggedInUser();
	$user_id = null;
	if (isset ($_POST['user_id'])) {
		if (General::validateId('user_id')) {
			$user_id = $_POST['user_id'];
		} else {
			throw new InputException('user_id');
		}
	}
		// TODO: Check relationship

		$user_id = $_POST['user_id'];

		$ret = $user -> get_pos($user_id);
		if ($ret) {
			$output = array('successful' => true,
							'position'   => $ret);
		} else {
			$output = array('successful' => false,
							'error'      => 'no_user_found',
							'msg'        => 'No users were found');
		}
		echo Json::arrayToJson($output);
} catch (InputException $ie) {
	Json::printInvalidInputError($ie -> getMessage());
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF