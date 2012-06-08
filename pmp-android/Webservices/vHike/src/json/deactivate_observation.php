<?php

/**
 * This service is used by an user to disable observation
 */
define('INCLUDE', TRUE);
require('./../inc/json_framework.inc.php');

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
	$user = Session::getInstance()->getLoggedInUser();
	Observation::disableObservation($user->getId());

	$output = array("successful" => TRUE,
	                "status"     => "deactivated");

	echo Json::arrayToJson($output);

} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
} catch (Exception $e) {
	Json::printError('Error', $e->getMessage(), TRUE);
}
Database::getInstance()->disconnect();
