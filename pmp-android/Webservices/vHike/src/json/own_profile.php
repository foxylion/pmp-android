<?php
/**
 * This service is used to show the profile of the logged in user
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
	$user = Session::getInstance()->getLoggedInUser();

	$output = array("successful"       => true,
					"id"               => $user->getId(),
					"username"         => $user->getUsername(),
					"email"            => $user->getEmail(),
					"firstname"        => $user->getFirstname(),
					"lastname"         => $user->getLastname(),
					"tel"              => $user->getTel(),
					"description"      => $user->getDescription(),
					"regdate"          => $user->getRegdate(),
					"email_public"     => $user->isEmailPublic(),
					"firstname_public" => $user->isFirstnamePublic(),
					"lastname_public"  => $user->isLastnamePublic(),
					"tel_public"       => $user->isTelPublic(),
					"rating_avg"       => $user->getRatingAvg(),
					"rating_num"       => $user->getRatingNum());

	echo Json::arrayToJson($output);
} catch (DatabaseException $de) {
	Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();
?>
