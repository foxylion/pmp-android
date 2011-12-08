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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemConnector;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemListActionType;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.NewAppointmentDialog;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.util.DialogManager;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Appointment;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector.SqlConnector;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnector;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;

public class CalendarAppActivity extends ListActivity {
    
    private CalendarAppActivity self = this;
    
    /**
     * The arrayAdapter of the list
     */
    private static ArrayAdapter<Appointment> arrayAdapter;
    
    /**
     * The actual context
     */
    private Context appContext;
    
    
    /**
     * Called when the activity is first created. Creates the list and shows the dates.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Model.getInstance().setContext(this.self);
        
        this.appContext = getApplicationContext();
        
        // Connector to check if the app is registered yet
        final PMPServiceConnector pmpconnector = new PMPServiceConnector(this.appContext);
        pmpconnector.addCallbackHandler(new AbstractConnectorCallback() {
            
            @Override
            public void onConnect(AbstractConnector connector) throws RemoteException {
                // Check if the service is registered yet
                if (!pmpconnector.getAppService().isRegistered(getPackageName())) {
                    Log.v("Registering");
                    DialogManager.getInstance().showWaitingDialog();
                    pmpconnector.getAppService().registerApp(getPackageName());
                    pmpconnector.unbind();
                } else {
                    Log.v("App registered");
                }
            }
            
            
            @Override
            public void onBindingFailed(AbstractConnector connector) {
                Looper.prepare();
                AlertDialog.Builder builder = new AlertDialog.Builder(self);
                builder.setMessage(R.string.no_register).setTitle(R.string.error).setCancelable(true)
                        .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                            
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                Looper.loop();
                
            }
        });
        // Connect to the service
        pmpconnector.bind();
        
        setContentView(R.layout.list_layout);
        
        // Array adapter that is needed to show the list of dates
        arrayAdapter = new ArrayAdapter<Appointment>(this, R.layout.list_item, Model.getInstance().getAppointmentList());
        Model.getInstance().setArrayAdapter(arrayAdapter);
        setListAdapter(arrayAdapter);
        
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        
        /*
         * Listener for long clicking on one item. Opens a context menu where
         * the user can delete a appointment or send it via email
         */
        listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                menu.setHeaderTitle(getString(R.string.menu));
                menu.add(0, 0, 0, R.string.delete);
                menu.add(1, 1, 0, R.string.send);
            }
        });
        
        // Update the visibility of the "no appointments available" textview
        updateNoAvaiableAppointmentsTextView();
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        /*
         * Changes the functionality according to the service feature that is set.
         * Will be called when the activity is started after on create and
         * called when the activity is shown again.
         */
        ((CalendarApp) getApplication()).changeFunctionalityAccordingToServiceFeature();
        
        // Update the visibility of the "no appointments avaiable" textview
        updateNoAvaiableAppointmentsTextView();
    }
    
    
    @Override
    public boolean onContextItemSelected(MenuItem aItem) {
        // The menu information
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) aItem.getMenuInfo();
        final Appointment clicked = Model.getInstance().getAppointmentByIndex(menuInfo.position);
        /*
         * Called when the user presses sth. in the menu that appears while long clicking
         */
        if (aItem.getItemId() == 0) {
            if (((App) getApplication()).isServiceFeatureEnabled("write")) {
                SqlConnector.getInstance().deleteAppointment(clicked.getId());
            } else {
                DialogManager.getInstance().showServiceFeatureInsufficientDialog(this);
            }
            
            return true;
        }
        if (aItem.getItemId() == 1) {
            if (((App) getApplication()).isServiceFeatureEnabled("send")
                    && ((App) getApplication()).isServiceFeatureEnabled("read")) {
                /*
                 * Connect to the EmailResourceGroup and send an mail with the date
                 */
                final String resGroupId = "de.unistuttgart.ipvs.pmp.resourcegroups.email";
                //                        final ResourceGroupServiceConnector resGroupCon = new ResourceGroupServiceConnector(this.appContext,
                //                                ((CalendarApp) this.appContext).getSignee(), resGroupId);
                //                        resGroupCon.addCallbackHandler(new IConnectorCallback() {
                //                            
                //                            @Override
                //                            public void disconnected() {
                //                                Log.d("Disconnected from " + resGroupId);
                //                            }
                //                            
                //                            
                //                            @Override
                //                            public void connected() {
                //                                Log.d("Connected to " + resGroupId);
                //                                try {
                //                                    IEmailOperations emailOP = IEmailOperations.Stub.asInterface(resGroupCon.getAppService()
                //                                            .getResource("emailOperations"));
                //                                    if (emailOP != null) {
                //                                        Calendar cal = new GregorianCalendar();
                //                                        cal.setTime(clicked.getDate());
                //                                        SimpleDateFormat formatter;
                //                                        formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                //                                        emailOP.sendEmail("", getString(R.string.subject), getString(R.string.appoint)
                //                                                + formatter.format(cal.getTime()) + "\n" + getString(R.string.desc) + ": "
                //                                                + clicked.getDescrpition());
                //                                    }
                //                                } catch (RemoteException e) {
                //                                    Log.e("Remote Exception: ", e);
                //                                } finally {
                //                                    resGroupCon.unbind();
                //                                }
                //                            }
                //                            
                //                            
                //                            @Override
                //                            public void bindingFailed() {
                //                                Log.e("Binding failed to " + resGroupId);
                //                            }
                //                        });
                //                        resGroupCon.bind();
                return true;
            } else {
                DialogManager.getInstance().showServiceFeatureInsufficientDialog(this);
            }
            
        }
        return false;
    }
    
    
    /**
     * Add the menu
     * 
     * @author Marcus Vetter
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calendar_menu, menu);
        return true;
    }
    
    
    /**
     * Respond to user interaction with the menu
     * 
     * @author Marcus Vetter
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
                            dialog.setTitle("Create new appointment");
                            dialog.show();
                        } else {
                            DialogManager.getInstance().showServiceFeatureInsufficientDialog(this);
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
                            DialogManager.getInstance().showServiceFeatureInsufficientDialog(this);
                        }
                        return true;
                    case R.id.import_appointments:
                        if (app.isServiceFeatureEnabled("import")) {
                            /*
                             * Fill the list of files for importing.
                             * It is also used to check for exporting, if a file already exists.
                             */
                            FileSystemConnector.getInstance().listStoredFiles(FileSystemListActionType.IMPORT);
                            
                        } else {
                            DialogManager.getInstance().showServiceFeatureInsufficientDialog(this);
                        }
                        return true;
                    case R.id.export_appointments:
                        if (app.isServiceFeatureEnabled("export")) {
                            /*
                             * Fill the list of files for importing.
                             * It is also used to check for exporting, if a file already exists.
                             */
                            FileSystemConnector.getInstance().listStoredFiles(FileSystemListActionType.EXPORT);
                            
                        } else {
                            DialogManager.getInstance().showServiceFeatureInsufficientDialog(this);
                        }
                        
                        return true;
                    default:
                        return super.onOptionsItemSelected(item);
                }
    }
    
    
    /**
     * Update the visibility of the "no appointments avaiable" textview
     */
    public void updateNoAvaiableAppointmentsTextView() {
        // add text view "no appointments available", if the list is empty
        TextView tv = (TextView) findViewById(R.id.no_appointments_avaiable);
        if (Model.getInstance().getAppointmentList().size() > 0) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
        }
    }
}
