package de.unistuttgart.ipvs.pmp.apps.vhike.model;
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
