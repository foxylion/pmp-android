package de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector;

import java.util.ArrayList;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Date;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.service.utils.IConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.ResourceGroupServiceConnector;

public class SqlConnector {
    /**
     * Instance of this class
     */
    private static SqlConnector instance;

    /**
     * The highest id that is set yet
     */
    private int highestId = 0;
    
    /**
     * Identifier of the needed resource group
     */
    private final String resGroupIdentifier = "de.unistuttgart.ipvs.pmp.resourcegroups.database";
    
    /**
     * The connector to the database resource group
     */
    ResourceGroupServiceConnector resGroupCon;

    /**
     * Private constructor because of singleton
     */
    private SqlConnector() {
	resGroupCon = new ResourceGroupServiceConnector(Model.getInstance().getContext().getApplicationContext(),
		((CalendarApp) Model.getInstance().getContext().getApplicationContext()).getSignee(),
		resGroupIdentifier);
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
     */
    public void loadDates() {
	resGroupCon.addCallbackHandler(new IConnectorCallback() {

	    @Override
	    public void disconnected() {
		Log.v(resGroupIdentifier + " disconnected");
	    }

	    @Override
	    public void connected() {
		Log.d("Connected to ResourceGroup " + resGroupIdentifier);
		if (resGroupCon.getAppService() == null){
		    Log.e(resGroupIdentifier + " appService is null");
		} else {
		   // Get resource
		}
	    }

	    @Override
	    public void bindingFailed() {
		Log.e(resGroupIdentifier + " binding failed");
	    }
	});
	resGroupCon.bind();
	
	
	
	ArrayList<Date> dList = new ArrayList<Date>();
	dList.add(new Date(getNewId(), "a", "01.02.2000"));
	dList.add(new Date(getNewId(), "b", "01.03.2000"));
	dList.add(new Date(getNewId(), "c", "01.04.2000"));
	dList.add(new Date(getNewId(), "d", "01.05.2000"));
	
	Model.getInstance().loadDates(dList);
	
    }

    /**
     * Stores the date in the SQL Database
     * 
     * @param date
     */
    public void storeNewDate(String date, String description) {
	/*
	 * TODO Store the date in the SQL Database
	 */
	Model.getInstance().addDate(new Date(getNewId(), description, date));
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
	Model.getInstance().deleteDateByID(id);
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
	Model.getInstance().changeDate(id, date, description);
    }

    /**
     * Returns a new id for a date
     * 
     * @return the new id
     */
    private int getNewId() {
	highestId++;
	return highestId;
    }
}
