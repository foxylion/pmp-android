<?php
    /**
     * This service is used identify anonymous profiles
     */
    define("INCLUDE", true);
    require ("./../inc/json_framework.inc.php");
    
    // Stop execution of script and print error message if user is not logged in
    Json::printErrorIfNotLoggedIn();
    
    try {
		$user = User::loadUser($_GET["user_id"]);
		if ($user != null) {
			$ret = User::isProfileAnonymous($user->getID());			 
			$output = array("successful" => true, "anonymous" => $ret);
			if ($user->isEmailPublic()) {
				$output["email"] = $user->getEmail();
			}
		}

		echo Json::arrayToJson($output);
        
    } catch (InvalidArgumentException $iae) {
        Json::printInvalidInputError();
    } catch (DatabaseException1 $de) {
        Json::printDatabaseError($de);
    }
    Database::getInstance() -> disconnect();
    
    // EOF