<?php
if (!defined("INCLUDE")) {
    exit;
}

class Query {
    private $id = -1;
    private $passenger = -1;
    private $seats = 0;
    private $currentLat = 0.0;
    private $currentLon = 0.0;
    private $destination = null;
    
    /**
     * Loads a query from the database and returns a query-object storing the information
     * of the loaded query
     * @param int $id  ID of the query to load from the database
     * @return Query Object storing data of the loaded query or null, if trip with the
     *              given id does not exists or parameter id is not numeric 
     * @throws InvalidArgumentException Thrown if the queries id is invalid
     */
    public static function loadQuery($id) {
        if (!General::validId($id)) {
           throw new InvalidArgumentException("The query-id is not valid.");
        }
        
        $query = new Query();
        return $query->fillAttributes("SELECT * FROM `".DB_PREFIX."_query` WHERE `id` = $id");
    }

    public static function searchQuery($driver_id)
    {
        $db = Database::getInstance();
        echo "SELECT query.id as queryid, passenger as userid, username, rating_avg as rating, current_lat as lat, current_lon as lon, seats " .
            "FROM " . DB_PREFIX . "_query query, " . DB_PREFIX . "_user user ".
            "WHERE user.id=passenger AND query.destination=(SELECT destination FROM " . DB_PREFIX . "_trip WHERE driver=$driver_id LIMIT 1) AND passenger!=$driver_id ".
            "ORDER BY rating LIMIT 0, 30";

        $result = $db->query("SELECT query.id as queryid, passenger as userid, username, rating_avg as rating, current_lat as lat, current_lon as lon, seats " .
            "FROM " . DB_PREFIX . "_query query, " . DB_PREFIX . "_user user ".
            "WHERE user.id=passenger AND query.destination=(SELECT destination FROM " . DB_PREFIX . "_trip WHERE driver=$driver_id LIMIT 1) AND passenger!=$driver_id ".
            "ORDER BY rating LIMIT 0, 30");
        $arr = null;
        $i = 0;
        while ($row = $db->fetch($result)) {
            $arr[$i++] = $row;
        }
        return $arr;
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
     * Creates a new query using the data set with the setX()-methods
     * @return int ID of the new query
     * @throws InputException Thrown, if an important field (e.g. destination) is missing
     */
    public function create() {
        // Cancel if important information is missing
        if ($this->seats <= 0 || $this->destination == null || 
                $this->passenger <= 0) {
            throw new InputException("Some mandatory fields not set.");
        }

        // TODO: Check if another requests already existed
        
        // Write data into table
        $db = Database::getInstance();
        $creation = Date(Database::DATE_FORMAT, time());

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
    public function getPassenger() {
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
