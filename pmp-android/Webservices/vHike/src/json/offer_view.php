<?php

/**
 * This service is used by a passenger to see which offers have been send to a
 * given query
 */
define('INCLUDE', true);
require('./../inc/json_framework.inc.php');

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
	$offers = Offer::loadOffers(Session::getInstance()->getLoggedInUser());
	$offersOutput = null;
	if ($offers && count($offers) > 0) {
		$offersOutput = array();
		foreach ($offers as $offer) {
			$driver = $offer->getTrip()->getDriver();
			$pos = $driver->getCurrentPosition();
			$offersOutput[] = array('offer'       => $offer->getId(),
									'userid'      => $driver->getId(),
									'username'    => $driver->getUsername(),
									'rating'      => $driver->getRatingAvg(),
									'rating_num'  => $driver->getRatingNum(),
									'lat'         => $pos['latitude'],
									'lon'         => $pos['longitude'],
									'last_update' => $pos['last_update']);
		}
	}
	$output = array('successful' => true,
					'offers'	 => $offersOutput);
	echo Json::arrayToJson($output);
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF offer_view.php