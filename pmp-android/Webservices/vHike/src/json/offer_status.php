<?php
/**
 * This service is used to rate a driver or a passenger that have participated
 * on an ended trip.
 */
define('INCLUDE', true);
require ('./../inc/json_framework.inc.php');

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();
$user = Session::getInstance()->getLoggedInUser();

try {
	if (!General::validateId('offer_id')) {
		Json::printInvalidInputError('offer_id');
	}

	$result = $user->get_offer_status($_POST['offer_id']);

	if ($result == -1) {
		$status = 'invalid_offer_id';
	} elseif ($result['sender'] == $user->getId()) {
		$s = (int)$result['status'];
		// Update status to 'reply read'
		if (($s & '10') == 2) {
			$user->set_offer_read($_POST['offer_id']);
		}
		if ($s == 0) { // 00000000
			$status = 'unread';
		} elseif (($s & '0100') == 4) { // 0000 0100
			$status = 'accepted';
		} elseif (($s & '1000') == 8) { // 0000 1000
			$status = 'denied';
		}
	} else {
		$s = (int)$result['status'];
		if (($s & 1) == 0) {
			$status = 'not_handled';
		} elseif (($s & '10') == 2) {
			$status = 'read';
		} else {
			$status = 'unread';
		}
	}

	echo Json::arrayToJson(array('successful' => true,
								 'status'     => $status,
								 'last_action'=> $result['time']));

} catch (InvalidArgumentException $iae) {
	Json::printInvalidInputError();
} catch (DatabaseException1 $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF offer_status.php