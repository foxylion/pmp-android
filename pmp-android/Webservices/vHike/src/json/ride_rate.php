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
    // First, make sure that the user is allowed to rate
    $recipient = User::loadUser($_POST["recipient"]);
    $trip = Trip::loadTrip($_POST["trip"]);
    $canRate = Ride::canRate(Session::getInstance()->getLoggedInUser(), $recipient, $trip);
    
    switch($canRate) {
        case Ride::CAN_RATE:
            $recipient->rate($_POST["rating"]);
            Ride::markAsRated(Session::getInstance()->getLoggedInUser(), $recipient, $trip);
            $status = "rated";
            break;
        
        case Ride::ALREADY_RATED:
            $status = "already_rated";
            break;
        
        case Ride::NOT_ENDED:
            $status = "not_ended";
            break;
        
        default:
            $status = "no_connection";
    }
    
    $output = array("successful" => true, "status" => $status);
    echo Json::arrayToJson($output);
    
} catch (InvalidArgumentException $iae) {
    Json::printInvalidInputError();
} catch (DatabaseException1 $de) {
    Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();
?>
