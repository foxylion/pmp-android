<?php
/**
 * This service is used to rate a driver or a passenger that have participated
 * on an ended trip.
 */
define('INCLUDE', TRUE);
require ("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
	$user = Session::getInstance()->getLoggedInUser();
	$ret = Observation::isObserved($user->getID());
	$output = array('successful' => TRUE,
	                'status'     => $ret);
	echo Json::arrayToJson($output);

} catch (InvalidArgumentException $iae) {
	Json::printInvalidInputError();
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF
