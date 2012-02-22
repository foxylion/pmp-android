<?php
/**
 * This service is used to rate a driver or a passenger that have participated
 * on an ended trip.
 */
define("INCLUDE", true);
require ("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
		$user = Session::getInstance() -> getLoggedInUser();
		$ret = Ride::is_picked($user->getID());
		$output = array("successful" => true, "picked" => $ret);
		echo Json::arrayToJson($output);

} catch (InvalidArgumentException $iae) {
	Json::printInvalidInputError();
} catch (DatabaseException1 $de) {
	Json::printDatabaseError($de);
}
Database::getInstance() -> disconnect();

// EOF