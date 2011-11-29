package de.unistuttgart.ipvs.pmp.apps.vhike.model;

import java.util.Date;
/**
 * {@link Profile} represent an user.
 * 
 * @author Alexander Wassiljew
 *
 */
public class Profile {

	/** 
	 * Fields
	 */
	private String user_name;
	private String firstname;
	private String lastname;
	
	private int rating;
	private Date regDate;
	
	/**
	 * Constructor to initialize a profile
	 * @param user_name
	 * @param firstname
	 * @param lastname
	 * @param rating
	 * @param regDate
	 */
	public Profile(String user_name, String firstname, String lastname,
			int rating, Date regDate) {
		super();
		this.user_name = user_name;
		this.firstname = firstname;
		this.lastname = lastname;
		this.rating = rating;
		this.regDate = regDate;
	}
	/**
	 * @return user name of a user 
	 */
	public String getUser_name() {
		return user_name;
	}
	/**
	 * @return firstname of a user 
	 */
	public String getFirstname() {
		return firstname;
	}
	/**
	 * @return lastname of a user 
	 */
	public String getLastname() {
		return lastname;
	}
	/**
	 * @return rating of a user 
	 */
	public int getRating() {
		return rating;
	}
	/**
	 * @return registration date of a user
	 */
	public Date getRegDate() {
		return regDate;
	}
	
	
	
}
