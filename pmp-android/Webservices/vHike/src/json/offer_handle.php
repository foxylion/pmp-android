<?php
/**
 * This service is used by a inquerier to accept or deny an offer
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

// Verify user input
/*if (!General::validId($_POST["offer"]) && !isset ($_POST["accept"]) && !is_bool($_POST["accept"])) {
    Json::printInvalidInputError();
}*/

try {
    // Check first if the given query-id belongs to the logged in user
    $offer = Offer::loadOffer($_POST["offer"]);
    if ($offer == null) {
        $status = "invalid_offer";
    } elseif ($offer->getQuery() == null 
            || !$offer->getQuery()->getPassenger()->isEqual(Session::getInstance()->getLoggedInUser())) {
        $status = "invalid_user";
    } else {
        // Accept or deny the offer
        if($_POST["accept"] == "true") {
            $offer->accept();
        } elseif($_POST["accept"] == "false") {
            $offer->deny();
        }
        $status = "handled";
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
