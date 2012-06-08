<?php
/**
 * This service is returns all rides that have been done by a driver
 * or a passenger
 */
define('INCLUDE', TRUE);
require ('./../inc/json_framework.inc.php');

/**
 * Utility function for creating the json output-frame
 *
 * @param Trip     $trip
 *
 * @return String[]
 */
function createJsonRide($trip)
{
	return array('trip'          => $trip->getId(),
	             'avail_seats'   => $trip->getAvailSeats(),
	             'destination'   => $trip->getDestination(),
	             'creation'      => $trip->getCreation(),
	             'ending'        => $trip->getEnding());
}


// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {

	if (isset($_POST['role']) && $_POST['role'] == 'driver') {
		// Load data for role 'driver'
		// ---------------------------
		//echo 'driver:\n';

		$driver = Session::getInstance()->getLoggedInUser();
		$rides = Ride::getRidesAsDriver($driver);
		$jsonRides = array();

		// Create Json-structure based on loaded array-data
		foreach ($rides as $key => $ride) {
			$jsonPassengers = array();
			foreach ($ride->getPassengers() as $user) {
				$jsonPassengers[] = array('userid'    => $user->getId(),
				                          'username'  => $user->getUsername(),
				                          'rating'    => $user->getRatingAvg(),
				                          'rating_num'=> $user->getRatingNum(),
				                          'rated'     => Rating::hasRated($driver, $user, $ride->getTrip()));
			}

			$jsonRide = createJsonRide($ride->getTrip());
			$jsonRide['passengers'] = $jsonPassengers;
			$jsonRides[] = $jsonRide;
		}

	} elseif (isset($_POST['role']) && $_POST['role'] == 'passenger') {
		// Load data for role 'passenger'
		// ------------------------------
		//echo 'passenger:\n';

		$passenger = Session::getInstance()->getLoggedInUser();
		$rides = Ride::getRidesAsPassenger($passenger);
		$jsonRides = array();

		foreach ($rides as $ride) {
			$driver = $ride->getDriver();
			$jsonDriver = array('userid'    => $driver->getId(),
			                    'username'  => $driver->getUsername(),
			                    'rating'    => $driver->getRatingAvg(),
			                    'rating_num'=> $driver->getRatingNum(),
			                    'rated'     => Rating::hasRated($passenger, $driver, $ride->getTrip()));

			$jsonPassengers = array();
			foreach ($ride->getPassengers() as $user) {
				if ($user->getId() != $passenger->getId()) {
					$jsonPassengers[] = array('userid'    => $user->getId(),
					                          'username'  => $user->getUsername(),
					                          'rating'    => $user->getRatingAvg(),
					                          'rating_num'=> $user->getRatingNum(),
					                          'rated'     => Rating::hasRated($passenger, $user, $ride->getTrip()));
				}
			}

			$jsonRide = createJsonRide($ride->getTrip());
			$jsonRide['driver'] = $jsonDriver;
			$jsonRide['passengers'] = $jsonPassengers;
			$jsonRides[] = $jsonRide;
		}

	} else {
		// Throw an exception if POST is invalid. This will cancel the normal output
		throw new InvalidArgumentException('POST parameter missing or invalid.');
	}

	$output = array('successful'=> TRUE,
	                'rides'     => $jsonRides);
	echo Json::arrayToJson($output);

} catch (InvalidArgumentException $iae) {
	Json::printInvalidInputError();
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF ride_history.php