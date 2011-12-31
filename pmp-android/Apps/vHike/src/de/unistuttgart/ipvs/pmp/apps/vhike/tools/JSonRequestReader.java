package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
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
			object = JSonRequestProvider.doRequest(listToParse, "register.php",
					false);
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
		listToParse.add(new ParamObject("username", username, true));
		listToParse.add(new ParamObject("password", pw, true));

		JsonObject object = null;
		try {
			object = JSonRequestProvider.doRequest(listToParse, "login.php",
					false);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean suc = false;
		if (object != null) {
			suc = object.get("successful").getAsBoolean();
		}
		String sid = null;
		String status = null;
		if (suc) {
			status = object.get("status").getAsString();
			Log.i("STATUS NACH DEM LOGIN:" + status);
			if (!status.equals("invalid")) {
				sid = object.get("sid").getAsString();
				Model.getInstance().setSid(sid);
				Model.getInstance().setOwnProfile(getOwnProfile(sid));
			}
			return status;
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
			object = JSonRequestProvider.doRequest(listToParse, "logout.php",
					false);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean suc = false;
		String status = null;
		if (object != null) {
			suc = object.get("successful").getAsBoolean();
			status = object.get("status").getAsString();
		}

		if (suc) {
			return status;
		} else {
			return status;
		}
	}

	/**
	 * Returns the Profile of the logged user
	 * 
	 * @param sid
	 * @return Profile
	 */
	public static Profile getOwnProfile(String sid) {
		listToParse.clear();
		listToParse.add(new ParamObject("sid", sid, false));

		JsonObject object = null;

		try {
			object = JSonRequestProvider.doRequest(listToParse,
					"own_profile.php", false);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean suc = false;
		int id = 0;
		String username = null;
		String email = null;
		String firstname = null;
		String lastname = null;
		String tel = null;
		String description = null;
		String regdate = null;
		boolean email_public = false;
		boolean firstname_public = false;
		boolean lastname_public = false;
		boolean tel_public = false;

		double rating_avg = 0;
		int rating_num = 0;
		if (object != null) {
			suc = object.get("successful").getAsBoolean();
			username = object.get("username").getAsString();
			Log.i("USERNAME:" + username);
			email = object.get("email").getAsString();
			firstname = object.get("firstname").getAsString();
			lastname = object.get("lastname").getAsString();
			tel = object.get("tel").getAsString();
			description = object.get("description").getAsString();
			regdate = object.get("regdate").getAsString();
			rating_avg = object.get("rating_avg").getAsFloat();
			rating_num = object.get("rating_num").getAsInt();
			email_public = object.get("email_public").getAsBoolean();
			firstname_public = object.get("firstname_public").getAsBoolean();
			lastname_public = object.get("lastname_public").getAsBoolean();
			tel_public = object.get("tel_public").getAsBoolean();
		}

		// String userid = object.get("id").getAsString();
		// TODO
		// String regdate = object.get("regdate").getAsString();
		Profile profile;

		Date date = new Date();
		if (suc) {
			profile = new Profile(username, email, firstname, lastname, tel,
					description, date, email_public, firstname_public,
					lastname_public, tel_public, rating_avg, rating_num);
			return profile;
		}
		return null;
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
			object = JSonRequestProvider.doRequest(listToParse,
					"get_profile.php", false);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean suc = false;
		String username = null;
		String email = null;
		String firstname = null;
		String lastname = null;
		String tel = null;
		String description = null;
		String regdate = null;
		boolean email_public = false;
		boolean firstname_public = false;
		boolean lastname_public = false;
		boolean tel_public = false;

		double rating_avg = 0;
		int rating_num = 0;
		if (object != null) {
			suc = object.get("successful").getAsBoolean();
			username = object.get("username").getAsString();
			email = object.get("email").getAsString();
			firstname = object.get("firstname").getAsString();
			lastname = object.get("lastname").getAsString();
			tel = object.get("tel").getAsString();
			description = object.get("description").getAsString();
			regdate = object.get("regdate").getAsString();
			rating_avg = object.get("rating_avg").getAsFloat();
			rating_num = object.get("rating_num").getAsInt();
			email_public = object.get("email_public").getAsBoolean();
			firstname_public = object.get("firstname_public").getAsBoolean();
			lastname_public = object.get("lastname_public").getAsBoolean();
			tel_public = object.get("tel_public").getAsBoolean();
		}

		// String userid = object.get("id").getAsString();
		// TODO
		// String regdate = object.get("regdate").getAsString();
		Profile profile;

		Date date = new Date();
		if (suc) {
			profile = new Profile(username, email, firstname, lastname, tel,
					description, date, email_public, firstname_public,
					lastname_public, tel_public, rating_avg, rating_num);
			return profile;
		}
		return null;
	}

	/**
	 * Announce a trip to the webservice
	 * 
	 * @return true if succeeded
	 */
	public static String announceTrip(String session_id, String destination,
			float current_lat, float current_lon, int avail_seats) {
		listToParse.clear();
		listToParse.add(new ParamObject("sid", session_id, false));

		listToParse.add(new ParamObject("destination", destination, true));
		listToParse.add(new ParamObject("current_lat", String
				.valueOf(current_lat), true));
		listToParse.add(new ParamObject("current_lon", String
				.valueOf(current_lon), true));
		listToParse.add(new ParamObject("avail_seats", String
				.valueOf(avail_seats), true));

		JsonObject object = null;
		try {
			object = JSonRequestProvider.doRequest(listToParse,
					"trip_announce.php", false);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean suc = false;
		int tripId = 0;
		String status = null;
		if (object != null) {
			suc = object.get("successful").getAsBoolean();
			if (suc) {
				status = object.get("status").getAsString();
				if (status.equals("announced")) {
					tripId = object.get("id").getAsInt();
					Model.getInstance().setTripId(tripId);
					Log.i(String.valueOf(Model.getInstance().getTripId()));
				}
			} else {
				return status = object.get("msg").getAsString();
			}

		}

		return status;
	}

	/**
	 * update the position of the user (driver)
	 * 
	 * @param sid
	 * @param trip_id
	 * @param current_lat
	 * @param current_lon
	 * @return
	 */
	public static String tripUpdatePos(String sid, int trip_id,
			float current_lat, float current_lon) {

		listToParse.clear();
		listToParse.add(new ParamObject("sid", sid, false));

		listToParse.add(new ParamObject("id", String.valueOf(trip_id), true));
		listToParse.add(new ParamObject("current_lat", String
				.valueOf(current_lat), true));
		listToParse.add(new ParamObject("current_lon", String
				.valueOf(current_lon), true));

		JsonObject object = null;
		try {
			object = JSonRequestProvider.doRequest(listToParse,
					"trip_update_pos.php", false);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean suc = false;
		String status = null;
		if (object != null) {
			suc = object.get("successful").getAsBoolean();
			status = object.get("status").getAsString();
			return status;
		}

		return status;
	}

	/**
	 * Update the data of the trip
	 * 
	 * @param sid
	 * @param trip_id
	 * @param avail_seats
	 * @return
	 */
	public static String tripUpdateData(String sid, int trip_id, int avail_seats) {
		listToParse.clear();
		listToParse.add(new ParamObject("sid", sid, false));

		listToParse.add(new ParamObject("id", String.valueOf(trip_id), true));
		listToParse.add(new ParamObject("avail_seats", String
				.valueOf(avail_seats), true));

		JsonObject object = null;
		try {
			object = JSonRequestProvider.doRequest(listToParse,
					"trip_update_data.php", false);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean suc = false;
		String status = null;
		if (object != null) {
			suc = object.get("successful").getAsBoolean();
			status = object.get("status").getAsString();
			return status;
		}

		return status;

	}

	/**
	 * End the trip
	 * 
	 * @param sid
	 * @param trip_id
	 * @return
	 */
	public static String endTrip(String sid, int trip_id) {
		listToParse.clear();
		listToParse.add(new ParamObject("sid", sid, false));

		listToParse.add(new ParamObject("id", String.valueOf(trip_id), true));

		JsonObject object = null;
		try {
			object = JSonRequestProvider.doRequest(listToParse,
					"trip_ended.php", false);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean suc = false;
		String status = null;
		if (object != null) {
			suc = object.get("successful").getAsBoolean();
			status = object.get("status").getAsString();
			return status;
		}

		return status;

	}

	/**
	 * Start searching for drivers
	 * 
	 * @param sid
	 * @param destination
	 * @param current_lat
	 * @param current_lon
	 * @param seats
	 * @return int id of the query
	 */
	public static int startQuery(String sid, String destination,
			float current_lat, float current_lon, int seats) {
		listToParse.clear();
		listToParse.add(new ParamObject("sid", sid, false));

		listToParse.add(new ParamObject("destination", destination, true));
		listToParse.add(new ParamObject("current_lat", String
				.valueOf(current_lat), true));
		listToParse.add(new ParamObject("current_lon", String
				.valueOf(current_lon), true));
		listToParse.add(new ParamObject("seats", String.valueOf(seats), true));

		JsonObject object = null;
		try {
			object = JSonRequestProvider.doRequest(listToParse,
					"query_start.php", false);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean suc = false;
		int id = Constants.QUERY_ID_ERROR;
		if (object != null) {
			suc = object.get("successful").getAsBoolean();
			if (suc) {
				id = object.get("id").getAsInt();
				Log.i(String.valueOf(id));
				return id;
			}
		}

		return id;
	}

	/**
	 * Delete the active query
	 * 
	 * @param sid
	 * @param id
	 *            query id
	 * @return String status
	 */
	public static String stopQuery(String sid, int id) {
		listToParse.clear();
		listToParse.add(new ParamObject("sid", sid, false));

		listToParse.add(new ParamObject("id", String.valueOf(id), true));

		JsonObject object = null;
		try {
			object = JSonRequestProvider.doRequest(listToParse,
					"query_delete.php", false);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		boolean suc = false;
		String status = null;
		if (object != null) {
			suc = object.get("successful").getAsBoolean();
			if (suc) {
				status = object.get("status").getAsString();
				return status;
			}

		}

		return status;
	}

	/**
	 * Driver search for the potential hitchhiker.
	 * 
	 * @param sid
	 * @param trip_id
	 * @param lat
	 * @param lon
	 * @param perimeter
	 *            /radius
	 */
	public static List<QueryObject> searchQuery(String sid, float lat,
			float lon, int perimeter) {

		listToParse.clear();
		listToParse.add(new ParamObject("sid", sid, false));
		listToParse.add(new ParamObject("lat", String.valueOf(lat), true));
		listToParse.add(new ParamObject("lon", String.valueOf(lon), true));
		listToParse.add(new ParamObject("distance", String.valueOf(perimeter),
				true));

		JsonObject object = null;

		try {
			object = JSonRequestProvider.doRequest(listToParse,
					"query_search.php", false);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean suc = false;
		List<QueryObject> queryObjects = null;
		if (object != null) {
			suc = object.get("successful").getAsBoolean();
			if (suc) {
				Log.i(object.get("queries").getAsJsonArray().toString());
				JsonArray array = object.get("queries").getAsJsonArray();
				/*
				 * "id":"11", "passenger":"1", "seats":"2",
				 * "current_lat":"48.7832", "current_lon":"9.1811",
				 * "destination":"Berlin", "distance":"104.884685142798"
				 */

				queryObjects = new ArrayList<QueryObject>();
				Log.i("Array Size: " + array.size());
				for (int i = 0; i < array.size(); i++) {
					Log.i("Element: " + i);
					JsonObject Iobject = array.get(i).getAsJsonObject();
					int id = Iobject.get("id").getAsInt();
					int passenger = Iobject.get("passenger").getAsInt();
					int seats = Iobject.get("seats").getAsInt();
					float cur_lat = Iobject.get("current_lat").getAsFloat();
					float cur_lon = Iobject.get("current_lon").getAsFloat();
					String destination = Iobject.get("destination")
							.getAsString();
					float distance = Iobject.get("distance").getAsFloat();

					QueryObject qObject = new QueryObject(id, passenger, seats,
							cur_lat, cur_lon, destination, distance);
					queryObjects.add(qObject);
				}
				Log.i("List size: " + String.valueOf(queryObjects.size()));
				return queryObjects;
			}
		}
		return queryObjects;
	}

	/**
	 * Driver send an offer to the hitchhiker
	 * 
	 * @param sid
	 * @param trip_id
	 * @param query_id
	 * @param message
	 * @return status
	 */
	public static String sendOffer(String sid, int trip_id, int query_id,
			String message) {
		listToParse.clear();
		listToParse.add(new ParamObject("sid", sid, false));
		listToParse.add(new ParamObject("trip", String.valueOf(trip_id), true));
		listToParse
				.add(new ParamObject("query", String.valueOf(query_id), true));
		listToParse.add(new ParamObject("message", String.valueOf(message),
				true));

		JsonObject object = null;

		try {
			object = JSonRequestProvider.doRequest(listToParse, "offer.php",
					false);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean suc = false;
		String status = "";
		if (object != null) {
			suc = object.get("successful").getAsBoolean();
			if (suc) {
				status = object.get("status").getAsString();
				return status;
			}
		}
		return status;
	}

	public static void viewOffer(String sid) {
		listToParse.clear();
		listToParse.add(new ParamObject("sid", sid, false));
		JsonObject object = null;

		try {
			object = JSonRequestProvider.doRequest(listToParse,
					"offer_view.php", false);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean suc = false;
		String status = "";
		if (object != null) {
			suc = object.get("succ essful").getAsBoolean();
			if (suc) {
				status = object.get("offers").getAsString();
				// TODO;
				Log.i(status);
			}
		}
	}

	/**
	 * Hitchhiker can accept or decline an offer
	 * 
	 * @param sid
	 * @param offer_id
	 * @param accept
	 * @return status
	 */
	public static String handleOffer(String sid, int offer_id, boolean accept) {
		listToParse.clear();
		listToParse.add(new ParamObject("sid", sid, false));

		listToParse
				.add(new ParamObject("offer", String.valueOf(offer_id), true));
		listToParse
				.add(new ParamObject("accept", String.valueOf(accept), true));
		JsonObject object = null;

		try {
			object = JSonRequestProvider.doRequest(listToParse,
					"offer_handle.php", false);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean suc = false;
		String status = "";
		if (object != null) {
			suc = object.get("successful").getAsBoolean();
			if (suc) {
				status = object.get("status").getAsString();
				return status;
			}
		}
		return status;
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
		String out_pos = null;
		String out_get = null;
		try {
			object = JSonRequestProvider.doRequest(listToParse, "test.php",
					false);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (object != null) {
			out_pos = object.get("output_pos").toString();
			out_get = object.get("output_get").toString();
		}

		Log.i("Postoutput:" + out_pos);
		Log.i("Getoutput:" + out_get);
		return null;
	}

}
