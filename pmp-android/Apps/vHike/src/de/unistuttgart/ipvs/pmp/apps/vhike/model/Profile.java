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
    
    private int id;
    
    private String firstname;
    private String lastname;
    private String tel;
    
    private String description;
    
    private double rating_avg;
    private double rating_num;
    private Date regDate;
    
    private String ownStatus;
    
    private boolean email_pub;
    private boolean firstname_pub;
    private boolean lastname_pub;
    private boolean tel_pub;
    
    
    /**
     * Constructor to initialize a profile
     * 
     * @param id
     * @param user_name
     * @param firstname
     * @param lastname
     * @param email
     * @param description
     * @param validationStatus
     * @param rating_avg
     * @param rating_num
     * @param regDate
     * 
     */
    public Profile(int id, String username, String email, String firstname, String lastname, String tel,
            String description, Date regDate, boolean email_pub, boolean firstname_pub, boolean lastname_pub,
            boolean tel_pub, double rating_avg, double rating_num) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.tel = tel;
        this.description = description;
        
        this.rating_avg = rating_avg;
        this.rating_num = rating_num;
        this.regDate = regDate;
        this.email_pub = email_pub;
        this.firstname_pub = firstname_pub;
        this.lastname_pub = lastname_pub;
        this.tel_pub = tel_pub;
    }
    
    
    public int getID() {
        return this.id;
    }
    
    
    /**
     * 
     * @return username
     */
    public String getUsername() {
        return this.username;
    }
    
    
    /**
     * Returns true, if email is public
     * 
     * @return
     */
    public boolean isEmail_pub() {
        return this.email_pub;
    }
    
    
    /**
     * Returns true, if firstname is public
     * 
     * @return
     */
    public boolean isFirstname_pub() {
        return this.firstname_pub;
    }
    
    
    /**
     * Returns true, if lastname is public
     * 
     * @return lastname of a user
     */
    public boolean isLastname_pub() {
        return this.lastname_pub;
    }
    
    
    /**
     * Returns true, if tel is public
     * 
     * @return
     */
    public boolean isTel_pub() {
        return this.tel_pub;
    }
    
    
    /**
     * 
     * @return tel nr of an user
     */
    public String getTel() {
        return this.tel;
    }
    
    
    /**
     * 
     * @return email of an user
     */
    public String getEmail() {
        return this.email;
    }
    
    
    /**
     * @return firstname of a user
     */
    public String getFirstname() {
        return this.firstname;
    }
    
    
    /**
     * @return lastname of a user
     */
    public String getLastname() {
        return this.lastname;
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
        return this.description;
    }
    
    
    /**
     * @return average rating of a user
     */
    public double getRating_avg() {
        return this.rating_avg;
    }
    
    
    /**
     * @return the number how many times a user was rated
     */
    public double getRating_num() {
        return this.rating_num;
    }
    
    
    /**
     * @return registration date of a user
     */
    public Date getRegDate() {
        return this.regDate;
    }
    
    
    /**
     * Returns the status
     * 
     * @return
     */
    public String getOwnStatus() {
        return this.ownStatus;
    }
    
    
    /**
     * Set status to be read later
     * 
     * @param ownStatus
     */
    public void setOwnStatus(String ownStatus) {
        this.ownStatus = ownStatus;
    }
    
}
