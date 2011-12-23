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

import android.os.RemoteException;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Appointment;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Severity;
import de.unistuttgart.ipvs.pmp.resourcegroups.database.IDatabaseConnection;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;

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
    private final String DB_TABLE_NAME = "Appointment";
    private final String ID = "ID";
    private final String DATE = "Appointment";
    private final String DESC = "Description";
    
    
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
        /*final ResourceGroupServiceConnector resGroupCon = new ResourceGroupServiceConnector(Model.getInstance()
                .getContext().getApplicationContext(), ((CalendarApp) Model.getInstance().getContext()
                .getApplicationContext()).getSignee(), this.resGroupIdentifier);*/
        final PMPServiceConnector pmpsc = new PMPServiceConnector(Model.getInstance().getContext()
                .getApplicationContext());
        
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
        int id = getNewId();
        //                        values.put(SqlConnector.this.ID, String.valueOf(id));
        //                        values.put(SqlConnector.this.DATE, String.valueOf(date.getTime()));
        //                        values.put(SqlConnector.this.DESC, description);
        //                        
        //                        long result = idc.insert(SqlConnector.this.DB_TABLE_NAME, null, values);
        //                        Log.v("Return value of insert: " + result);
        //                        if (result != -1) {
        //                            Log.v("Storing new appointment: id: " + String.valueOf(id) + " date: " + date
        //                                    + " description: " + description);
        Model.getInstance().addAppointment(new Appointment(id, name, description, date, severity));
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
    public void changeAppointment(final Integer id, final Date date, final String description, Severity severity) {
        
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
        //        final ResourceGroupServiceConnector resGroupCon = new ResourceGroupServiceConnector(Model.getInstance()
        //                .getContext().getApplicationContext(), ((CalendarApp) Model.getInstance().getContext()
        //                .getApplicationContext()).getSignee(), this.resGroupIdentifier);
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
        //                        if (!idc.isTableExisted(SqlConnector.this.DB_TABLE_NAME)) {
        //                            Map<String, String> columns = new HashMap<String, String>();
        //                            columns.put(SqlConnector.this.ID, "TEXT");
        //                            columns.put(SqlConnector.this.DATE, "TEXT");
        //                            columns.put(SqlConnector.this.DESC, "TEXT");
        //                            // Creates the table
        //                            Log.v("Creating table");
        //                            if (idc.createTable(SqlConnector.this.DB_TABLE_NAME, columns, null)) {
        //                                Log.v("Table created. Name: " + SqlConnector.this.DB_TABLE_NAME);
        //                                Model.getInstance().tableCreated(true);
        //                            } else {
        //                                Log.e("Couldn't create table");
        //                            }
        //                        } else {
        //                            Log.v("Table already exists");
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
     * Returns a new id for a date
     * 
     * @return the new id
     */
    private int getNewId() {
        this.highestId++;
        return this.highestId;
    }
}
