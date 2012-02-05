<?php

/**
 * This service is used by a passenger to see which offers have been send to a
 * given query
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
	$offers = Offer::loadOffers(Session::getInstance()->getLoggedInUser());
	$offersOutput = null;
	if ($offers && count($offers) > 0) {
		$offersOutput = array();
		foreach ($offers as $offer) {
			$offersOutput[] = array("offer"       => $offer->getId(),
									"userid"      => $offer->getTrip()->getDriver()->getId(),
									"username"    => $offer->getTrip()->getDriver()->getUsername(),
									"rating"      => $offer->getTrip()->getDriver()->getRatingAvg(),
									"rating_num"  => $offer->getTrip()->getDriver()->getRatingNum(),
									"lat"         => $offer->getTrip()->getCurrentLat(),
									"lon"         => $offer->getTrip()->getCurrentLon(),
									"last_update" => $offer->getTrip()->getLastUpdate());
		}
	}
	$output = array("successful" => true,
					"offers"	 => $offersOutput);
	echo Json::arrayToJson($output);
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();
?>
