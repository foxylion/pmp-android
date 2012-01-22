<?php
if (!defined("INCLUDE")) {
    exit;
}


/**
 * This class gives access to the position data of a user and provides functionality
 * to change the current position of a user
 *
 * @author Patrick Strobel
 * @version 1.0.0
 */
class position {
    
    /** @var int */
    private $id = -1;
    /** @var float */
    private $latitude;
    /** @var float */
    private $longitude;
    /** 
     * Timestamp represting the point in time of the latest updated of the positioning data
     * @var int
     */
    private $lastUpdate;
    
    /**
     * Load the positioning data for a given user.
     * @param User $user    User, for how the position should be loaded
     * @return Position The positioning data of the user"
     * @throws  InvalidArgumentException Thrown, if user is not a object of class "User"
     */
    public static function loadPosition($user) {
        if (!($user instanceof User)) {
            throw new InvalidArgumentException("The user-parameter has to be an user-object.");
        }
        
        $db = Database::getInstance();
        $row = $db->fetch($db->query("SELECT 
                                        `id`,
                                        `latitude`,
                                        `longitude`,
                                        unix_timesamp(`last_update`) AS `last_update_ts`
                                      FROM ".DB_PREFIX."_position 
                                      WHERE `user` = ".$user->getId()));
        
        if($db->getAffectedRows() <= 0) {
            return null;
        }
        
        self::loadPositionBySqlResult($row);
        
    }
    
    /**
     * Creates a position from a given sql-result array.
     * @param Array $result Array storing the information of the position.
     *                      This has to be an array where the key representes
     *                      the tables name.
     * @param String $idFieldName Specifies the name of the id-field. Used when
     *                          the id field name is changed by SQL's "AS" statement
     * @return Position Position-object storing the information from the given result-array
     * @internal    This is for internal use only as this function could be used to
     *              create a position-object from a non existing database entry!
     * @throws InvalidArgumentException Thrown, if on of the arguments is invalid
     */
    public static function loadPositionBySqlResult($result, $idFieldName = "id") {
        if (!is_array($result) || $idFieldName == null || $idFieldName == "" ||
            $result[$idFieldName] == null) {
            throw new InvalidArgumentException("Result or ifFieldName is invalid");
        }
        
        $pos = new Position();
        $pos->id = $row["id"];
        $pos->latitude = $row["latitude"];
        $pos->longitude = $row["longitude"];
        $pos->lastUpdate = $row["last_update_ts"];
        
        return $pos;
    }
    
    /**
     * Inserts a new dataset into the db to store the position of the given user.
     * If there is already a dataset for the given user, this methode won't insert
     * an entry into the db
     * @param User $user 
     * @throws  InvalidArgumentException Thrown, if user is not a object of class "User"
     */
    public static function createDataset($user) {
        if (!($user instanceof User)) {
            throw new InvalidArgumentException("The user-parameter has to be an user-object.");
        }
        
        $db = Database::getInstance();
        
        
        $db->query("START TRANSACTION");
        
        // Check if there's already an entry for the given user
        $db->query("SELECT * FROM ".DB_PREFIX."_position WHERE `user` = ".$user->getId());
        
        // Insert a dataset if there is no entry 
        if ($db->getAffectedRows() <= 0) {
            $db->query("INSERT INTO `" . DB_PREFIX . "_position` (
                            `user`
                        ) VALUES (
                            " . $user->getId() . "
                        )");
        }
        
        $db->query("COMMIT");
    }
    
    /**
     * Updates the position of the user this positioning data belongs to
     * @param float $latitude   The new latitude
     * @param float $longitude  The new longitude
     * @return boolean  True, if the dataset was updated successfully
     * @throws InvalidArgumentException Thrown, if latitude or longitude has an invalid format
     */
    public function updatePosition($latitude, $longitude) {
        if (!General::validLatitude($latitude) || !General::validLongitude($longitude)) {
            throw new InvalidArgumentException("Latitude or longitude has an invalid format or range");
        }
        
        $db = Database::getInstance();
        $updated = $db->query("UPDATE `" . DB_PREFIX . "_position`
                               SET `latitude` = '" . $latitude . "',
                                   `longitude` = '" . $longitude . "',
                                   `last_update` = from_unixtime(" . time() . ")                                       
                               WHERE `id` = " . $this->id);

        $this->latitude = $latitude;
        $this->longitude = $longitude;

        return ($updated && $db->getAffectedRows() > 0);
        
    }
    
    /**
     * Returns this users latest latitude
     * @return float    The latitude 
     */
    public function getLatitude() {
        return $this->latitude;
    }
    
    /**
     * Returns this users latest longitude
     * @return float    The longitude 
     */
    public function getLongitude() {
        return $this->longitude;
    }
    
    /**
     * Return the point in time on which the positioning data has been
     * updated the last time
     * @return int  The unix timesamp 
     */
    public function getLastUpdateTimesamp() {
        return $this->lastUpdate;
    }
}
?>
