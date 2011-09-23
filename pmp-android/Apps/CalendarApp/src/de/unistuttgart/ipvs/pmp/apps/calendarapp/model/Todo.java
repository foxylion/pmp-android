package de.unistuttgart.ipvs.pmp.apps.calendarapp.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Represents one date object that is stored
 * 
 * @author Thorsten
 * 
 */
public class Todo {
    
    /**
     * The descrption of the date
     */
    private String descrpition;
    
    /**
     * Date as string representation
     */
    private Date date;
    
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
    public Todo(int id, String descrpition, Date date) {
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
        return this.descrpition;
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
    public Date getDate() {
        return this.date;
    }
    
    
    /**
     * Sets the given date
     * 
     * @param date
     *            to set
     */
    public void setDate(Date date) {
        this.date = date;
    }
    
    
    /**
     * Returns the id of the date
     * 
     * @return the id
     */
    public Integer getId() {
        return this.id;
    }
    
    
    /**
     * Returns the string represetation for the list view
     */
    @Override
    public String toString() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return this.descrpition + "\n" + formatter.format(cal.getTime());
    }
}
