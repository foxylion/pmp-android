<?php
	/**
	 * This service enables a profiles anonymity
	 */
	define("INCLUDE", true);
	require ("./../inc/json_framework.inc.php");
	
	// Stop execution of script and print error message if user is not logged in
	Json::printErrorIfNotLoggedIn();
	
	try {
		$user = Session::getInstance() -> getLoggedInUser();
		$user->enableAnonymity();
		$output = array("successful" => true, "status" => "updated");
		echo Json::arrayToJson($output);
		
	} catch (DatabaseException1 $de) {
		Json::printDatabaseError($de);
	}
	Database::getInstance() -> disconnect();
	
	// EOF