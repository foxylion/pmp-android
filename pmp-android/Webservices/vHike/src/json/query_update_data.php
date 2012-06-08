<?php
/**
 * This service is used to to update the information about a trip
 */
define("INCLUDE", TRUE);
require ("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
	// Get trip for the given id
	$query = Query::loadQuery($_POST["id"]);

	// Make sure that this trip exists and belongs to the logged in user
	if ($query == NULL) {
		$status = "no_query";
	} elseif (!$query->getPassenger()->isEqual(Session::getInstance()->getLoggedInUser())) {
		$status = "invalid_user";
	} elseif ($query->updateWantedSeats($_POST["wanted_seats"], $_POST["id"])) {
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
