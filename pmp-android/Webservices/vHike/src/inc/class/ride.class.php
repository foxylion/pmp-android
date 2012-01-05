<?php
if (!defined("INCLUDE")) {
    exit;
}

/**
 * Stores data of a given passenger
 * @author Patrick Strobel 
 * @version 1.0.0
 */
class Passenger {
    
    /** @var Ride  */
    private $ride;

    /** @var User  */
    private $user;
    /** @var boolean  */
    private $rated;

    /**
     * @param User $user
     * @param boolean $rated 
     */
    public function __construct($user, $rated, $ride) {
        $this->ride = $ride;
        $this->user = $user;
        $this->rated = $rated;
    }

    /**
     * Returns the user that was the passenger on this ride
     * @return User The passenger 
     */
    public function getUser() {
        return $this->user;
    }

    /**
     * Returns if the passenger on this ride been rated.
     * @return boolean  True, if the passenger has been rated
     *                  This will always return false if this object was
     *                  created using Ride.getRidesAsPassenger() 
     */
    public function isRated() {
        return $this->rated;
    }
    
    public function markAsRated() {
        //echo "mark user";
        $db = Database::getInstance();
        $db->query("UPDATE `".DB_PREFIX."_ride` 
                    SET `passenger_rated` = 1
                    WHERE `trip` = ".$this->ride->getTrip()->getId()."
                    AND `passenger` = ".$this->user->getId());
    }

}

/**
 * Gives access to rides that have been done by a driver or a passanger
 * @author Dang Huynh, Patrick Strobel 
 * @version 1.0.0
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
    /** @var Passenger[]  */
    private $passengers = array();

    /** @var Trip */
    private $trip = null;
    
    private function __construct() { }
    
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
                $ride->driverRated = $row["driver_rated"];
                $ride->trip = $trip;
            }
            
            $ride->passengers[] = new Passenger(User::loadUserBySqlResult($row, "paxId"), (bool)$row["passenger_rated"], $ride);
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

                $rides[(int)$row["tid"]] = $ride;
            }

            // Add passengers
            $ride = $rides[$row["tid"]];
            $passenger = new Passenger(User::loadUserBySqlResult($row, "paxId"), (bool)$row["passenger_rated"], $ride);
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
                                driver.*, driver.`id` AS driverId
                             FROM
                                `" . DB_PREFIX . "_ride` AS r, 
                                `" . DB_PREFIX . "_trip` AS t, 
                                `" . DB_PREFIX . "_user` AS driver
                             WHERE r.`trip` = t.`id`
                             AND t.`driver` = driver.`id`
                             AND r.`passenger` = " . $passenger->getId());

        $rides = array();

        // As there is only one ride per passenger, we can a ride-object per row
        while (($row = $db->fetch($query)) != null) {
            $ride = new Ride();
            $ride->driver = User::loadUserBySqlResult($row, "driverId");
            $ride->driverRated = (bool) $row["driver_rated"];
            $ride->trip = Trip::loadTripBySqlResult($row, "tid", "driverId");
            $ride->passengers[] = new Passenger($passenger, false, $ride);

            $rides[(int)$row["tid"]] = $ride;
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
    public static function getRidesByDistance($passenger_id, $distance)
    {
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
     * Checks if the given rater is allowed to rate the given recipient.
     * @param User $rater   Person that want's to rate
     * @param User $recipient Person that should receive the ratin
     * @param Trip $trip    Trip, this rating belongs to
     * @return enum  CAN_RATE, if the rater has participated on the recipients trip,
     *                  this ride has ended and if the rater hasn't rated the recipient yet.
     *                  Otherwise this will return ALREADY_RATED, NOT_ENDED or NO_CONNECTION.
     *                  If one parameter is NULL or rater and receipient are equal, this will return NO_CONNECTION
     * @throws  InvalidArgumentException If one argument is of invalid type
     */
    public static function canRate($rater, $recipient, $trip) {
        if (!($trip instanceof Trip)) {
            throw new InvalidArgumentException("Trip has to be an object of class Trip");            
        }
        
        if (!$trip->hasEnded()) {
            return self::NOT_ENDED;
        }
        
        if ($rater == null || $recipient == null || $trip == null ||
                $rater->isEqual($recipient)) {
            return self::NO_CONNECTION;
        }
        
        $ride = self::getRideByTrip($trip);
        
        if ($ride == null) {
            return self::NO_CONNECTION;
        }
        
        //echo "loaded";
        
        // If rater was the driver on this trip, check all passenger
        if ($ride->getDriver()->isEqual($rater)) {
            
            // Search passenger
            foreach ($ride->getPassengers() as $passenger) {
                if ($passenger->getUser()->isEqual($recipient)) {
                    if ($passenger->isRated()) {
                        return self::ALREADY_RATED;
                    } else {
                        return self::CAN_RATE;
                    }                    
                }
            }
            
            return self::NO_CONNECTION;
        }
        
        // Check if the rater was a passenger on this trip
        $isPassenger = false;
        foreach ($ride->getPassengers() as $passenger) {
            if ($passenger->getUser()->isEqual($rater)) {
                $isPassenger = true;
            }
        }
        
        // Rater was a passenger
        if ($isPassenger) {
            if ($ride->isDriverRated()) {
                return self::ALREADY_RATED;
            } else {
                return self::CAN_RATE;
            }
        }
        
        return self::NO_CONNECTION;
        
    }
    
    /**
     * Marks a user as rated.
     * @param User $rater   Person that want's to rate
     * @param User $recipient Person that should receive the ratin
     * @param Trip $trip    Trip, this rating belongs to
     * @throws  InvalidArgumentException If one argument is of invalid type
     */
    public static function markAsRated($rater, $recipient, $trip) {
        if (!($rater instanceof User) || !($recipient instanceof User) ||!($trip instanceof Trip)) {
            throw new InvalidArgumentException("Trip or user is of invalid type");            
        }
        
        $ride = Ride::getRideByTrip($trip);
        
        if ($ride->getDriver()->isEqual($recipient)) {
            // Is user is the driver, rate the driver
            $ride->markDriverAsRated($rater);
        } else {
            foreach ($ride->getPassengers() as $passenger) {
                // Loop through all passengers and rate the passenger if found
                if ($passenger->getUser()->isEqual($recipient)) {
                    $passenger->markAsRated();
                    break;
                }
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
     * Returns if the driver on this ride been rated.
     * @return boolean  True, if the driver has been rated
     *                  This will always return false if this object was
     *                  created using getRidesAsDriver()
     */
    public function isDriverRated() {
        return $this->driverRated;
    }
    
    /**
     * Marks the driver as rated
     * @param User $rater User how has rated this user
     */
    private function markDriverAsRated($rater) {
        //echo "mark driver";
        $db = Database::getInstance();
        $db->query("UPDATE `".DB_PREFIX."_ride` 
                    SET `driver_rated` = 1
                    WHERE `trip` = ".$this->trip->getId()."
                    AND `passenger` = ".$rater->getId());
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
    
    

}

?>
