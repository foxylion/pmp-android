package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

import com.google.gson.JsonObject;

import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;

/**
 * JSonRequestReader makes it possible to read requested JSON Objects easily It
 * provides all the methods to communicate with the webservice outside.
 * 
 * @author Alexander Wassiljew
 * 
 */
public class JSonRequestReader {

	/**
	 * Try to login into the app with given username and password
	 * 
	 * @param name
	 *            nickname
	 * @param pw
	 *            password
	 * 
	 * @return session_id
	 */
	public static String login(String name, String pw) {
		JsonObject object = JSonRequestProvider
				.getJSONData(Constants.WEBSERVICE_URL + "/login.php");
		object.get("");
		return "";
	}

	/**
	 * Logout the user with given session id
	 * 
	 * @param session_id
	 * @return true, if logout succeeded
	 */
	public static boolean logout(String session_id) {

		return false;
	}

	/**
	 * Get profile of given user id
	 * 
	 * @param session_id
	 *            actual session id
	 * @param id
	 *            of an user
	 */
	public static Profile getProfile(String session_id, int id) {

		return null;
	}

	/**
	 * Announce a trip to the webservice
	 * 
	 * @return true if succeeded
	 */
	public static boolean announceTrip(String session_id) {

		return false;
	}

}
