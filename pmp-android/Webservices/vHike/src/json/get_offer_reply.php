<?php
/**
 * This service is used to retrieve a information from a offer
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

$user = Session::getInstance() -> getLoggedInUser();

try {
	
	$res = Offer::getOfferReply($_POST["tripid"], $_POST["uid"]);
	
	if ($res) {
		$output = array('successful'	=> true,
						'status'		=> 'result',
						'offers'		=> $res);
		echo Json::arrayToJson($output);
	} else {
		$output = array('successful'	=> true,
						'status'		=> 'no_offer_found');
		echo Json::arrayToJson($output);
	}
	
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
} catch (Exception $e) {
	echo($e->getMessage());
}
Database::getInstance()->disconnect();

// EOF get_offer_reply.php