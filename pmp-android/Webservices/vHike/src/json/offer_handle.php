<?php
/**
 * This service is used by a inquirer to accept or deny an offer
 */
define('INCLUDE', true);
require('./../inc/json_framework.inc.php');

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

// Verify user input
if (!isset ($_POST['accept']) OR !is_bool((boolean)$_POST['accept'])) {
	Json::printInvalidInputError();
}

try {
	// Check first if the given query-id belongs to the logged in user
	$offer = Offer::loadOffer($_POST['offer']);
	if ($offer == null) {
		$status = 'invalid_offer';
	} elseif ($offer->getQuery() == null
		|| !$offer->getQuery()->getPassenger()->isEqual(Session::getInstance()->getLoggedInUser())
	) {
		$status = 'invalid_user';
	} else {
		// Accept or deny the offer
		$status = 'handled';
		if ((boolean)$_POST['accept'] == TRUE) {
			$offer->accept();
		} else {
			$offer->deny();
		}
	}

	$output = array('successful' => true,
					'status'	 => $status);
	echo Json::arrayToJson($output);

} catch (InvalidArgumentException $iae) {
	Json::printInvalidInputError();
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();