<?php
if (!defined("INCLUDE")) {
    exit;
}

/**
 * Gives access to rides that have been done by a driver or a passanger
 * @author Dang Huynh, Patrick Strobel 
 * @version 1.0.1
 */
class Ride {
    const CAN_RATE = 0;
    const ALREADY_RATED = 1;
    const NOT_ENDED = 2;
    const NO_CONNECTION = 3;

    /** @var User  */
    private $driver = null;

    /** @var boolean  */
    private $driverRated = false;

    /** @var User[]  */
    private $passengers = array();

    /** @var Trip */
    private $trip = null;

    private function __construct() {
        
    }

    /**
     * Loads a ride for a given trip
     * @param Trip $trip    Trip to load the ride for
     * @return Ride Loaded ride or null, if there are no rides for the given trip
     * @throws  InvalidArgumentException If the given trip is not an object of class Trip
     */
    public static function getRideByTrip($trip) {
        if (!($trip instanceof Trip)) {
            throw new InvalidArgumentException("Trip has to be an object of class Trip");
        }

        $db = Database::getInstance();
        $query = $db->query("SELECT 
                                r.*, r.`id` AS rid, 
                                pax.*, pax.`id` AS paxId
                             FROM 
                                `" . DB_PREFIX . "_ride` AS r, 
                                `" . DB_PREFIX . "_user` AS pax
                             WHERE r.`trip` = " . $trip->getId() . "
                             AND r.`passenger` = pax.`id`");

        // Build passenger array
        $ride = null;

        while (($row = $db->fetch($query)) != null) {
            if ($ride == null) {
                $ride = new Ride();
                $ride->driver = $trip->getDriver();
                $ride->trip = $trip;
            }

            $ride->passengers[] = User::loadUserBySqlResult($row, "paxId");
        }

        if ($ride == null) {
            return null;
        } else {
            return $ride;
        }
    }

    /**
     * Loads all rides by a given driver.
     * @param User $driver  Driver to load the rides for
     * @return Ride[]  Array storing all rides that have been done by the given user
     * @throws  InvalidArgumentException If the given driver is not an object of class User
     */
    public static function getRidesAsDriver($driver) {
        if (!($driver instanceof User)) {
            throw new InvalidArgumentException("Driver has to be an object of class User");
        }

        $db = Database::getInstance();
        $query = $db->query("SELECT 
                                r.*, r.`id` AS rid, 
                                t.*, t.`id` AS tid,
                                pax.*, pax.`id` AS paxId
                             FROM 
                                `" . DB_PREFIX . "_ride` AS r, 
                                `" . DB_PREFIX . "_trip` AS t, 
                                `" . DB_PREFIX . "_user` AS pax
                             WHERE r.`trip` = t.`id`
                             AND r.`passenger` = pax.`id`
                             AND t.`driver` = " . $driver->getId());

        // Stores all ride using the trip-id as index/key
        $rides = array();

        while (($row = $db->fetch($query)) != null) {
            // If this is a new ride -> create new ride-object
            if (!array_key_exists($row["tid"], $rides)) {
                $ride = new Ride();
                $ride->driver = $driver;
                $ride->driverRated = false;
                $ride->trip = Trip::loadTripBySqlResult($row, "tid", "paxId");

                $rides[(int) $row["tid"]] = $ride;
            }

            // Add passengers
            $ride = $rides[$row["tid"]];
            $passenger = User::loadUserBySqlResult($row, "paxId");
            $ride->passengers[] = $passenger;
        }



        return $rides;
    }

    /**
     * Loads all rides by a given passenger.
     * @param User $passenger  Passenger to load the rides for
     * @return Ride[]  Array storing all rides that have been done by the given user
     * @throws  InvalidArgumentException If the given passenger is not an object of class User
     */
    public static function getRidesAsPassenger($passenger) {
        if (!($passenger instanceof User)) {
            throw new InvalidArgumentException("Driver has to be an object of class User");
        }

        $db = Database::getInstance();
        
        $query = $db->query("SELECT
                                r.*, r.`id` AS rid, 
                                t.*, t.`id` AS tid,
                                pax.*, pax.`id` AS paxId,
                                driver.`id` AS driverId
                             FROM
                                `" . DB_PREFIX . "_ride` AS r, 
                                `" . DB_PREFIX . "_trip` AS t, 
                                `" . DB_PREFIX . "_user` AS pax,
                                `" . DB_PREFIX . "_user` AS driver
                             WHERE r.`trip` in (SELECT `trip` FROM `" . DB_PREFIX . "_ride` WHERE `passenger` = ". $passenger->getId() .")
                             AND r.`trip` = t.`id`
                             AND t.`driver` = driver.`id`
                             AND r.`passenger` = pax.`id`");

        $rides = array();

        // As there is only one ride per passenger, we can a ride-object per row
        while (($row = $db->fetch($query)) != null) {
             // If this is a new ride -> create new ride-object
            if (!array_key_exists($row["tid"], $rides)) {
                $ride = new Ride();
                $ride->driver = User::loadUser($row["driver"]);
                $ride->trip = Trip::loadTripBySqlResult($row, "tid", "paxId");

                $rides[(int) $row["tid"]] = $ride;
            }
            
            
            // Add passengers
            $ride = $rides[$row["tid"]];
            $passenger = User::loadUserBySqlResult($row, "paxId");
            $ride->passengers[] = $passenger;
        }

        return $rides;
    }

    /**
     * Search for drivers in the surrounding area within the given distance
     * @static
     * @param int $passenger_id ID of the requesting user
     * @param int $distance The maximal distance between the drivers and the requesting user in meter
     * @return array|null List of the eligible drivers
     */
    public static function getRidesByDistance($passenger_id, $distance) {
        $db = Database::getInstance();
        $query = $db->query("CALL list_driver($passenger_id, $distance);");
        $arr = null;
        $i = 0;
        while ($row = $db->fetch($query)) {
            $arr[$i++] = $row;
        }
        return $arr;
    }
    

    /**
     *
     * @param User $passenger 
     */
    public function isPassenger($passenger) {
        if (!($passenger instanceof User)) {
            throw new InvalidArgumentException("Passenger has to be an object of class User");
        }

        foreach ($this->passengers as $passengerToCheck) {
            if ($passenger->isEqual($passengerToCheck)) {
                return true;
            }
        }
    }
    
    /**
     * Returns the user that has been the driver on this ride
     * @return User The driver 
     */
    public function getDriver() {
        return $this->driver;
    }

    /**
     * Return the trip this ride belongs to
     * @return Trip The trip 
     */
    public function getTrip() {
        return $this->trip;
    }

    /**
     * Returns all users that have been passengers on this ride
     * @return Passenger[]    The passenger 
     */
    public function getPassengers() {
        return $this->passengers;
    }

	/**
	 * Mark an user as picked up
	 * @return true if succeed, false otherwise
	 */
	static function pick_up($user_id, $trip_id){
		$db = Database::getInstance();
		$query = $db->query("UPDATE `dev_ride` SET `picked_up`=1 where passenger =$user_id and trip=$trip_id");
		if($db->getAffectedRows()>0){
			return true;
		}else{
			return false;
		}
		
	}

	/**
	 * Get the users from ride table
	 * @return true if succeed, false otherwise
	 */
	static function offer_accepted($trip_id){
	   $db = Database::getInstance();
	   $query = $db->query("SELECT passenger,picked_up FROM dev_ride WHERE trip= $trip_id");
	   $return = null;
	   if($query){	   
	   	while($row=$db->fetch($query)) {
		   	$return[] = $row;
	   	   }
		
	   }
	   return $return;
	}
}

?>
