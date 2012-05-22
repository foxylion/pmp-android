<?php
	/**
	 * This service loads all my preplanned trips
	 */
	define("INCLUDE", true);
	require ("./../inc/json_framework.inc.php");
	
	// Stop execution of script and print error message if user is not logged in
	Json::printErrorIfNotLoggedIn();
	
	try {
		$user = Session::getInstance() -> getLoggedInUser();
		$lift_ids = Trip::getLiftIds($_POST["uid"]);
		$liftOutput = array();
		
		foreach ($lift_ids as $lift_id) {
			$res = Trip::getLiftsById($_POST["uid"], $lift_id);
			$liftOutput[] = $res;
		}

		$output = array("successful"	=> true, 
						"my_trips" 		=> $liftOutput);
		echo Json::arrayToJson($output);
		
	} catch (DatabaseException1 $de) {
		Json::printDatabaseError($de);
	} catch (Exception $e) {
		echo($e->getMessage());
	}
	Database::getInstance() -> disconnect();
	
	// EOF