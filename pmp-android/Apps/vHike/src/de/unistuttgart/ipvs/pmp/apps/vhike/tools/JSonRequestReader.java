package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.JsonObject;

import de.unistuttgart.ipvs.pmp.Log;
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

	public static boolean register(String username, String password,
			String email, String firstname, String lastname, String tel,
			String description, boolean email_public, boolean firstname_public,
			boolean lastname_public, boolean tel_public) {

		listToParse.clear();
		listToParse.add(new ParamObject("username", username, true));
		listToParse.add(new ParamObject("passwordw", password, true));
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
			object = JSonRequestProvider.doRequest(listToParse);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String suc = object.get("successfull").getAsString();
		String status = object.get("status").getAsString();

		return Boolean.getBoolean(suc);
	}

	/**
	 * Try to login into the app with given username and password
	 * 
	 * @param name
	 *            username
	 * @param pw
	 *            password
	 * 
	 * @return session_id
	 */
	public static String login(String username, String pw) {

		listToParse.clear();
		listToParse.add(new ParamObject("username", username, false));
		listToParse.add(new ParamObject("password", pw, false));

		JsonObject object = null;
		try {
			object = JSonRequestProvider.doRequest(listToParse);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String id = object.get("sid").getAsString();
		String status = object.get("status").getAsString();
		String suc = object.get("successfull").getAsString();

		Log.i("SID:" + id + " STATUS:" + status + " SUCCESSFULL:" + suc);

		return "NIY";
	}

	/**
	 * Logout the user with given session id
	 * 
	 * @param session_id
	 * @return true, if logout succeeded
	 */
	public static boolean logout(String session_id) {

		listToParse.clear();
		listToParse.add(new ParamObject("sid", session_id, false));

		try {
			JsonObject object = JSonRequestProvider.doRequest(listToParse);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

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
			object = JSonRequestProvider.doRequest(listToParse);
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
