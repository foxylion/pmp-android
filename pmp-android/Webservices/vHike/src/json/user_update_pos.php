<?php
/**
 * This service is used to update the current position of a user
 *
 * @author  Dang Huynh, Patrick Strobel
 * @version 1.0.1
 */
define("INCLUDE", TRUE);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

$user = Session::getInstance()->getLoggedInUser();

try {

	if (!$user->getCurrentTripId() && !$user->getCurrentQueryIds()) {
		echo Json::arrayToJson(array("successful" => TRUE,
		                             "status"     => "no_trip_or_query"));
	} else {
		if (!General::validateLatitude("lat") || !General::validateLongitude("lon")) {
			Json::printInvalidInputError();
		}
		$user->updatePosition($_POST["lat"], $_POST["lon"]);
		echo Json::arrayToJson(array("successful" => TRUE,
		                             "status"     => "updated"));
	}

} catch (InvalidArgumentException $iae) {
	Json::printInvalidInputError();
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF