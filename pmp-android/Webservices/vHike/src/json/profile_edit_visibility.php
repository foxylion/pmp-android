<?php
/**
 * This service is used to edit the visibility of a users profile-fields
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
	$user = Session::getInstance()->getLoggedInUser();
	$user->updateVisibility($_POST["lastname_public"],$_POST["firstname_public"],$_POST["email_public"], $_POST["tel_public"]);
	echo Json::arrayToJson(array("successful" => true,
								 "status"     => "updated"));
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();

// EOF