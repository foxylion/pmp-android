<?php
/**
 * This service is used to log out a logged in user
 */
define('INCLUDE', TRUE);
require('./../inc/json_framework.inc.php');

Session::getInstance()->logOut();
$output = array("successful" => TRUE);
echo Json::arrayToJson($output);
Database::getInstance()->disconnect();

// EOF