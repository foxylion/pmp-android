<?php
/**
 * This service is used to retrieve a information from a offer
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
	$user = Session::getInstance() -> getLoggedInUser();
	$offers = Offer::getOfferReply($_POST["tripid"], $_POST["uid"]);
	$offersOutput = null;
	if ($offers && count($offers) > 0) {
		$offersOutput = array();
		foreach ($offers as $offer) {
			$offersOutput[] = array ('offer'		=> $offer->getId(),
									 'trip'   		=> $offer->trip,
									 'query'  		=> $offer->query,
									 'status'		=> $offer->status,
									 'sender' 		=> $offer->sender,
									 'recipient'	=> $offer->recipient,
									 'message' 		=> $offer->message);
		}

	} else {
		$output = array("successful" => true,
						"offers"	 => $offersOutput);
	}
	echo Json::arrayToJson($output);
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
} catch (Exception $e) {
	echo($e->getTraceAsString());
}
Database::getInstance()->disconnect();

// EOF get_offer_reply.php