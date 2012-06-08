<?php

/**
 * This service is used by an user to enable observation
 */
define('INCLUDE', TRUE);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
	$user = Session::getInstance()->getLoggedInUser();
	$hash = Observation::enableObservation($user->getId());
	$output = array("successful" => TRUE,
	                "obs_nr"     => $hash);
	echo Json::arrayToJson($output);
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
} catch (Exception $e) {
	Json::printError($e, $e->getMessage(), TRUE);
}
Database::getInstance()->disconnect();

// EOF