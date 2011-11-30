<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

class UserException extends Exception {

}

/**
 * Stores information about a user used for registration functionality.
 * This class' method escapes strings and verifies their length but does
 * not check if the user's name or email is already in use.
 */
class RegistrationData {
    private $username;
    private $password;
    private $email;
    private $firstname;
    private $lastname;
    private $tel;
    private $description;
    private $emailPublic;
    private $firstnamePublic;
    private $lastnamePublic;
    private $telPublic;
    
    public function getUsername() {
        return $this->username;
    }
    
    public function getPassword() {
        return $this->password;
    }
    
    public function getPasswordHash() {
        return md5($this->password);
    }
    
    public function getEmail() {
        return $this->email;
    }
    
    public function getFirstname() {
        return $this->firstname;
    }
    
    public function getLastname() {
        return $this->lastname;
    }
    
    public function getTel() {
        return $this->tel;
    }
    
    public function getDescription() {
        return $this->description;
    }
    
    public function isEmailPublic() {
        return $this->emailPublic;
    }
    
    public function isFirstnamePublic() {
        return $this->firstnamePublic;
    }
    
    public function isLastnamePublic() {
        return $this->lastnamePublic;
    }
    
    public function isTelPublic() {
        return $this->telPublic;
    }
    
    /**
     *
     * @param String $value
     * @return boolean 
     */
    public function setUsername($value) {
        $value = Database::getInstance()->secureInput($value);
        if ($this->validLength($value)) {
            $this->username = $value;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     *
     * @param String $value
     * @return boolean 
     */
    public function setPassword($value) {
        $value = Database::getInstance()->secureInput($value);
        if ($this->validLength($value)) {
            $this->password = $value;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     *
     * @param String $value
     * @return boolean 
     */
    public function setEmail($value) {
        // TODO: Check format
        $value = Database::getInstance()->secureInput($value);
        if ($this->validLength($value)) {
            $this->email = $value;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     *
     * @param String $value
     * @return boolean 
     */
    public function setFirstname($value) {
        $value = Database::getInstance()->secureInput($value);
        if ($this->validLength($value)) {
            $this->firstname = $value;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     *
     * @param String $value
     * @return boolean 
     */
    public function setTel($value) {
        // TODO: Check format e.g. only numbers, "-", "+"
        $value = Database::getInstance()->secureInput($value);
        if ($this->validLength($value)) {
            $this->tel = $value;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     *
     * @param String $value
     * @return boolean 
     */
    public function setLastname($value) {
        $value = Database::getInstance()->secureInput($value);
        if ($this->validLength($value)) {
            $this->lastname = $value;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     *
     * @param String $value 
     */
    public function setDescription($value) {
        $this->description = Database::getInstance()->secureInput($value);
    }
    
    /**
     *
     * @param String $value 
     */
    public function setEmailPublic($public) {
        if (strcasecmp($public, "true") == 0) {
            $this->emailPublic = true;
        }
    }
    
    /**
     *
     * @param String $value 
     */
    public function setFirstnamePublic($public) {
        if (strcasecmp($public, "true") == 0) {
            $this->firstnamePublic = true;
        }
    }
    
    /**
     *
     * @param String $value 
     */
    public function setLastnamePublic($public) {
        if (strcasecmp($public, "true") == 0) {
            $this->lastnamePublic = true;
        }
    }
    
    /**
     *
     * @param String $value 
     */
    public function setTelPublic($public) {
        if (strcasecmp($public, "true") == 0) {
            $this->telPublic = true;
        }
    }
    
    private function validLength($input) {
        $length = strlen($input);
        
        if ($length > 3 && $length <= 100) {
            return true;
        } else {
            return false;
        }
        
    }
}
        
/**
 * Handles access to user data and allow to create a new user
 * Most of the method's may throw a DatabaseException if quering the database fails
 *
 * @author Patrick
 */
class user {

    /**
     * Registers a user to the system. This does not check if there is already a
     * user with the same email or password.
     * @param RegistrationDate $regdata Object holding data used for user registration
     */
    public static function register($regdata) {
        $db = Database::getInstance();
        
        $regdate = Date("Y-m-d H:i:s", time());
               
        //echo "Insert new User into db";
        
        $db->query("INSERT INTO `".DB_PREFIX."_user` (
                        `username`,
                        `password`,
                        `email`,
                        `firstname`,
                        `lastname`,
                        `tel`,
                        `description`,
                        `email_public`,
                        `firstname_public`,
                        `lastname_public`,
                        `tel_public`,
                        `regdate`
                    ) VALUES (
                        \"".$regdata->getUsername()."\",
                        \"".$regdata->getPasswordHash()."\",
                        \"".$regdata->getEmail()."\",
                        \"".$regdata->getFirstname()."\",
                        \"".$regdata->getLastname()."\",
                        \"".$regdata->getTel()."\",
                        \"".$regdata->getDescription()."\",
                        \"".$regdata->isEmailPublic()."\",
                        \"".$regdata->isFirstnamePublic()."\",
                        \"".$regdata->isLastnamePublic()."\",
                        \"".$regdata->isTelPublic()."\",
                        \"$regdate\"
                    )");
        
       
        
        
    }
    
    /**
     * Checks if the given username is already in use.
     * @param String $username
     * @return boolean True, if user exists
     */
    public static function usernameExists($username) {
        return self::getUserId($username) < 0 ? false : true;
    }
    
    /**
     * Returns the id of a given user
     * @param String $username  User's name to get the id for
     * @return int  The user's id or negative, if id wasn't found
     */
    public static function getUserId($username) {
        $db = Database::getInstance();
        
        // Escape input data
        $username = $db->secureInput($username);
        
        // Execute query and fetch result-array
        $result = $db->query("SELECT `id` FROM `".DB_PREFIX."_user` WHERE `username` = \"$username\"");
        $row = $row = $db->fetch($result);
        
        // Return -1 if there's no user with this name
        return $row == null ? -1 : $row['id'];
    }
    
    /**
     * Checks if the given e-mail is already in use.
     * @param String $username
     * @return boolean True, if e-mail is in use
     */
    public static function emailExists($email) {
        $db = Database::getInstance();
        
        $email = $db->secureInput($email);
        
        $result = $db->query("SELECT count(*) AS count FROM `".DB_PREFIX."_user` WHERE `email` = \"$email\"");
        $row = $db->fetch($result);
        
        return $row['count'] > 0 ? true : false;
    }
}

?>
