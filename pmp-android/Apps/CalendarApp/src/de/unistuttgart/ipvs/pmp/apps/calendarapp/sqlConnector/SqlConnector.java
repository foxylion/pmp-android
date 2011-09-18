package de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.RemoteException;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Date;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.resourcegroups.database.IDatabaseConnection;
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
     * Resource identifier TODO
     */
    private String resIdentifier = "";

    /**
     * Private constructor because of singleton
     */
    private SqlConnector() {
	createTable();
    }

    /*
     * Constants for the database table
     */
    private final String DBNAME = "calendarapp";
    private final String ID = "id";
    private final String DATE = "date";
    private final String DESC = "description";

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
     * Loads the dates stored in the SQL database. This method calls
     * {@link Model#loadDates(ArrayList)} to store the dates in the model
     * 
     */
    public void loadDates() {
	final ResourceGroupServiceConnector resGroupCon = new ResourceGroupServiceConnector(
		Model.getInstance().getContext().getApplicationContext(),
		((CalendarApp) Model.getInstance().getContext()
			.getApplicationContext()).getSignee(),
		resGroupIdentifier);
	resGroupCon.addCallbackHandler(new IConnectorCallback() {

	    @Override
	    public void disconnected() {
		Log.v(resGroupIdentifier + " disconnected");
	    }

	    @Override
	    public void connected() {
		Log.d("Connected to ResourceGroup " + resGroupIdentifier);
		if (resGroupCon.getAppService() == null) {
		    Log.e(resGroupIdentifier + " appService is null");
		} else {
		    // Get resource
		    try {
			IDatabaseConnection idc = IDatabaseConnection.Stub
				.asInterface(resGroupCon.getAppService()
					.getResource(resIdentifier));
			// TODO
			// Cursor cursor = idc.queryWithLimit(DBNAME, null,
			// null, null, null, null, null, null);
			// ArrayList<Date> dateList = new ArrayList<Date>();
			// while (!cursor.isLast()) {
			// int id = Integer.valueOf(cursor.getString(0));
			// if (id > highestId){
			// highestId = id;
			// }
			// dateList.add(new Date(id, cursor.getString(2), cursor
			// .getString(1)));
			// cursor.moveToNext();
			// }
			// Model.getInstance().loadDates(dateList);
		    } catch (RemoteException e) {
			Log.e("Remote Exception", e);
		    }
		}
		resGroupCon.unbind();
	    }

	    @Override
	    public void bindingFailed() {
		Log.e(resGroupIdentifier + " binding failed");
	    }
	});
	resGroupCon.bind();

	// ArrayList<Date> dList = new ArrayList<Date>();
	// dList.add(new Date(getNewId(), "a", "01.02.2000"));
	// dList.add(new Date(getNewId(), "b", "01.03.2000"));
	// dList.add(new Date(getNewId(), "c", "01.04.2000"));
	// dList.add(new Date(getNewId(), "d", "01.05.2000"));

	// Model.getInstance().loadDates(dList);

    }

    /**
     * Stores the new date in the SQL Database
     * 
     * @param date
     */
    public void storeNewDate(final String date, final String description) {

	final ResourceGroupServiceConnector resGroupCon = new ResourceGroupServiceConnector(
		Model.getInstance().getContext().getApplicationContext(),
		((CalendarApp) Model.getInstance().getContext()
			.getApplicationContext()).getSignee(),
		resGroupIdentifier);
	resGroupCon.addCallbackHandler(new IConnectorCallback() {

	    @Override
	    public void disconnected() {
		Log.v(resGroupIdentifier + " disconnected");
	    }

	    @Override
	    public void connected() {
		Log.d("Connected to ResourceGroup " + resGroupIdentifier);
		if (resGroupCon.getAppService() == null) {
		    Log.e(resGroupIdentifier + " appService is null");
		} else {
		    // Get resource
		    try {
			IDatabaseConnection idc = IDatabaseConnection.Stub
				.asInterface(resGroupCon.getAppService()
					.getResource(resIdentifier));
			// The values to add
			Map<String, String> values = new HashMap<String, String>();
			int id = getNewId();
			values.put(ID, String.valueOf(id));
			values.put(DATE, date);
			values.put(DESC, description);

			if (idc.insert(DBNAME, null, values) != -1) {
			    Model.getInstance().addDate(
				    new Date(id, description, date));
			} else {
			    Log.e("Date didn't inserted into database");
			}
		    } catch (RemoteException e) {
			Log.e("Remote Exception", e);
		    }
		}
		resGroupCon.unbind();
	    }

	    @Override
	    public void bindingFailed() {
		Log.e(resGroupIdentifier + " binding failed");
	    }
	});
	resGroupCon.bind();
    }

    /**
     * Delete the date out of the SQL database with the given id
     * 
     * @param id
     *            id of the date to delete
     */
    public void deleteDate(final int id) {

	final ResourceGroupServiceConnector resGroupCon = new ResourceGroupServiceConnector(
		Model.getInstance().getContext().getApplicationContext(),
		((CalendarApp) Model.getInstance().getContext()
			.getApplicationContext()).getSignee(),
		resGroupIdentifier);
	resGroupCon.addCallbackHandler(new IConnectorCallback() {

	    @Override
	    public void disconnected() {
		Log.v(resGroupIdentifier + " disconnected");
	    }

	    @Override
	    public void connected() {
		Log.d("Connected to ResourceGroup " + resGroupIdentifier);
		if (resGroupCon.getAppService() == null) {
		    Log.e(resGroupIdentifier + " appService is null");
		} else {
		    // Get resource
		    try {
			IDatabaseConnection idc = IDatabaseConnection.Stub
				.asInterface(resGroupCon.getAppService()
					.getResource(resIdentifier));
			String[] args = new String[1];
			args[0] = String.valueOf(id);
			/*
			 * Delete the date out of the database and if exactly
			 * once removed the remove it out of the model
			 */
			if (idc.delete(DBNAME, ID + " = ?", args) == 1) {
			    Model.getInstance().deleteDateByID(id);
			}
		    } catch (RemoteException e) {
			Log.e("Remote Exception", e);
		    }
		}
		resGroupCon.unbind();
	    }

	    @Override
	    public void bindingFailed() {
		Log.e(resGroupIdentifier + " binding failed");
	    }
	});
	resGroupCon.bind();
    }

    /**
     * Changes the date at the SQL database
     * 
     * @param date
     *            date that has changed
     */
    public void changeDate(final int id, final String date,
	    final String description) {

	final ResourceGroupServiceConnector resGroupCon = new ResourceGroupServiceConnector(
		Model.getInstance().getContext().getApplicationContext(),
		((CalendarApp) Model.getInstance().getContext()
			.getApplicationContext()).getSignee(),
		resGroupIdentifier);
	resGroupCon.addCallbackHandler(new IConnectorCallback() {

	    @Override
	    public void disconnected() {
		Log.v(resGroupIdentifier + " disconnected");
	    }

	    @Override
	    public void connected() {
		Log.d("Connected to ResourceGroup " + resGroupIdentifier);
		if (resGroupCon.getAppService() == null) {
		    Log.e(resGroupIdentifier + " appService is null");
		} else {
		    // Get resource
		    try {
			IDatabaseConnection idc = IDatabaseConnection.Stub
				.asInterface(resGroupCon.getAppService()
					.getResource(resIdentifier));
			Map<String, String> values = new HashMap<String, String>();
			values.put(DATE, date);
			values.put(DESC, description);
			String[] args = new String[1];
			args[0] = String.valueOf(id);

			/*
			 * Change the date in the database and only if one row
			 * was changed change it in the model
			 */
			if (idc.update(DBNAME, values, ID + " = ?", args) == 1) {
			    Model.getInstance().changeDate(id, date,
				    description);
			}
		    } catch (RemoteException e) {
			Log.e("Remote Exception", e);
		    }
		}
		resGroupCon.unbind();
	    }

	    @Override
	    public void bindingFailed() {
		Log.e(resGroupIdentifier + " binding failed");
	    }
	});
	resGroupCon.bind();
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

    /**
     * Creates a table if there exists none
     */
    private void createTable() {
	// Create the table if there exists none
	if (!Model.getInstance().isTableCreated()) {
	    final ResourceGroupServiceConnector resGroupCon = new ResourceGroupServiceConnector(
		    Model.getInstance().getContext().getApplicationContext(),
		    ((CalendarApp) Model.getInstance().getContext()
			    .getApplicationContext()).getSignee(),
		    resGroupIdentifier);
	    resGroupCon.addCallbackHandler(new IConnectorCallback() {

		@Override
		public void disconnected() {
		    Log.v(resGroupIdentifier + " disconnected");
		}

		@Override
		public void connected() {
		    Log.d("Connected to ResourceGroup " + resGroupIdentifier);
		    if (resGroupCon.getAppService() == null) {
			Log.e(resGroupIdentifier + " appService is null");
		    } else {
			// Get resource
			try {
			    IDatabaseConnection idc = IDatabaseConnection.Stub
				    .asInterface(resGroupCon.getAppService()
					    .getResource(resIdentifier));
			    Map<String, String> columns = new HashMap<String, String>();
			    columns.put(ID, "TEXT");
			    columns.put(DATE, "TEXT");
			    columns.put(DESC, "TEXT");
			    // Create table here TODO Table Constraints?
			     if (idc.createTable(DBNAME, columns, null)) {
			     Model.getInstance().tableCreated(true);
			     }
			} catch (RemoteException e) {
			    Log.e("Remote Exception", e);
			}
		    }
		    resGroupCon.unbind();
		}

		@Override
		public void bindingFailed() {
		    Log.e(resGroupIdentifier + " binding failed");
		}
	    });
	    resGroupCon.bind();
	}
    }
}
