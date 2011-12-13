<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
     */
    public static function loadTrip($id) {
        if (!is_numeric($id)) {
            return null;
        }
        
        $trip = new Trip();
        return $trip->fillAttributes("SELECT * FROM `".DB_PREFIX."_trip` WHERE `id` = $id");
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
     * @return int 
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
