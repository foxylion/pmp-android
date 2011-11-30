<?php
/**
 * This service is used to register a new user to the system
 */

define("INCLUDE", true);
require("/../inc/json_framework.inc.php");

$data = new RegistrationData();
$invalidString = null;

// Set and verify all input data
if (!$data->setUsername($_POST["username"])) {
    $invalidString .= "invalid_username,";
} else {
    // Check if username is in use
    if (User::usernameExists($_POST["username"])) {
        $invalidString .= "username_exists,";
    }
}

if (!$data->setPassword($_POST["password"])) {
    $invalidString .= "invalid_password,";
} else {
    // Check if email is in use
    if (User::emailExists($_POST["email"])) {
        $invalidString .= "email_exists,";
    }
}

if (!$data->setEmail($_POST["email"])) {
    $invalidString .= "invalid_email,";
}
if (!$data->setFirstname($_POST["firstname"])) {
    $invalidString .= "invalid_firstname,";
}
if (!$data->setLastname($_POST["lastname"])) {
    $invalidString .= "invalid_lastname,";
}
if (!$data->setTel($_POST["tel"])) {
    $invalidString .= "invalid_tel,";
}

$data->setDescription($_POST["description"]);

// Set boolean input data
$data->setEmailPublic($_POST["email_public"]);
$data->setFirstnamePublic($_POST["firstname_public"]);
$data->setLastnamePublic($_POST["lastname_public"]);
$data->setTelPublic($_POST["tel_public"]);



// If there where errors -> print error
if ($invalidString != null) {
    // Remove last ","
    $invalidString = substr($invalidString, 0, strlen($invalidString) - 1);
    echo $invalidString;
    
} else {
    //try {
        User::register($data);
   // } catch (DatabaseException $de) {

    //}
    echo "alles OK";
}
?>