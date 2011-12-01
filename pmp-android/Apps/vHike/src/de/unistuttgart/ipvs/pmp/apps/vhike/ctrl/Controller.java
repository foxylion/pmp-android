package de.unistuttgart.ipvs.pmp.apps.vhike.ctrl;

import java.util.Map;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.JSonRequestReader;

/**
 * Controls the behaviour of vHike
 * 
 * @author Alexander Wassiljew
 * 
 */
public class Controller {

	/**
	 * Constructor
	 */
	public Controller() {
	}

	/**
	 * Log on an user and save the session id in the {@link Model}
	 * 
	 * @param username
	 * @param pw
	 * @return true if succeed
	 */
	public boolean login(String username, String pw) {

		String status = JSonRequestReader.login(username, pw);
		Log.i("Status im CTRL: " + status);
		if (status.equals("logged_in")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Log out an user
	 * 
	 * @param sid
	 * @return true if succeed
	 */
	public boolean logout(String sid) {
		String status = JSonRequestReader.logout(sid);
		if (status.equals("logged_out")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Registar an user
	 * 
	 * @return int code specified in {@link Constants}
	 */
	public int register(Map<String, String> list) {

		String status = JSonRequestReader.register(list.get("username"),
				list.get("password"), list.get("email"), list.get("firstname"),
				list.get("lastname"), list.get("tel"), list.get("description"),
				Boolean.parseBoolean(list.get("email_public")),
				Boolean.parseBoolean(list.get("firstname_public")),
				Boolean.parseBoolean(list.get("lastname_public")),
				Boolean.parseBoolean(list.get("tel_public")));

		if (status.equals("registered")) {
			return Constants.REG_STAT_REGISTERED;
		} else if (status.equals("username_exists")) {
			return Constants.REG_STAT_USED_USERNAME;
		} else if (status.equals("email_exists")) {
			return Constants.REG_STAT_USED_MAIL;
		} else if (status.equals("invalid_username")) {
			return Constants.REG_STAT_INVALID_USERNAME;
		} else if (status.equals("invalid_password")) {
			return Constants.REG_STAT_INVALID_PW;
		} else if (status.equals("invalid_firstname")) {
			return Constants.REG_STAT_INVALID_FIRSTNAME;
		} else if (status.equals("invalid_lastname")) {
			return Constants.REG_STAT_INVALID_LASTNAME;
		} else if (status.equals("invalid_tel")) {
			return Constants.REG_STAT_INVALID_TEL;
		}

		return Constants.REG_NOT_SUCCESSFUL;
	}

	/**
	 * Returns the Profile of an user
	 * 
	 * @param user_id
	 * @return {@link Profile}
	 */
	public Profile getProfile(String session_id, int user_id) {
		return JSonRequestReader.getProfile(session_id, user_id);
	}

	/**
	 * Announce a trip to the webservice
	 * 
	 * @param session_id
	 * @param destination
	 * @return true, if announcing a trip is succeeded
	 */
	public boolean announceTrip(String session_id, String destination) {
		return JSonRequestReader.announceTrip(session_id, destination);
	}
}
