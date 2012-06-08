<?php
/**
 * This service is used to to update the drivers current position
 */
define("INCLUDE", TRUE);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
	// Get trip for the given id
	$trip = Trip::loadTrip($_POST["id"]);

	// Make sure that this trip exists and belongs to the logged in user
	if ($trip == NULL) {
		$status = "no_trip";
	} elseif (!$trip->getDriver()->isEqual(Session::getInstance()->getLoggedInUser())) {
		$status = "invalid_user";
	} elseif ($trip->hasEnded()) {
		$status = "has_ended";
	} elseif ($trip->updatePosition($_POST["current_lat"], $_POST["current_lon"])) {
		$status = "updated";
	} else {
		$status = "already_uptodate";
	}

	echo Json::arrayToJson($output = array("successful" => TRUE,
	                                       "status"     => $status));
} catch (InvalidArgumentException $iae) {
	Json::printInvalidInputError();
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF