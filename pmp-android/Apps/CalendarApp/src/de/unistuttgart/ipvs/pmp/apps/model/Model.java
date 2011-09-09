package de.unistuttgart.ipvs.pmp.apps.model;

import java.util.ArrayList;
import java.util.Collections;

import de.unistuttgart.ipvs.pmp.apps.exception.DateNotFoundException;
import de.unistuttgart.ipvs.pmp.apps.sqlConnector.SqlConnector;

public class Model {

    /**
     * Instance of this class
     */
    private static Model instance;

    /**
     * Holds all stored dates
     */
    private ArrayList<Date> dateList;

    /**
     * Stores the highest id
     */
    public static int highestId = 0;

    /**
     * Private constructor because of singleton
     */
    private Model() {
	dateList = SqlConnector.getInstance().loadDates();
	Collections.sort(dateList, new DateComparator());
    }

    /**
     * Returns the stored instance of the class or creates a new one if there is
     * none
     * 
     * @return instance of this class
     */
    public static Model getInstance() {
	if (instance == null) {
	    instance = new Model();
	}
	return instance;
    }

    /**
     * Adds the date to the model and stores it at the database
     * 
     * @param date
     *            date to store
     */
    public void addDate(Date date) {
	SqlConnector.getInstance().storeNewDate(date.getId(), date.getDate(),
		date.getDescrpition());
	/*
	 * If no error happened then store it local
	 */
	dateList.add(date);
	Collections.sort(dateList, new DateComparator());
    }

    /**
     * Deletes the date at the model and at the database
     * 
     * @param id
     *            id of the date to delete
     */
    public void deleteDateByID(int id) {
	SqlConnector.getInstance().deleteDate(id);
	/*
	 * If no error happened delete it local
	 */
	for (Date date : dateList) {
	    if (date.getId() == id) {
		dateList.remove(date);
	    }
	}
    }

    /**
     * Deletes the date at the model and at the database
     * 
     * @param index
     *            index of the date object
     */
    public void deleteDateByIndex(int index) {
	Date date = dateList.get(index);
	SqlConnector.getInstance().deleteDate(date.getId());
	/*
	 * If no error happened delete it local
	 */
	dateList.remove(index);
    }

    /**
     * Changes the date
     * 
     * @param id
     *            unique id of the date
     * @param dateString
     *            string representation of the date
     * @param description
     *            descrpition of the date
     */
    public void changeDate(int id, String dateString, String description) {
	SqlConnector.getInstance().changeDate(id, dateString, description);
	/*
	 * If no error happend change it local
	 */
	for (Date date : dateList) {
	    if (date.getId() == id) {
		date.setDate(dateString);
		date.setDescription(description);
		Collections.sort(dateList, new DateComparator());
	    }
	}
    }

    /**
     * Returns the date with the given id
     * 
     * @param id
     *            id of the searched date
     * @return the date
     * @throws DateNotFoundException
     *             thrown if the date was not found
     */
    public Date getDateById(int id) throws DateNotFoundException {
	for (Date date : dateList) {
	    if (date.getId() == id) {
		return date;
	    }
	}
	throw new DateNotFoundException();
    }

    /**
     * Returns the date at the given index of the list
     * 
     * @param index
     *            index of the date
     * @return the date
     */
    public Date getDateByIndex(int index) {
	return dateList.get(index);
    }

    /**
     * Returns the whole list of dates
     * 
     * @return
     */
    public ArrayList<Date> getDateList() {
	return dateList;
    }

    /**
     * Returns a new id for a new date
     * 
     * @return
     */
    public int getNewId() {
	return highestId + 1;
    }

}
