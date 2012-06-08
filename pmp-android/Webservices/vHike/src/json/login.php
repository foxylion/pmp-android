<?php

/**
 * This service is used to log on a registered user.
 */

define('INCLUDE', TRUE);
require('./../inc/json_framework.inc.php');

try {
	$session = Session::getInstance();

	$status;

	switch ($session->logIn($_POST['username'], $_POST['password'])) {
		case Session::USER_INACTIVATED:
			$status = 'inactivated';
			break;

		case Session::USER_INVALID;
			$status = 'invalid';
			break;

		case Session::USER_LOGGED_IN:
			$status = 'logged_in';
			break;
	}

	$output = array('successful' => TRUE,
	                'status'     => $status,
	                'sid'        => $session->getSid());
	echo Json::arrayToJson($output);
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
} catch (Exception $e) {
	echo $e -> getTraceAsString();
}
Database::getInstance()->disconnect();

// EOF