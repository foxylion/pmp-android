<?php
/**
 * This service is used to rate a driver or a passenger that have participated
 * on an ended trip.
 */
define('INCLUDE', TRUE);
require ('./../inc/json_framework.inc.php');

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {

	$trip_id = $_POST['trip_id'];
	$ret = Ride::offer_accepted($trip_id);
	if ($ret) {
		$output = array('successful' => TRUE,
		                'passengers' => $ret);
	}
	echo Json::arrayToJson($output);

} catch (InvalidArgumentException $iae) {
	Json::printInvalidInputError();
} catch (DatabaseException1 $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF