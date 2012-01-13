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
package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemConnector;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemListActionType;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.adapter.SeparatedListAdapter;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.NewAppointmentDialog;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.util.UiManager;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Appointment;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector.SqlConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.email.IEmailOperations;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnector;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;

public class CalendarAppActivity extends ListActivity {
    
    private CalendarAppActivity self = this;
    
    /**
     * The actual aplication context
     */
    private Context appContext;  
    
    /**
     * Called when the activity is first created. Creates the list and shows the dates.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Model.getInstance().setContext(this.self);
        Model.getInstance().addHandler(new Handler());
        
        this.appContext = getApplicationContext();
        
        setContentView(R.layout.list_layout);
        
        SeparatedListAdapter spa = new SeparatedListAdapter(this.appContext);
        setListAdapter(spa);
        Model.getInstance().setArrayAdapter(spa);
        
        /*
         * Listener for long clicking on one item. Opens a context menu where
         * the user can delete a appointment or send it via email
         */
        this.getListView().setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                menu.setHeaderTitle(getString(R.string.menu));
                menu.add(0, 0, 0, R.string.delete);
                menu.add(1, 1, 0, R.string.send);
            }
        });
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        // Connector to check if the app is registered yet
        final PMPServiceConnector pmpconnector = new PMPServiceConnector(this.appContext);
        pmpconnector.addCallbackHandler(new AbstractConnectorCallback() {
            
            @Override
            public void onConnect(AbstractConnector connector) throws RemoteException {
                
                // Check if the service is registered yet
                if (!pmpconnector.getAppService().isRegistered(getPackageName())) {
                    Log.v("Registering");
                    UiManager.getInstance().showWaitingDialog();
                    pmpconnector.getAppService().registerApp(getPackageName());
                    
                    /*
                     * Changes the functionality according to the service feature that is set.
                     * Will be called when the activity is started after on resume and
                     * called when the activity is shown again.
                     */
                    ((CalendarApp) getApplication()).changeFunctionalityAccordingToServiceFeature(false);
                } else {
                    Log.v("App registered");
                    ((CalendarApp) getApplication()).changeFunctionalityAccordingToServiceFeature(true);
                }
            }
            
            
            @Override
            public void onBindingFailed(AbstractConnector connector) {
                Looper.prepare();
                AlertDialog.Builder builder = new AlertDialog.Builder(self);
                builder.setMessage(R.string.not_found).setTitle(R.string.error).setCancelable(true)
                        .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                            
                            public void onClick(DialogInterface dialog, int id) {
                                // Close the dialog and close the calendar app
                                dialog.cancel();
                                self.finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                Looper.loop();
                
            }
        });
        
        // Connect to the service
        pmpconnector.bind();
    }
    
    
    @Override
    protected void onPause() {
        super.onPause();
        Model.getInstance().clearLocalListWithoutTextViewUpdate();
        
    }
    
    
    @Override
    public boolean onContextItemSelected(MenuItem aItem) {
        // The menu information
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) aItem.getMenuInfo();
        final Appointment clicked = (Appointment) Model.getInstance().getArrayAdapter().getItem(menuInfo.position);
        
        /*
         * Called when the user presses sth. in the menu that appears while long clicking
         */
        if (aItem.getItemId() == 0) {
            if (((App) getApplication()).isServiceFeatureEnabled("write")) {
                new SqlConnector().deleteAppointment(clicked);
            } else {
                String[] req = new String[1];
                req[0] = "write";
                UiManager.getInstance().showServiceFeatureInsufficientDialog(req);
            }
            
            return true;
        }
        if (aItem.getItemId() == 1) {
            new Thread() {
                
                @Override
                public void run() {
                    App app = ((App) appContext);
                    IBinder binder = app.getResourceBlocking("de.unistuttgart.ipvs.pmp.resourcegroups.email",
                            "emailOperations");
                    
                    if (binder != null) {
                        IEmailOperations emailOP = IEmailOperations.Stub.asInterface(binder);
                        try {
                            String subject = getString(R.string.subject) + ": " + clicked.getName();
                            String severity = getString(R.string.severity) + " ";
                            switch (clicked.getSeverity()) {
                                case HIGH:
                                    severity += getString(R.string.severity_high);
                                    break;
                                case MIDDLE:
                                    severity += getString(R.string.severity_middle);
                                    break;
                                case LOW:
                                    severity += getString(R.string.severity_low);
                                    break;
                            }
                            emailOP.sendEmail("", subject, getString(R.string.date) + " " + clicked.getDateString()
                                    + "\n" + getString(R.string.desc) + ": " + clicked.getDescrpition() + "\n"
                                    + severity);
                        } catch (RemoteException e) {
                            Log.e("Couldn't send E-Mail", e);
                        }
                        
                    } else {
                        // Request other service features
                        ArrayList<String> sfs = new ArrayList<String>();
                        if (!((App) getApplication()).isServiceFeatureEnabled("send")) {
                            sfs.add("send");
                        }
                        if (!((App) getApplication()).isServiceFeatureEnabled("read")) {
                            sfs.add("read");
                        }
                        
                        UiManager.getInstance().showServiceFeatureInsufficientDialog(
                                sfs.toArray(new String[sfs.size()]));
                    }
                }
            }.start();
            return true;
        }
        return false;
    }
    
    
    /**
     * Add the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calendar_menu, menu);
        return true;
    }
    
    
    /**
     * Respond to user interaction with the menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        App app = (App) self.getApplication();
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_appointment:
                if (app.isServiceFeatureEnabled("write")) {
                    
                    // Show the new appointment dialog
                    Dialog dialog = new NewAppointmentDialog(Model.getInstance().getContext());
                    dialog.show();
                } else {
                    String[] req = new String[1];
                    req[0] = "write";
                    UiManager.getInstance().showServiceFeatureInsufficientDialog(req);
                }
                return true;
            case R.id.delete_all_appointments:
                if (app.isServiceFeatureEnabled("write")) {
                    
                    // Show the confirm dialog for deleting all appointments
                    new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.delete_all_appointments)
                            .setMessage(R.string.delete_all_appointments_question)
                            .setPositiveButton(R.string.conf, new DialogInterface.OnClickListener() {
                                
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Model.getInstance().deleteAllAppointments();
                                }
                                
                            }).show();
                } else {
                    String[] req = new String[1];
                    req[0] = "write";
                    UiManager.getInstance().showServiceFeatureInsufficientDialog(req);
                }
                return true;
            case R.id.import_appointments:
                if (app.isServiceFeatureEnabled("import") && app.isServiceFeatureEnabled("write")) {
                    /*
                     * Fill the list of files for importing.
                     * It is also used to check for exporting, if a file already exists.
                     */
                    new FileSystemConnector().listStoredFiles(FileSystemListActionType.IMPORT);
                    
                } else {
                    // Request other service features
                    ArrayList<String> sfs = new ArrayList<String>();
                    if (!((App) getApplication()).isServiceFeatureEnabled("import")) {
                        sfs.add("import");
                    }
                    if (!((App) getApplication()).isServiceFeatureEnabled("write")) {
                        sfs.add("write");
                    }
                    UiManager.getInstance().showServiceFeatureInsufficientDialog(sfs.toArray(new String[sfs.size()]));
                }
                return true;
            case R.id.export_appointments:
                if (app.isServiceFeatureEnabled("export")) {
                    /*
                     * Fill the list of files for importing.
                     * It is also used to check for exporting, if a file already exists.
                     */
                    new FileSystemConnector().listStoredFiles(FileSystemListActionType.EXPORT);
                    
                } else {
                    String[] req = new String[1];
                    req[0] = "export";
                    UiManager.getInstance().showServiceFeatureInsufficientDialog(req);
                }
                
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    
    /**
     * Update the visibility of the "no appointments available" textview
     */
    public void updateNoAvaiableAppointmentsTextView() {
        
        // add text view "no appointments available", if the list is empty
        TextView tv = (TextView) findViewById(R.id.no_appointments_avaiable);
        if (Model.getInstance().isModelEmpty()) {
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
        }
    }
}
