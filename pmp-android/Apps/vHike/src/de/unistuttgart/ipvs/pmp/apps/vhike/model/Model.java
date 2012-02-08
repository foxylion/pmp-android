package de.unistuttgart.ipvs.pmp.apps.vhike.model;

import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.HistoryRideObject;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.OfferObject;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.QueryObject;

/**
 * Model for vHike, it holds all necessary informations to work with
 * 
 * @author Alexander Wassiljew
 * 
 */
public class Model {
    
    /**
     * picked up users
     */
    private List<Integer> pickedUsers;
    
    
    public boolean isPicked(int userid) {
        boolean picked = false;
        for (int invID : pickedUsers) {
            if (userid == invID) {
                picked = true;
            }
        }
        return picked;
    }
    
    
    /**
     * Adds a user to the Picked List
     * 
     * @param userid
     */
    public void addToPickedUser(int userid) {
        pickedUsers.add(userid);
    }
    
    
    public void clearPickedUserList() {
        pickedUsers.clear();
    }
    
    
    public void removePickedUser(int userid){
        pickedUsers.remove(userid);
    }
    /**
     * Invited users
     */
    private List<Integer> offeredUser;
    
    
    public boolean isInInvitedList(int userid) {
        boolean invited = false;
        for (int invID : offeredUser) {
            if (userid == invID) {
                invited = true;
            }
        }
        return invited;
    }
    
    
    /**
     * Adds a user to the Offered List
     * 
     * @param userid
     */
    public void addToInvitedUser(int userid) {
        offeredUser.add(userid);
    }
    
    
    public void clearInvitedUserList() {
        offeredUser.clear();
    }
    
    /**
     * Declined userlist
     */
    private List<Integer> bannedUser;
    
    
    /**
     * Checks if user is on bannlist
     * 
     * @param userid
     * @return true if yes
     */
    public boolean isInBannList(int userid) {
        boolean banned = false;
        for (int bannedID : bannedUser) {
            if (userid == bannedID) {
                banned = true;
            }
        }
        
        return banned;
    }
    
    
    /**
     * Adds user to bannlist
     * 
     * @param userid
     */
    public void addToBannList(int userid) {
        bannedUser.add(userid);
    }
    
    
    /**
     * Clear the bannlist
     */
    public void clearBannList() {
        bannedUser.clear();
    }
    
    /**
     * Instance of the Object {@link Model}
     */
    static Model theInstance;
    /**
     * Session ID
     */
    private String sid;
    
    /**
     * This is the trip id, in which the user participate at the moment
     */
    private int tripId;
    /**
     * This is the query id of an user
     */
    private int queryId;
    
    /**
     * Holds queries of the Driver, within are hitchhiker
     */
    private List<QueryObject> queryHolder;
    
    
    public List<QueryObject> getQueryHolder() {
        return queryHolder;
    }
    
    
    public void setQueryHolder(List<QueryObject> queryHolder) {
        this.queryHolder = queryHolder;
    }
    
    private List<HistoryRideObject> historyObjHolder;
    
    
    public List<HistoryRideObject> getHistoryObjHolder() {
        return historyObjHolder;
    }
    
    
    public void setHistoryObjHolder(List<HistoryRideObject> hsitoryObjHolder) {
        this.historyObjHolder = hsitoryObjHolder;
    }
    
    /**
     * Holds offers of the Driver, within are hitchhiker
     */
    private List<OfferObject> offerHolder;
    
    
    public List<OfferObject> getOfferHolder() {
        return offerHolder;
    }
    
    
    public void setOfferHolder(List<OfferObject> offerHolder) {
        this.offerHolder = offerHolder;
    }
    
    
    public int getQueryId() {
        return queryId;
    }
    
    
    public void setQueryId(int qid) {
        this.queryId = qid;
    }
    
    
    public int getTripId() {
        return tripId;
    }
    
    
    public void setTripId(int tripId) {
        this.tripId = tripId;
    }
    
    /**
     * Own profile
     */
    private Profile ownProfile;
    
    
    /**
     * Private Constructor = Singleton
     */
    private Model() {
        bannedUser = new ArrayList<Integer>();
        offeredUser = new ArrayList<Integer>();
        pickedUsers = new ArrayList<Integer>();
    }
    
    
    /**
     * Returns an instance of {@link Model}
     * 
     * @return instance
     */
    public static Model getInstance() {
        if (theInstance != null) {
            return theInstance;
        } else {
            theInstance = new Model();
            return theInstance;
        }
    }
    
    
    /**
     * Returns the Session ID
     * 
     * @return sid
     */
    public String getSid() {
        return sid;
    }
    
    
    /**
     * Set the actual sid
     * 
     * @param sid
     */
    public void setSid(String sid) {
        this.sid = sid;
    }
    
    
    /**
     * Returns the own profile
     * 
     * @return {@link Profile}
     */
    public Profile getOwnProfile() {
        return ownProfile;
    }
    
    
    /**
     * Set {@link Profile}
     * 
     * @param ownProfile
     */
    public void setOwnProfile(Profile ownProfile) {
        this.ownProfile = ownProfile;
    }
    
}
