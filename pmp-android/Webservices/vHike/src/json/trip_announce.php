<?php
/**
 * This service is used to to announce a new trip
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

$trip = new Trip();
$user = Session::getInstance()->getLoggedInUser();

try {
    // Set user input. Cancel if there's a invalid value in a input string
    if (!$trip->setDestination($_POST["destination"]) || !$trip->setCurrentLat($_POST["current_lat"]) ||
            !$trip->setCurrentLon($_POST["current_lon"]) || !$trip->setAvailSeats($_POST["avail_seats"])) {
        $output = array("successful" => true, 
                        "status" => "invalid_input");
    } if (Trip::openTripExists($user->getId())) {
        $output = array("successful" => true, 
                        "status" => "open_trip_exists");        
    } else {
        $trip->setDriver($user);
        $id = $trip->create();
        $output = array("successful" => true, 
                        "status" => "announced",
                        "id" => $id);
    }
    echo Json::arrayToJson($output);
} catch (DatabaseException $de) {
    Json::printDatabaseError($de);
}
?>
