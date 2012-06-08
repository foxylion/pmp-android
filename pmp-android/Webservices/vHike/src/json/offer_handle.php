<?php
/**
 * This service is used by a inquirer to accept or deny an offer
 */
define('INCLUDE', TRUE);
require('./../inc/json_framework.inc.php');

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

// Verify user input
if (!isset ($_POST['accept']) OR !General::isBoolean($_POST['accept'])) {
	Json::printInvalidInputError();
}

try {
	// Check first if the given query-id belongs to the logged in user
	$offer = Offer::loadOffer($_POST['offer']);
	if ($offer == NULL) {
		$status = 'invalid_offer';
	} elseif ($offer->getQuery() == NULL
		|| !$offer->getQuery()->getPassenger()->isEqual(Session::getInstance()->getLoggedInUser())
	) {
		$status = 'invalid_user';
	} else {
		// Accept or deny the offer
		if ($_POST['accept'] == 1 OR strcasecmp($_POST['accept'], 'true') == 0 OR strcasecmp($_POST['accept'], '1') == 0) {
			$offer->accept();
			$status = 'accepted';
		} else {
			if ($offer->deny() == 0) {
				$status = 'cannot_update';
			} else {
				$status = 'denied';
			}
		}
	}

	$output = array('successful' => TRUE,
	                'status'     => $status);
	echo Json::arrayToJson($output);

} catch (InvalidArgumentException $iae) {
	Json::printInvalidInputError();
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF offer_handle.php