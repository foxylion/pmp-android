<?php
if (!defined("INCLUDE")) {
    exit;
}


/**
 * Handles access to user data and allows to create a new user
 * Most of the method's may throw a DatabaseException if quering the database fails
 *
 * @author Patrick
 */
class user {
    
    private $id = -1;
    private $username = null;
    private $passwordHash = null;
    private $email = null;
    private $firstname = null;
    private $lastname = null;
    private $tel = null;
    private $description = null;
    private $regdate = null;
    private $emailPublic = false;
    private $firstnamePublic = false;
    private $lastnamePublic = false;
    private $telPublic = false;
    private $ratingAvg = 0;
    private $ratingNum = 0;
    private $activated = false;
    
    
    /**
     * Loads a user from the database and returns a user-object storing the information
     * of the loaded user
     * @param int $id  ID of the user to load from the database
     * @return User Object storing data of the loaded user or null, if user with the
     *              given id does not exists 
     * @throws InvalidArgumentException Thrown, if id is invalid (e.g. not numeric)
     */
    public static function loadUser($id) {
        if (!General::validId($id)) {
            throw new InvalidArgumentException("The offer ID is invalid");
        }
        
        $user = new User();
        return $user->fillAttributes("SELECT * FROM `".DB_PREFIX."_user` WHERE `id` = $id");
    }
    
    
    /**
     * Creates a user from a given sql-result array.
     * @param Array $result Array storing the information of the user
     *                      This has to be an array where the key representes
     *                      the tables name.
     * @param type $idFieldName Specifies the name of the id-field. Used when
     *                          the id field name is changed by SQL's "AS" statement
     * @return User User-object storing the information from the given result-array
     * @internal    This is for internal use only as this function could be used to 
     *              create a user-object from a non existing database entry!
     * @throws InvalidArgumentException Thrown, if on of the arguments is invalid
     */
    public static function loadUserBySqlResult($result, $idFieldName = "id") {
        if (!is_array($result) || $idFieldName == null || $idFieldName == "" ||
                $result[$idFieldName] == null) {
            throw new InvalidArgumentException("Result or ifFieldName is invalid");
        }
        
        $user = new User();
        
        $user->id = (int)$result[$idFieldName];
        $user->username = $result["username"];
        $user->passwordHash = $result["password"];
        $user->email = $result["email"];
        $user->firstname = $result["firstname"];
        $user->lastname = $result["lastname"];
        $user->tel = $result["tel"];
        $user->description = $result["description"];
        $user->regdate = $result["regdate"];
        $user->emailPublic = (bool)$result["email_public"];
        $user->firstnamePublic = (bool)$result["firstname_public"];
        $user->lastnamePublic = (bool)$result["lastname_public"];
        $user->telPublic = (bool)$result["tel_public"];
        $user->ratingAvg = (float)$result["rating_avg"];
        $user->ratingNum = (int)$result["rating_num"];
        $user->activated = $result["activated"];
        
        return $user;
        
    }
    
    /**
     * Loads a user from the database and returns a user-object storing the information
     * of the loaded user
     * @param String $name  Name of the user to load from the database
     * @return User Object storing data of the loaded user or null, if user with the
     *              given id does not exists or parameter id is not numeric 
     */
    public static function loadUserByName($name) {
        $name = Database::getInstance()->secureInput($name);
        
        $user = new User();
        return $user->fillAttributes("SELECT * FROM `".DB_PREFIX."_user` WHERE `username` = \"$name\"");
    } 
    
    private function fillAttributes($sqlQuery) {
        $db = Database::getInstance();
        $row = $db->fetch($db->query($sqlQuery));
        
        if ($row["id"] == null) {
            return null;
        }
        
        // Write data into attributes
        return $this->loadUserBySqlResult($row);
    } 
    

    
    /**
     * Registers a user to the system. This does not check if there is already a
     * user with the same email or password.
     * @param UserDate $regdata Object holding data used for user registration
     * @throws InputEception Thrown, if a mandatory field (like "username") or a
     *                      visibility field (like "email_public") is not set 
     *  
     */
    public function register() {
        
        // Verify that all data is set
        if ($this->username == null || $this->passwordHash == null || 
                $this->email == null || $this->firstname == null || 
                $this->lastname == null || $this->tel == null) {
            throw new InputException("Some mandatory fields not set.");
        }
      
        
        // Write data into table
        $db = Database::getInstance();
        $regdate = Date(Database::DATE_FORMAT, time());
        
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
                        \"".$this->username."\",
                        \"".$this->passwordHash."\",
                        \"".$this->email."\",
                        \"".$this->firstname."\",
                        \"".$this->lastname."\",
                        \"".$this->tel."\",
                        \"".$this->description."\",
                        \"".$this->emailPublic."\",
                        \"".$this->firstnamePublic."\",
                        \"".$this->lastnamePublic."\",
                        \"".$this->isTelPublic()."\",
                        \"$regdate\"
                    )");
        
       $this->id = $db->getId();      
    }
    
    
     /**
     * Generates a verification key for the user and send's the key
     * to the users email address. If there's already a verification key in
     * the database for the given user, the key is send to the user's
     * email address once again
     */
    public function sendVerificationKey() {
        // Cancel if no user existing in the database is linked to this object
        if ($this->id < 0) {
            return;
        }
        
        $db = Database::getInstance();
        
        // Check if key has already been written to the database
        $result = $db->query("SELECT `key` FROM `".DB_PREFIX."_verification`
                              WHERE `user` = ".$this->id);
        $row = $db->fetch($result);
        
        $key;
        
        // If key is in database, use it. Otherwise generate new one
        if ($row) {
            $key = $row["key"];
        } else {
            $key = General::randomString(32);
            $db->query("INSERT INTO `".DB_PREFIX."_verification` (
                            `user`,
                            `key`
                        ) VALUES (
                            $this->id,
                            \"$key\"
                        )");
        }


       // Create verification url and send it via e-mail
       $url = "http://".BASE_URL."/verification.php?userid=$this->id&key=$key";
       $message = "Hello $this->firstname $this->lastname,\n\n" .
                  "your account has been created. In order to log in, you have to verify your e-mail address. " .
                  "To do so, open the following link:\n\n" .
                  "$url\n\n" .
                  "Regards,\n" .
                  "Your vHike-System";
        
        mail($this->email, "Account verification", $message, "From: ".ADMIN_EMAIL);
        
    }
    
    
    /**
     * Checks if the given verification key matches the give user id and
     * activates the account if they match.
     * @param int $id       Userid to match with the given key
     * @param String $key   Key to match with the given userid
     * @return boolean  True, if id and key matched, otherwise false
     */
    public static function verifyUser($id, $key) {
        $result = $db->query("SELECT `key` FROM `".DB_PREFIX."_verification`
                              WHERE `user` = $id AND
                                    `key` = \"$key\"");
        $row = $db->fetch($result);
        
        // If the verification key is valid, activate user account
        if (row) {
            // Activate account
            $db->query("UPDATE `".DB_PREFIX."_user`
                        SET `activated` = 1
                        WHERE `id` = $id");
            
            // Remove key
            $db->query("DELETE FROM `".DB_PREFIX."_user`
                        WHERE `user` = $id");
            
        }
        
    }
    
    public function getId() {
        return $this->id;
    }
    
    public function getUsername() {
        return $this->username;
    }
    
    public function getPasswordHash() {
        return $this->passwordHash;
    }

    public function getCurrentTripId() {
        $db = Database::getInstance();
        $result = $db->fetch($db->query("SELECT `id` FROM `" . DB_PREFIX . "_trip` WHERE `driver`=" .
            $this->id . " LIMIT 1"));
        if ($result) {
            return $result['id'];
        } else {
            return NULL;
        }
    }

    public function getCurrentQueryId(){
        return;
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
    
    public function getRegdate() {
        return $this->regdate;
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
    
    public function getRatingAvg() {
        return $this->ratingAvg;
    }
    
    public function getRatingNum() {
        return $this->ratingNum;
    }
    
    public function isActivated() {
        return $this->activated;
    }
    
    /**
     * Rates this user
     * @param int $rating Rating for this user
     * @throws InvalidArgumentException Thrown, if rating is not between 1 and 5´
     */
    public function rate($rating) {
        if (!is_numeric($rating) || $rating < 1 || $rating > 5) {
            throw new InvalidArgumentException("Rating has to be between 1 and 5");
        }
        
        // Read old avg rating and number of ratings
        $db = Database::getInstance();
        $row = $db->fetch($db->query("SELECT `rating_avg`, `rating_num` 
                                      FROM `".DB_PREFIX."_user`
                                      WHERE `id` = ".$this->id));
        
        $avg = (double)$row["rating_avg"];
        $num = (int)$row["rating_num"];
        
        // Calculated new rating and write it back to the db
        $newNum = $num + 1;
        $newAvg = ($avg * $num + $rating) / ($newNum);
        $db->query("UPDATE `".DB_PREFIX."_user` 
                    SET `rating_avg` = ".$newAvg.", `rating_num` = ".$newNum."
                    WHERE `id` = ".$this->id);
                
    }
    
    /**
     *
     * @param String $value
     * @return boolean 
     */
    public function setUsername($value) {
        $value = Database::getInstance()->secureInput($value);
        if (General::validLength($value)) {
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
        if (General::validLength($value)) {
            $this->passwordHash = self::hashPassword($value);
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
        if (General::validLength($value)) {
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
        if (General::validLength($value)) {
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
        if (General::validLength($value)) {
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
        if (General::validLength($value)) {
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
    
    public static function hashPassword($password) {
        return md5($password);
    }
    
    /**
     * Compares two user-object and returns true if they are equal
     * @param User $user 
     * @return boolean  True, if the object are equal
     */
    public function isEqual($user) {
        if ($user instanceof User) {
            return ($this->id == $user->id); 
        } else {
            return false;
        }
    }
}

?>
