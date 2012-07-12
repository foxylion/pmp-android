<?php
/**
 * This service is used to announce a new ride request
 */
define('INCLUDE', TRUE);
require ('./../inc/json_framework.inc.php');

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

$query = new Query();
$user = Session::getInstance()->getLoggedInUser();

try {
    // Set user input. Cancel if there's a invalid value in a input string
    if (!$query->setDestination($_POST['destination']) || !$query->setWantedSeats($_POST['seats'])) {
        Json::printError('invalid_input', 'At least one POST-Parameter is invalid');
    }

    // Set the current position if available
    if (General::validateLatitude('current_lat') && General::validateLongitude('current_lon')) {
        $query->setCurrentLat($_POST['current_lat']);
        $query->setCurrentLon($_POST['current_lon']);
        $user->updatePosition($_POST['current_lat'], $_POST['current_lon']);
    }

    $date = General::validateId('date') ? $date = $_POST['date'] : '';
    $departure = isset($_POST['departure']) ? $_POST['departure'] : '';

//	if ($user->isQueryExisted($query->getDestination()) OR $user->isRideExisted($query->getDestination())) {
//		$output = array('successful'=> TRUE,
//		                'status'    => 'query_or_ride_existed');
//	} else {
    $query->setPassenger($user->getId());
    $id = $query->create($date, $departure);
    $output = array('successful' => TRUE,
        'status' => 'announced',
        'id' => $id);
//	}

    echo Json::arrayToJson($output);

} catch (DatabaseException $de) {
    Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF query_start.php