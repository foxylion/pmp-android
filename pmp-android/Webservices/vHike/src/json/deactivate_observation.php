<?php

/**
 * This service is used by an user to disable observation
 */
define('INCLUDE', true);
require('./../inc/json_framework.inc.php');

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
	$user = Session::getInstance()->getLoggedInUser();
	Observation::disableObservation($user);
	
	$output[] = array('successful' => true);
	
  echo Json::arrayToJson($output);
  
}catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

?>