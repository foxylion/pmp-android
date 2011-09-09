package de.unistuttgart.ipvs.pmp.apps.sqlConnector;

import java.util.ArrayList;

import de.unistuttgart.ipvs.pmp.apps.model.Date;
import de.unistuttgart.ipvs.pmp.apps.model.Model;

public class SqlConnector {
    /**
     * Instance of this class
     */
    private static SqlConnector instance;

    /**
     * Private constructor because of singleton
     */
    private SqlConnector() {
    }

    /**
     * Returns the stored instance of the class or creates a new one if there is
     * none
     * 
     * @return instance of this class
     */
    public static SqlConnector getInstance() {
	if (instance == null) {
	    instance = new SqlConnector();
	}
	return instance;
    }

    /**
     * Loads the dates stored in the SQL database. This is called when the
     * {@link Model} is instantiated
     * 
     * @return ArrayList<Date> with the loaded dates out of the SQL database
     */
    public ArrayList<Date> loadDates() {
	ArrayList<Date> dList = new ArrayList<Date>();
//	for (int i = 0; i < 5; i++) {
//	    dList.add(new Date(1, "abc", "12.12.2001"));
//	    dList.add(new Date(2, "cde", "01.02.1999"));
//	    dList.add(new Date(3, "efg", "01.01.2200"));
//	}
	Model.highestId = 4;
	return dList;
    }

    /**
     * Stores the date in the SQL Database
     * 
     * @param date
     */
    public void storeNewDate(int id, String date, String description) {
	/*
	 * TODO Store the date in the SQL Database
	 */
    }

    /**
     * Delete the date out of the SQL database with the given id
     * 
     * @param id
     *            id of the date to delete
     */
    public void deleteDate(int id) {
	/*
	 * TODO
	 */
    }

    /**
     * Changes the date at the SQL database
     * 
     * @param date
     *            date that has changed
     */
    public void changeDate(int id, String date, String description) {
	/*
	 * TODO
	 */
    }

}
