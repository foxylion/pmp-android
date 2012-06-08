<?php
if (!defined("INCLUDE")) {
	exit;
}

/**
 * Handles the user session.
 * Allows to check if the user is currently logged in and gives functionality
 * to log in a given user or to log out the currently logged in user
 * @author Patrick Strobel
 * @version 1.0.0
 */
class Session
{

	/** @var Session */
	private static $instance = NULL;
	/** @var User */
	private $user = NULL;

	const USER_INVALID = 0;
	const USER_INACTIVATED = 1;
	const USER_LOGGED_IN = 2;


	/**
	 * Load logged in user data from database if user is logged in
	 */
	private function __construct()
	{
		// Define and start session
		session_name("sid");
		session_start();

		// Nothing to do if there's no session data
		if (!isset($_SESSION["username"]) || !isset($_SESSION["password"]) ||
			!isset($_SESSION["ip"])
		) {
			return;
		}

		// Make sure that the user's IP-address matches the address of the last login
		if ($_SESSION["ip"] != $_SERVER["REMOTE_ADDR"]) {
			return;
		}

		$user = User::loadUserByName($_SESSION["username"]);

		// Store user object if user does exist, the password is valid and account is activated
		if ($user != NULL && $user->getPasswordHash() == User::hashPassword($_SESSION["password"]) &&
			$user->isActivated()
		) {
			$this->user = $user;
		}
	}

	/**
	 * Returns the single instance of the session-object
	 * @return Session Session-Object
	 */
	public static function getInstance()
	{
		if (self::$instance == NULL) {
			self::$instance = new Session();
		}
		return self::$instance;
	}

	/**
	 * Gives access to the currently used session id used
	 * @return String   Session-ID
	 */
	public function getSid()
	{
		return session_id();
	}

	/**
	 * Determines if a user is logged in
	 * @return boolean  True, if user is logged in
	 */
	public function isLoggedIn()
	{
		return ($this->user != NULL);
	}

	/**
	 * Get the user that's currently logged in
	 * @return User The currently logged in user or null, if no user is logged in
	 */
	public function getLoggedInUser()
	{
		return $this->user;
	}

	/**
	 * Logs in the user if the given data is valid
	 * @param String $username
	 * @param String $password
	 * @return int  Value of one of the $USER_X constant.
	 */
	public function logIn($username, $password)
	{
		// Verify username and password
		$user = User::loadUserByName($username);

		if ($user != NULL && $user->getPasswordHash() == User::hashPassword($password)) {
			if (!$user->isActivated()) {
				return self::USER_INACTIVATED;
			} else {
				$_SESSION["ip"] = $_SERVER["REMOTE_ADDR"];
				$_SESSION["username"] = $username;
				$_SESSION["password"] = $password;
				$this->user = $user;
				return self::USER_LOGGED_IN;
			}
		} else {
			// Log out user if an user is already logged in
			$this->logOut();
			return self::USER_INVALID;
		}
	}

	/**
	 * Logs out the user
	 */
	public function logOut()
	{
		/*unset($_SESSION["username"]);
unset($_SESSION["password"]);
unset($_SESSION["ip"]);*/
		session_destroy();
		$this->user = NULL;
	}
}

?>
