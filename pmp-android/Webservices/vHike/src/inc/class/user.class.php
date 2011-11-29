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
    private $email_public;
    private $firstname_public;
    private $lastname_public;
    private $tel_public;
    
    /**
     *
     * @param String $value
     * @return boolean 
     */
    public function setUsername($value) {
        $value = Database::getInstance()->secureInput($value);
        if ($this->validLength($value)) {
            $this->username = $username;
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
            $this->email_public = true;
        }
    }
    
    /**
     *
     * @param String $value 
     */
    public function setFirstnamePublic($public) {
        if (strcasecmp($public, "true") == 0) {
            $this->firstname_public = true;
        }
    }
    
    /**
     *
     * @param String $value 
     */
    public function setLastnamePublic($public) {
        if (strcasecmp($public, "true") == 0) {
            $this->lastname_public = true;
        }
    }
    
    /**
     *
     * @param String $value 
     */
    public function setTelPublic($public) {
        if (strcasecmp($public, "true") == 0) {
            $this->tel_public = true;
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
        
       
        
        
    }
    
    /**
     * Checks if the given username is already in use.
     * @param String $username
     * @return boolean 
     */
    public static function usernameExists($username) {
        // TODO escape
        return true;
    }
    
    /**
     * Checks if the given e-mail is already in use.
     * @param String $username
     * @return boolean 
     */
    public static function emailExists($email) {
        // TODO escpae
        return true;
    }
}

?>
