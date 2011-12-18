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

		Log.i("USERNAME: " + username);
		Log.i("PASSSWORD: " + pw);
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
	 * @return TRIP_STATUS_ANNOUNCED, TRIP_STATUS_OPEN_TRIP,STATUS_ERROR
	 */
	public int announceTrip(String session_id, String destination,
			float current_lat, float current_lon, int avail_seats) {
		Log.i(session_id + ", " + destination + ", " + current_lat + ", " + current_lat + ", " +avail_seats);
		String status = JSonRequestReader.announceTrip(session_id, destination,
				current_lat, current_lon, avail_seats);
		
		if(status.equals("announced")){
			return Constants.TRIP_STATUS_ANNOUNCED;
		}else if(status.equals("open_trip_exists")){
			return Constants.TRIP_STATUS_OPEN_TRIP;
		}
		return Constants.STATUS_ERROR;
	}

	/**
	 * Updates the position of the driver
	 * 
	 * @param sid
	 * @param trip_id
	 * @param current_lat
	 * @param current_lon
	 * @return STATUS_UPDATED, STATUS_UPTODATE, STATUS_NOTRIP, STATUS_HASENDED
	 *         STATUS_INVALID_USER see {@link Constants} and design.html
	 */
	public int tripUpdatePos(String sid, int trip_id, float current_lat,
			float current_lon) {
		String status = JSonRequestReader.tripUpdatePos(sid, trip_id,
				current_lat, current_lon);

		if (status.equals("updated")) {
			return Constants.STATUS_UPDATED;
		} else if (status.equals("already_uptodate")) {
			return Constants.STATUS_UPTODATE;
		} else if (status.equals("no_trip")) {
			return Constants.STATUS_NOTRIP;
		} else if (status.equals("has_ended")) {
			return Constants.STATUS_HASENDED;
		} else if (status.equals("invalid_user")) {
			return Constants.STATUS_INVALID_USER;
		}
		return 0;
	}

	/**
	 * Updates the data of the trip
	 * 
	 * @param sid
	 * @param trip_id
	 * @param avail_seats
	 * @return STATUS_UPDATED, STATUS_UPTODATE, STATUS_NOTRIP, STATUS_HASENDED
	 *         STATUS_INVALID_USER see {@link Constants} and design.html
	 */
	public int tripUpdateData(String sid, int trip_id, int avail_seats) {
		String status = JSonRequestReader.tripUpdateData(sid, trip_id,
				avail_seats);

		if (status.equals("updated")) {
			return Constants.STATUS_UPDATED;
		} else if (status.equals("already_uptodate")) {
			return Constants.STATUS_UPTODATE;
		} else if (status.equals("no_trip")) {
			return Constants.STATUS_NOTRIP;
		} else if (status.equals("has_ended")) {
			return Constants.STATUS_HASENDED;
		} else if (status.equals("invalid_user")) {
			return Constants.STATUS_INVALID_USER;
		}
		return 0;
	}
	/**
	 * End the active trip
	 * @param sid
	 * @param trip_id
	 * @return STATUS_UPDATED, STATUS_UPTODATE, STATUS_NOTRIP, STATUS_HASENDED
	 *         STATUS_INVALID_USER see {@link Constants} and design.html
	 */
	public int endTrip(String sid, int trip_id) {
		String status = JSonRequestReader.endTrip(sid, trip_id);

		if (status.equals("updated")) {
			return Constants.STATUS_UPDATED;
		} else if (status.equals("already_uptodate")) {
			return Constants.STATUS_UPTODATE;
		} else if (status.equals("no_trip")) {
			return Constants.STATUS_NOTRIP;
		} else if (status.equals("has_ended")) {
			return Constants.STATUS_HASENDED;
		} else if (status.equals("invalid_user")) {
			return Constants.STATUS_INVALID_USER;
		}
		return 0;
	}
	
	
}
