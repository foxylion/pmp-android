<?php

/**
 * This service is used by a passenger to see which offers have been send to a
 * given query
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
    $offers = Offer::loadOffers(Session::getInstance()->getLoggedInUser());
    $offersOutput = array();
    foreach ($offers as $offer) {
        $offersOutput[] = array("offer" => $offer->getId(), 
                                "userid" => $offer->getDriver()->getId(),
                                "username" => $offer->getDriver()->getUsername(),
                                "rating" => $offer->getDriver()->getRatingAvg(),
                                "rating_num" => $offer->getDriver()->getRatingNum(),
                                "lat" => $offer->getCurrentLat(),
                                "lon" => $offer->getCurrentLon());
    }
    $output = array("successful" => true, "offers" => $offersOutput);
    echo Json::arrayToJson($output);
} catch (DatabaseException $de) {
    Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();
?>
