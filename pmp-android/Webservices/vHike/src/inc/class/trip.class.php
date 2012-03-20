<?php
if (!defined('INCLUDE')) {
	exit;
}

/**
 * Allows to create a new trip and gives access to existing trips and their data
 * @author  Dang Huynh, Patrick Strobel
 * @version 1.0.1
 */
class Trip {
	const OPEN_TRIP_EXISTS = 1;

	private $id = -1;
	/** @var User */
	private $driver = null;
	private $availSeats = 0;
	private $destination = null;
	private $creation = null;
	private $ending = 0;

	private function __construct() {
	}

	/**
	 * Loads a trip from the database and returns a trip-object storing the information
	 * of the loaded trip
	 *
	 * @param int $id  ID of the user to load from the database
	 *
	 * @return Trip Object storing data of the loaded trip or null, if trip with the
	 *			  given id does not exists or parameter id is not numeric
	 * @throws InvalidArgumentException Thrown, if the trip's id is invalid
	 */
	public static function loadTrip($id) {
		if (!General::validId($id)) {
			throw new InvalidArgumentException("The trip-ID is not valid.");
		}

		$db = Database::getInstance();
		$row = $db->fetch($db->query("SELECT
                                        t.*, t.`id` AS tid,
                                        u.*, u.`id` AS uid
                                      FROM `" . DB_PREFIX . "_trip` AS t
                                      INNER JOIN `" . DB_PREFIX . "_user` AS u ON t.`driver` = u.`id`
                                      AND t.`id` = " . $id));
		if ($db->getAffectedRows() <= 0) {
			return null;
		}
		return self::loadTripBySqlResult($row, "tid", "uid");
	}

	/**
	 * Creates a trip from a given sql-result array.
	 * This has to include the user-data of the driver!
	 *
	 * @param Array  $result			Array storing the information of the trip
	 *								  This has to be an array where the key represents
	 *								  the tables name.
	 * @param String $idFieldName	   Specifies the name of the id-field. Used when
	 *								  the id field name is changed by SQL's "AS" statement
	 * @param String $userIdFieldName   Specifies the name of the user's id-field.
	 *
	 * @return Trip Trip-object storing the information from the given result-array
	 * @internal	This is for internal use only as this function could be used to
	 *			  create a trip-object from a non existing database entry!
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
		$trip->driver = User::loadUserBySqlResult($result, $userIdFieldName);
		$trip->availSeats = $result["avail_seats"];
		//		$trip->currentLat = $result["current_lat"];
		//		$trip->currentLon = $result["current_lon"];
		//		$trip->lastUpdate = $result["last_update"];
		$trip->destination = $result["destination"];
		$trip->creation = $result["creation"];
		$trip->ending = $result["ending"];

		return $trip;
	}


	/**
	 * Creates a new trip using
	 *
	 * @param User             $driver
	 * @param int              $availSeats
	 * @param String           $destination
	 *
	 * @param null|int|float   $date
	 *
	 * @internal param float $current_lat
	 * @internal param float $current_lon
	 * @return Trip The created trip
	 * @throws InvalidArgumentException Thrown, if input data is invalid
	 */
	public static function create($driver, $availSeats, $destination, $date = null) {
		// Cancel if important information is missing
		if (!($driver instanceof User) || $availSeats <= 0 || $availSeats >= 100 || !General::validLength($destination)) {
			throw new InvalidArgumentException("At least one parameter is of wrong type or format");
		}

		// Cancel if there's already an opened trip
		if (self::openTripExists($driver) && $date == null) {
			throw new InvalidArgumentException("At least one parameter is of wrong type or format", self::OPEN_TRIP_EXISTS);
		}

		if ($date == null) {
			$started = 1;
			$d = 'NOW()';
		} else {
			$started = 0;
			$d = 'FROM_UNIXTIME(' . $date / 1000 . ')';
		}

		// Write data into table
		$db = Database::getInstance();

		$db->query("INSERT INTO `" . DB_PREFIX . "_trip` (
                        `driver`,
                        `avail_seats`,
                        `destination`,
                        `creation`,
                        `started`
                    ) VALUES (
                        \"" . $driver->getId() . "\",
                        \"" . $availSeats . "\",
                        \"" . $destination . "\",
                        $d,
                        $started
                    )");

		$trip = new Trip();
		$trip->id = $db->getId();
		$trip->driver = $driver;
		$trip->availSeats = $availSeats;
		$trip->destination = $destination;
		$trip->creation = Date(Database::DATE_TIME_FORMAT, time());

		return $trip;
	}

	/**
	 * Updates the driver's current position
	 *
	 * @param float $currentLat
	 * @param float $currentLon
	 *
	 * @return boolean  True, if data was updated successfully
	 * @deprecated
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

		//		$this->currentLat = $currentLat;
		//		$this->currentLon = $currentLon;

		return ($updated && $db->getAffectedRows() > 0);
	}

	/**
	 * Updates the number of available seats in the driver car
	 *
	 * @param $availSeats
	 *
	 * @internal param int $tripid The driver's id
	 *
	 * @return boolean  True, if data was updated successfully
	 */
	public function updateData($availSeats) {
		if (!is_numeric($availSeats) || $availSeats < 0 || $availSeats > 100) {
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
	 * Ends an active trip
	 *
	 * @param int $driver_id ID of the driver
	 * @param int $trip_id
	 *
	 * @return string Status of the action
	 */
	public static function endTrip($driver_id = -1, $trip_id = -1) {
		if ($driver_id == -1) {
			return 'invalid_user';
		}

		$query = "UPDATE `" . DB_PREFIX . "_trip`
				  SET `ending` = NOW()
                  WHERE `driver` = $driver_id
                  AND `ending` = 0";
		if ($trip_id > -1) {
			$query .= "\nAND `id` = $trip_id";
		}

		$db = Database::getInstance();
		$db->query($query);
		if ($db->getAffectedRows() == 0) {
			return 'nothing_to_update';
		} else {
			return 'trip_ended';
		}
	}

	/**
	 * Checks if there is already an opened trip for the given user
	 *
	 * @param User $driver
	 *
	 * @return type
	 */
	public static function openTripExists($driver) {
		if (!($driver instanceof User)) {
			throw new InvalidArgumentException("Driver has to be of class User");
		}

		$db = Database::getInstance();
		$res = $db->query("SELECT * FROM `" . DB_PREFIX . "_trip`
                             WHERE `driver` = " . $driver->getId() . "
                             AND `ending` = 0");

		return $db->getNumRows($res) > 0;

	}

//	public static function getPassengersFromTrip($tripid) {
//		$db = Database::getInstance();
//		$query = $db->query("SELECT ride.passenger AS passengerid, user.username, " .
//								"AVG(rate.rating) as rating_avg, COUNT(rate.rating) as rating_num " .
//								"FROM " . DB_PREFIX . "_ride AS ride " .
//								"INNER JOIN " . DB_PREFIX . "_user AS user ON ride.passenger=user.id " .
//								"INNER JOIN " . DB_PREFIX . "_rating AS rate ON ride.passenger = rate.recipient " .
//								"WHERE ride.trip=$tripid");
//
//		$arr = null;
//		for ($i = 0; $i < $db->getAffectedRows(); $i++) {
//			$row = $db->fetch($query);
//			if ($row["passengerid"] != null) {
//				$arr[$i] = $row;
//			}
//		}
//		return $arr;
//	}

//	public static function getPassengersFromRide($tripid, $userid) {
//		$db = Database::getInstance();
//		// Get passengers from trip except one's own info
//		$query = $db->query("SELECT " . DB_PREFIX . "_ride.passenger AS passengerid, " . DB_PREFIX . "_user.username, " . DB_PREFIX . "_user.rating_avg, " .
//								"" . DB_PREFIX . "_user.rating_num, " . DB_PREFIX . "_ride.passenger_rated, " . DB_PREFIX . "_ride.driver_rated " .
//								"FROM " . DB_PREFIX . "_ride INNER JOIN " . DB_PREFIX . "_user ON " . DB_PREFIX . "_ride.passenger = " . DB_PREFIX . "_user.id " .
//								"WHERE trip=$tripid AND passenger!=$userid");
//
//		$arr = null;
//		$i = 0;
//		while ($row = $db->fetch($query)) {
//			$arr[$i++] = $row;
//		}
//		return $arr;
//	}

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

//	public function getCurrentLat() {
//		return $this->currentLat;
//	}

//	public function getCurrentLon() {
//		return $this->currentLon;
//	}

//	public function getLastUpdate() {
//		return $this->lastUpdate;
//	}

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

	public static function get_open_trip($user_id) {
		$db = Database::getInstance();

		$result = $db->query("SELECT " . DB_PREFIX . "_trip.id,\n" .
								 DB_PREFIX . "_trip.driver,\n" .
								 DB_PREFIX . "_trip.avail_seats,\n" .
								 DB_PREFIX . "_trip.destination,\n" .
								 "UNIX_TIMESTAMP(" . DB_PREFIX . "_trip.creation) AS creation " .
								 "FROM `" . DB_PREFIX . "_trip` " .
								 "WHERE `driver` = $user_id AND `ending` = 0 LIMIT 1");
		if ($db->getNumRows($result) > 0) {
			return $db->fetch($result);
		} else {
			return NULL;
		}
	}
}

// EOF trip.class.php