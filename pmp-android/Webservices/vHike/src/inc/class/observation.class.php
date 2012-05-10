<?php
if (!defined('INCLUDE')) {
    exit;
}

class Observation{
	 /**
     * Makes the observationmode accessable.
     * 
     * @param int $user_id Id of an user, to be observed
     * @return Random Observation_nr
     *              
     */
	public static function enableObservation($user_id)
    {
        $db = Database::getInstance();
        $zahl = mt_rand();
        $query = $db->query("INSERT INTO dev_observation VALUES(user_id=$user_id, obs_nr=$zahl)");

        return $zahl;
    }
    
   /**
     * Makes the observationmode not accessable.
     * 
     * @param int $user_id Id of an user
     *              
     */  
	public static function disableObservation($user_id)
    {
        $db = Database::getInstance();
        $query = $db->query("DELETE FROM dev_observation WHERE user_id=$user_id");
    }
}
?>