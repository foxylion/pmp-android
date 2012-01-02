package de.unistuttgart.ipvs.pmp.apps.vhike.model;

import java.util.List;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.OfferObject;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.QueryObject;

/**
 * Model for vHike, it holds all necessary informations to work with
 * @author Alexander Wassiljew
 *
 */
public class Model {

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
		if(this.queryHolder!=null)
		Log.i(String.valueOf("Diese: " + this.queryHolder.size()));
		Log.i(String.valueOf("Neue" +queryHolder.size()));
		this.queryHolder = queryHolder;
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
	
	public int getQueryId(){
		return queryId;
	}
	
	public void setQueryId(int qid){
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
	private Model(){
		
	}
	
	/**
	 * Returns an instance of {@link Model}
	 * @return instance
	 */
	public static Model getInstance(){
		if(theInstance!=null){
			return theInstance;
		}else{
			theInstance = new Model();
			return theInstance;
		}
	}
	/**
	 * Returns the Session ID
	 * @return sid
	 */
	public String getSid() {
		return sid;
	}
	/**
	 * Set the actual sid
	 * @param sid
	 */
	public void setSid(String sid) {
		this.sid = sid;
	}

	/**
	 * Returns the own profile
	 * @return {@link Profile}
	 */
	public Profile getOwnProfile() {
		return ownProfile;
	}
	/**
	 * Set {@link Profile}
	 * @param ownProfile
	 */
	public void setOwnProfile(Profile ownProfile) {
		this.ownProfile = ownProfile;
	}
	
	
	
}
