<?php
if (!defined("INCLUDE")) {
    exit;
}

class Trip {
    private $id = -1;
    private $driver = -1;
    private $availSeats = 0;
    private $currentLat = 0.0;
    private $currentLon = 0.0;
    private $destination = null;
    private $creation = null;
    private $ending = null;
    
    /**
     * Loads a trip from the database and returns a trip-object storing the information
     * of the loaded trip
     * @param int $id  ID of the user to load from the database
     * @return Trip Object storing data of the loaded trip or null, if trip with the
     *              given id does not exists or parameter id is not numeric 
     * @throws InvalidArgumentException Thrown, if the trip's id is invalid
     */
    public static function loadTrip($id) {
        if (!General::validId($id)) {
           throw new InvalidArgumentException("The trip-id is not valid.");
        }
        
        $db = Database::getInstance();
        $row = $db->fetch($db->query("SELECT * FROM `".DB_PREFIX."_trip` WHERE `id` = ".$id));
        
        if ($row["id"] == null) {
            return null;
        } else {
            $trip = new Trip();
            return $trip->loadTripBySqlResult($row);
        }
    }
    
    /**
     * Creates a trip from a given sql-result array.
     * @param Array $result Array storing the information of the trip
     *                      This has to be an array where the key representes
     *                      the tables name.
     * @param type $idFieldName Specifies the name of the id-field. Used when
     *                          the id field name is changed by SQL's "AS" statement
     * @return Trip Trip-object storing the information from the given result-array
     * @internal    This is for internal use only as this function could be used to 
     *              create a trip-object from a non existing database entry!
     * @throws InvalidArgumentException Thrown, if on of the arguments is invalid
     */
    public static function loadTripBySqlResult($result, $idFieldName = "id") {
        if (!is_array($result) || $idFieldName == null || $idFieldName == "" ||
                $result[$idFieldName] == null) {
            throw new InvalidArgumentException("Result or ifFieldName is invalid");
        }
        
        $trip = new Trip();
        
        $trip->id = (int)$result[$idFieldName];
        $trip->driver = $result["driver"];
        $trip->availSeats = $result["avail_seats"];
        $trip->currentLat = $result["current_lat"];
        $trip->currentLon = $result["current_lon"];
        $trip->destination = $result["destination"];
        $trip->creation = $result["creation"];
        $trip->ending = $result["ending"];
        
        return $trip;
        
    }
    
    /**
     * Creates a new trip using the data set with the setX()-methods
     * @return int ID of the new trip
     * @throws InputException Thrown, if an important field (e.g. destination) is missing
     */
    public function create() {
        // Cancel if important information is missing
        if ($this->availSeats <= 0 || $this->destination == null || 
                $this->driver == null) {
            throw new InputException("Some mandatory fields not set.");
        }
        
        // Write data into table
        $db = Database::getInstance();
        $creation = Date(Database::DATE_FORMAT, time());
        
        $db->query("INSERT INTO `".DB_PREFIX."_trip` (
                        `driver`,
                        `avail_seats`,
                        `current_lat`,
                        `current_lon`,
                        `destination`,
                        `creation`
                    ) VALUES (
                        \"".$this->driver."\",
                        \"".$this->availSeats."\",
                        \"".$this->currentLat."\",
                        \"".$this->currentLon."\",
                        \"".$this->destination."\",
                        \"".$creation."\"
                    )");
        
       return $this->id = $db->getId();      
    }
    
    /**
     * Updates the driver's current position
     * @param int $tripid The driver's id
     * @return boolean  True, if data was updated successfully 
     */
    public function updatePosition($tripid) {
        if (!is_numeric($tripid) || $tripid < 1) {
            return false;
        }
        
        $db = Database::getInstance();
        $updated = $db->query("UPDATE `".DB_PREFIX."_trip` 
                               SET `current_lat` = '".$this->currentLat."',
                                   `current_lon` = '".$this->currentLon."'
                               WHERE `id` = ".$tripid."
                               AND `driver` = ".$this->driver."
                               AND `ending` = 0");
        return ($updated && $db->getAffectedRows() > 0);
    }
    
    /**
     * Updates the number of available seats in the driver car
     * @param int $tripid The driver's id
     * @return boolean  True, if data was updated successfully 
     */
    public function updateAvailSeats($tripid) {
        if (!is_numeric($tripid) || $tripid < 1) {
            return false;
        }
        
        $db = Database::getInstance();
        $updated = $db->query("UPDATE `".DB_PREFIX."_trip` 
                               SET `avail_seats` = '".$this->availSeats."'
                               WHERE `id` = ".$tripid."
                               AND `driver` = ".$this->driver."
                               AND `ending` = 0");
        return ($updated && $db->getAffectedRows() > 0);
    }
    
    /**
     * Ends/closes the trip so that rating is now activated
     * @param int $tripid The driver's id
     * @return boolean  True, if data was updated successfully 
     */
    public function updateEndTrip($tripid) {
        if (!is_numeric($tripid) || $tripid < 1) {
            return false;
        }
        
        $db = Database::getInstance();
        $updated = $db->query("UPDATE `".DB_PREFIX."_trip` 
                               SET `ending` = '".Date(Database::DATE_FORMAT, time())."'
                               WHERE `id` = ".$tripid."
                               AND `driver` = ".$this->driver."
                               AND `ending` = 0");
        return ($updated && $db->getAffectedRows() > 0);
    }
    
    public static function openTripExists($driver) {
        if (!is_numeric($driver)) {
            throw new InputException("Driver id has to be numeric");
        }
        
        $db = Database::getInstance();
        $count = $db->fetch($db->query("SELECT count(*) AS count 
                                        FROM `".DB_PREFIX."_trip`
                                        WHERE `driver` = $driver 
                                        AND `ending` = 0"));
        
        return $count["count"] > 0;
        
    } 
    
    public function getId() {
        return $this->id;
    }
    
    /**
     *
     * @return User 
     */
    public function getDriver() {
        $loggedInUser = Session::getInstance()->getLoggedInUser();
        if ($loggedInUser != null && $this->driver == $loggedInUser->getId()) {
            return $loggedInUser;
        } else {
            return User::loadUser($this->driver);
        }
    }
    /**
     *
     * @return int 
     */
    public function getDriverId() {
        return $this->driver;
    }
    
    public function getAvailSeats() {
        return $this->availSeats;
    }
    
    public function getCurrentLat() {
        return $this->currentLat;
    }
    
    public function getCurrentLon() {
        return $this->currentLon;
    }
    
    public function getDestination() {
        return $this->destination;
    }
    
    public function getCreation() {
        return $this->creation;
    }
    
    public function getEnding() {
        return $this->ending;
    }
    
    /**
     * Returns if this trip has ended
     * @return boolean True, if trip has ended
     */
    public function hasEnded() {
        return !($this->ending == Database::DATA_NULL);
    }
    
    /**
     *
     * @param int $user 
     * @return boolean
     */
    public function setDriver($user) {
        if (is_numeric($user) && $user > 0) {
            $this->driver = $user;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     *
     * @param int $seats 
     * @return boolean
     */
    public function setAvailSeats($seats) {
        if (is_numeric($seats) && $seats >= 1 && $seats <= 99) {
            $this->availSeats = $seats;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     *
     * @param float $lat 
     * @return boolean
     */
    public function setCurrentLat($lat) {
        if (is_numeric($lat)) {
            $this->currentLat = $lat;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     *
     * @param float $lon 
     * @return boolean
     */
    public function setCurrentLon($lon) {
        if (is_numeric($lon)) {
            $this->currentLon = $lon;
            return true;
        } else {
            return false;
        }
    }
    
    public function setDestination($destination) {
        $destination = Database::getInstance()->secureInput($destination);
        if (General::validLength($destination)) {
            $this->destination = $destination;
            return true;
        } else {
            return false;
        }
    }
    
    
}
?>
