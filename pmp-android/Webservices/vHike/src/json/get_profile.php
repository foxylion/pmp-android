<?php
/**
 * This service is used to show the profile of a rgistered user
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
    $user = User::loadUser($_GET["id"]);
    
    if ($user != null) {
        $output = array("successful" => true, 
                        "status" => "found",
                        "id" => $user->getId(), 
                        "username" => $user->getUsername());
        
        if ($user->isEmailPublic()) {
            $output["email"] = $user->getEmail();
        }
        
        if ($user->isFirstnamePublic()) {
            $output["fistname"] = $user->getFirstname();
        }
        
        if ($user->isLastnamePublic()) {
            $output["lastname"] = $user->getLastname();
        }
        
        if ($user->isTelPublic()) {
            $output["tel"] = $user->getTel();
        }
        
        $output += array("description" => $user->getDescription(),
                        "regdate" => $user->getRegdate(),
                        "email_public" => $user->isEmailPublic(),
                        "firstname_public" => $user->isFirstnamePublic(),
                        "lastname_public" => $user->isLastnamePublic(),
                        "tel_public" => $user->isTelPublic(),
                        "rating_avg" => $user->getRatingAvg(),
                        "rating_num" => $user->getRatingNum());
    } else {
        $output = array("successful" => true, 
                        "status" => "not_found");
    }
    
    echo Json::arrayToJson($output);
} catch (DatabaseException $de) {
    Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();
?>
