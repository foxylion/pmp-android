package de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.os.RemoteException;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Appointment;
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
    private String resIdentifier = "de.unistuttgart.ipvs.pmp.resourcegroups.database";
    
    
    /**
     * Private constructor because of singleton
     */
    private SqlConnector() {
        createTable();
    }
    
    /*
     * Constants for the database table
     */
    private final String DBNAME = "appointmentlist";
    private final String ID = "id";
    private final String DATE = "appointment";
    private final String DESC = "description";
    
    
    /**
     * Returns the stored instance of the class or creates a new one if there is none
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
     * Loads the dates stored appointments in the SQL database. This method calls
     * {@link Model#loadAppointments(ArrayList)} to store the dates in the model.
     * 
     */
    public void loadAppointments() {
        final ResourceGroupServiceConnector resGroupCon = new ResourceGroupServiceConnector(Model.getInstance()
                .getContext().getApplicationContext(), ((CalendarApp) Model.getInstance().getContext()
                .getApplicationContext()).getSignee(), this.resGroupIdentifier);
        
        resGroupCon.addCallbackHandler(new IConnectorCallback() {
            
            @Override
            public void disconnected() {
                Log.v(SqlConnector.this.resGroupIdentifier + " disconnected");
            }
            
            
            @Override
            public void connected() {
                Log.d("Connected to ResourceGroup " + SqlConnector.this.resGroupIdentifier);
                if (resGroupCon.getAppService() == null) {
                    Log.e(SqlConnector.this.resGroupIdentifier + " appService is null");
                } else {
                    // Get resource
                    try {
                        IDatabaseConnection idc = IDatabaseConnection.Stub.asInterface(resGroupCon.getAppService()
                                .getResource(SqlConnector.this.resIdentifier));
                        
                        ArrayList<Appointment> todoList = new ArrayList<Appointment>();
                        
                        // Getting the number of the rows
                        long rowCount = idc.query(SqlConnector.this.DBNAME, null, null, null, null, null, null);
                        
                        // Getting the rows
                        for (int itr = 0; itr < rowCount; itr++) {
                            
                            String[] columns = idc.getRowAt(itr);
                            int id = Integer.valueOf(columns[0]);
                            todoList.add(new Appointment(id, columns[2], new Date(Long.valueOf(columns[1]))));
                            Log.v("Loading appointment: ID: " + String.valueOf(id) + " date: " + columns[1] + " description: "
                                    + columns[2]);
                            // Check if there's a new highest id
                            if (id > SqlConnector.this.highestId) {
                                SqlConnector.this.highestId = id;
                            }
                        }
                        idc.close();
                        Model.getInstance().loadAppointments(todoList);
                    } catch (RemoteException e) {
                        Log.e("Remote Exception", e);
                    } finally {
                        resGroupCon.unbind();
                    }
                }
            }
            
            
            @Override
            public void bindingFailed() {
                Log.e(SqlConnector.this.resGroupIdentifier + " binding failed");
            }
        });
        resGroupCon.bind();
    }
    
    
    /**
     * Stores the new appointment in the SQL Database and then calls {@link Model#addDate(Appointment)}.
     * 
     * @param date
     *            the date
     * @param description
     *            the description
     */
    public void storeNewAppointment(final Date date, final String description) {
        
        final ResourceGroupServiceConnector resGroupCon = new ResourceGroupServiceConnector(Model.getInstance()
                .getContext().getApplicationContext(), ((CalendarApp) Model.getInstance().getContext()
                .getApplicationContext()).getSignee(), this.resGroupIdentifier);
        
        resGroupCon.addCallbackHandler(new IConnectorCallback() {
            
            @Override
            public void disconnected() {
                Log.v(SqlConnector.this.resGroupIdentifier + " disconnected");
            }
            
            
            @Override
            public void connected() {
                Log.d("Connected to ResourceGroup " + SqlConnector.this.resGroupIdentifier);
                if (resGroupCon.getAppService() == null) {
                    Log.e(SqlConnector.this.resGroupIdentifier + " appService is null");
                } else {
                    // Get resource
                    try {
                        IDatabaseConnection idc = IDatabaseConnection.Stub.asInterface(resGroupCon.getAppService()
                                .getResource(SqlConnector.this.resIdentifier));
                        // The values to add
                        Map<String, String> values = new HashMap<String, String>();
                        int id = getNewId();
                        values.put(SqlConnector.this.ID, String.valueOf(id));
                        values.put(SqlConnector.this.DATE, String.valueOf(date.getTime()));
                        values.put(SqlConnector.this.DESC, description);
                        
                        long result = idc.insert(SqlConnector.this.DBNAME, null, values);
                        Log.v("Return value of insert: " + result);
                        if (result != 0) {
                            Log.v("Storing new appointment: id: " + String.valueOf(id) + " date: " + date + " description: "
                                    + description);
                            Model.getInstance().addAppointment(new Appointment(id, description, date));
                        } else {
                            Toast.makeText(Model.getInstance().getContext(), Model.getInstance().getContext().getString(R.string.err_store),
                                    Toast.LENGTH_SHORT).show();
                            Log.e("Appointment not stored");
                        }
                        idc.close();
                    } catch (RemoteException e) {
                        Log.e("Remote Exception", e);
                    } finally {
                        resGroupCon.unbind();
                    }
                }
            }
            
            
            @Override
            public void bindingFailed() {
                Log.e(SqlConnector.this.resGroupIdentifier + " binding failed");
            }
        });
        resGroupCon.bind();
    }
    
    
    /**
     * Delete the appointment out of the SQL database with the given id and then calls {@link Model#deleteDateByID(int)}
     * 
     * @param id
     *            id of the appointment to delete
     */
    public void deleteAppointment(final int id) {
        
        final ResourceGroupServiceConnector resGroupCon = new ResourceGroupServiceConnector(Model.getInstance()
                .getContext().getApplicationContext(), ((CalendarApp) Model.getInstance().getContext()
                .getApplicationContext()).getSignee(), this.resGroupIdentifier);
        
        resGroupCon.addCallbackHandler(new IConnectorCallback() {
            
            @Override
            public void disconnected() {
                Log.v(SqlConnector.this.resGroupIdentifier + " disconnected");
            }
            
            
            @Override
            public void connected() {
                Log.d("Connected to ResourceGroup " + SqlConnector.this.resGroupIdentifier);
                if (resGroupCon.getAppService() == null) {
                    Log.e(SqlConnector.this.resGroupIdentifier + " appService is null");
                } else {
                    // Get resource
                    try {
                        IDatabaseConnection idc = IDatabaseConnection.Stub.asInterface(resGroupCon.getAppService()
                                .getResource(SqlConnector.this.resIdentifier));
                        String[] args = new String[1];
                        args[0] = String.valueOf(id);
                        /*
                         * Delete the date out of the database and if exactly
                         * once removed then remove it out of the model
                         */
                        if (idc.delete(SqlConnector.this.DBNAME, SqlConnector.this.ID + " = ?", args) == 1) {
                            Log.v("Deleting date: id: " + String.valueOf(id));
                            Model.getInstance().deleteAppointmentByID(id);
                        } else {
                            Toast.makeText(Model.getInstance().getContext(), Model.getInstance().getContext().getString(R.string.err_del),
                                    Toast.LENGTH_SHORT).show();
                        }
                        idc.close();
                    } catch (RemoteException e) {
                        Log.e("Remote Exception", e);
                    } finally {
                        resGroupCon.unbind();
                    }
                }
            }
            
            
            @Override
            public void bindingFailed() {
                Log.e(SqlConnector.this.resGroupIdentifier + " binding failed");
            }
        });
        resGroupCon.bind();
    }
    
    
    /**
     * Changes the appointment at the SQL database and then calls {@link Model#changeAppointment(int, String, String)}
     * 
     * @param id
     *            the id of the appointment to change
     * @param date
     *            the date that has changed
     * @param description
     *            the description that has changed
     */
    public void changeAppointment(final Integer id, final Date date, final String description) {
        
        final ResourceGroupServiceConnector resGroupCon = new ResourceGroupServiceConnector(Model.getInstance()
                .getContext().getApplicationContext(), ((CalendarApp) Model.getInstance().getContext()
                .getApplicationContext()).getSignee(), this.resGroupIdentifier);
        
        resGroupCon.addCallbackHandler(new IConnectorCallback() {
            
            @Override
            public void disconnected() {
                Log.v(SqlConnector.this.resGroupIdentifier + " disconnected");
            }
            
            
            @Override
            public void connected() {
                Log.d("Connected to ResourceGroup " + SqlConnector.this.resGroupIdentifier);
                if (resGroupCon.getAppService() == null) {
                    Log.e(SqlConnector.this.resGroupIdentifier + " appService is null");
                } else {
                    // Get resource
                    try {
                        IDatabaseConnection idc = IDatabaseConnection.Stub.asInterface(resGroupCon.getAppService()
                                .getResource(SqlConnector.this.resIdentifier));
                        Map<String, String> values = new HashMap<String, String>();
                        values.put(SqlConnector.this.DATE, String.valueOf(date.getTime()));
                        values.put(SqlConnector.this.DESC, description);
                        
                        /*
                         * Change the date in the database and only if one row
                         * was changed change, then change it in the model
                         */
                        if (idc.update(SqlConnector.this.DBNAME, values,
                                SqlConnector.this.ID + " = " + String.valueOf(id), null) == 1) {
                            Log.v("Changing date with id " + String.valueOf(id) + " to: date: " + date
                                    + " description: " + description);
                            Model.getInstance().changeAppointment(id, date, description);
                        } else {
                            Toast.makeText(Model.getInstance().getContext(), Model.getInstance().getContext().getString(R.string.err_change),
                                    Toast.LENGTH_SHORT).show();
                        }
                        idc.close();
                    } catch (RemoteException e) {
                        Log.e("Remote Exception", e);
                    } finally {
                        resGroupCon.unbind();
                    }
                }
            }
            
            
            @Override
            public void bindingFailed() {
                Log.e(SqlConnector.this.resGroupIdentifier + " binding failed");
            }
        });
        resGroupCon.bind();
    }
    
    
    /**
     * Creates a table if there exists none. The table name is "appointmentlist".
     */
    private void createTable() {
        if (!Model.getInstance().isTableCreated()) {
            final ResourceGroupServiceConnector resGroupCon = new ResourceGroupServiceConnector(Model.getInstance()
                    .getContext().getApplicationContext(), ((CalendarApp) Model.getInstance().getContext()
                    .getApplicationContext()).getSignee(), this.resGroupIdentifier);
            resGroupCon.addCallbackHandler(new IConnectorCallback() {
                
                @Override
                public void disconnected() {
                    Log.v(SqlConnector.this.resGroupIdentifier + " disconnected");
                }
                
                
                @Override
                public void connected() {
                    Log.d("Connected to ResourceGroup " + SqlConnector.this.resGroupIdentifier);
                    if (resGroupCon.getAppService() == null) {
                        Log.e(SqlConnector.this.resGroupIdentifier + " appService is null");
                    } else {
                        // Get resource
                        try {
                            IDatabaseConnection idc = IDatabaseConnection.Stub.asInterface(resGroupCon.getAppService()
                                    .getResource(SqlConnector.this.resIdentifier));
                            Map<String, String> columns = new HashMap<String, String>();
                            columns.put(SqlConnector.this.ID, "TEXT");
                            columns.put(SqlConnector.this.DATE, "TEXT");
                            columns.put(SqlConnector.this.DESC, "TEXT");
                            // Creates the table
                            Log.v("Creating table");
                            if (idc.createTable(SqlConnector.this.DBNAME, columns, null)) {
                                Log.v("Table created. Name: " + SqlConnector.this.DBNAME);
                                Model.getInstance().tableCreated(true);
                            } else {
                                Log.e("Couldn't create table");
                            }
                            idc.close();
                        } catch (RemoteException e) {
                            Log.e("Remote Exception", e);
                        } finally {
                            resGroupCon.unbind();
                        }
                    }
                }
                
                
                @Override
                public void bindingFailed() {
                    Log.e(SqlConnector.this.resGroupIdentifier + " binding failed");
                }
            });
            resGroupCon.bind();
        }
    }
    
    
    /**
     * Returns a new id for a date
     * 
     * @return the new id
     */
    private int getNewId() {
        this.highestId++;
        return this.highestId;
    }
}
