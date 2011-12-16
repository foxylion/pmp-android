<?php
/**
 * This service is used by a driver to send an offer for a given query
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
    $offer = new Offer();
    
    // Cancel if input is invalid
    if(!$offer->setDriver(Session::getInstance()->getLoggedInUser()) ||
            !$offer->setQueryId($_POST["query"])) {
        Json::printError("invalid_input", "At least one POST-Parameter is invalid");
    }
    
    $offer->create();
    $output = array("successful" => true, "status" => "sent");
    echo Json::arrayToJson($output);
    
} catch(OfferEception $oe) {
    switch($oe->getCode()) {
        case OfferException::EXISTS_ALREADY:
            $status = "already_sent";
            break;
        case OfferException::QUERY_NOT_FOUND:
            $status = "invalid_query";
            break;
    }
    $output = array("successful" => true, "status" => $status);
    echo Json::arrayToJson($output);
    
} catch(DatabaseException $de) {
    
}
?>
