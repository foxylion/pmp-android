<?php
/**
 * This service is used to show the profile of the logged in user
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error messsge if user is not logged in
Json::printErrorIfNotLoggedIn();

Database::getInstance()->disconnect();
?>
