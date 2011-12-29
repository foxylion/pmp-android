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
package de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Appointment;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Severity;
import de.unistuttgart.ipvs.pmp.resourcegroups.database.IDatabaseConnection;

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
     * Resource identifier
     */
    private String resIdentifier = "DatabaseRG";
    
    
    /**
     * Private constructor because of singleton
     */
    private SqlConnector() {
        createTable();
    }
    
    /*
     * Constants for the database table
     */
    private final String DB_TABLE_NAME = "Appointments";
    private final String ID = "ID";
    private final String NAME = "Name";
    private final String DESC = "Description";
    private final String DATE = "Date";
    private final String SEVERITY = "Severity";
    
    
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
        
        IBinder binder = ((CalendarApp) Model.getInstance().getContext().getApplicationContext()).getResourceBlocking(
                resGroupIdentifier, resIdentifier);
        
        if (binder != null) {
            IDatabaseConnection idc = IDatabaseConnection.Stub.asInterface(binder);
            
            // Getting the number of the rows
            long rowCount;
            try {
                rowCount = idc.query(SqlConnector.this.DB_TABLE_NAME, null, null, null, null, null,
                        SqlConnector.this.DATE);
                // Getting the rows 
                for (int itr = 0; itr < rowCount; itr++) {
                    String[] columns = {};
                    columns = idc.getRowAt(itr);
                    
                    // Storing everything from this appointment
                    int id = Integer.valueOf(columns[0]);
                    String name = columns[1];
                    String desc = columns[2];
                    Date date = new Date(Long.valueOf(columns[3]));
                    Severity severity = Severity.valueOf(columns[4]);
                    
                    // Storing in the model
                    Model.getInstance().addAppointment(new Appointment(id, name, desc, date, severity));
                    Log.v("Loading appointment: ID: " + String.valueOf(id) + " date: " + columns[2] + " name: " + name
                            + "description: " + columns[1] + " severity " + severity.toString());
                    
                    // Check if there's a new highest id                 
                    if (id > SqlConnector.this.highestId) {
                        SqlConnector.this.highestId = id;
                    }
                }
            } catch (RemoteException e) {
                Toast.makeText(Model.getInstance().getContext(),
                        Model.getInstance().getContext().getString(R.string.err_load), Toast.LENGTH_SHORT).show();
                Log.e("Remote Exception", e);
            }
            
        } else {
            Log.e("Could not connect to database");
        }
        
        //        resGroupCon.addCallbackHandler(new IConnectorCallback() {
        //            
        //            @Override
        //            public void disconnected() {
        //                Log.v(SqlConnector.this.resGroupIdentifier + " disconnected");
        //            }
        //            
        //            
        //            @Override
        //            public void connected() {
        //                Log.d("Connected to ResourceGroup " + SqlConnector.this.resGroupIdentifier);
        //                if (resGroupCon.getAppService() == null) {
        //                    Log.e(SqlConnector.this.resGroupIdentifier + " appService is null");
        //                } else {
        //                    // Get resource
        //                    IDatabaseConnection idc = null;
        //                    try {
        //                        idc = IDatabaseConnection.Stub.asInterface(resGroupCon.getAppService().getResource(
        //                                SqlConnector.this.resIdentifier));
        //                        
        //                        ArrayList<Appointment> todoList = new ArrayList<Appointment>();
        //                        
        //                        // Getting the number of the rows
        //                        long rowCount = idc.query(SqlConnector.this.DB_TABLE_NAME, null, null, null, null, null,
        //                                SqlConnector.this.DATE);
        //                        
        //                        // Getting the rows 
        //                        for (int itr = 0; itr < rowCount; itr++) {
        //                            String[] columns = {};
        //                            try {
        //                                columns = idc.getRowAt(itr);
        //                                int id = Integer.valueOf(columns[0]);
        //                                
        //                                Date date = new Date(Long.valueOf(columns[2]));
        //                                todoList.add(new Appointment(id, columns[1], date));
        //                                Log.v("Loading appointment: ID: " + String.valueOf(id) + " date: " + columns[2]
        //                                        + " description: " + columns[1]);
        //                                // Check if there's a new highest id
        //                                if (id > SqlConnector.this.highestId) {
        //                                    SqlConnector.this.highestId = id;
        //                                }
        //                            } catch (Exception e) {
        //                                Log.e("Load appointment failed. Skipped: " + columns.toString());
        //                                Log.v(e.toString());
        //                            }
        //                        }
        //                        Model.getInstance().loadAppointments(todoList);
        //                    } catch (RemoteException e) {
        //                        Toast.makeText(Model.getInstance().getContext(),
        //                                Model.getInstance().getContext().getString(R.string.err_load), Toast.LENGTH_SHORT)
        //                                .show();
        //                        Log.e("Remote Exception", e);
        //                    } catch (Exception e) {
        //                        Toast.makeText(Model.getInstance().getContext(),
        //                                Model.getInstance().getContext().getString(R.string.err_load), Toast.LENGTH_SHORT)
        //                                .show();
        //                        Log.e("Exception", e);
        //                    } finally {
        //                        if (idc != null) {
        //                            try {
        //                                idc.close();
        //                            } catch (RemoteException e) {
        //                                Log.e("Cannot close the DatabaseConnection: ", e);
        //                            }
        //                        }
        //                        resGroupCon.unbind();
        //                    }
        //                }
        //            }
        //            
        //            
        //            @Override
        //            public void bindingFailed() {
        //                Log.e(SqlConnector.this.resGroupIdentifier + " binding failed");
        //            }
        //        });
        //        resGroupCon.bind();
    }
    
    
    /**
     * Stores the new appointment in the SQL Database and then calls {@link Model#addDate(Appointment)}.
     * 
     * @param date
     *            the date
     * @param description
     *            the description
     */
    public void storeNewAppointment(final Date date, final String name, final String description,
            final Severity severity) {
        
        IBinder binder = ((CalendarApp) Model.getInstance().getContext().getApplicationContext()).getResourceBlocking(
                resGroupIdentifier, resIdentifier);
        
        if (binder != null) {
            IDatabaseConnection idc = IDatabaseConnection.Stub.asInterface(binder);
            try {
                // The values to add
                Map<String, String> values = new HashMap<String, String>();
                int id = getNewId();
                
                values.put(SqlConnector.this.ID, String.valueOf(id));
                values.put(SqlConnector.this.NAME, name);
                values.put(SqlConnector.this.DESC, description);
                values.put(SqlConnector.this.DATE, String.valueOf(date.getTime()));
                values.put(SqlConnector.this.SEVERITY, severity.toString());
                
                long result = idc.insert(SqlConnector.this.DB_TABLE_NAME, null, values);
                Log.v("Return value of insert: " + result);
                if (result != -1) {
                    Log.v("Storing new appointment: id: " + String.valueOf(id) + " date: " + date + " description: "
                            + description);
                    Model.getInstance().addAppointment(new Appointment(id, name, description, date, severity));
                } else {
                    Toast.makeText(Model.getInstance().getContext(),
                            Model.getInstance().getContext().getString(R.string.err_store), Toast.LENGTH_SHORT).show();
                    Log.e("Appointment not stored");
                }
            } catch (RemoteException e) {
                Toast.makeText(Model.getInstance().getContext(),
                        Model.getInstance().getContext().getString(R.string.err_store), Toast.LENGTH_SHORT).show();
                Log.e("Remote Exception", e);
            }
        } else {
            Log.e("Could not connect to database");
        }
        
        //        final ResourceGroupServiceConnector resGroupCon = new ResourceGroupServiceConnector(Model.getInstance()
        //                .getContext().getApplicationContext(), ((CalendarApp) Model.getInstance().getContext()
        //                .getApplicationContext()).getSignee(), this.resGroupIdentifier);
        //        
        //        resGroupCon.addCallbackHandler(new IConnectorCallback() {
        //            
        //            @Override
        //            public void disconnected() {
        //                Log.v(SqlConnector.this.resGroupIdentifier + " disconnected");
        //            }
        //            
        //            
        //            @Override
        //            public void connected() {
        //                Log.d("Connected to ResourceGroup " + SqlConnector.this.resGroupIdentifier);
        //                if (resGroupCon.getAppService() == null) {
        //                    Log.e(SqlConnector.this.resGroupIdentifier + " appService is null");
        //                } else {
        //                    // Get resource
        //                    IDatabaseConnection idc = null;
        //                    try {
        //                        idc = IDatabaseConnection.Stub.asInterface(resGroupCon.getAppService().getResource(
        //                                SqlConnector.this.resIdentifier));
        //                        
        //                        // The values to add
        //                        Map<String, String> values = new HashMap<String, String>();
        //        int id = getNewId();
        //                        values.put(SqlConnector.this.ID, String.valueOf(id));
        //                        values.put(SqlConnector.this.DATE, String.valueOf(date.getTime()));
        //                        values.put(SqlConnector.this.DESC, description);
        //                        
        //                        long result = idc.insert(SqlConnector.this.DB_TABLE_NAME, null, values);
        //                        Log.v("Return value of insert: " + result);
        //                        if (result != -1) {
        //                            Log.v("Storing new appointment: id: " + String.valueOf(id) + " date: " + date
        //                                    + " description: " + description);
        //        Model.getInstance().addAppointment(new Appointment(id, name, description, date, severity));
        //                        } else {
        //                            Toast.makeText(Model.getInstance().getContext(),
        //                                    Model.getInstance().getContext().getString(R.string.err_store), Toast.LENGTH_SHORT)
        //                                    .show();
        //                            Log.e("Appointment not stored");
        //                        }
        //                    } catch (RemoteException e) {
        //                        Toast.makeText(Model.getInstance().getContext(),
        //                                Model.getInstance().getContext().getString(R.string.err_store), Toast.LENGTH_SHORT)
        //                                .show();
        //                        Log.e("Remote Exception", e);
        //                    } catch (Exception e) {
        //                        Toast.makeText(Model.getInstance().getContext(),
        //                                Model.getInstance().getContext().getString(R.string.err_store), Toast.LENGTH_SHORT)
        //                                .show();
        //                        Log.e("Exception", e);
        //                    } finally {
        //                        if (idc != null) {
        //                            try {
        //                                idc.close();
        //                            } catch (RemoteException e) {
        //                                Log.e("Cannot close connection: ", e);
        //                            }
        //                        }
        //                        resGroupCon.unbind();
        //                    }
        //                }
        //            }
        //            
        //            
        //            @Override
        //            public void bindingFailed() {
        //                Log.e(SqlConnector.this.resGroupIdentifier + " binding failed");
        //            }
        //        });
        //        resGroupCon.bind();
    }
    
    
    /**
     * Delete the appointment out of the SQL database with the given id and then calls {@link Model#deleteDateByID(int)}
     * 
     * @param id
     *            id of the appointment to delete
     */
    public void deleteAppointment(final Appointment app) {
        IBinder binder = ((CalendarApp) Model.getInstance().getContext().getApplicationContext()).getResourceBlocking(
                resGroupIdentifier, resIdentifier);
        
        if (binder != null) {
            IDatabaseConnection idc = IDatabaseConnection.Stub.asInterface(binder);
            try {
                
                String[] args = new String[1];
                args[0] = String.valueOf(app.getId());
                
                /*
                 * Delete the date out of the database
                 */
                if (idc.delete(SqlConnector.this.DB_TABLE_NAME, SqlConnector.this.ID + " = ?", args) == 1) {
                    Log.v("Deleting date: id: " + String.valueOf(app.getId()));
                } else {
                    Toast.makeText(Model.getInstance().getContext(),
                            Model.getInstance().getContext().getString(R.string.err_del), Toast.LENGTH_SHORT).show();
                }
            } catch (RemoteException e) {
                Toast.makeText(Model.getInstance().getContext(),
                        Model.getInstance().getContext().getString(R.string.err_del), Toast.LENGTH_SHORT).show();
                Log.e("Remote Exception", e);
            }
        } else {
            Log.e("Could not connect to database");
        }
        
        //        final ResourceGroupServiceConnector resGroupCon = new ResourceGroupServiceConnector(Model.getInstance()
        //                .getContext().getApplicationContext(), ((CalendarApp) Model.getInstance().getContext()
        //                .getApplicationContext()).getSignee(), this.resGroupIdentifier);
        //        
        //        resGroupCon.addCallbackHandler(new IConnectorCallback() {
        //            
        //            @Override
        //            public void disconnected() {
        //                Log.v(SqlConnector.this.resGroupIdentifier + " disconnected");
        //            }
        //            
        //            
        //            @Override
        //            public void connected() {
        //                Log.d("Connected to ResourceGroup " + SqlConnector.this.resGroupIdentifier);
        //                if (resGroupCon.getAppService() == null) {
        //                    Log.e(SqlConnector.this.resGroupIdentifier + " appService is null");
        //                } else {
        //                    // Get resource
        //                    IDatabaseConnection idc = null;
        //                    try {
        //                        idc = IDatabaseConnection.Stub.asInterface(resGroupCon.getAppService().getResource(
        //                                SqlConnector.this.resIdentifier));
        //                        String[] args = new String[1];
        //                        args[0] = String.valueOf(id);
        //                        /*
        //                         * Delete the date out of the database and if exactly
        //                         * once removed then remove it out of the model
        //                         */
        //                        if (idc.delete(SqlConnector.this.DB_TABLE_NAME, SqlConnector.this.ID + " = ?", args) == 1) {
        //                            Log.v("Deleting date: id: " + String.valueOf(id));
        //                        } else {
        //                            Toast.makeText(Model.getInstance().getContext(),
        //                                    Model.getInstance().getContext().getString(R.string.err_del), Toast.LENGTH_SHORT)
        //                                    .show();
        //                        }
        //                    } catch (RemoteException e) {
        //                        Toast.makeText(Model.getInstance().getContext(),
        //                                Model.getInstance().getContext().getString(R.string.err_del), Toast.LENGTH_SHORT)
        //                                .show();
        //                        Log.e("Remote Exception", e);
        //                    } catch (Exception e) {
        //                        Toast.makeText(Model.getInstance().getContext(),
        //                                Model.getInstance().getContext().getString(R.string.err_del), Toast.LENGTH_SHORT)
        //                                .show();
        //                        Log.e("Exception", e);
        //                    } finally {
        //                        if (idc != null) {
        //                            try {
        //                                idc.close();
        //                            } catch (RemoteException e) {
        //                                Log.e("Cannot close connection: ", e);
        //                            }
        //                        }
        //                        resGroupCon.unbind();
        //                    }
        //                }
        //            }
        //            
        //            
        //            @Override
        //            public void bindingFailed() {
        //                Log.e(SqlConnector.this.resGroupIdentifier + " binding failed");
        //            }
        //        });
        //        resGroupCon.bind();
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
    public void changeAppointment(final Integer id, final Date date, String name, final String description,
            Severity severity) {
        IBinder binder = ((CalendarApp) Model.getInstance().getContext().getApplicationContext()).getResourceBlocking(
                resGroupIdentifier, resIdentifier);
        
        if (binder != null) {
            IDatabaseConnection idc = IDatabaseConnection.Stub.asInterface(binder);
            try {
                Map<String, String> values = new HashMap<String, String>();
                
                values.put(SqlConnector.this.NAME, name);
                values.put(SqlConnector.this.DESC, description);
                values.put(SqlConnector.this.DATE, String.valueOf(date.getTime()));
                values.put(SqlConnector.this.SEVERITY, severity.toString());
                
                /*
                 * Change the date in the database and only if one row
                 * was changed change, then change it in the model
                 */
                if (idc.update(SqlConnector.this.DB_TABLE_NAME, values,
                        SqlConnector.this.ID + " = " + String.valueOf(id), null) == 1) {
                    Log.v("Changing date with id " + String.valueOf(id) + " to: date: " + date + " description: "
                            + description);
                } else {
                    Toast.makeText(Model.getInstance().getContext(),
                            Model.getInstance().getContext().getString(R.string.err_change), Toast.LENGTH_SHORT).show();
                }
            } catch (RemoteException e) {
                Toast.makeText(Model.getInstance().getContext(),
                        Model.getInstance().getContext().getString(R.string.err_change), Toast.LENGTH_SHORT).show();
                Log.e("Remote Exception", e);
            }
        } else {
            Log.e("Could not connect to database");
        }
        
        //        final ResourceGroupServiceConnector resGroupCon = new ResourceGroupServiceConnector(Model.getInstance()
        //                .getContext().getApplicationContext(), ((CalendarApp) Model.getInstance().getContext()
        //                .getApplicationContext()).getSignee(), this.resGroupIdentifier);
        //        
        //        resGroupCon.addCallbackHandler(new IConnectorCallback() {
        //            
        //            @Override
        //            public void disconnected() {
        //                Log.v(SqlConnector.this.resGroupIdentifier + " disconnected");
        //            }
        //            
        //            
        //            @Override
        //            public void connected() {
        //                Log.d("Connected to ResourceGroup " + SqlConnector.this.resGroupIdentifier);
        //                if (resGroupCon.getAppService() == null) {
        //                    Log.e(SqlConnector.this.resGroupIdentifier + " appService is null");
        //                } else {
        //                    // Get resource
        //                    IDatabaseConnection idc = null;
        //                    try {
        //                        idc = IDatabaseConnection.Stub.asInterface(resGroupCon.getAppService().getResource(
        //                                SqlConnector.this.resIdentifier));
        //                        Map<String, String> values = new HashMap<String, String>();
        //                        values.put(SqlConnector.this.DATE, String.valueOf(date.getTime()));
        //                        values.put(SqlConnector.this.DESC, description);
        //                        
        //                        /*
        //                         * Change the date in the database and only if one row
        //                         * was changed change, then change it in the model
        //                         */
        //                        if (idc.update(SqlConnector.this.DB_TABLE_NAME, values,
        //                                SqlConnector.this.ID + " = " + String.valueOf(id), null) == 1) {
        //                            Log.v("Changing date with id " + String.valueOf(id) + " to: date: " + date
        //                                    + " description: " + description);
        //                        } else {
        //                            Toast.makeText(Model.getInstance().getContext(),
        //                                    Model.getInstance().getContext().getString(R.string.err_change), Toast.LENGTH_SHORT)
        //                                    .show();
        //                        }
        //                    } catch (RemoteException e) {
        //                        Toast.makeText(Model.getInstance().getContext(),
        //                                Model.getInstance().getContext().getString(R.string.err_change), Toast.LENGTH_SHORT)
        //                                .show();
        //                        Log.e("Remote Exception", e);
        //                    } catch (Exception e) {
        //                        Toast.makeText(Model.getInstance().getContext(),
        //                                Model.getInstance().getContext().getString(R.string.err_change), Toast.LENGTH_SHORT)
        //                                .show();
        //                        Log.e("Exception", e);
        //                    } finally {
        //                        if (idc != null) {
        //                            try {
        //                                idc.close();
        //                            } catch (RemoteException e) {
        //                                Log.e("Cannot close connection: ", e);
        //                            }
        //                        }
        //                        resGroupCon.unbind();
        //                    }
        //                }
        //            }
        //            
        //            
        //            @Override
        //            public void bindingFailed() {
        //                Log.e(SqlConnector.this.resGroupIdentifier + " binding failed");
        //            }
        //        });
        //        resGroupCon.bind();
    }
    
    
    /**
     * Creates a table if there exists none. The table name is "Appointment".
     */
    private void createTable() {
        
        IBinder binder = ((CalendarApp) Model.getInstance().getContext().getApplicationContext()).getResourceBlocking(
                resGroupIdentifier, resIdentifier);
        
        if (binder != null) {
            IDatabaseConnection idc = IDatabaseConnection.Stub.asInterface(binder);
            try {
                if (!idc.isTableExisted(SqlConnector.this.DB_TABLE_NAME)) {
                    
                    // Columns of the table
                    Map<String, String> columns = new HashMap<String, String>();
                    columns.put(SqlConnector.this.ID, "TEXT");
                    columns.put(SqlConnector.this.NAME, "TEXT");
                    columns.put(SqlConnector.this.DESC, "TEXT");
                    columns.put(SqlConnector.this.DATE, "TEXT");
                    columns.put(SqlConnector.this.SEVERITY, "TEXT");
                    
                    // Creates the table
                    Log.v("Creating table");
                    
                    // Create the table
                    if (idc.createTable(SqlConnector.this.DB_TABLE_NAME, columns, null)) {
                        Log.v("Table created. Name: " + SqlConnector.this.DB_TABLE_NAME);
                    } else {
                        Log.e("Couldn't create table");
                    }
                } else {
                    Log.v("Table already exists");
                }
            } catch (RemoteException e) {
                Toast.makeText(Model.getInstance().getContext(),
                        Model.getInstance().getContext().getString(R.string.err_create), Toast.LENGTH_SHORT).show();
                Log.e("Remote Exception", e);
            }
            
        } else {
            Log.e("Couldn't connect to " + resGroupIdentifier);
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
