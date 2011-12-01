package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;

/**
 * JSonRequestReader makes it possible to read requested JSON Objects easily It
 * provides all the methods to communicate with the webservice outside.
 * 
 * @author Alexander Wassiljew
 * 
 */
public class JSonRequestReader {

	static List<ParamObject> listToParse = new ArrayList<ParamObject>();

	/**
	 * This method send a request to the webservice to register an user in the
	 * database
	 * 
	 * @param username
	 * @param password
	 * @param email
	 * @param firstname
	 * @param lastname
	 * @param tel
	 * @param description
	 * @param email_public
	 * @param firstname_public
	 * @param lastname_public
	 * @param tel_public
	 * @return status (see vHike/webservice/design.html)
	 */
	public static String register(String username, String password,
			String email, String firstname, String lastname, String tel,
			String description, boolean email_public, boolean firstname_public,
			boolean lastname_public, boolean tel_public) {

		listToParse.clear();
		listToParse.add(new ParamObject("username", username, true));
		listToParse.add(new ParamObject("password", password, true));
		listToParse.add(new ParamObject("email", email, true));
		listToParse.add(new ParamObject("firstname", firstname, true));
		listToParse.add(new ParamObject("lastname", lastname, true));
		listToParse.add(new ParamObject("tel", tel, true));
		listToParse.add(new ParamObject("email_public", String
				.valueOf(email_public), true));
		listToParse.add(new ParamObject("firstname_public", String
				.valueOf(firstname_public), true));
		listToParse.add(new ParamObject("lastname_public", String
				.valueOf(lastname_public), true));
		listToParse.add(new ParamObject("tel_public", String
				.valueOf(tel_public), true));

		JsonObject object = null;
		try {
			object = JSonRequestProvider.doRequest(listToParse, "register.php");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String status = "";
		if (object != null) {
			boolean suc = object.get("successful").getAsBoolean();
			if (suc) {
				status = object.get("status").getAsString();
			}
			return status;
		} else {
			return status;
		}

	}

	/**
	 * Try to login into the app with given username and password
	 * 
	 * @param name
	 *            username
	 * @param pw
	 *            password
	 * 
	 * @return status (see vHike/webservice/design.html)
	 */
	public static String login(String username, String pw) {

		listToParse.clear();
		listToParse.add(new ParamObject("username", username, false));
		listToParse.add(new ParamObject("password", pw, false));

		JsonObject object = null;
		try {
			object = JSonRequestProvider.doRequest(listToParse, "login.php");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean suc = object.get("successful").getAsBoolean();
		String sid = null;
		String status = null;
		if (suc) {
			status = object.get("status").getAsString();
			sid = object.get("sid").getAsString();
			Model.getInstance().setSid(sid);
			return status;
				// TODO Aufruf von eigenem Profil holen
				// getOwnProfile();
		}
		return status;
	}

	/**
	 * Logout the user with given session id
	 * 
	 * @param session_id
	 * @return true, if logout succeeded
	 */
	public static String logout(String session_id) {

		listToParse.clear();
		listToParse.add(new ParamObject("sid", session_id, false));
		JsonObject object = null;
		try {
			object = JSonRequestProvider.doRequest(listToParse, "logout.php");

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		boolean suc = object.get("successful").getAsBoolean();
		String status = object.get("status").getAsString();
		if (suc) {
			return status;
		} else {
			return status;
		}

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

		listToParse.clear();
		listToParse.add(new ParamObject("id", String.valueOf(id), false));
		listToParse.add(new ParamObject("sid", session_id, false));

		JsonObject object = null;
		JsonArray array = null;
		try {
			object = JSonRequestProvider.doRequest(listToParse,"getProfile.php");

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		boolean suc = object.get("successful").getAsBoolean();
		String userid = object.get("id").getAsString();
		String username = object.get("username").getAsString();
		String regdate = object.get("regdate").getAsString();
		double rating = object.get("rating").getAsDouble();
		// Test TODO
		Date date = new Date();
		Profile profile;
		if (suc) {
			profile = new Profile(username, "email", "firstname", "lastname",
					"tel", "description", rating, date,
					false, false, false, false);
			return profile;
		} else {
			return null;
		}

	}

	/**
	 * Announce a trip to the webservice
	 * 
	 * @return true if succeeded
	 */
	public static boolean announceTrip(String session_id, String destination) {
		listToParse.clear();
		listToParse.add(new ParamObject("sid", session_id, false));
		listToParse.add(new ParamObject("destination", destination, true));

		JsonObject object = null;
		try {
			object = JSonRequestProvider.doRequest(listToParse, "announceTrip.php");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String suc = object.get("successful").toString();
		if (suc.equals("true")) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Dummy method don't touch it
	 * 
	 * @param name
	 * @return
	 */
	public static String dummyMethod(String name, String mood) {
		Log.i("To Parse name:" + name);
		Log.i("To Parse mood:" + mood);
		listToParse.clear();
		listToParse.add(new ParamObject("name", name, true));
		listToParse.add(new ParamObject("mood", mood, false));

		JsonObject object = null;
		try {
			object = JSonRequestProvider.doRequest(listToParse, "test.php");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String out_pos = object.get("output_pos").toString();
		String out_get = object.get("output_get").toString();

		Log.i("Postoutput:" + out_pos);
		Log.i("Getoutput:" + out_get);
		return null;
	}

}
