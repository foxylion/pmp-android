<?php
/**
 * This service is used to announce a new trip
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
	// Set user input. Cancel if there's a invalid value in a input string
	$driver = Session::getInstance()->getLoggedInUser();
	$trip = Trip::create($driver, $_POST["avail_seats"], $_POST["current_lat"],
						 $_POST["current_lon"], $_POST["destination"]);

	$output = array("successful" => true,
					"status"     => "announced",
					"id"         => $trip->getId());
	echo Json::arrayToJson($output);

} catch (InvalidArgumentException $iae) {
	if ($iae->getCode() == Trip::OPEN_TRIP_EXISTS) {
		echo Json::arrayToJson(array("successful" => true,
									 "status"     => "open_trip_exists"));
	} else {
		Json::printInvalidInputError();
	}

} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();
?>
