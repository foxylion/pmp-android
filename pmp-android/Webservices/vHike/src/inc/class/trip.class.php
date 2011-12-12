<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

class Trip {
    private $id = -1;
    private $driver = null;
    private $availSeats = 0;
    private $currentLat = 0.0;
    private $currentLon = 0.0;
    private $destination = null;
    private $creation = null;
    private $ending = null;
    
    /**
     * Loads a trip from the database and returns a trip-object storing the information
     * of the loaded user
     * @param int $id  ID of the user to load from the database
     * @return Trip Object storing data of the loaded trip or null, if trip with the
     *              given id does not exists or parameter id is not numeric 
     */
    public static function loadTrip($id) {
        if (!is_numeric($id)) {
            return null;
        }
        
        $user = new User();
        return $user->fillAttributes("SELECT * FROM `".DB_PREFIX."_trip` WHERE `id` = $id");
    }
    
    private function fillAttributes($sqlQuery) {
        $db = Database::getInstance();
        $row = $db->fetch($db->query($sqlQuery));
        
        if ($row["id"] == null) {
            return null;
        }
        
        // Write data into attributes
        $this->id = (int)$row["id"];
        $this->driver = $row["driver"];
        $this->availSeats = $row["avail_seats"];
        $this->currentLat = $row["current_lat"];
        $this->currentLon = $row["current_lon"];
        $this->destination = $row["destination"];
        $this->creation = $row["creation"];
        $this->ending = $row["ending"];
        $this->regdate = $row["regdate"];
        
        return $this;
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
                        \"".$this->driver->getId()."\",
                        \"".$this->availSeats."\",
                        \"".$this->currentLat."\",
                        \"".$this->currentLon."\",
                        \"".$this->destination."\",
                        \"".$creation."\"
                    )");
        
       return $this->id = $db->getId();      
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
     *
     * @param User $user 
     * @return boolean
     */
    public function setDriver($user) {
        if ($user instanceof User) {
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
        if (is_numeric($seats)) {
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
