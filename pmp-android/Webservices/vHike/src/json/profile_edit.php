<?php
/**
 * This service is used to edit a users profile
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
    $user = Session::getInstance()->getLoggedInUser();
    $user->updateProfile($_POST["firstname"], $_POST["lastname"], $_POST["tel"], $_POST["description"]);
    echo Json::arrayToJson(array("successful" => true, "status" => "updated"));
} catch (InvalidArgumentException $iae) {
    $code = $iae->getCode();

    // Build error status from error code
    $status = "";
    if ($code & User::INVALID_FIRSTNAME) {
        $status .= "invalid_firstname,";
    }
    if ($code & User::INVALID_LASTNAME) {
        $status .= "invalid_lastname,";
    }
    if ($code & User::INVALID_TEL) {
        $status .= "invalid_tel,";
    }

    // Remove last ","
    $status = substr($status, 0, strlen($status) - 1);

    echo Json::arrayToJson(array("successful" => true, "status" => $status));
} catch (DatabaseException $de) {
    Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();
?>
