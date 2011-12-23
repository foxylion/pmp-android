<?php

/**
 * This service is used by a driver to send an offer for a given query
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
    // Make sure that the trip belong to the currently logged in user
    $trip = Trip::loadTrip($_POST["trip"]);
    $query = Query::loadQuery($_POST["query"]);
    $driver = Session::getInstance()->getLoggedInUser();
     
    if ($trip == null) {
        $status = "invalid_trip";
    } elseif ($query == null) {
        $status = "invalid_query";
    } else {
        Offer::make($query, $trip, $driver, $_POST['message']);
        $status = "sent";
    }
    

    $output = array("successful" => true, "status" => $status);
    echo Json::arrayToJson($output);
    
} catch (OfferException $oe) {
    switch ($oe->getCode()) {
        case OfferException::INVALID_TRIP:
            $status = "invalid_trip";
            break;
        case OfferException::EXISTS_ALREADY:
            $status = "already_sent";
            break;
    }
    $output = array("successful" => true, "status" => $status);
    echo Json::arrayToJson($output);
} catch (InvalidArgumentException $iae) {
    Json::printInvalidInputError();
} catch (DatabaseException $de) {
    Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();
?>
