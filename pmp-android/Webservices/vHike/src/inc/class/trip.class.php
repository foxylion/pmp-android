<?php
if (!defined("INCLUDE")) {
    exit;
}

class Trip {
    const OPEN_TRIP_EXISTS = 1;

    private $id = -1;
    /** @var User */
    private $driver = null;
    private $availSeats = 0;
    private $currentLat = 0.0;
    private $currentLon = 0.0;
    private $destination = null;
    private $creation = null;
    private $ending = 0;

    private function __construct() {
    }

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
        $row = $db->fetch($db->query("SELECT 
                                        t.*, t.`id` AS tid,
                                        u.*, u.`id` AS uid
                                      FROM 
                                        `" . DB_PREFIX . "_trip` AS t,
                                        `" . DB_PREFIX . "_user` AS u
                                      WHERE t.`driver` = u.`id`
                                      AND t.`id` = " . $id));

        if ($db->getAffectedRows() <= 0) {
            return null;
        }

        return self::loadTripBySqlResult($row, "tid", "uid");

    }

    /**
     * Creates a trip from a given sql-result array.
     * This has to include the user-data of the driver!
     * @param Array $result Array storing the information of the trip
     *                      This has to be an array where the key representes
     *                      the tables name.
     * @param String $idFieldName Specifies the name of the id-field. Used when
     *                          the id field name is changed by SQL's "AS" statement
     * @param String $userIdFieldName   Specifies the name of the user's id-field.
     * @return Trip Trip-object storing the information from the given result-array
     * @internal    This is for internal use only as this function could be used to
     *              create a trip-object from a non existing database entry!
     * @throws InvalidArgumentException Thrown, if on of the arguments is invalid
     */
    public static function loadTripBySqlResult($result, $idFieldName = "id", $userIdFieldName = "uid") {
        if (!is_array($result) || $idFieldName == null || $idFieldName == "" ||
            $result[$idFieldName] == null
        ) {
            throw new InvalidArgumentException("Result or ifFieldName is invalid");
        }

        $trip = new Trip();

        $trip->id = (int)$result[$idFieldName];
        $trip->driver = User::loadUserBySqlResult($result, "uid");
        $trip->availSeats = $result["avail_seats"];
        $trip->currentLat = $result["current_lat"];
        $trip->currentLon = $result["current_lon"];
        $trip->destination = $result["destination"];
        $trip->creation = $result["creation"];
        $trip->ending = $result["ending"];

        return $trip;

    }


    /**
     * Creates a new trip using
     * @param User $driver
     * @param int $availSeats
     * @param float $current_lat
     * @param float $current_lon
     * @param String $destination
     * @return Trip The created trip
     * @throws InvalidArgumentException Thrown, if input data is invalid
     */
    public static function create($driver, $availSeats, $currentLat, $currentLon, $destination) {
        // Cancel if important information is missing
        if (!($driver instanceof User) || $availSeats <= 0 || $availSeats >= 100 ||
            !is_numeric($currentLat) || !is_numeric($currentLon) || !General::validLength($destination)
        ) {
            throw new InvalidArgumentException("At least one parameter is of wrong type or format");
        }

        // Cancel if theres already an opend trip
        if (self::openTripExists($driver)) {
            throw new InvalidArgumentException("At least one parameter is of wrong type or format", self::OPEN_TRIP_EXISTS);
        }

        // Write data into table
        $db = Database::getInstance();
        $creation = Date(Database::DATE_FORMAT, time());

        $db->query("INSERT INTO `" . DB_PREFIX . "_trip` (
                        `driver`,
                        `avail_seats`,
                        `current_lat`,
                        `current_lon`,
                        `destination`,
                        `creation`
                    ) VALUES (
                        \"" . $driver->getId() . "\",
                        \"" . $availSeats . "\",
                        \"" . $currentLat . "\",
                        \"" . $currentLon . "\",
                        \"" . $destination . "\",
                        \"" . $creation . "\"
                    )");

        $trip = new Trip();
        $trip->id = $db->getId();
        $trip->driver = $driver;
        $trip->availSeats = $availSeats;
        $trip->currentLat = $currentLat;
        $trip->currentLon = $currentLon;
        $trip->destination = $destination;
        $trip->creation = $creation;

        return $trip;
    }

    /**
     * Updates the driver's current position
     * @param float $currentLat
     * @param float $currentLon
     * @return boolean  True, if data was updated successfully
     */
    public function updatePosition($currentLat, $currentLon) {
        if (!is_numeric($currentLat) || !is_numeric($currentLon)) {
            throw new InvalidArgumentException("Lat or lon is not a float");
        }

        $db = Database::getInstance();
        $updated = $db->query("UPDATE `" . DB_PREFIX . "_trip`
                               SET `current_lat` = '" . $currentLat . "',
                                   `current_lon` = '" . $currentLon . "'
                               WHERE `id` = " . $this->id . "
                               AND `driver` = " . $this->driver->getId() . "
                               AND `ending` = 0");

        $this->currentLat = $currentLat;
        $this->currentLon = $currentLon;

        return ($updated && $db->getAffectedRows() > 0);
    }

    /**
     * Updates the number of available seats in the driver car
     * @param int $tripid The driver's id
     * @return boolean  True, if data was updated successfully
     */
    public function updateData($availSeats) {
        if (!is_numeric($availSeats) || $availSeats <= 0 || $availSeats >= 100) {
            throw new InvalidArgumentException("AvailSeats has to be numeric and between 1 and 99");
        }

        $db = Database::getInstance();
        $updated = $db->query("UPDATE `" . DB_PREFIX . "_trip`
                               SET `avail_seats` = '" . $availSeats . "'
                               WHERE `id` = " . $this->id . "
                               AND `driver` = " . $this->driver->getId() . "
                               AND `ending` = 0");

        $this->availSeats = $availSeats;

        return ($updated && $db->getAffectedRows() > 0);
    }

    /**
     * Ends/closes the trip so that rating is now activated
     * @param int $tripid The driver's id
     * @return boolean  True, if data was updated successfully
     */
    public function endTrip() {
        $ending = Date(Database::DATE_FORMAT, time());
        $db = Database::getInstance();
        $updated = $db->query("UPDATE `" . DB_PREFIX . "_trip`
                               SET `ending` = '" . $ending . "'
                               WHERE `id` = " . $this->id . "
                               AND `driver` = " . $this->driver->getId() . "
                               AND `ending` = 0");

        $this->ending = $ending;
        return ($updated && $db->getAffectedRows() > 0);
    }

    /**
     * Checks if there is already an opened trip for the given user
     * @param User $driver
     * @return type
     */
    public static function openTripExists($driver) {
        if (!($driver instanceof User)) {
            throw new InvalidArgumentException("Driver has to be of class User");
        }

        $db = Database::getInstance();
        $count = $db->query("SELECT * FROM `" . DB_PREFIX . "_trip`
                             WHERE `driver` = " . $driver->getId() . "
                             AND `ending` = 0");

        return $db->getAffectedRows() > 0;

    }

    public static function getPassengersFromTrip($tripid) {
        $db = Database::getInstance();

        $query = $db->query("SELECT dev_ride.passenger AS passengerid, dev_user.username, dev_user.rating_avg, " .
                            "dev_user.rating_num, dev_ride.passenger_rated, dev_ride.driver_rated " .
                            "FROM dev_ride INNER JOIN dev_user ON dev_ride.passenger = dev_user.id " .
                            "WHERE trip=$tripid");
        $arr = null;
        $i = 0;
        while ($row = $db->fetch($query)) {
            $arr[$i++] = $row;
        }
        return $arr;
    }

    public static function getPassengersFromRide($tripid, $userid) {
        $db = Database::getInstance();
        // Get passengers from trip except one's own info
        $query = $db->query("SELECT dev_ride.passenger AS passengerid, dev_user.username, dev_user.rating_avg, " .
            "dev_user.rating_num, dev_ride.passenger_rated, dev_ride.driver_rated " .
            "FROM dev_ride INNER JOIN dev_user ON dev_ride.passenger = dev_user.id " .
            "WHERE trip=$tripid AND passenger!=$userid");

        $arr = null;
        $i = 0;
        while ($row = $db->fetch($query)) {
            $arr[$i++] = $row;
        }
        return $arr;
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
     * Returns if this trip has ended
     * @return boolean True, if trip has ended
     */
    public function hasEnded() {
        return !($this->ending == Database::DATA_NULL);
    }
}

?>
