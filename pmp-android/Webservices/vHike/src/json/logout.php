<?php
/**
 * This service is used to log out a logged in user
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

Session::getInstance()->logOut();
$output = array("successful" => true);
echo Json::arrayToJson($output);
Database::getInstance()->disconnect();

// EOF