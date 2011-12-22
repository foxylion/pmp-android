<?php
/**
 * This service is used to close a trip
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

// Store information of the trip we want to update
$trip = new Trip();
if (!$trip->setDriver(Session::getInstance()->getLoggedInUser()->getId())) {
    Json::printError("invalid_input", "At least one POST-Parameter is invalid");
}

try {
    if ($trip->updateEndTrip($_POST["id"])) {
        // Update successfully
        $status = "updated";
        
    } else {
        // Update failed, check why
        $loadedTrip = Trip::loadTrip($_POST["id"]);
        
        if ($loadedTrip == null) {
            $status = "no_trip";
        } elseif ($loadedTrip->getDriver() != Session::getInstance()->getLoggedInUser()->getId()) {
            $status = "invalid_user";
        } elseif ($loadedTrip->hasEnded()) {
            $status = "has_ended";
        } else {
            $status = "already_uptodate";
        }
        
    }
    
    $output = array("successful" => true, 
                      "status" => $status);
    echo Json::arrayToJson($output);
} catch (DatabaseException $de) {
    Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();
?>
