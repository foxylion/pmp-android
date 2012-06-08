<?php
/**
 * This service is used to get all the trips started by the logged in user.
 *
 * GET  Parameters:
 * - sid Session ID
 * POST Parameters:
 * - all (boolean)
 *
 * @return string A JSON response. If 'all' was set, this service will return all trips started by the user, otherwise
 *          only the trips which weren't started or ended will be returned.
 *
 * - <b>error</b> Error string if available
 * - <b>msg</b> Error message if available
 *
 * - status 'no_trip'
 * OR
 * - <b>trips</b> : array, include all these information
 * [{
 * - <b>trip_id</b>
 * - <b>avail_seats</b>
 * - <b>destination</b>
 * - <b>creation</b> (long) millisecond
 * - <b>started</b> boolean
 * - <b>ending</b> (long) millisecond
 *}, ...]
 *
 * @author  Dang Huynh
 * @version 1.1
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

	if (isset($_GET['all'])) {
		$trips = Trip::get_trips($user->getId(), TRUE);
	} elseif (isset($_GET['compact'])) {
		$trips = Trip::get_trips($user->getId());

		for ($i = 0; $i < count($trips); $i++) {
			$trip = $trips[$i];
			$info = Trip::get_trip_stats($trip['id']);
			$trips[$i] = array_merge($trip, $info);
		}
	} else {
		$trips = Trip::get_trips($user->getId());
	}

	// Result
	if ($trips == NULL) {
		$output = array('status'=> 'no_trip');
	} else {
		$output = array('trips'=> $trips);
	}
	echo Json::arrayToJson($output);
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF