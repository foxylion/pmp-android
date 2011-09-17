package de.unistuttgart.ipvs.pmp.apps.calendarapp.model;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Button;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.exception.DateNotFoundException;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities.CalendarAppActivity;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector.SqlConnector;

public class Model {

    /**
     * Instance of this class
     */
    private static Model instance;

    /**
     * Holds all stored dates
     */
    private ArrayList<Date> dateList = new ArrayList<Date>();

    /**
     * Stores the highest id
     */
    public static int highestId = 0;

    /**
     * The context of the app
     */
    private CalendarAppActivity appContext;

    /**
     * Array adapter of the list to refresh it
     */
    private ArrayAdapter<Date> arrayAdapter;

    /**
     * The newDate button of the app to dis- and enable it
     */
    private Button newDateButton;

    /**
     * Private constructor because of singleton
     */
    private Model() {
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

    public void loadDates() {
	dateList.clear();
	for (Date date : SqlConnector.getInstance().loadDates()){
	    Log.v("Adding date");
	    dateList.add(date);
	}
	
	Collections.sort(dateList, new DateComparator());
	arrayAdapter.notifyDataSetChanged();
    }

    /**
     * Sets the array adapter that the model can refresh the list when something
     * is changed
     * 
     * @param adapter
     *            the ArrayAdapter of the list with dates
     */
    public void setArrayAdapter(ArrayAdapter<Date> adapter) {
	this.arrayAdapter = adapter;
    }

    /**
     * Sets the context of the app
     * 
     * @param context
     *            context of the app
     */
    public void setContext(CalendarAppActivity context) {
	appContext = context;
    }

    /**
     * Returns the context of the app
     * 
     * @return app context
     */
    public CalendarAppActivity getContext() {
	return appContext;
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
	arrayAdapter.notifyDataSetChanged();
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
	arrayAdapter.notifyDataSetChanged();
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
	arrayAdapter.notifyDataSetChanged();
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
	arrayAdapter.notifyDataSetChanged();
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
	highestId++;
	return highestId;
    }

    /**
     * Returns the service level of the app
     * 
     * @return the service level
     */
    public int getServiceLevel() {
	SharedPreferences app_preferences = PreferenceManager
		.getDefaultSharedPreferences(appContext);
	return app_preferences.getInt("servicelevel", 0);
    }

    /**
     * Sets the service level and stores it at the preferences of the app
     * 
     * @param serviceLevel
     *            the service level to set
     */
    public void setServiceLevel(int serviceLevel) {
	SharedPreferences app_preferences = PreferenceManager
		.getDefaultSharedPreferences(appContext);
	SharedPreferences.Editor editor = app_preferences.edit();
	editor.putInt("servicelevel", serviceLevel);
	if (!editor.commit()) {
	    Log.e("Error while commiting preferences");
	}
    }

    /**
     * Clears the local stored list of dates but not the dates stored at the
     * database
     */
    public void clearLocalList() {
	dateList.clear();
	arrayAdapter.notifyDataSetChanged();
    }

    /**
     * Returns the new date button of the app
     * 
     * @return the new date button
     */
    public Button getNewDateButton() {
	return newDateButton;
    }

    /**
     * Sets the new date button
     * 
     * @param newDateButton
     *            button of the app
     */
    public void setNewDateButton(Button newDateButton) {
	this.newDateButton = newDateButton;
    }

}
