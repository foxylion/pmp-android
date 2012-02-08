package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
    
    private static final String TAG = "JSonRequestReader";
    
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
    public static String register(String username, String password, String email, String firstname, String lastname,
            String tel, String description, boolean email_public, boolean firstname_public, boolean lastname_public,
            boolean tel_public) {
        
        listToParse.clear();
        listToParse.add(new ParamObject("username", username, true));
        listToParse.add(new ParamObject("password", password, true));
        listToParse.add(new ParamObject("email", email, true));
        listToParse.add(new ParamObject("firstname", firstname, true));
        listToParse.add(new ParamObject("lastname", lastname, true));
        listToParse.add(new ParamObject("tel", tel, true));
        listToParse.add(new ParamObject("email_public", String.valueOf(email_public), true));
        listToParse.add(new ParamObject("firstname_public", String.valueOf(firstname_public), true));
        listToParse.add(new ParamObject("lastname_public", String.valueOf(lastname_public), true));
        listToParse.add(new ParamObject("tel_public", String.valueOf(tel_public), true));
        
        JsonObject object = null;
        try {
            object = JSonRequestProvider.doRequest(listToParse, "register.php", false);
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
            object = JSonRequestProvider.doRequest(listToParse, "login.php", false);
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
            Log.i(TAG, "STATUS NACH DEM LOGIN:" + status);
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
            object = JSonRequestProvider.doRequest(listToParse, "logout.php", false);
            
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
            object = JSonRequestProvider.doRequest(listToParse, "own_profile.php", false);
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
            id = object.get("id").getAsInt();
            username = object.get("username").getAsString();
            Log.i(TAG, "USERNAME:" + username);
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
            profile = new Profile(id, username, email, firstname, lastname, tel, description, date, email_public,
                    firstname_public, lastname_public, tel_public, rating_avg, rating_num);
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
            object = JSonRequestProvider.doRequest(listToParse, "get_profile.php", false);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean suc = false;
        String username = null;
        String email = "xxx";
        String firstname = "xxx";
        String lastname = "xxx";
        String tel = "xxx";
        String description = null;
        String regdate = null;
        boolean email_public = false;
        boolean firstname_public = false;
        boolean lastname_public = false;
        boolean tel_public = false;
        int userid = 0;
        double rating_avg = 0;
        int rating_num = 0;
        if (object != null) {
            suc = object.get("successful").getAsBoolean();
            userid = object.get("id").getAsInt();
            email_public = object.get("email_public").getAsBoolean();
            firstname_public = object.get("firstname_public").getAsBoolean();
            lastname_public = object.get("lastname_public").getAsBoolean();
            tel_public = object.get("tel_public").getAsBoolean();
            
            if (email_public) {
                email = object.get("email").getAsString();
            }
            if (firstname_public) {
                firstname = object.get("firstname").getAsString();
            }
            if (lastname_public) {
                lastname = object.get("lastname").getAsString();
            }
            if (tel_public) {
                tel = object.get("tel").getAsString();
            }
            username = object.get("username").getAsString();
            description = object.get("description").getAsString();
            Log.i(TAG, "GETPROFILE_: description:" + description);
            regdate = object.get("regdate").getAsString();
            rating_avg = object.get("rating_avg").getAsFloat();
            rating_num = object.get("rating_num").getAsInt();
            
        }
        
        // String userid = object.get("id").getAsString();
        // TODO
        // String regdate = object.get("regdate").getAsString();
        Profile profile;
        
        Date date = new Date();
        if (suc) {
            profile = new Profile(userid, username, email, firstname, lastname, tel, description, date, email_public,
                    firstname_public, lastname_public, tel_public, rating_avg, rating_num);
            return profile;
        }
        return null;
    }
    
    
    /**
     * Announce a trip to the webservice
     * 
     * @return true if succeeded
     */
    public static String announceTrip(String session_id, String destination, float current_lat, float current_lon,
            int avail_seats) {
        listToParse.clear();
        listToParse.add(new ParamObject("sid", session_id, false));
        
        listToParse.add(new ParamObject("destination", destination, true));
        listToParse.add(new ParamObject("current_lat", String.valueOf(current_lat), true));
        listToParse.add(new ParamObject("current_lon", String.valueOf(current_lon), true));
        listToParse.add(new ParamObject("avail_seats", String.valueOf(avail_seats), true));
        
        JsonObject object = null;
        try {
            object = JSonRequestProvider.doRequest(listToParse, "trip_announce.php", false);
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
                    Log.i(TAG, String.valueOf(Model.getInstance().getTripId()));
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
    public static String tripUpdatePos(String sid, int trip_id, float current_lat, float current_lon) {
        
        listToParse.clear();
        listToParse.add(new ParamObject("sid", sid, false));
        
        listToParse.add(new ParamObject("id", String.valueOf(trip_id), true));
        listToParse.add(new ParamObject("current_lat", String.valueOf(current_lat), true));
        listToParse.add(new ParamObject("current_lon", String.valueOf(current_lon), true));
        
        JsonObject object = null;
        try {
            object = JSonRequestProvider.doRequest(listToParse, "trip_update_pos.php", false);
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
    
    
    public static String userUpdatePos(String sid, float lat, float lon) {
        listToParse.clear();
        listToParse.add(new ParamObject("sid", sid, false));
        
        listToParse.add(new ParamObject("lat", String.valueOf(lat), true));
        listToParse.add(new ParamObject("lon", String.valueOf(lon), true));
        
        JsonObject object = null;
        try {
            object = JSonRequestProvider.doRequest(listToParse, "user_update_pos.php", false);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean suc = false;
        String status = "update_fail";
        if (object != null) {
            suc = object.get("successful").getAsBoolean();
            if (suc) {
                status = object.get("status").getAsString();
            }
            
            return status;
        }
        
        return status;
    }
    
    public static PositionObject getUserPosition(String sid, int user_id) {
        listToParse.clear();
        listToParse.add(new ParamObject("sid", sid, false));
        
        listToParse.add(new ParamObject("user_id", String.valueOf(user_id), true));
        
        JsonObject object = null;
        try {
            object = JSonRequestProvider.doRequest(listToParse, "getPosition.php", false);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean suc = false;
        String status = "";
        JsonArray array;
        PositionObject posObj = null;
        if (object != null) {
            suc = object.get("successful").getAsBoolean();
            if (suc) {
                array = object.get("position").getAsJsonArray();
                float lat = array.get(0).getAsFloat();
                float lon = array.get(1).getAsFloat();
                posObj = new PositionObject(lat, lon);
            }
            
        }
        return posObj;
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
        listToParse.add(new ParamObject("avail_seats", String.valueOf(avail_seats), true));
        
        JsonObject object = null;
        try {
            object = JSonRequestProvider.doRequest(listToParse, "trip_update_data.php", false);
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
    
    
    //TODO
    public static List<TripObject> getTripPassengers() {
        
        return null;
        
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
            object = JSonRequestProvider.doRequest(listToParse, "trip_ended.php", false);
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
    public static int startQuery(String sid, String destination, float current_lat, float current_lon, int seats) {
        Log.i(TAG, sid);
        Log.i(TAG, destination);
        Log.i(TAG, String.valueOf(current_lat));
        Log.i(TAG, String.valueOf(current_lon));
        Log.i(TAG, String.valueOf(seats));
        listToParse.clear();
        listToParse.add(new ParamObject("sid", sid, false));
        
        listToParse.add(new ParamObject("destination", destination, true));
        listToParse.add(new ParamObject("current_lat", String.valueOf(current_lat), true));
        listToParse.add(new ParamObject("current_lon", String.valueOf(current_lon), true));
        listToParse.add(new ParamObject("seats", String.valueOf(seats), true));
        
        JsonObject object = null;
        try {
            object = JSonRequestProvider.doRequest(listToParse, "query_start.php", false);
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
                Log.i(TAG, String.valueOf(id));
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
            object = JSonRequestProvider.doRequest(listToParse, "query_delete.php", false);
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
     * @param lat
     * @param lon
     * @param perimeter
     *            /radius
     */
    public static List<QueryObject> searchQuery(String sid, float lat, float lon, int perimeter) {
        
        listToParse.clear();
        listToParse.add(new ParamObject("sid", sid, false));
        listToParse.add(new ParamObject("lat", String.valueOf(lat), true));
        listToParse.add(new ParamObject("lon", String.valueOf(lon), true));
        listToParse.add(new ParamObject("distance", String.valueOf(perimeter), true));
        
        JsonObject object = null;
        
        try {
            object = JSonRequestProvider.doRequest(listToParse, "query_search.php", false);
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
                String status = object.get("status").getAsString();
                if (status.equals("result")) {
                    JsonArray array = object.get("queries").getAsJsonArray();
                    Log.i(TAG, array.toString());
                    queryObjects = new ArrayList<QueryObject>();
                    
                    /*
                     * "queryid": Integer, "userid": Integer, "username":
                     * String, "rating": Float, "lat": Float, "lon": Float,
                     * "seats": Integer, distance: Float
                     */
                    
                    for (int i = 0; i < array.size(); i++) {
                        JsonObject Iobject = array.get(i).getAsJsonObject();
                        int queryid = Iobject.get("queryid").getAsInt();
                        
                        int userid = Iobject.get("userid").getAsInt();
                        
                        String username = Iobject.get("username").getAsString();
                        //						float rating = Iobject.get("rating").getAsFloat();
                        
                        float cur_lat = Iobject.get("lat").getAsFloat();
                        float cur_lon = Iobject.get("lon").getAsFloat();
                        int seats = Iobject.get("seats").getAsInt();
                        float distance = Iobject.get("distance").getAsFloat();
                        
                        QueryObject qObject = new QueryObject(queryid, userid, username, cur_lat, cur_lon, seats,
                                distance);
                        queryObjects.add(qObject);
                    }
                    Model.getInstance().setQueryHolder(queryObjects);
                    return queryObjects;
                }
            }
        }
        Model.getInstance().setQueryHolder(queryObjects);
        return queryObjects;
    }
    
    
    /**
     * Hitchhiker search for offered rides
     * 
     * @param sid
     * @param lat
     * @param lon
     * @param perimeter
     *            /radius
     */
    public static List<RideObject> searchRides(String sid, float lat, float lon, int perimeter) {
        
        listToParse.clear();
        listToParse.add(new ParamObject("sid", sid, false));
        listToParse.add(new ParamObject("lat", String.valueOf(lat), true));
        listToParse.add(new ParamObject("lon", String.valueOf(lon), true));
        listToParse.add(new ParamObject("distance", String.valueOf(perimeter), true));
        
        JsonObject object = null;
        
        try {
            object = JSonRequestProvider.doRequest(listToParse, "ride_search.php", false);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean suc = false;
        List<RideObject> rideObjects = null;
        if (object != null) {
            suc = object.get("successful").getAsBoolean();
            if (suc) {
                
                JsonArray array = object.get("queries").getAsJsonArray();
                
                rideObjects = new ArrayList<RideObject>();
                
                for (int i = 0; i < array.size(); i++) {
                    
                    JsonObject Iobject = array.get(i).getAsJsonObject();
                    int tripid = Iobject.get("tripid").getAsInt();
                    int seats = Iobject.get("seats").getAsInt();
                    float cur_lat = Iobject.get("lat").getAsFloat();
                    float cur_lon = Iobject.get("lon").getAsFloat();
                    
                    String destination = Iobject.get("destination").getAsString();
                    int driverid = Iobject.get("driverid").getAsInt();
                    
                    String username = Iobject.get("username").getAsString();
                    float rating = Iobject.get("rating").getAsFloat();
                    
                    float distance = Iobject.get("distance").getAsFloat();
                    
                    RideObject qObject = new RideObject(tripid, seats, cur_lat, cur_lon, destination, driverid,
                            username, rating, distance);
                    rideObjects.add(qObject);
                }
                
                return rideObjects;
            }
        }
        return rideObjects;
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
    public static String sendOffer(String sid, int trip_id, int query_id, String message) {
        
        Log.i(TAG, sid);
        Log.i(TAG, String.valueOf(trip_id));
        Log.i(TAG, String.valueOf(query_id));
        Log.i(TAG, message);
        
        listToParse.clear();
        listToParse.add(new ParamObject("sid", sid, false));
        listToParse.add(new ParamObject("trip", String.valueOf(trip_id), true));
        listToParse.add(new ParamObject("query", String.valueOf(query_id), true));
        listToParse.add(new ParamObject("message", String.valueOf(message), true));
        
        JsonObject object = null;
        
        try {
            object = JSonRequestProvider.doRequest(listToParse, "offer.php", false);
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
     * Hitchhiker wants to show all offers which were made to him
     * 
     * @param sid
     */
    public static List<OfferObject> viewOffer(String sid) {
        listToParse.clear();
        listToParse.add(new ParamObject("sid", sid, false));
        JsonObject object = null;
        
        try {
            object = JSonRequestProvider.doRequest(listToParse, "offer_view.php", false);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean suc = false;
        List<OfferObject> offerObjects = null;
        if (object != null) {
            suc = object.get("successful").getAsBoolean();
            if (suc) {
                JsonArray array;
                try{
                    array = object.get("offers").getAsJsonArray();   
                    offerObjects = new ArrayList<OfferObject>();
                    for (int i = 0; i < array.size(); i++) {
                        JsonObject Iobject = array.get(i).getAsJsonObject();
                        int offer_id = Iobject.get("offer").getAsInt();
                        int user_id = Iobject.get("userid").getAsInt();
                        String username = Iobject.get("username").getAsString();
                        float rating = Iobject.get("rating").getAsFloat();
                        float rating_num = Iobject.get("rating_num").getAsFloat();
                        float lat = Iobject.get("lat").getAsFloat();
                        float lon = Iobject.get("lon").getAsFloat();
                        
                        OfferObject oObject = new OfferObject(offer_id, user_id, username, rating, rating_num, lat, lon);
                        offerObjects.add(oObject);
                    }
                    Model.getInstance().setOfferHolder(offerObjects);
                }catch(Exception ex){
                    offerObjects = new ArrayList<OfferObject>();
                }
                
                return offerObjects;
            }
        }
        Model.getInstance().setOfferHolder(offerObjects);
        return offerObjects;
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
        
        listToParse.add(new ParamObject("offer", String.valueOf(offer_id), true));
        listToParse.add(new ParamObject("accept", String.valueOf(accept), true));
        JsonObject object = null;
        
        try {
            object = JSonRequestProvider.doRequest(listToParse, "offer_handle.php", false);
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
     * Hitchhiker can accept or decline an offer
     * 
     * @param sid
     * @param offer_id
     * @param accept
     * @return status
     */
    public static boolean pick_up(String sid, int user_id) {
        listToParse.clear();
        listToParse.add(new ParamObject("sid", sid, false));
        
        listToParse.add(new ParamObject("user_id", String.valueOf(user_id), true));
        JsonObject object = null;
        
        try {
            object = JSonRequestProvider.doRequest(listToParse, "pick_up.php", false);
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
                return true;
            }
        }
        return false;
    }
    
    public static boolean isPicked(String sid) {
        listToParse.clear();
        listToParse.add(new ParamObject("sid", sid, false));
        
        JsonObject object = null;
        
        try {
            object = JSonRequestProvider.doRequest(listToParse, "isPicked.php", false);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean suc = false;
        String status = "";
        boolean picked = false;
        if (object != null) {
            suc = object.get("successful").getAsBoolean();
            if (suc) {
                picked = object.get("picked").getAsBoolean();
            }
        }
        return picked;
    }
    
    
    public static List<PassengerObject> offer_accepted(String sid, int trip_id) {
        listToParse.clear();
        listToParse.add(new ParamObject("sid", sid, false));
        
        listToParse.add(new ParamObject("trip_id", String.valueOf(trip_id), true));
        JsonObject object = null;
        
        try {
            object = JSonRequestProvider.doRequest(listToParse, "offer_accepted.php", false);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean suc = false;
        List<PassengerObject> objects = new ArrayList<PassengerObject>();
        String status = "";
        JsonArray array_passengers;
        if (object != null) {
            suc = object.get("successful").getAsBoolean();
            if (suc) {
                array_passengers = object.get("passengers").getAsJsonArray();
                for (JsonElement jsonElement : array_passengers) {
                    JsonObject pass_obj = jsonElement.getAsJsonObject();
                    PassengerObject passenger = new PassengerObject(pass_obj.get("passenger").getAsInt(), pass_obj.get(
                            "picked_up").getAsBoolean());
                    objects.add(passenger);
                }
            }
        }
        return objects;
    }
    
    
    /**
     * Returns the history for the user
     * 
     * @param sid
     * @param role
     *            see {@link Constants}
     * @return List of {@link HistoryRideObject}
     */
    public static List<HistoryRideObject> getHistory(String sid, String role) {
        
        Log.i(TAG, "SID:" + sid);
        Log.i(TAG, "Role:" + role);
        
        listToParse.clear();
        listToParse.add(new ParamObject("sid", sid, false));
        listToParse.add(new ParamObject("role", role, true));
        
        JsonObject object = null;
        try {
            object = JSonRequestProvider.doRequest(listToParse, "ride_history.php", false);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        List<HistoryRideObject> historyObjects = null;
        List<HistoryPersonObject> historyPersons = null;
        boolean suc = false;
        JsonArray array_rides;
        if (object != null) {
            suc = object.get("successful").getAsBoolean();
            if (suc) {
                array_rides = object.get("rides").getAsJsonArray();
                Log.i(TAG, array_rides.toString());
                historyObjects = new ArrayList<HistoryRideObject>();
                historyPersons = new ArrayList<HistoryPersonObject>();
                for (int i = 0; i < array_rides.size(); i++) {
                    JsonObject IObject = array_rides.get(i).getAsJsonObject();
                    int tripid = IObject.get("trip").getAsInt();
                    int avail_seats = IObject.get("avail_seats").getAsInt();
                    String destination = IObject.get("destination").getAsString();
                    String creation = IObject.get("creation").getAsString();
                    String ending = IObject.get("ending").getAsString();
                    
                    if (role.equals(Constants.ROLE_DRIVER)) {
                        JsonArray array_passengers = IObject.get("passengers").getAsJsonArray();
                        for (int j = 0; j < array_passengers.size(); j++) {
                            JsonObject passObjects = array_passengers.get(j).getAsJsonObject();
                            
                            int userid = passObjects.get("userid").getAsInt();
                            String username = passObjects.get("username").getAsString();
                            float rating = passObjects.get("rating").getAsFloat();
                            int rating_num = passObjects.get("rating_num").getAsInt();
                            boolean rated = passObjects.get("rated").getAsBoolean();
                            
                            HistoryPersonObject person = new HistoryPersonObject(userid, username, rating, rating_num,
                                    rated);
                            historyPersons.add(person);
                        }
                    } else {
                        JsonArray array_drivers = IObject.get("passengers").getAsJsonArray();
                        for (int j = 0; j < array_drivers.size(); j++) {
                            JsonObject passObjects = array_drivers.get(j).getAsJsonObject();
                            
                            int userid = passObjects.get("userid").getAsInt();
                            String username = passObjects.get("username").getAsString();
                            float rating = passObjects.get("rating").getAsFloat();
                            int rating_num = passObjects.get("rating_num").getAsInt();
                            boolean rated = passObjects.get("rated").getAsBoolean();
                            
                            HistoryPersonObject person = new HistoryPersonObject(userid, username, rating, rating_num,
                                    rated);
                            historyPersons.add(person);
                        }
                        JsonObject driver = IObject.get("driver").getAsJsonObject();
                        
                        int userid = driver.get("userid").getAsInt();
                        String username = driver.get("username").getAsString();
                        float rating = driver.get("rating").getAsFloat();
                        int rating_num = driver.get("rating_num").getAsInt();
                        boolean rated = driver.get("rated").getAsBoolean();
                        
                        HistoryPersonObject person = new HistoryPersonObject(userid, username, rating, rating_num,
                                rated);
                        historyPersons.add(person);
                    }
                    HistoryRideObject ride = new HistoryRideObject(tripid, avail_seats, creation, ending, destination,
                            historyPersons);
                    historyObjects.add(ride);
                }
            }
            Model.getInstance().setHistoryObjHolder(historyObjects);
        }
        
        return historyObjects;
    }
    
    
    /**
     * Dummy method don't touch it
     * 
     * @param name
     * @return
     */
    public static String dummyMethod(String name, String mood) {
        Log.i(TAG, "To Parse name:" + name);
        Log.i(TAG, "To Parse mood:" + mood);
        listToParse.clear();
        listToParse.add(new ParamObject("name", name, true));
        listToParse.add(new ParamObject("mood", mood, false));
        
        JsonObject object = null;
        String out_pos = null;
        String out_get = null;
        try {
            object = JSonRequestProvider.doRequest(listToParse, "test.php", false);
            
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (object != null) {
            out_pos = object.get("output_pos").toString();
            out_get = object.get("output_get").toString();
        }
        
        Log.i(TAG, "Postoutput:" + out_pos);
        Log.i(TAG, "Getoutput:" + out_get);
        return null;
    }
    
}
