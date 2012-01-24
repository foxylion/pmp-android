<?php
/**
 * This service is used to update the current position of a user
 *
 * @author Dang Huynh, Patrick Strobel
 * @version 1.0.1
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

$user = Session::getInstance() -> getLoggedInUser();

try {

    if (!General::validLatitude($_POST["lat"]) || !General::validLongitude($_POST["lon"])) {
        Json::printInvalidInputError();
    }
    
    $user->getPosition()->updatePosition($_POST["lat"], $_POST["lon"]);
    echo Json::arrayToJson(array("successful" => true, "status" => "updated"));
    
    /*if (!@General::validCoordinate($_POST["lat"]) || !@General::validCoordinate($_POST["lon"])) {
        Json::printError("invalid_input", "At least one POST-Parameter is invalid");
    }

    if (!$user->getCurrentTripId() && !$user->getCurrentQueryIds()) {
        echo Json::arrayToJson(array("successful" => true, "status" => "no_update"));
    } elseif ($user->updatePosition($_POST["lat"], $_POST["lon"])) {
        echo Json::arrayToJson(array("successful" => true, "status" => "updated"));
    } else {
        echo Json::arrayToJson(array("successful" => true, "status" => "update_fail"));
    }
*/

} catch (InvalidArgumentException $iae) {
    Json::printInvalidInputError();
} catch (DatabaseException $de) {
    Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();
?>