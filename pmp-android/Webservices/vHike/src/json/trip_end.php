<?php
/**
 * This service is used to end an activated trip.
 *
 * GET  Parameters:
 * - sid Session ID
 *
 * POST Parameters:<br/>
 * - <b>id</b> <i>(optional)</i> The trip ID. If trip ID is not provided, this script will end all open trips of the logged in user (which there should not be more than one).<br/>
 *
 * @todo End a planned trip
 *
 * @return string A JSON response
 * - <b>error</b> Error string if available
 * - <b>msg</b> Error message if available
 * - <b>status</b> If the action was successful
 * <pre>
 * 'invalid_id'
 * 'nothing_to_update' No open trip found for the provided user and trip ID
 * 'trip_ended' Trip ended successfully
 * </pre>
 *
 * @author Dang Huynh
 * @version 2.0
 * @package vhike.services
 */
/**
 * @ignore
 */
define('INCLUDE', TRUE);
/**
 * @ignore
 */
require('./../inc/json_framework.inc.php');

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
	$user = Session::getInstance()->getLoggedInUser();

	if (isset($_POST['id'])) {
		if (!General::validateId('id')) {
			throw new InvalidArgumentException('Trip ID');
		}
		$status = Trip::endTrip($user->getId(), $_POST['id']);
	} else {
		$status = Trip::endTrip($user->getId());
	}

	echo Json::arrayToJson($output = array("status"     => $status));
} catch (InvalidArgumentException $iae) {
	Json::printInvalidInputError();
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF