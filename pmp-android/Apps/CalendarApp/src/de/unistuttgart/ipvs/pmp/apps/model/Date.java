package de.unistuttgart.ipvs.pmp.apps.model;

/**
 * Represents one date object that is stored
 * 
 * @author Thorsten
 * 
 */
public class Date {

    /**
     * The descrption of the date
     */
    private String descrpition;

    /**
     * Date as string representation
     */
    private String date;
    
    /**
     * Unique id to identifiy the date
     */
    private Integer id;

    /**
     * Constructor to create a new date object
     * 
     * @param descrpition
     *            descrpition of the date
     * @param date
     *            date as String
     */
    public Date(int id, String descrpition, String date) {
	this.descrpition = descrpition;
	this.date = date;
	this.id = id;
    }

    /**
     * Getter for the description
     * 
     * @return the descrpition of the date
     */
    public String getDescrpition() {
	return descrpition;
    }

    /**
     * Sets the description
     * 
     * @param description
     *            to set
     */
    public void setDescription(String description) {
	this.descrpition = description;
    }

    /**
     * Returns the date
     * 
     * @return the date as string
     */
    public String getDate() {
	return date;
    }

    /**
     * Sets the given date
     * 
     * @param date
     *            to set
     */
    public void setDate(String date) {
	this.date = date;
    }

    /**
     * Returns the id of the date
     * @return the id
     */
    public Integer getId() {
	return id;
    }

    /**
     * Returns the string represetation for the list view
     */
    @Override
    public String toString() {
	return descrpition + "\n" + date;
    }
}
