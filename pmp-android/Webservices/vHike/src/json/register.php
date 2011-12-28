<?php
/**
 * This service is used to register a new user to the system
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

try {
    $user = User::register($_POST["username"], $_POST["password"], $_POST["email"], 
            $_POST["firstname"], $_POST["lastname"], $_POST["tel"], 
            $_POST["description"], $_POST["email_public"], $_POST["firstname_public"], 
            $_POST["lastname_public"], $_POST["tel_public"]);

    $user->sendVerificationKey();
    echo Json::arrayToJson(array("successful" => true, "status" => "registered"));
} catch (InvalidArgumentException $iae) {
    $code = $iae->getCode();

// Build error status from error code
    $status = "";
    if ($code & User::USERNAME_EXISTS) {
        $status .= "username_exists,";
    }
    if ($code & User::EMAIL_EXISTS) {
        $status .= "email_exists,";
    }
    if ($code & User::INVALID_USERNAME) {
        $status .= "invalid_username,";
    }
    if ($code & User::INVALID_EMAIL) {
        $status .= "invalid_email,";
    }
    if ($code & User::INVALID_FIRSTNAME) {
        $status .= "invalid_firstname,";
    }
    if ($code & User::INVALID_LASTNAME) {
        $status .= "invalid_lastname,";
    }
    if ($code & User::INVALID_TEL) {
        $status .= "invalid_tel,";
    }
    if ($code & User::INVALID_PASSWORD) {
        $status .= "invalid_password,";
    }

    // Remove last ","
    $status = substr($status, 0, strlen($status) - 1);

    echo Json::arrayToJson(array("successful" => true, "status" => $status));
} catch (DatabaseException $de) {
    Json::printDatabaseError($de);
}

Database::getInstance()->disconnect();
?>