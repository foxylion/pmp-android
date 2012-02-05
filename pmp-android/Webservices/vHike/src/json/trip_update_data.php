<?php
/**
 * This service is used to to update the information about a trip
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
	// Get trip for the given id
	$trip = Trip::loadTrip($_POST["id"]);

	// Make sure that this trip exists and belongs to the logged in user
	if ($trip == null) {
		$status = "no_trip";
	} elseif (!$trip->getDriver()->isEqual(Session::getInstance()->getLoggedInUser())) {
		$status = "invalid_user";
	} elseif ($trip->hasEnded()) {
		$status = "has_ended";
	} elseif ($trip->updateData($_POST["avail_seats"])) {
		$status = "updated";
	} else {
		$status = "already_uptodate";
	}

	echo Json::arrayToJson($output = array("successful" => true,
										   "status"     => $status));


} catch (InvalidArgumentException $iae) {
	Json::printInvalidInputError();
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();
?>
