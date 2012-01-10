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
import android.os.Looper;
import android.os.RemoteException;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities.CalendarAppActivity;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Appointment;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Severity;
import de.unistuttgart.ipvs.pmp.resourcegroups.database.IDatabaseConnection;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnector;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;

public class SqlConnector {
    
    /**
     * Identifier of the needed resource group
     */
    private final String resGroupIdentifier = "de.unistuttgart.ipvs.pmp.resourcegroups.database";
    
    /**
     * Resource identifier
     */
    private String resIdentifier = "DatabaseRG";
    
    private String pkgName = Model.getInstance().getContext().getPackageName();
    
    private CalendarAppActivity appContext = Model.getInstance().getContext();
    
    
    /**
     * Private constructor because of singleton
     */
    public SqlConnector() {
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
     * Loads the dates stored appointments in the SQL database. This method calls
     * {@link Model#loadAppointments(ArrayList)} to store the dates in the model.
     * 
     */
    public void loadAppointments() {
        final PMPServiceConnector pmpconnector = new PMPServiceConnector(this.appContext);
        pmpconnector.addCallbackHandler(new AbstractConnectorCallback() {
            
            @Override
            public void onConnect(AbstractConnector connector) throws RemoteException {
                IBinder binder = pmpconnector.getAppService().getResource(pkgName, resGroupIdentifier, resIdentifier);
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
                            Severity severity = Severity.valueOf(columns[3]);
                            Date date = new Date(Long.valueOf(columns[4]));
                            
                            // Storing in the model
                            Model.getInstance().addAppointment(new Appointment(id, name, desc, date, severity));
                            Log.v("Loading appointment: ID: " + String.valueOf(id) + " date: " + columns[2] + " name: "
                                    + name + " description: " + columns[1] + " severity " + severity.toString());
                            
                            // Check if there's a new highest id                 
                            if (id > Model.getInstance().getHighestId()) {
                                Model.getInstance().setHighestId(id);
                            }
                        }
                    } catch (RemoteException e) {
                        Toast.makeText(Model.getInstance().getContext(),
                                Model.getInstance().getContext().getString(R.string.err_load), Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Remote Exception", e);
                    } finally {
                        idc.close();
                    }
                }
            }
            
            
            @Override
            public void onBindingFailed(AbstractConnector connector) {
                Log.e("Could not connect to database resource");
                Looper.prepare();
                Toast.makeText(appContext, R.string.err_connect_db, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        });
        pmpconnector.bind();
    }
    
    
    /**
     * Stores the appointment in the database AND in the model
     * 
     * @param date
     *            date of the appointment
     * @param name
     *            name of the appointment
     * @param description
     *            description of the appointment
     * @param severity
     *            {@link Severity} of the appointment
     */
    public void storeNewAppointment(final Date date, final String name, final String description,
            final Severity severity) {
        
        final PMPServiceConnector pmpconnector = new PMPServiceConnector(this.appContext);
        pmpconnector.addCallbackHandler(new AbstractConnectorCallback() {
            
            @Override
            public void onConnect(AbstractConnector connector) throws RemoteException {
                IBinder binder = pmpconnector.getAppService().getResource(pkgName, resGroupIdentifier, resIdentifier);
                if (binder != null) {
                    IDatabaseConnection idc = IDatabaseConnection.Stub.asInterface(binder);
                    try {
                        // The values to add
                        Map<String, String> values = new HashMap<String, String>();
                        int id = Model.getInstance().getNewHighestId();
                        
                        values.put(SqlConnector.this.ID, String.valueOf(id));
                        values.put(SqlConnector.this.NAME, name);
                        values.put(SqlConnector.this.DESC, description);
                        values.put(SqlConnector.this.DATE, String.valueOf(date.getTime()));
                        values.put(SqlConnector.this.SEVERITY, severity.toString());
                        
                        long result = idc.insert(SqlConnector.this.DB_TABLE_NAME, null, values);
                        Log.v("Return value of insert: " + result);
                        if (result != -1) {
                            Log.v("Storing new appointment: id: " + String.valueOf(id) + " date: " + date
                                    + " description: " + description);
                            Model.getInstance().addAppointment(new Appointment(id, name, description, date, severity));
                        } else {
                            Toast.makeText(Model.getInstance().getContext(),
                                    Model.getInstance().getContext().getString(R.string.err_store), Toast.LENGTH_SHORT)
                                    .show();
                            Log.e("Appointment not stored");
                        }
                    } catch (RemoteException e) {
                        Toast.makeText(Model.getInstance().getContext(),
                                Model.getInstance().getContext().getString(R.string.err_store), Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Remote Exception", e);
                    } finally {
                        idc.close();
                    }
                }
            }
            
            
            @Override
            public void onBindingFailed(AbstractConnector connector) {
                Log.e("Could not connect to database resource");
                Looper.prepare();
                Toast.makeText(appContext, R.string.err_connect_db, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        });
        pmpconnector.bind();
    }
    
    
    /**
     * Stores the appointment ONLY in the database and NOT in the model
     * 
     * @param date
     *            date of the appointment
     * @param name
     *            name of the appointment
     * @param description
     *            description of the appointment
     * @param severity
     *            {@link Severity} of the appointment
     */
    public void storeNewAppointmentWithoutModel(final Date date, final String name, final String description,
            final Severity severity) {
        
        final PMPServiceConnector pmpconnector = new PMPServiceConnector(this.appContext);
        pmpconnector.addCallbackHandler(new AbstractConnectorCallback() {
            
            @Override
            public void onConnect(AbstractConnector connector) throws RemoteException {
                IBinder binder = pmpconnector.getAppService().getResource(pkgName, resGroupIdentifier, resIdentifier);
                if (binder != null) {
                    IDatabaseConnection idc = IDatabaseConnection.Stub.asInterface(binder);
                    try {
                        // The values to add
                        Map<String, String> values = new HashMap<String, String>();
                        int id = Model.getInstance().getNewHighestId();
                        
                        values.put(SqlConnector.this.ID, String.valueOf(id));
                        values.put(SqlConnector.this.NAME, name);
                        values.put(SqlConnector.this.DESC, description);
                        values.put(SqlConnector.this.DATE, String.valueOf(date.getTime()));
                        values.put(SqlConnector.this.SEVERITY, severity.toString());
                        
                        long result = idc.insert(SqlConnector.this.DB_TABLE_NAME, null, values);
                        Log.v("Return value of insert: " + result);
                        if (result != -1) {
                            Log.v("Storing new appointment: id: " + String.valueOf(id) + " date: " + date
                                    + " description: " + description);
                        } else {
                            Toast.makeText(Model.getInstance().getContext(),
                                    Model.getInstance().getContext().getString(R.string.err_store), Toast.LENGTH_SHORT)
                                    .show();
                            Log.e("Appointment not stored");
                        }
                    } catch (RemoteException e) {
                        Toast.makeText(Model.getInstance().getContext(),
                                Model.getInstance().getContext().getString(R.string.err_store), Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Remote Exception", e);
                    } finally {
                        idc.close();
                    }
                }
            }
            
            
            @Override
            public void onBindingFailed(AbstractConnector connector) {
                Log.e("Could not connect to database resource");
                Looper.prepare();
                Toast.makeText(appContext, R.string.err_connect_db, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        });
        pmpconnector.bind();
    }
    
    
    /**
     * Delete the appointment out of the SQL database with the given id and then calls {@link Model#deleteDateByID(int)}
     * 
     * @param id
     *            id of the appointment to delete
     */
    public void deleteAppointment(final Appointment app) {
        final PMPServiceConnector pmpconnector = new PMPServiceConnector(this.appContext);
        pmpconnector.addCallbackHandler(new AbstractConnectorCallback() {
            
            @Override
            public void onConnect(AbstractConnector connector) throws RemoteException {
                IBinder binder = pmpconnector.getAppService().getResource(pkgName, resGroupIdentifier, resIdentifier);
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
                                    Model.getInstance().getContext().getString(R.string.err_del), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } catch (RemoteException e) {
                        Toast.makeText(Model.getInstance().getContext(),
                                Model.getInstance().getContext().getString(R.string.err_del), Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Remote Exception", e);
                    } finally {
                        idc.close();
                    }
                }
            }
            
            
            @Override
            public void onBindingFailed(AbstractConnector connector) {
                Log.e("Could not connect to database resource");
                Looper.prepare();
                Toast.makeText(appContext, R.string.err_connect_db, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        });
        pmpconnector.bind();
    }
    
    
    public void deleteAllApointments() {
        final PMPServiceConnector pmpconnector = new PMPServiceConnector(this.appContext);
        pmpconnector.addCallbackHandler(new AbstractConnectorCallback() {
            
            @Override
            public void onConnect(AbstractConnector connector) throws RemoteException {
                IBinder binder = pmpconnector.getAppService().getResource(pkgName, resGroupIdentifier, resIdentifier);
                if (binder != null) {
                    IDatabaseConnection idc = IDatabaseConnection.Stub.asInterface(binder);
                    try {
                        if (idc.deleteTable(DB_TABLE_NAME)) {
                            Log.d("Table deleted");
                        } else {
                            Log.e("Could not delete table");
                        }
                    } catch (RemoteException e) {
                        Toast.makeText(Model.getInstance().getContext(),
                                Model.getInstance().getContext().getString(R.string.err_del), Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Remote Exception", e);
                    } finally {
                        idc.close();
                    }
                }
            }
            
            
            @Override
            public void onBindingFailed(AbstractConnector connector) {
                Log.e("Could not connect to database resource");
                Looper.prepare();
                Toast.makeText(appContext, R.string.err_connect_db, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        });
        pmpconnector.bind();
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
    public void changeAppointment(final Integer id, final Date date, final Date oldDate, final String name, final String description,
            final Severity severity) {
        final PMPServiceConnector pmpconnector = new PMPServiceConnector(this.appContext);
        pmpconnector.addCallbackHandler(new AbstractConnectorCallback() {
            
            @Override
            public void onConnect(AbstractConnector connector) throws RemoteException {
                IBinder binder = pmpconnector.getAppService().getResource(pkgName, resGroupIdentifier, resIdentifier);
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
                            Log.v("Changing date with id " + String.valueOf(id) + " to: date: " + date
                                    + " description: " + description);
                        } else {
                            Toast.makeText(Model.getInstance().getContext(),
                                    Model.getInstance().getContext().getString(R.string.err_change), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } catch (RemoteException e) {
                        Toast.makeText(Model.getInstance().getContext(),
                                Model.getInstance().getContext().getString(R.string.err_change), Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Remote Exception", e);
                    } finally {
                        idc.close();
                    }
                }
            }
            
            
            @Override
            public void onBindingFailed(AbstractConnector connector) {
                Log.e("Could not connect to database resource");
                Looper.prepare();
                Toast.makeText(appContext, R.string.err_connect_db, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        });
        pmpconnector.bind();
    }
    
    
    /**
     * Creates a table if there exists none. The table name is "Appointment".
     */
    public void createTable() {
        final PMPServiceConnector pmpconnector = new PMPServiceConnector(this.appContext);
        pmpconnector.addCallbackHandler(new AbstractConnectorCallback() {
            
            @Override
            public void onConnect(AbstractConnector connector) throws RemoteException {
                
                IBinder binder = pmpconnector.getAppService().getResource(pkgName, resGroupIdentifier, resIdentifier);
                
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
                                Model.getInstance().getContext().getString(R.string.err_create), Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Remote Exception", e);
                    } finally {
                        idc.close();
                    }
                }
            }
            
            
            @Override
            public void onBindingFailed(AbstractConnector connector) {
                Log.e("Could not connect to database resource");
                Looper.prepare();
                Toast.makeText(appContext, R.string.err_connect_db, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        });
        
        pmpconnector.bind();
    }
}
