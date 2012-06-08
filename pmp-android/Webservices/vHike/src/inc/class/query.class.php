<?php
if (!defined('INCLUDE')) {
	exit;
}

/**
 * Allows to create a new query and gives access to existing queries
 * @author Dang Huynh, Patrick Strobel
 * @version 1.0.1
 */
class Query
{
	private $id = -1;
	private $passenger = -1;
	private $seats = 0;
	//    private $currentLat = 0.0;
	//    private $currentLon = 0.0;
	private $destination = NULL;

	/**
	 * Loads a query from the database and returns a query-object storing the information
	 * of the loaded query
	 * @param int $id  ID of the query to load from the database
	 * @return Query Object storing data of the loaded query or null, if trip with the
	 *              given id does not exists or parameter id is not numeric
	 * @throws InvalidArgumentException Thrown if the queries id is invalid
	 */
	public static function loadQuery($id)
	{
		if (!General::validId($id)) {
			throw new InvalidArgumentException("The query-id is not valid.");
		}

		$query = new Query();
		return $query->fillAttributes("SELECT * FROM `" . DB_PREFIX . "_query` WHERE `id` = $id");
	}

	/**
	 * Search for ride requests in the surrounding area within the given distance
	 * @static
	 * @param int $driver_id ID of the driver
	 * @param int $distance The maximal distance between the driver and the hikers in meter
	 * @return array|null List of the eligible requests
	 */
	public static function searchQuery($driver_id, $distance)
	{
		$db = Database::getInstance();
		$query = $db->query("CALL list_hiker($driver_id, $distance);");
		$arr = NULL;
		$i = 0;
		while ($row = $db->fetch($query)) {
			$arr[$i++] = $row;
		}
		return $arr;
	}

	private function fillAttributes($sqlQuery)
	{
		$db = Database::getInstance();
		$row = $db->fetch($db->query($sqlQuery));

		if ($row["id"] == NULL) {
			return NULL;
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
	public function create()
	{
		// Cancel if important information is missing
		if ($this->seats <= 0 || $this->destination == NULL || $this->passenger <= 0) {
			throw new InputException("Some mandatory fields not set.");
		}

		// TODO: Check if another requests already existed

		// Write data into table
		$db = Database::getInstance();

		$db->query("INSERT INTO `" . DB_PREFIX . "_query` (
`passenger`,
`seats`,
`destination`
) VALUES (
\"" . $this->passenger . "\",
\"" . $this->seats . "\",
\"" . $this->destination . "\"
)");

		return $this->id = $db->getId();
	}

	/**
	 * Updates the passenger's current position
	 * @param int $queryid The passenger's id
	 * @return boolean  True, if data was updated successfully
	 * @deprecated
	 */
	//    public function updatePosition($queryid) {
	//        if (!is_numeric($queryid) || $queryid < 1) {
	//            return false;
	//        }
	//
	//        $db = Database::getInstance();
	//        $updated = $db->query("UPDATE `".DB_PREFIX."_query`
	//                               SET `current_lat` = '".$this->currentLat."',
	//                                   `current_lon` = '".$this->currentLon."'
	//                               WHERE `id` = ".$queryid."
	//                               AND `passenger` = ".$this->passenger);
	//        return ($updated && $db->getAffectedRows() > 0);
	//    }

	/**
	 * Updates the number of available seats in the passenger car
	 * @param int $queryid The passenger's id
	 * @return boolean  True, if data was updated successfully
	 */
	public function updateWantedSeats($wantedSeats, $queryid)
	{
		if (!is_numeric($wantedSeats) || $wantedSeats < 1) {
			return FALSE;
		}

		$db = Database::getInstance();
		$updated = $db->query("UPDATE `" . DB_PREFIX . "_query` SET `seats` = " . $wantedSeats . " WHERE `id` = " . $queryid . " AND `passenger` = " . $this->passenger);
		return ($updated && $db->getAffectedRows() > 0);
	}

	public function getId()
	{
		return $this->id;
	}

	/**
	 *
	 * @return int
	 */
	public function getPassengerId()
	{
		return $this->passenger;
	}

	/**
	 *
	 * @return User
	 */
	public function getPassenger()
	{
		return User::loadUser($this->passenger);
	}

	public function getWantedSeats()
	{
		return $this->seats;
	}

	//    public function getCurrentLat() {
	//        return $this->currentLat;
	//    }
	//
	//    public function getCurrentLon() {
	//        return $this->currentLon;
	//    }

	public function getDestination()
	{
		return $this->destination;
	}

	/**
	 *
	 * @param int $user
	 * @return boolean
	 */
	public function setPassenger($user)
	{
		if (is_numeric($user) && $user > 0) {
			$this->passenger = $user;
			return TRUE;
		} else {
			return FALSE;
		}
	}

	/**
	 *
	 * @param int $seats
	 * @return boolean
	 */
	public function setWantedSeats($seats)
	{
		if (is_numeric($seats) && $seats >= 1 && $seats <= 99) {
			$this->seats = $seats;
			return TRUE;
		} else {
			return FALSE;
		}
	}

	/**
	 *
	 * @param float $lat
	 * @return boolean
	 */
	public function setCurrentLat($lat)
	{
		if (is_numeric($lat)) {
			$this->currentLat = $lat;
			return TRUE;
		} else {
			return FALSE;
		}
	}

	/**
	 *
	 * @param float $lon
	 * @return boolean
	 */
	public function setCurrentLon($lon)
	{
		if (is_numeric($lon)) {
			$this->currentLon = $lon;
			return TRUE;
		} else {
			return FALSE;
		}
	}

	public function setDestination($destination)
	{
		$destination = Database::getInstance()->secureInput($destination);
		if (General::validLength($destination)) {
			$this->destination = $destination;
			return TRUE;
		} else {
			return FALSE;
		}
	}

}
