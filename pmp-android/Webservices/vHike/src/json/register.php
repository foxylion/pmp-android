<?php
/**
 * This service is used to register a new user to the system
 */

define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

$user = new User();
$invalidString = null;

// Set and verify all input data
if (!$user->setUsername($_POST["username"])) {
    $invalidString .= "invalid_username,";
} else {
    // Check if username is in use
    if (User::usernameExists($_POST["username"])) {
        $invalidString .= "username_exists,";
    }
}

if (!$user->setPassword($_POST["password"])) {
    $invalidString .= "invalid_password,";
} else {
    // Check if email is in use
    if (User::emailExists($_POST["email"])) {
        $invalidString .= "email_exists,";
    }
}

if (!$user->setEmail($_POST["email"])) {
    $invalidString .= "invalid_email,";
}
if (!$user->setFirstname($_POST["firstname"])) {
    $invalidString .= "invalid_firstname,";
}
if (!$user->setLastname($_POST["lastname"])) {
    $invalidString .= "invalid_lastname,";
}
if (!$user->setTel($_POST["tel"])) {
    $invalidString .= "invalid_tel,";
}

$user->setDescription($_POST["description"]);

// Set boolean input data
$user->setEmailPublic($_POST["email_public"]);
$user->setFirstnamePublic($_POST["firstname_public"]);
$user->setLastnamePublic($_POST["lastname_public"]);
$user->setTelPublic($_POST["tel_public"]);



// If there where errors -> print error
if ($invalidString != null) {
    // Remove last ","
    $invalidString = substr($invalidString, 0, strlen($invalidString) - 1);
    $output = array("successful" => true, "status" => $invalidString);
    echo Json::arrayToJson($output);
    
} else {
    try {
        $user->register();
        $user->sendVerificationKey();
    } catch (DatabaseException $de) {
        Json::printError("INVALID_DATABASE-QUERY", $de->__toString());

    }
    $output = array("successful" => true, "status" => "registered");
    echo Json::arrayToJson($output);
}

Database::getInstance()->disconnect();
?>