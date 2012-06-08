<?php
/**
 * This service provides an overview over one trip
 *
 * GET  Parameters:
 * - sid Session ID
 * POST Parameters:
 * - tripId Trip's ID
 *
 * @return string A JSON response.
 *
 * - <b>error</b> Error string if available
 * - <b>msg</b> Error message if available
 *
 *
 * @author  Dang Huynh
 * @version 1.0
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

	if (!General::validateId('tripId')) {
		throw new InputException('tripId');
	}

	$output = Trip::get_trip_overview($user->getId(), $_POST['tripId']);

	echo Json::arrayToJson($output);
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
} catch (InputException $ie) {
	Json::printInvalidInputError($ie->getMessage());
}
Database::getInstance()->disconnect();

// EOF