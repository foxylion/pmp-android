<?php
if (!defined("INCLUDE")) {
	exit;
}

class DatabaseException extends Exception {

}

/**
 * Encapsulates the access to the database
 * @author  Dang Huynh, Patrick Strobel
 * @version 1.0.1
 */
class Database {

	const DATE_TIME_FORMAT = "Y-m-d H:i:s";
	const DATA_NULL = "0000-00-00 00:00:00";

	/** @var Database */
	private static $instance = null;

	/** @var boolean */
	private $handler = null;

	private function __construct() {

	}

	/**
	 *
	 * @return Database Instance
	 */
	public static function getInstance() {
		if (self::$instance == null) {
			self::$instance = new Database();
		}
		return self::$instance;
	}

	/**
	 * Connects to the database server if the connection hasn't been established yet
	 * and opens the actual database
	 * @throws DatabaseException Thrown, if unable to connect to database server
	 */
	public function connect() {
		// Cancel if connection is already established
		if ($this->handler != null) {
			return;
		}

		// Connect to server
		$handler = mysql_connect(DB_HOST, DB_USER, DB_PASSW, false, 65536);

		if (!$handler) {
			throw new DatabaseException("Cannot connect to database");
		}

		// Open database
		$db = mysql_select_db(DB_NAME, $handler);

		if (!$db) {
			throw new DatabaseException("Cannot select database");
		}

		$this->handler = $handler;
	}

	/**
	 * Executes a database query
	 * @throws DatabaseException Thrown, if database connection hasn't been established
	 *			  before or if the query-string is invalid
	 *
	 * @param String $query Query to send to the database server
	 *
	 * @return MySql_result Query's result
	 */
	public function query($query) {
		if (defined('ECHO'))
			echo $query . "\n";

		if ($this->handler == null || !$this->handler) {
			throw new DatabaseException("Not connected to database");
		}

		$result = mysql_query($query);
		if (!$result) {
			throw new DatabaseException("Invalid database query \"$query\". MySQL says \"" . mysql_error() . "\"");
		}

		return $result;
	}

	/**
	 * Retrieves the next data row of the given query-result handler
	 *
	 * @param MySQL_result $result SQL result handler
	 *
	 * @return array	Associated array. Key's are the column's names
	 */
	public function fetch($result) {
		return mysql_fetch_assoc($result);
	}

	/**
	 * Returns the auto-increment value of the new dataset
	 * @return int Auto-increment value
	 */
	public function getId() {
		return mysql_insert_id();
	}

	/**
	 * Returns the number of rows that have been changed by the update or insert statement
	 *
	 * @return int  Number of rows
	 */
	public function getAffectedRows() {
		return mysql_affected_rows();
	}


	/**
	 * Returns the number of rows returned by the last query statement
	 *
	 * @param MySQL_result $result SQL result handler
	 *
	 * @return int  Number of rows
	 */
	public function getNumRows($result) {
		return mysql_num_rows($result);
	}

	/**
	 * Escapes dangerous sequences in a user input to avoid SQL injection
	 *
	 * @param type $input
	 *
	 * @return type
	 */
	public function secureInput($input) {
		return mysql_real_escape_string($input);
	}

	/**
	 * Disconnects from the database if connection has been established before
	 */
	public function disconnect() {
		if ($this->handler != null) {
			mysql_close($this->handler);
		}
	}

}

?>
