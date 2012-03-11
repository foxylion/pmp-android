<?php
/**
 * This service is used to get the open trip by the logged in user if it's available.
 *
 * GET  Parameters:
 * - sid Session ID
 *
 * @return string A JSON response
 * - <b>error</b> Error string if available
 * - <b>msg</b> Error message if available
 *
 * If the user has an open trip, these information will be included:
 * - <b>trip_id</b>
 * - <b>avail_seats</b>
 * - <b>destination</b>
 * - <b>creation</b> long (Unix Timestamp)
 *
 * @author  Dang Huynh
 * @version 1.0
 * @package vhike.services
 */
/**
 * @ignore
 */
define('INCLUDE', true);
/**
 * @ignore
 */
require('./../inc/json_framework.inc.php');

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
	$user = Session::getInstance()->getLoggedInUser();
	$trip = Trip::get_open_trip($user->getId());
	if ($trip) {
		$output = array('id'         => $trip['id'],
						'avail_seats'=> $trip['avail_seats'],
						'destination'=> $trip['destination'],
						'creation'   => $trip['creation']);
	} else {
		$output = array('status'=>'no_trip');
	}
	echo Json::arrayToJson($output);
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF