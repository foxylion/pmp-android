/*
 * Copyright 2011 pmp-android development team
 * Project: CalendarApp
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.apps.calendarapp.model;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Represents one date object that is stored
 * 
 * @author Thorsten Berberich
 * 
 */
public class Appointment {
    
    /**
     * The description of the date
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
     *            Description of the date
     * @param date
     *            date as String
     */
    public Appointment(int id, String descrpition, Date date) {
        this.descrpition = descrpition;
        this.date = date;
        this.id = id;
    }
    
    
    /**
     * Getter for the description
     * 
     * @return the description of the date
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
     * Returns the string representation for the list view
     */
    @Override
    public String toString() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(this.date);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        return this.descrpition + "\n" + dateFormat.format(cal.getTime());
    }
}
