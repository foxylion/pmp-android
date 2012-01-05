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
    $rater = Session::getInstance()->getLoggedInUser();
    $recipient = User::loadUser($_POST["recipient"]);
    $trip = Trip::loadTrip($_POST["trip"]);
    $canRate = Rating::allowed($rater, $recipient, $trip);
    
    switch($canRate) {
        case Rating::CAN_RATE:
            Rating::rate($rater, $recipient, $trip, $_POST["rating"]);
            $status = "rated";
            break;
        
        case Rating::ALREADY_RATED:
            $status = "already_rated";
            break;
        
        case Rating::NOT_ENDED:
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
