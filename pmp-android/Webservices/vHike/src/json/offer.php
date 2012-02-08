<?php

/**
 * This service is used by a driver to send an offer for a given query
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
	// Load data for the given parameters^^
	$trip = Trip::loadTrip($_POST["trip"]);
	$query = Query::loadQuery($_POST["query"]);
	$driver = Session::getInstance()->getLoggedInUser();


	// Show message if there's no data for the given values
	if ($trip == null) {
		$status = "invalid_trip";
	} elseif ($query == null) {
		$status = "invalid_query";
	} else {
		Offer::make($query, $trip, $driver, $_POST['message']);
		$status = "sent";
	}


	$output = array("successful" => true,
					"status"     => $status);
	echo Json::arrayToJson($output);

} catch (OfferException $oe) {
	// Show message if the given parameter don't match
	switch ($oe->getCode()) {
		case OfferException::INVALID_TRIP:
			$status = "invalid_trip";
			break;
		case OfferException::EXISTS_ALREADY:
			$status = "already_sent";
			break;
	}
	$output = array("successful" => true,
					"status"     => $status);
	echo Json::arrayToJson($output);
} catch (InvalidArgumentException $iae) {
	Json::printInvalidInputError();
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();
?>
