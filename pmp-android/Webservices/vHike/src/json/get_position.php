<?php
/**
 * This service is used to rate a driver or a passenger that have participated
 * on an ended trip.
 */
define('INCLUDE', true);
require ('./../inc/json_framework.inc.php');

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
	if (General::validId($_POST['user_id'])) {

		// TODO: Check relationship

		$user_id = $_POST['user_id'];
		$ret = position::getPosition($user_id);
		if ($ret) {
			$output = array('successful' => true,
							'position'   => $ret);
		} else {
			$output = array('successful' => false,
							'error'      => 'no_user_found',
							'msg'        => 'No users were found');
		}
		echo Json::arrayToJson($output);
	} else {
		throw new InvalidArgumentException('user_id');		
	}
} catch (InvalidArgumentException $iae) {
	Json::printInvalidInputError();
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF