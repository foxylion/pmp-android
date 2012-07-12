<?php
/**
 * This service is used to to update the information about a trip
 */
define('INCLUDE', TRUE);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
    if (!General::validateId('trip_id'))
        throw new InputException('trip_id');

    $trip_id = $_POST['trip_id'];
	$user_id = Session::getInstance()->getLoggedInUser()->getId();

    $output = Trip::cancel_trip($trip_id, $user_id);
    echo Json::arrayToJson($output = array('delete'=>$output));

} catch (InvalidArgumentException $iae) {
	Json::printInvalidInputError();
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();
// EOF