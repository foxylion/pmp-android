<?php
/**
 * This service is returns all rides that have been done by a driver
 * or a passenger
 */
define("INCLUDE", true);
require ("./../inc/json_framework.inc.php");

/**
 * Utility function for creating the json output-frame
 * @param Trip $trip
 * @param String $addName
 * @param String[] $addArray
 * @return String[] 
 */
function createJsonRide($trip, $addName, $addArray) {

    return array("trip" => $trip->getId(),
        "avail_seats" => $trip->getAvailSeats(),
        "destination" => $trip->getDestination(),
        "creation" => $trip->getCreation(),
        "ending" => $trip->getEnding(),
        $addName => $addArray);
}

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();


try {

    if (isset($_POST["role"]) && $_POST["role"] == "driver") {
        // Load data for role "driver"
        // ---------------------------
        echo "driver:\n";
        
        $rides = Ride::getRidesAsDriver(Session::getInstance()->getLoggedInUser());
        $jsonRides = array();
        
        // Create Json-structure based on loaded array-data
        foreach ($rides as $key => $ride) {
            $jsonPassengers = array();
            foreach ($ride->getPassengers() as $passenger) {
                $user = $passenger->getUser();
                $jsonPassengers[] = array("userid" => $user->getId(),
                                          "username" => $user->getUsername(),
                                          "rating" => $user->getRatingAvg(),
                                          "rating_num" => $user->getRatingNum(),
                                          "rated" => $passenger->isRated());
            }
            $jsonRides[] = createJsonRide($ride->getTrip(), "passengers", $jsonPassengers);
        }
        
    } elseif (isset($_POST["role"]) && $_POST["role"] == "passenger") {
        // Load data for role "passenger"
        // ------------------------------
        echo "passenger:\n";
        
        $rides = Ride::getRidesAsPassenger(Session::getInstance()->getLoggedInUser());
        $jsonRides = array();
        
        foreach ($rides as $ride) {
            $driver = $ride->getDriver();
            $jsonDriver = array("userid" => $driver->getId(),
                                "username" => $driver->getUsername(),
                                "rating" => $driver->getRatingAvg(),
                                "rating_num" => $driver->getRatingNum(),
                                "rated" => $ride->isDriverRated());
            
            $jsonRides[] = createJsonRide($ride->getTrip(), "driver", $jsonDriver);
        }
        
    } else {
        // Throw an exception if POST is invalid. This will cancel the normal output
        throw new InvalidArgumentException("POST parameter missing or invalid.");
    }
        
    $output = array("successful" => true, "rides" => $jsonRides);
    echo Json::arrayToJson($output);
    
} catch (InvalidArgumentException $iae) {
    Json::printInvalidInputError();
} catch (DatabaseException $de) {
    Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();
?>
