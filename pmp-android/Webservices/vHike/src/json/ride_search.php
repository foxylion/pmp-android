<?php
/**
 * This service is used by the hiker to search for available rides
 */
define("INCLUDE", true);
require ("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

$query = new Query();
$user = Session::getInstance() -> getLoggedInUser();

try {
	// Set user input. Cancel if there's a invalid value in a input string
	if (!$query -> setCurrentLat($_POST["lat"]) || !$query -> setCurrentLon($_POST["lon"]) ||
        !is_numeric($_POST["distance"]) || $_POST["distance"]<0 || $_POST["distance"]>10000) {
		Json::printError("invalid_input", "At least one POST-Parameter is invalid");
	}

    $queryIds = $user->getCurrentQueryId();

    if($queryIds != NULL) {
        $result = $ride->searchRide($user->getId(), $_POST["distance"]);
//        if ($result) {
//            $output = array("successful" => true, "status" => "result", "queries" => $result);
//            echo Json::arrayToJson($output);
//        } else {
//            $output = array("successful" => true, "status" => "result", "queries" => NULL);
//            echo Json::arrayToJson($output);
//        }
        echo Json::arrayToJson($user->getCurrentQueryId());
    } else {
        $output = array("successful" => true, "status" => "no_query");
        echo Json::arrayToJson($output);
    }

} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
} catch (Exception $e) {
    Json::printError("Some Error", $e->getMessage());
}
Database::getInstance() -> disconnect();
?>