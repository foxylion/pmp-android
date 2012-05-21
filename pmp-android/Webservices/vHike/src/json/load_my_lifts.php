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
		$lifts = Trip::getMyLifts($_POST["uid"]);
		$lift_ids = Trip::getLiftIds($_POST["uid"]);
		$passenger_count = null;
		$invite_count = null;
		
		foreach ($lift_ids as $lift_id) {
			$passenger_count = Ride::getLiftPassengers($lift_id);
			$invite_count = Offer::getInvites($lift_id, $_POST["uid"]);
		}
		
		$res = null;
		
		$output = array("successful"	=> true, 
						"lifts" 		=> $lifts,
						"lidft_ids"		=> $lift_ids,
						"pas_count"		=> $passenger_count,
						"inv_count"		=> $invite_count);
		echo Json::arrayToJson($output);
		
	} catch (DatabaseException1 $de) {
		Json::printDatabaseError($de);
	} catch (Exception $e) {
		echo($e->getMessage());
	}
	Database::getInstance() -> disconnect();
	
	// EOF