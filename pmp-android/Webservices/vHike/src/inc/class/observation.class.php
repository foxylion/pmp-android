<?php
if (!defined('INCLUDE')) {
	exit;
}

class Observation {
	/**
	 * Makes the observation mode accessible.
	 *
	 * @param int $user_id Id of an user, to be observed
	 *
	 * @return Random Observation_nr
	 */
	public static function enableObservation($user_id) {
		// TODO: Check if the user already has an observation number
		$db = Database::getInstance();
		$hash = mt_rand();
		$db->query("INSERT INTO dev_observation (user_id, obs_nr) VALUES($user_id, '$hash')");
		return $hash;
	}

	/**
	 * Makes the observation mode not accessible.
	 *
	 * @param int $user_id Id of an user
	 */
	public static function disableObservation($user_id) {
		$db = Database::getInstance();
		$query = $db->query("DELETE FROM dev_observation WHERE user_id=$user_id");
	}


	/**
	 * Checks whether observation mode is on
	 *
	 * @param int $user_id Id of an user
	 * @return bool True if observation mode is active, false otherwise
	 */
	public static function isObserved($user_id) {
		$db = Database::getInstance();
		$query = $db->query("SELECT user_id FROM dev_observation WHERE user_id=$user_id");

		if ($query) {
			return true;
		} else {
			return false;
		}
	}
}
