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
	$offer = Offer::getOfferReply($_GET["tripid"], $_GET["uid"]);
	
	if ($offer != null) {
		$output = array("successful" => true,
						"status"	 => "successful",
						"id"		 => $offer->id,
						"trip"		 => $offer->trip,
						"query" 	 => $offer->query,
						"status" 	 => $offer->status,
						"sender" 	 => $offer->sender,
						"recipient"  => $offer->recipient,
						"message" 	 => $offer->message);

	} else {
		$output = array("successful" => true,
						"status"	 => "no_offer_found");
	}

	echo Json::arrayToJson($output);
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF get_offer_reply.php