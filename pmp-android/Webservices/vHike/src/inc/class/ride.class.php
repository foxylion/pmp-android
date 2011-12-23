<?php
if (!defined("INCLUDE")) {
    exit;
}

class Passenger {

    /** @var User  */
    private $user;
    /** @var boolean  */
    private $rated;

    /**
     * @param User $user
     * @param boolean $rated 
     */
    public function __construct($user, $rated) {
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

}

class Ride {

    /** @var User  */
    private $driver = null;
    /** @var boolean  */
    private $driverRated = false;
    /** @var Passenger[]  */
    private $passengers = array();

    /** @var Trip */
    private $trip = null;

    /**
     * Loads all rides by a given driver.
     * @param User $driver  Driver to load the rides for
     * @return Ride[]  Array storing all rides that have been done by the given user
     * @throws  InvalidArgumentException If the given driver is not an object of class User
     */
    public function getRidesAsDriver($driver) {
        if (!($driver instanceof User)) {
            throw new InvalidArgumentException("Driver has to be an object of class User");
        }

        $db = Database::getInstance();
        $query = $db->query("SELECT 
                                r.*, r.`id` AS rid, 
                                t.*, t.`id` AS tid,
                                pax.*, pax.`id` AS paxId
                             FROM 
                                `" . DB_PREFIX . "_rides` AS r, 
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
                $ride->trip = Trip::loadTripBySqlResult($row, "tid");

                $rides[$row["tid"]] = $ride;
            }

            // Add passengers
            $ride = $rides["tid"];
            $passenger = new Passenger(User::loadUserBySqlResult($row, "paxId"), $row["passenger_rated"]);
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
        $query = $db->query("SELECT *
                             FROM
                                `" . DB_PREFIX . "_rides` AS r, 
                                `" . DB_PREFIX . "_trip` AS t, 
                                `" . DB_PREFIX . "_user` AS driver
                             WHERE r.`trip` = t.`ìd`
                             AND t.`driver` = driver.`id`
                             AND r.`passenger` = " . $passenger->getId());

        $rides = array();

        // As there is only one ride per passenger, we can a ride-object per row
        while (($row = $db->fetch($query)) != null) {
            $ride = new Ride();
            $ride->driver = User::loadUserBySqlResult($row, "driverId");
            $ride->driverRated = (bool) $row["driver_rated"];
            $ride->trip = Trip::loadTripBySqlResult($row, "tid");
            $ride->passengers[] = new Passenger($passenger, false);

            $rides[$row["tid"]] = $ride;
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
