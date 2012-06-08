<?php
/**
 * This service is used to delete ride request(s) of a logged in user
 */
define('INCLUDE', TRUE);
require ('./../inc/json_framework.inc.php');

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

$user = Session::getInstance()->getLoggedInUser();

try {

	if (isset($_POST['all']) && strcasecmp($_POST['all'], 'delete_all') == 0) {
		$result = $user->deleteAllQueries();
	} elseif (!General::validateId('query_id')) {
		echo $_POST['query_id'];
		Json::printInvalidInputError('query_id');
	} else {
		$result = $user->deleteQuery($_POST['query_id']);
	}


	echo Json::arrayToJson(array('successful'        => TRUE,
	                             'num_query_deleted' => $result));

} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF query_delete.php