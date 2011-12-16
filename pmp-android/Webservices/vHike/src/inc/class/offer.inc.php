<?php
if (!defined("INCLUDE")) {
    exit;
}

class OfferException extends Exception {
    const EXISTS_ALREADY = 0;
    const QUERY_NOT_FOUND = 1;
}

class Offer {
    
    private $id = -1;
    /**
     *
     * @var User 
     */
    private $driver = null;
    /**
     *
     * @var int 
     */
    private $queryId = -1;
    private $message = null;
    
    /**
     * Loads information for the given offer-id from the databse
     * @param int $id   ID of the offer to load from the database
     * @return Offer    Offer object storing the loaded information 
     */
    public static function loadOffer($id) {
        if (!is_numeric($id) || $id <=0) {
            return null;
        }
        
        $offer = new Offer();
        
        $db = Database::getInstance();
        $row = $db->fetch($db->query("SELECT * 
                                      FROM `".DB_PREFIX."_offer` AS 'o', `".DB_PREFIX."_user` AS 'u'        
                                      WHERE `o.driver` = `u.id`"));
        
        
        // TODO: Setting attributes
    }
    
    /**
     * Creates a new query using the data set with the setX()-methods
     * @return int ID of the new offer or -1 if there's already a offer with the same driver and query
     * @throws InputException Thrown, if an important field (e.g. destination) is missing
     * @throws OfferException Thrown, if the given driver has already send a offer for the given query 
     *                          (code = ALREDY_EXISTS) or if there is no query with the given id
     *                          (code = QUERY_NOT_FOUND)
     */
    public function create() {
        // Cancel if important information is missing
        if ($this->driver == null || $this->queryId <= 0) {
            throw new InputException("Some mandatory fields not set.");
        }
        
        if ($this->offerExists()) {
            throw new OfferException("The given user has already send an offer for the given query", OfferException::ALREADY_EXISTS);
        }
        
        $query = new Query();
        if($query->loadQuery($this->queryId) == null) {
            throw new OfferException("There is no query for the given id", OfferException::QUERY_NOT_FOUND);
        }
        
        // Write data into table
        $db = Database::getInstance();
        $creation = Date(Database::DATE_FORMAT, time());
        
        $db->query("INSERT INTO `".DB_PREFIX."_offer` (
                        `driver`,
                        `query`,
                        `message`
                    ) VALUES (
                        ".$this->driver.",
                        ".$this->query.",
                        \"".$this->message."\"
                    )");
        
       return $this->id = $db->getId();      
    }
    
    /**
     * Checks if the given driver has already sent an offer for the given query
     * @return boolean  True, if there's already an offer with the given details
     *                  If driver or query is not set, this will always return false
     */
    public function offerExists() {        
        if ($this->driver == null || $this->query <= 0) {
            return false;
        }
        
        $db = Database::getInstance();
        $count = $db->fetch($db->query("SELECT count(*) AS count 
                                        FROM `".DB_PREFIX."_offer`
                                        WHERE `driver` = ".$this->driver->getId()." 
                                        AND `query` = ".$this->queryId));
        
        return $count["count"] > 0;
    }
  
    
    /**
     *
     * @return User 
     */
    public function getDriver() {
        return $this->driver;
    }
    
    /**
     *
     * @return int 
     */
    public function getQueryId() {
        return $this->queryId;
    }
    
    /**
     *
     * @return String 
     */
    public function getMessage() {
        return $this->message;
    }
    
    /**
     *
     * @param User $driver 
     * @return boolean
     */
    public function setDriver($driver) {
        if ($driver instanceof User) {
            $this->driver = driver;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     *
     * @param int $query
     * @return boolean 
     */
    public function setQueryId($query) {
        if (!is_numeric($query) || $query <= 0) {
            return false;
        } else {
            $this->queryId = $query;
            return true;
        }
        
    }
    
    /**
     *
     * @param String $message 
     */
    public function setMessage($message) {
        $message = Database::getInstance()->secureInput($message);
        $this->message = $message;
    }
}
?>
