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
	private String username;
	private String email;
	
	private String firstname;
	private String lastname;
	private String tel;
	
	private String description;
	
	private double rating;
	private Date regDate;
	
	private String ownStatus;
	
	private boolean email_pub;
	private boolean firstname_pub;
	private boolean lastname_pub;
	private boolean tel_pub;
	
	/**
	 * Constructor to initialize a profile
	 * 
	 * @param user_name
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param description
	 * @param validationStatus
	 * @param rating
	 * @param regDate
	 * 
	 */
	public Profile(String username, String email, String firstname, String lastname,
			String tel, String description, double rating, Date regDate,
			boolean email_pub, boolean firstname_pub, boolean lastname_pub, boolean tel_pub) {
		this.username = username;
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.tel = tel;
	
		this.rating = rating;
		this.regDate = regDate;
		this.email_pub = email_pub;
		this.firstname_pub = firstname_pub;
		this.lastname_pub = lastname_pub;
		this.tel_pub = tel_pub;
	}
	/**
	 * 
	 * @return username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * Returns true, if email is public
	 * @return 
	 */
	public boolean isEmail_pub() {
		return email_pub;
	}
	/**
	 * Returns true, if firstname is public
	 * @return
	 */
	public boolean isFirstname_pub() {
		return firstname_pub;
	}

	/**
	 * Returns true, if lastname is public
	 * @return  lastname of a user
	 */
	public boolean isLastname_pub() {
		return lastname_pub;
	}
	/**
	 * Returns true, if tel is public
	 * @return
	 */
	public boolean isTel_pub() {
		return tel_pub;
	}
	/**
	 * 
	 * @return tel nr of an user
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * 
	 * @return email of an user
	 */
	public String getEmail() {
		return email;
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
	 * email address of a user
	 * 
	 * @return
	 */

	/**
	 * 
	 * @return description of a user
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return rating of a user
	 */
	public double getRating() {
		return rating;
	}

	/**
	 * @return registration date of a user
	 */
	public Date getRegDate() {
		return regDate;
	}
	
	/**
	 * Returns the status 
	 * @return
	 */
	public String getOwnStatus() {
		return ownStatus;
	}
	/**
	 * Set status to be read later
	 * @param ownStatus
	 */
	public void setOwnStatus(String ownStatus) {
		this.ownStatus = ownStatus;
	}

}
