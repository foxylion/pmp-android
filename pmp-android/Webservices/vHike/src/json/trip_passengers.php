<?php
/**
 *
 */
define("INCLUDE", TRUE);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

$user = Session::getInstance()->getLoggedInUser();

try {

	if ($tripid = $user->getCurrentTripId()) {
		$passengers = Trip::getPassengersFromTrip($tripid);
		$output = array("successful" => TRUE,
		                "status"     => "result",
		                "passengers" => $passengers);
	} elseif ($tripInfo = $user->getCurrentRideTripInfo()) {
		echo $tripInfo;
		$passengers = Trip::getPassengersFromRide($tripInfo["tripid"], $user->getId());
		$output = array("successful" => TRUE,
		                "status"     => "result",
		                "driver"     => array("id"         => $tripInfo["driver"],
		                                      "username"   => $tripInfo["driver"],
		                                      "rating_avg" => $tripInfo["rating_avg"],
		                                      "rating_num" => $tripInfo["rating_num"]),
		                "passengers" => $passengers);
	} else {
		$output = array("successful" => TRUE,
		                "status"     => "no_trip_or_ride");
	}

	echo Json::arrayToJson($output);

} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF