<?php
/**
 * This service enables a profiles anonymity
 */
define("INCLUDE", TRUE);
require ("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
	$user = Session::getInstance()->getLoggedInUser();
	$user->disableAnonymity();
	$output = array("successful" => TRUE,
	                "status"     => "updated");
	echo Json::arrayToJson($output);

} catch (DatabaseException1 $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF