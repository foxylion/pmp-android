<?php
if (!defined("INCLUDE")) {
    exit;
}


/**
 * Handles access to user data and allows to create a new user
 * Most of the method's may throw a DatabaseException if quering the database fails
 *
 * @author Patrick Strobel
 * @version 1.0.2
 */
class user {

    const INVALID_USERNAME = 1; // 00000001
    const INVALID_EMAIL = 2; // 00000010
    const INVALID_FIRSTNAME = 4; // 00000100
    const INVALID_LASTNAME = 8; // 00001000
    const INVALID_TEL = 16; // 00010000
    const USERNAME_EXISTS = 32; // 00100000
    const EMAIL_EXISTS = 64; // 01000000
    const INVALID_PASSWORD = 128; // 10000000

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
    private $activated = false;
    
    /** @var Rating */
    private $rating = null;

    private function __construct() {
    }

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
        return $user->fillAttributes("SELECT * FROM `" . DB_PREFIX . "_user` WHERE `id` = $id");
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
            $result[$idFieldName] == null
        ) {
            throw new InvalidArgumentException("Result or idFieldName (" . $idFieldName . ") is invalid");
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
        return $user->fillAttributes("SELECT * FROM `" . DB_PREFIX . "_user` WHERE `username` = \"$name\"");
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

    private static function isPasswordValid($password) {
        return strlen($password) >= 8;
    }

    /**
     * Checks if a given name-string is valid.
     * That is, if it does start with a letter/digit, has a valid lenght and does only
     * have one "-", "_" or space between two letters/digits
     * @param String $name String to validate
     * @return boolean  True, if string is valid
     */
    private static function isNameValid($name) {
        if (!General::validLength($name)) {
            return false;
        }

        $char = General::REG_INTCHARS;
        $match = preg_match("/^[" . $char . "0-9]+([-_[:space:]]?[" . $char . "0-9])+$/i", $name);
        return $match > 0;
    }

    /**
     * Check if a given string is a valid e-mail-address.
     * That is, if the address follows the format "prefix@postfix.domain
     * whereas prefix and postfix may be a string build of (language dependent)
     * characters including ".", "_" and "-" (but only one of them between two chars).
     * The domain has to be at least 2 characters long and build up using a-z only.
     * @param String $email E-Mail to validate
     * @return boolean  True, if email is valid
     */
    private static function isEmailValid($email) {
        $char = General::REG_INTCHARS;
        $match = preg_match("/^[" . $char . "0-9]+([-_\.]?[" . $char . "0-9])+@[" . $char . "0-9]+([-_\.]?[" . $char . "0-9])+\.[a-z]{2,}$/i", $email);
        return $match > 0;
    }

    /**
     * Checks if a given telephone number is valid.
     * That is, if the number does contain digits only, where two digits might be
     * spererated by a single "-". The number might also begin with a single "+"
     * @param String $tel   Telephone number to validate
     * @return boolean  True, if telephone number is valid
     */
    private static function isTelValid($tel) {
        if (!General::validLength($tel)) {
            return false;
        }

        $match = preg_match("/^\+?[0-9]+(-?[0-9])+$/", $tel);
        return $match > 0;
    }


    /**
     * Registers a user to the system.
     * @param String $username
     * @param String $password
     * @param String $email
     * @param String $firstname
     * @param String $lastname
     * @param String $tel
     * @param String $description
     * @param boolean $emailPublic
     * @param boolean $firstnamePublic
     * @param boolean $lastnamePublic
     * @param boolean $telPublic
     * @return User The registered user
     * @throws InvalidArgumentException Thrown, if a mandatory field (like "username") or a
     *                      visibility field (like "email_public") is not set. Use the code
     *                      to determine which field was invalid
     */
    public static function register($username, $password, $email, $firstname, $lastname,
                                    $tel, $description, $emailPublic, $firstnamePublic, $lastnamePublic,
                                    $telPublic) {

        $db = Database::getInstance();
        $passwordHash = User::hashPassword($password);
        $invalid = 0;

        // Check if input is valid
        if (!self::isNameValid($username)) {
            $invalid |= self::INVALID_USERNAME;
        } elseif (self::usernameExists($username)) {
            $invalid |= self::USERNAME_EXISTS;
        }

        if (!self::isEmailValid($email)) {
            $invalid |= self::INVALID_EMAIL;
        } elseif (self::emailExists($email)) {
            $invalid |= self::EMAIL_EXISTS;
        }

        if (!self::isNameValid($firstname)) {
            $invalid |= self::INVALID_FIRSTNAME;
        }

        if (!self::isNameValid($lastname)) {
            $invalid |= self::INVALID_LASTNAME;
        }

        if (!self::isTelValid($tel)) {
            $invalid |= self::INVALID_TEL;
        }

        if (!self::isPasswordValid($password)) {
            $invalid |= self::INVALID_PASSWORD;
        }


        $description = $db->secureInput($description);
        $emailPublic = (bool)$emailPublic;
        $firstnamePublic = (bool)$firstnamePublic;
        $lastnamePublic = (bool)$lastnamePublic;
        $telPublic = (bool)$telPublic;

        // Throw an expection if one or more input data was invalid
        if ($invalid > 0) {
            throw new InvalidArgumentException("At leat one input data was invalid. See code for details", $invalid);
        }

        try {
            // !!Transactions will only work when InnoDB is used!!
            $db->query("START TRANSACTION");
            
            // Write user data into table
            $db->query("INSERT INTO `" . DB_PREFIX . "_user` (
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
                            \"" . $username . "\",
                            \"" . $passwordHash . "\",
                            \"" . $email . "\",
                            \"" . $firstname . "\",
                            \"" . $lastname . "\",
                            \"" . $tel . "\",
                            \"" . $description . "\",
                            \"" . $emailPublic . "\",
                            \"" . $firstnamePublic . "\",
                            \"" . $lastnamePublic . "\",
                            \"" . $telPublic . "\",
                            from_unixtime(" . time() . ")
                        )");

            $user = new User();
            $user->id = $db->getId();
            $user->username = $username;
            $user->email = $email;
            $user->emailPublic = $emailPublic;
            $user->firstname = $firstname;
            $user->firstnamePublic = $firstnamePublic;
            $user->lastname = $lastname;
            $user->lastnamePublic = $lastnamePublic;
            $user->tel = $tel;
            $user->telPublic = $telPublic;
            $user->passwordHash = $passwordHash;
            $user->description = $description;
            $user->regdate = $regdate;


            // Create dataset for storing this user's postition
            Position::createDataset($user);

            $db->query("COMMIT");
        } catch (Exception $e) {
            echo "\nROLEBACK!!!\n";
            $db->query("ROLLBACK");
            throw $e;
        }

        return $user;
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
        $result = $db->query("SELECT `key` FROM `" . DB_PREFIX . "_verification`
                              WHERE `user` = " . $this->id);
        $row = $db->fetch($result);

        $key = null;

        // If key is in database, use it. Otherwise generate new one
        if ($row) {
            $key = $row["key"];
        } else {
            $key = General::randomString(32);
            $db->query("INSERT INTO `" . DB_PREFIX . "_verification` (
                            `user`,
                            `key`
                        ) VALUES (
                            $this->id,
                            \"$key\"
                        )");
        }


        // Create verification url and send it via e-mail
        $url = "http://" . BASE_URL . "/verification.php?user=" . $this->id . "&key=" . $key;
        $message = "Hello " . $this->firstname . " " . $this->lastname . ",\n\n" .
            "Thank you for your registration on vHike. Your account has been created.\n\n" .
            "In order to log in, you have to verify your e-mail address. " .
            "To do so, open the following link:\n\n" .
            "$url\n\n" .
            "Regards,\n" .
            "Your vHike-System";

        mail($this->email, "Account verification", $message, "From: " . ADMIN_EMAIL);

    }

    /**
     * Updates the user's profile
     * @param String $firstname
     * @param String $lastname
     * @param String $tel
     * @param String $description
     * @throws InvalidArgumentException Thrown, if firstname, lastname or tel-nr.
     *                                  has an invalid length. See <code>register</code> for details
     */
    public function updateProfile($firstname, $lastname, $tel, $description) {
        // Verify input-data
        $invalid = 0;
        if (!self::isNameValid($firstname)) {
            $invalid |= self::INVALID_FIRSTNAME;
        }

        if (!self::isNameValid($lastname)) {
            $invalid |= self::INVALID_LASTNAME;
        }

        if (!self::isTelValid($tel)) {
            $invalid |= self::INVALID_TEL;
        }

        // Throw an expection if one or more input data was invalid
        if ($invalid > 0) {
            throw new InvalidArgumentException("At leat one input data was invalid. See code for details", $invalid);
        }

        $db = Database::getInstance();
        $description = $db->secureInput($description);

        // Write new data into database
        $db->query("UPDATE `" . DB_PREFIX . "_user`
                    SET 
                        `firstname` = \"" . $firstname . "\",
                        `lastname`  = \"" . $lastname . "\",
                        `tel`  = \"" . $tel . "\",
                        `description`  = \"" . $description . "\"
                    WHERE `id` = " . $this->id);

        $this->firstname = $firstname;
        $this->lastname = $lastname;
        $this->tel = $tel;
        $this->description = $description;
    }

    /**
     * Updates/Sets the visibility of the users profile-data.
     * @param boolean $emailPublic
     * @param boolean $firstnamePublic
     * @param boolean $lastnamePublic
     * @param boolean $telPublic
     */
    public function updateVisibility($emailPublic, $firstnamePublic,
                                     $lastnamePublic, $telPublic) {

        $emailPublic = (bool)$emailPublic;
        $firstnamePublic = (bool)$firstnamePublic;
        $lastnamePublic = (bool)$lastnamePublic;
        $telPublic = (bool)$telPublic;

        $db = Database::getInstance();

        // Write new data into database
        $db->query("UPDATE `" . DB_PREFIX . "_user`
                    SET 
                        `email_public` = \"" . $emailPublic . "\",
                        `firstname_public`  = \"" . $firstnamePublic . "\",
                        `lastname_public`  = \"" . $lastnamePublic . "\",
                        `tel_public`  = \"" . $telPublic . "\"
                    WHERE `id` = " . $this->id);

        $this->emailPublic = $emailPublic;
        $this->firstnamePublic = $firstnamePublic;
        $this->lastnamePublic = $lastnamePublic;
        $this->telPublic = $telPublic;
    }


    /**
     * Checks if the given verification key matches the give user id and
     * activates the account if they match.
     * @param int $id       Userid to match with the given key
     * @param String $key   Key to match with the given userid
     * @return boolean  True, if id and key matched and user has been activated, otherwise false
     */
    public static function verifyUser($id, $key) {
        $db = Database::getInstance();

        $db->query("SELECT `key` FROM `" . DB_PREFIX . "_verification`
                    WHERE `user` = $id 
                    AND `key` = \"" . $key . "\"");

        // If the verification key is valid, activate user account
        if ($db->getAffectedRows() > 0) {
            // Activate account
            $db->query("UPDATE `" . DB_PREFIX . "_user`
                        SET `activated` = 1
                        WHERE `id` = $id");

            // Remove key
            $db->query("DELETE FROM `" . DB_PREFIX . "_verification`
                        WHERE `user` = $id");

            return true;
        } else {
            return false;
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
            $this->id . " AND `ending`=0 LIMIT 1"));
        if ($result) {
            return $result['id'];
        } else {
            return NULL;
        }
    }

    public function getCurrentQueryIds() {
        $db = Database::getInstance();
        $query = $db->query("SELECT `id` FROM `" . DB_PREFIX . "_query` WHERE `passenger`=$this->id");
        $arr = null;
        $i = 0;
        while ($row = $db->fetch($query)) {
            $arr[$i++] = $row["id"];
        }
        return $arr;
    }

    // Get trip info from a rider's ID
    public function getCurrentRideTripInfo() {
        $db = Database::getInstance();
        $query = $db->query(
            "SELECT dev_trip.id AS tripid, dev_trip.driver, dev_user.username, dev_user.rating_avg, dev_user.rating_num " .
            "FROM dev_trip INNER JOIN dev_ride ON dev_trip.id = dev_ride.trip" .
            "              INNER JOIN dev_user ON dev_trip.driver = dev_trip.driver " .
            "WHERE dev_ride.passenger=" . $this->getId() . " AND dev_trip.ending = 0 LIMIT 1");
        if ($db->getAffectedRows()==0) {
            return null;
        } else {
            return $db->fetch($query);
        }
    }

    public function updatePosition($lat, $lon) {
        $db = Database::getInstance();
        try {
            $db->query("START TRANSACTION");
            $db->query("UPDATE `" . DB_PREFIX . "_query` SET `current_lat`=$lat, `current_lon`=$lon WHERE `passenger`=$this->id");
            $db->query("UPDATE `" . DB_PREFIX . "_trip`  SET `current_lat`=$lat, `current_lon`=$lon WHERE `driver`=$this->id AND `ending`=0");
            $db->query("COMMIT");
        } catch (DatabaseException $de) {
            $db->query("ROLLBACK");
            return false;
        }
        return true;
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

    public function getEmail() {
        return $this->email;
    }

    public function getRatingAvg() {
        if ($this->rating == null) {
            $this->rating = Rating::loadRating($this);
        }
        return $this->rating->getAverage();
    }

    public function getRatingNum() {
        if ($this->rating == null) {
            $this->rating = Rating::loadRating($this);
        }
        return $this->rating->getNumber();
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
                                      FROM `" . DB_PREFIX . "_user`
                                      WHERE `id` = " . $this->id));

        $avg = (double)$row["rating_avg"];
        $num = (int)$row["rating_num"];

        // Calculated new rating and write it back to the db
        $newNum = $num + 1;
        $newAvg = ($avg * $num + $rating) / ($newNum);
        $db->query("UPDATE `" . DB_PREFIX . "_user`
                    SET `rating_avg` = " . $newAvg . ", `rating_num` = " . $newNum . "
                    WHERE `id` = " . $this->id);

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
        $result = $db->query("SELECT `id` FROM `" . DB_PREFIX . "_user` WHERE `username` = \"$username\"");
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

        $result = $db->query("SELECT count(*) AS count FROM `" . DB_PREFIX . "_user` WHERE `email` = \"$email\"");
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
