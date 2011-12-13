<?php
if (!defined("INCLUDE")) {
    exit;
}

class DatabaseException extends Exception {

}

/**
 * Encapsulares the access to the database 
 * @author Patrick
 */
class Database {

    const DATE_FORMAT = "Y-m-d H:i:s";
    const DATA_NULL = "0000-00-00 00:00:00";
    
    private static $instance = null;

    private $handler = null;

    private function __construct() {
        
    }

    /**
     *
     * @return Database Instance
     */
    public static function getInstance() {
        if(self::$instance == null) {
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
        // Cancel if connection is alreay established
        if ($this->handler != null) {
            return;
        }
        
        // Connect to server
        $handler = mysql_connect(DB_HOST, DB_USER, DB_PASSW);

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
     * @throws DatabaseException Thrown, if database connaction hasn't been established
     *              before or if the query-string is invalid
     * @param String $query   Query to sendto the database server
     * @return MySql_result Query's result
     */
    public function query($query) {
        if ($this->handler == null) {
            throw new DatabaseException("Not connected to database");
        }
        
        $result = mysql_query($query);
        if (!$result) {
            throw new DatabaseException("Invalid database query \"$query\". MySQL says \"" . mysql_error() . "\"");
        }

        return $result;
    }
    
    /**
     * Retrieves the next data row of an given query-result handler
     * @param MySQL_result $result SQL result handler
     * @return array    Associated array. Key's are the column's names 
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
     * @return int  Number of rows 
     */
    public function getAffectedRows() {
        return mysql_affected_rows();
    }


    /**
     * Escapes dangerous squences in a user input to avoid SQL injection
     * @param type $input
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
