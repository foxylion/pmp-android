<?php
/**
 * This service is used to announce a new ride request
 */
define("INCLUDE", true);
require ("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

$query = new Query();
$user = Session::getInstance() -> getLoggedInUser();

try {
	// Set user input. Cancel if there's a invalid value in a input string
	if (!$query -> setDestination($_POST["destination"]) || !$query -> setCurrentLat($_POST["current_lat"]) || !$query -> setCurrentLon($_POST["current_lon"]) || !$query -> setWantedSeats($_POST["seats"])) {
		Json::printError("invalid_input", "At least one POST-Parameter is invalid");
	}

	$query -> setPassenger($user -> getId());
	$id = $query -> create();
	$output = array("successful" => true, "status" => "announced", "id" => $id);
	echo Json::arrayToJson($output);

} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance() -> disconnect();
?>