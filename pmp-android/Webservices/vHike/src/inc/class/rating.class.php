<?php

/**
 * Handles ratings
 * @author Patrick Strobel 
 * @version 1.0.0
 */
class Rating {
    
    const CAN_RATE = 0;
    const ALREADY_RATED = 1;
    const NOT_ENDED = 2;
    const NO_CONNECTION = 3;
    
    /** @var int */
    private $avg;
    /** @var int */
    private $num;
    
    private function __construct() {}
    
    /**
     * Loads the rating for a given user
     * @param User $recipient
     * @return Rating 
     */
    public static function loadRating($recipient) {
        if (!($recipient instanceof User)) {
            throw new InvalidArgumentException("Parameter is of wrong type");
        }

        $db = Database::getInstance();
        $row = $db->fetch($db->query("SELECT 
                                        avg(`rating`) AS avg, 
                                        count(`rating`) AS num
                                      FROM
                                        `" . DB_PREFIX . "_rating`
                                      WHERE `recipient` = " . $recipient->getId()));
        
        $rating = new Rating();
        $rating->avg = ($row["avg"] != null ? $row["avg"] : 0);
        $rating->num = $row["num"];
        return $rating;
    }
    
    public function getAverage() {
        return $this->avg;
    }
    
    public function getNumber() {
        return $this->num;
    }
    
    /** 
     * Checks if the rater is allowed to rate using the given data
     * @param User $rater   User that wants to rate
     * @param User $recipient   User that should recive the rating
     * @param Trip $trip    Trip on which both, rater and recipient, should have participated
     * @return enum CAN_RATE, if the rater has participated on the recipients trip,
     *                  this ride has ended and if the rater hasn't rated the recipient yet.
     *                  Otherwise this will return ALREADY_RATED, NOT_ENDED or NO_CONNECTION.
     *                  If one parameter is NULL or rater and receipient are equal, this will return NO_CONNECTION
     * @throws  InvalidArgumentException If one argument is of invalid type  
     */
    public static function allowed($rater, $recipient, $trip) {        
        if (!($rater instanceof User)) {
            throw new InvalidArgumentException("Rater has to be an object of class User");            
        }
        
        if (!($recipient instanceof User)) {
            throw new InvalidArgumentException("Recipient has to be an object of class User");            
        }
        
        if (!($trip instanceof Trip)) {
            throw new InvalidArgumentException("Trip has to be an object of class Trip");            
        }
        
        // Rating is only allowed for trips that have already ended
        if (!$trip->hasEnded()) {
            return self::NOT_ENDED;
        }
        
        // User are not allowed to rate themselves
        if ($rater->isEqual($recipient)) {
            return self::NO_CONNECTION;
        }
        
        // Check if there is a relationship between rater and recipient
        // ---------------------------------------------------
        $ride = Ride::getRideByTrip($trip);
        
        if ($ride == null) {
            return self::NO_CONNECTION;
        }
       
        // If rater is the driver on this trip, check if recipient is a passenger
        if ($ride->getDriver()->isEqual($rater) && !$ride->isPassenger($recipient)) {
            return self::NO_CONNECTION;
        }
        
        // If rater is a passenger, check if recipient is a driver or a passenger
        if ($ride->isPassenger($rater) && 
                !($ride->getDriver()->isEqual($recipient) || $ride->isPassenger($recipient))) {
            return self::NO_CONNECTION;
        }
        
        // Check if the receipient hasn't already rated the recipient
        // ---------------------------------------------------
        $db = Database::getInstance();
        
        // Get all rating for the given trip
        $db->query("SELECT * FROM `".DB_PREFIX."_rating` 
                    WHERE `rater` = ".$rater->getId()." 
                    AND `recipient` = ".$recipient->getId()."
                    AND `trip` = ".$trip->getId());
        
        if ($db->getAffectedRows() > 0) {
            return self::ALREADY_RATED;
        }
        
        return self::CAN_RATE;
    }
    
    /**
     * Checks if the given rater has already rated the given recipient 
     * @param User $rater   The rater
     * @param User $recipient   The recipient
     * @return boolean  True, if the rater has already rated the recipient    
     */
    public static function hasRated($rater, $recipient, $trip) {
        return self::allowed($rater, $recipient, $trip) == self::ALREADY_RATED;
    }
    

    /**
     * Rates a given user. This does not check if a rating is possible or allowed.
     * Use allowed() to check this!
     * @param User $rater   User that wants to rate
     * @param User $recipient   User that should recive the rating
     * @param Trip $trip    Trip on which both, rater and recipient, should have participated
     * @param int  $rating  The rating
     * @return boolean  True, if rating was inserted into database
     * @throws  InvalidArgumentException If one argument is of invalid type or
     *                                   if rating is not between 1 and 5´ 
     */
    public static function rate($rater, $recipient, $trip, $rating) {
        if (!($rater instanceof User)) {
            throw new InvalidArgumentException("Rater has to be an object of class User");            
        }
        
        if (!($recipient instanceof User)) {
            throw new InvalidArgumentException("Recipient has to be an object of class User");            
        }
        
        if (!($trip instanceof Trip)) {
            throw new InvalidArgumentException("Trip has to be an object of class Trip");            
        }
        
        if (!is_numeric($rating) || $rating < 1 || $rating > 5) {
            throw new InvalidArgumentException("Rating has to be between 1 and 5");
        }
        
        $db = Database::getInstance();
        
        $db->query("INSERT INTO `" . DB_PREFIX . "_rating` (
                        `rater`,
                        `recipient`,
                        `trip`,
                        `rating`
                    ) VALUES (
                        \"" . $rater->getId() . "\",
                        \"" . $recipient->getId(). "\",
                        \"" . $trip->getId() . "\",
                        \"" . $rating . "\"
                    )");
        
        return $db->getAffectedRows() > 0;
    }
}
?>
