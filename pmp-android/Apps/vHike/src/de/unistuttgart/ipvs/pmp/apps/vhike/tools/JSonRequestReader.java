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
			object = JSonRequestProvider.doRequest(listToParse);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String status = "";
		if (object != null) {
			String suc = object.get("successful").toString();
			if (suc.equals("true")) {
				status = object.get("status").toString();
			}
			return status;
		} else {
			return "Registration failed!";
		}

	}

	/**
	 * Try to login into the app with given username and password
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
		String suc = object.get("successful").toString();
		String sid = null;
		String status = null;
		if (suc.equals("true")) {
			status = object.get("status").toString();
			sid = object.get("sid").toString();
			
			if(sid != null){
				Log.i("Session ID: "+sid);
				return sid;
				//TODO  Aufruf von eigenem Profil holen
				// getOwnProfile(); 
			}
		}

		Log.i("SID:" + sid + " STATUS:" + status + " SUCCESSFUL:" + suc);

		return sid;
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
		JsonObject object = null;
		try {
			object = JSonRequestProvider.doRequest(listToParse);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String suc = object.get("successful").toString();
		if(suc.equals("true")){
			return true;
		}else{
			return false;	
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
		try {
			object = JSonRequestProvider.doRequest(listToParse);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String suc = object.get("").toString();
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
