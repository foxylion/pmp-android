package de.unistuttgart.ipvs.pmp.apps.vhike.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.FoundProfilePos;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.SliderObject;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.HistoryRideObject;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.JSonRequestReader;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.OfferObject;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.PassengerObject;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.PositionObject;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.QueryObject;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.RideObject;

/**
 * Controls the behavior of vHike
 * 
 * @author Alexander Wassiljew
 * 
 */
public class ControllerWS {
    
    /**
     * Constructor
     */
    public ControllerWS() {
    }
    
    
    /**
     * Announce a trip to the web service
     * 
     * @param session_id
     * @param destination
     * @return TRIP_STATUS_ANNOUNCED, TRIP_STATUS_OPEN_TRIP,STATUS_ERROR
     */
    public int announceTrip(final String session_id, final String destination, final float current_lat,
            final float current_lon, final int avail_seats) {
        Log.i(this, session_id + ", " + destination + ", " + current_lat + ", " + current_lat + ", " + avail_seats);
        final String status = JSonRequestReader.announceTrip(session_id, destination, current_lat, current_lon,
                avail_seats);
        
        if (status.equals("announced")) {
            return Constants.TRIP_STATUS_ANNOUNCED;
        } else if (status.equals("open_trip_exists")) {
            return Constants.TRIP_STATUS_OPEN_TRIP;
        }
        return Constants.STATUS_ERROR;
        
    }
    
    
    /**
     * End the active trip
     * 
     * @param sid
     * @param trip_id
     * @return STATUS_UPDATED, STATUS_UPTODATE, STATUS_NOTRIP, STATUS_HASENDED
     *         STATUS_INVALID_USER see {@link Constants} and design.html
     */
    public int endTrip(final String sid, final int trip_id) {
        final String status = JSonRequestReader.endTrip(sid, trip_id);
        
        if (status.equals("updated")) {
            return Constants.STATUS_UPDATED;
        } else if (status.equals("already_uptodate")) {
            return Constants.STATUS_UPTODATE;
        } else if (status.equals("no_trip")) {
            return Constants.STATUS_NO_TRIP;
        } else if (status.equals("has_ended")) {
            return Constants.STATUS_HASENDED;
        } else if (status.equals("invalid_user")) {
            return Constants.STATUS_INVALID_USER;
        }
        return 0;
    }
    
    
    /**
     * Returns the History of an user
     * 
     * @param sid
     * @param role
     * @return
     */
    public List<HistoryRideObject> getHistory(final String sid, final String role) {
        final List<HistoryRideObject> list = JSonRequestReader.getHistory(sid, role);
        Log.i(this, "getHistory history size: " + list.size());
        return list;
    }
    
    
    /**
     * Returns the Profile of an user
     * 
     * @param user_id
     * @return {@link Profile}
     */
    public Profile getProfile(final String session_id, final int user_id) {
        final Profile profile = JSonRequestReader.getProfile(session_id, user_id);
        Log.i(this, "Controller->Profile->Description:" + profile.getDescription());
        return profile;
    }
    
    
    public PositionObject getUserPosition(final String sid, final int user_id) {
        final PositionObject object = JSonRequestReader.getUserPosition(sid, user_id);
        
        return object;
    }
    
    
    /**
     * Hitchhiker can accept or decline offers
     * 
     * @param sid
     * @param offer_id
     * @param accept
     * @return STATUS_HANDLED, STATUS_INVALID_OFFER, STATUS_INVALID_USER,
     *         STATUS_ERROR
     */
    public int handleOffer(final String sid, final int offer_id, final boolean accept) {
        final String status = JSonRequestReader.handleOffer(sid, offer_id, accept);
        
        if (!status.equals("")) {
            if (status.equals("accepted")) {
                return Constants.STATUS_HANDLED;
            } else if (status.equals("invalid_offer")) {
                return Constants.STATUS_INVALID_OFFER;
            } else if (status.equals("invalid_user")) {
                return Constants.STATUS_INVALID_USER;
            } else if (status.equals("denied")) {
                return Constants.STATUS_ERROR;
            } else if (status.equals("cannot_update")) {
                return Constants.STATUS_ERROR;
            }
        }
        return Constants.STATUS_ERROR;
    }
    
    
    /**
     * Checks if the user where picked up
     * 
     * @param sid
     * @return true if picked up, false otherwise
     */
    public boolean isPicked(final String sid) {
        final boolean bool = JSonRequestReader.isPicked(sid);
        return bool;
    }
    
    
    /**
     * Log on an user and save the session id in the {@link Model}
     * 
     * @param username
     * @param pw
     * @return true if succeed
     */
    public boolean login(final String username, final String pw) {
        
        Log.i(this, "USERNAME: " + username);
        Log.i(this, "PASSSWORD: " + pw);
        final String status = JSonRequestReader.login(username, pw);
        Log.i(this, "Status im CTRL: " + status);
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
    public boolean logout(final String sid) {
        final String status = JSonRequestReader.logout(sid);
        if (status.equals("logged_out")) {
            return true;
        } else {
            return false;
        }
    }
    
    
    public List<SliderObject> mergeQOLwithFU(final List<QueryObject> qobjs, final List<FoundProfilePos> foundList) {
        final List<SliderObject> sliderList = new ArrayList<SliderObject>();
        for (final FoundProfilePos foundProfile : foundList) {
            sliderList.add(new SliderObject(foundProfile));
        }
        for (final QueryObject objects : qobjs) {
            sliderList.add(new SliderObject(new FoundProfilePos(objects.getUserid(), objects.getCur_lat(), objects
                    .getCur_lon(), objects.getQueryid())));
        }
        return sliderList;
    }
    
    
    /**
     * Get a List with Passengers and their status to pick up.
     * 
     * @param sid
     *            Session id
     * @param trip_id
     *            Trip id
     * @return List of {@link PassengerObject}
     */
    public int offer_accepted(final String sid, final int offer_id) {
        final String status = JSonRequestReader.offer_accepted(sid, offer_id);
        if (status.equals("unread")) {
            return Constants.STATUS_UNREAD;
        } else if (status.equals("accepted")) {
            return Constants.STATUS_ACCEPTED;
        } else if (status.equals("denied")) {
            return Constants.STATUS_DENIED;
        }
        
        return Constants.STATUS_UNREAD;
    }
    
    
    /**
     * Picks up a hitchhiker
     * 
     * @param sid
     * @param user_id
     * @return true if succeeded, false otherwise
     */
    public boolean pick_up(final String sid, final int user_id) {
        final boolean bool = JSonRequestReader.pick_up(sid, user_id);
        
        return bool;
    }
    
    
    public String rateUser(final String sid, final int userid, final int tripid, final int rating) {
        final String status = JSonRequestReader.rateUser(sid, userid, tripid, rating);
        Log.i(this, "CONTROLLER STATUS AFTER RATING:" + status);
        return status;
    }
    
    
    /**
     * Register an user
     * 
     * @return Code specified in {@link Constants}
     */
    public int register(final Map<String, String> list) {
        
        final String status = JSonRequestReader.register(list.get("username"), list.get("password"), list.get("email"),
                list.get("firstname"), list.get("lastname"), list.get("tel"), list.get("description"),
                Boolean.parseBoolean(list.get("email_public")), Boolean.parseBoolean(list.get("firstname_public")),
                Boolean.parseBoolean(list.get("lastname_public")), Boolean.parseBoolean(list.get("tel_public")));
        
        if (status.equals("registered")) {
            return Constants.STATUS_SUCCESS;
        } else if (status.contains("username_exists")) {
            return Constants.REG_STAT_USED_USERNAME;
        } else if (status.contains("email_exists")) {
            return Constants.REG_STAT_USED_MAIL;
        } else if (status.contains("invalid_username")) {
            return Constants.REG_STAT_INVALID_USERNAME;
        } else if (status.contains("invalid_password")) {
            return Constants.REG_STAT_INVALID_PW;
        } else if (status.contains("invalid_firstname")) {
            return Constants.REG_STAT_INVALID_FIRSTNAME;
        } else if (status.contains("invalid_lastname")) {
            return Constants.REG_STAT_INVALID_LASTNAME;
        } else if (status.contains("invalid_tel")) {
            return Constants.REG_STAT_INVALID_TEL;
        }
        
		return Constants.STATUS_ERROR;
    }
    
    
    /**
     * Driver search for potential hitchhikers
     * 
     * @param sid
     * @param lat
     * @param lon
     * @param perimeter
     * @return List if QueryObjects otherwise, null
     */
    public List<QueryObject> searchQuery(final String sid, final float lat, final float lon, final int perimeter) {
        final List<QueryObject> queryList = JSonRequestReader.searchQuery(sid, lat, lon, perimeter);
        
        return queryList;
        
    }
    
    
    /**
     * Hitchhiker search for the drivers in the given perimeter
     * 
     * @param sid
     * @param lat
     * @param lon
     * @param perimeter
     * @return List if QueryObjects otherwise, null
     */
    public List<RideObject> searchRides(final String sid, final float lat, final float lon, final int perimeter) {
        final List<RideObject> queryList = JSonRequestReader.searchRides(sid, lat, lon, perimeter);
        
        return queryList;
        
    }
    
    
    /**
     * Sends an offer to the hitchhiker
     * 
     * @param sid
     * @param trip_id
     * @param query_id
     * @param message
     * @return STATUS_SENT, STATUS_INVALID_TRIP, STATUS_INVALID_QUERY,
     *         STATUS_ALREADY_SENT see {@link Constants}
     */
    public int sendOffer(final String sid, final int trip_id, final int query_id, final String message) {
        final String status = JSonRequestReader.sendOffer(sid, trip_id, query_id, message);
        
        if (!status.equals("")) {
            
            if (status.equals("invalid_trip")) {
                return Constants.STATUS_INVALID_TRIP;
            } else if (status.equals("invalid_query")) {
                return Constants.STATUS_INVALID_QUERY;
            } else if (status.equals("already_sent")) {
                return Constants.STATUS_ALREADY_SENT;
            } else {
                return Integer.valueOf(status);
            }
        }
        return Constants.STATUS_ERROR;
    }
    
    
    /**
     * Starts the Query and returns the id, if the creation succeeded
     * 
     * @param sid
     * @param destination
     * @param current_lat
     * @param current_lon
     * @param avail_seats
     * @return QUERY_ID_ERROR || queryId
     */
    public int startQuery(final String sid, final String destination, final float current_lat, final float current_lon,
            final int avail_seats) {
        final int queryId = JSonRequestReader.startQuery(sid, destination, current_lat, current_lon, avail_seats);
        if (queryId != Constants.QUERY_ID_ERROR) {
            Model.getInstance().setQueryId(queryId);
            return queryId;
        } else {
            return Constants.QUERY_ID_ERROR;
        }
    }
    
    
    /**
     * Delete the active query.
     * 
     * @param sid
     * @param queryId
     * @return
     *         STATUS_QUERY_DELETED,STATUS_NO_QUERY,STATUS_INVALID_USER,STATUS_ERROR
     */
    public int stopQuery(final String sid, final int queryId) {
        final String status = JSonRequestReader.stopQuery(sid, queryId);
        
        if (status != null) {
            if (status.equals("deleted")) {
                return Constants.STATUS_QUERY_DELETED;
            } else if (status.equals("no_query")) {
                return Constants.STATUS_NO_QUERY;
            } else if (status.equals("invalid_user")) {
                return Constants.STATUS_INVALID_USER;
            }
        }
        return Constants.STATUS_ERROR;
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
    public int tripUpdateData(final String sid, final int trip_id, final int avail_seats) {
        final String status = JSonRequestReader.tripUpdateData(sid, trip_id, avail_seats);
        
        if (status.equals("updated")) {
            return Constants.STATUS_UPDATED;
        } else if (status.equals("already_uptodate")) {
            return Constants.STATUS_UPTODATE;
        } else if (status.equals("no_trip")) {
            return Constants.STATUS_NO_TRIP;
        } else if (status.equals("has_ended")) {
            return Constants.STATUS_HASENDED;
        } else if (status.equals("invalid_user")) {
            return Constants.STATUS_INVALID_USER;
        }
        return 0;
    }
    
    
    /**
     * Updates the users position
     * 
     * @param sid
     * @param lat
     * @param lon
     * @return STATUS_UPDATED, STATUS_UPTODATE, STATUS_ERROR
     */
    public int userUpdatePos(final String sid, final float lat, final float lon) {
        final String status = JSonRequestReader.userUpdatePos(sid, lat, lon);
        
        if (status.equals("updated")) {
            return Constants.STATUS_UPDATED;
        } else if (status.equals("no_update")) {
            return Constants.STATUS_UPTODATE;
        } else if (status.equals("update_fail")) {
            return Constants.STATUS_ERROR;
        }
        return Constants.STATUS_ERROR;
    }
    
    
    /**
     * 
     * @param sid
     * @return List if OfferObjects otherwise, null
     */
    public List<OfferObject> viewOffers(final String sid) {
        final List<OfferObject> offerList = JSonRequestReader.viewOffer(sid);
        return offerList;
        
    }
    
}