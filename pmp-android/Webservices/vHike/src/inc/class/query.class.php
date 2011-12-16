<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

class Query {
    private $id = -1;
    private $passenger = -1;
    private $seats = 0;
    private $currentLat = 0.0;
    private $currentLon = 0.0;
    private $destination = null;
    
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
        return $trip->fillAttributes("SELECT * FROM `".DB_PREFIX."_query` WHERE `id` = $id");
    }
    
    private function fillAttributes($sqlQuery) {
        $db = Database::getInstance();
        $row = $db->fetch($db->query($sqlQuery));
        
        if ($row["id"] == null) {
            return null;
        }
        
        // Write data into attributes
        $this->id = (int)$row["id"];
        $this->passenger = $row["passenger"];
        $this->seatsSeats = $row["seats"];
        $this->currentLat = $row["current_lat"];
        $this->currentLon = $row["current_lon"];
        $this->destination = $row["destination"];
        
        return $this;
    } 
    
    /**
     * Creates a new trip using the data set with the setX()-methods
     * @return int ID of the new trip
     * @throws InputException Thrown, if an important field (e.g. destination) is missing
     */
    public function create() {
        // Cancel if important information is missing
        if ($this->seats <= 0 || $this->destination == null || 
                $this->passenger == null) {
            throw new InputException("Some mandatory fields not set.");
        }
        
        // Write data into table
        $db = Database::getInstance();
        $creation = Date(Database::DATE_FORMAT, time());
				
				echo "INSERT INTO `".DB_PREFIX."_query` (
                        `passenger`,
                        `seats`,
                        `current_lat`,
                        `current_lon`,
                        `destination`
                    ) VALUES (
                        \"".$this->passenger."\",
                        \"".$this->seats."\",
                        ".$this->currentLat.",
                        ".$this->currentLon.",
                        \"".$this->destination."\"
                    )";
        
        $db->query("INSERT INTO `".DB_PREFIX."_query` (
                        `passenger`,
                        `seats`,
                        `current_lat`,
                        `current_lon`,
                        `destination`
                    ) VALUES (
                        \"".$this->passenger."\",
                        \"".$this->seats."\",
                        ".$this->currentLat.",
                        ".$this->currentLon.",
                        \"".$this->destination."\"
                    )");
        
       return $this->id = $db->getId();      
    }
    
    /**
     * Updates the passenger's current position
     * @param int $queryid The passenger's id
     * @return boolean  True, if data was updated successfully 
     */
    public function updatePosition($queryid) {
        if (!is_numeric($queryid) || $queryid < 1) {
            return false;
        }
        
        $db = Database::getInstance();
        $updated = $db->query("UPDATE `".DB_PREFIX."_query` 
                               SET `current_lat` = '".$this->currentLat."',
                                   `current_lon` = '".$this->currentLon."'
                               WHERE `id` = ".$queryid."
                               AND `passenger` = ".$this->passenger);
        return ($updated && $db->getAffectedRows() > 0);
    }
    
    /**
     * Updates the number of available seats in the passenger car
     * @param int $queryid The passenger's id
     * @return boolean  True, if data was updated successfully 
     */
    public function updateWantedSeats($queryid) {
        if (!is_numeric($queryid) || $queryid < 1) {
            return false;
        }
        
        $db = Database::getInstance();
        $updated = $db->query("UPDATE `".DB_PREFIX."_query` 
                               SET `seats` = '".$this->seats."'
                               WHERE `id` = ".$queryid."
                               AND `passenger".$this->passenger);
        return ($updated && $db->getAffectedRows() > 0);
    }
    
    public function getId() {
        return $this->id;
    }
    
    /**
     *
     * @return int 
     */
    public function getDriver() {
        return $this->passenger;
    }
    
    public function getWantedSeats() {
        return $this->seats;
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
           
    /**
     *
     * @param int $user 
     * @return boolean
     */
    public function setPassenger($user) {
        if (is_numeric($user) && $user > 0) {
            $this->passenger = $user;
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
    public function setWantedSeats($seats) {
        if (is_numeric($seats) && $seats >= 1 && $seats <= 99) {
            $this->seats = $seats;
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
