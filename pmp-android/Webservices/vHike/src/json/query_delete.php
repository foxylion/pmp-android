<?php
/**
 * This service is used to delete ride request(s) of a logged in user
 */
define('INCLUDE', true);
require ('./../inc/json_framework.inc.php');

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

$user = Session::getInstance()->getLoggedInUser();

try {

	if (isset($_POST['all']) && strcmp($_POST['all'], 'delete_all') == 0) {
		$result = $user->deleteAllQueries();
	} elseif (!General::validateId('query_id')) {
		Json::printInvalidInputError();
	}
	$result = $user->deleteQuery($_POST['query_id']);

	echo Json::arrayToJson(array('successful'=> true, 'num_query_deleted' => $result));

} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF query_delete.php