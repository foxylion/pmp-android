package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.ExportDialog;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.NewAppointmentDialog;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.util.DialogManager;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Appointment;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector.SqlConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.email.IEmailOperations;
import de.unistuttgart.ipvs.pmp.service.utils.IConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;
import de.unistuttgart.ipvs.pmp.service.utils.ResourceGroupServiceConnector;

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
        final PMPServiceConnector connector = new PMPServiceConnector(this.appContext,
                ((CalendarApp) this.appContext).getSignee());
        connector.addCallbackHandler(new IConnectorCallback() {
            
            @Override
            public void disconnected() {
                Log.e("Disconnected");
            }
            
            
            @Override
            public void connected() {
                
                // Check if the service is registered yet
                if (!connector.isRegistered()) {
                    DialogManager.getInstance().showWaitingDialog();
                    
                    // Register the service
                    ((App) getApplication()).register(getApplicationContext());
                    
                    connector.unbind();
                } else {
                    Log.v("App already registered");
                    connector.unbind();
                }
            }
            
            
            @Override
            public void bindingFailed() {
                Log.e("Binding failed during registering app.");
            }
        });
        
        // Connect to the service
        connector.bind();
        
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
        
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        /*
         * Changes the functionality according to the service level that is set.
         * Will be called when the activity is started after on create and
         * called when the activity is shown again.
         */
        ((CalendarApp) getApplication()).changeFunctionalityAccordingToServiceLevel();
    }
    
    
    @Override
    public boolean onContextItemSelected(MenuItem aItem) {
        // Get the service level
        int serviceLevel = Model.getInstance().getServiceLevel();
        
        // The menu information
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) aItem.getMenuInfo();
        final Appointment clicked = Model.getInstance().getAppointmentByIndex(menuInfo.position);
        /*
         * Called when the user presses sth. in the menu that appears while long clicking
         */
        if (aItem.getItemId() == 0) {
            if (serviceLevel >= 2) {
                SqlConnector.getInstance().deleteAppointment(clicked.getId());
            } else {
                
            }
            
            return true;
        }
        if (aItem.getItemId() == 1) {
            if (serviceLevel == 3 || serviceLevel == 5 || serviceLevel == 7) {
                /*
                 * Connect to the EmailResourceGroup and send an mail with the date
                 */
                final String resGroupId = "de.unistuttgart.ipvs.pmp.resourcegroups.email";
                final ResourceGroupServiceConnector resGroupCon = new ResourceGroupServiceConnector(appContext,
                        ((CalendarApp) appContext).getSignee(), resGroupId);
                resGroupCon.addCallbackHandler(new IConnectorCallback() {
                    
                    @Override
                    public void disconnected() {
                        Log.d("Disconnected from " + resGroupId);
                    }
                    
                    
                    @Override
                    public void connected() {
                        Log.d("Connected to " + resGroupId);
                        try {
                            IEmailOperations emailOP = IEmailOperations.Stub.asInterface(resGroupCon.getAppService()
                                    .getResource("emailOperations"));
                            if (emailOP != null) {
                                Calendar cal = new GregorianCalendar();
                                cal.setTime(clicked.getDate());
                                SimpleDateFormat formatter;
                                formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                                emailOP.sendEmail("", getString(R.string.subject), getString(R.string.appoint)
                                        + formatter.format(cal.getTime()) + "\n" + getString(R.string.desc) + ": "
                                        + clicked.getDescrpition());
                            }
                        } catch (RemoteException e) {
                            Log.e("Remote Exception: ", e);
                        } finally {
                            resGroupCon.unbind();
                        }
                    }
                    
                    
                    @Override
                    public void bindingFailed() {
                        Log.e("Binding failed to " + resGroupId);
                    }
                });
                resGroupCon.bind();
                return true;
            } else {
                
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
        // Get the service level
        int serviceLevel = Model.getInstance().getServiceLevel();
        
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_appointment:
                if (serviceLevel >= 2) {
                    // Show the new appointment dialog
                    Dialog dialog = new NewAppointmentDialog(Model.getInstance().getContext());
                    dialog.setTitle("Create new appointment");
                    dialog.show();
                } else {
                    DialogManager.getInstance().showServiceLevelInsufficientDialog();
                }
                return true;
            case R.id.delete_all_appointments:
                if (serviceLevel >= 2) {
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
                    DialogManager.getInstance().showServiceLevelInsufficientDialog();
                }
                return true;
            case R.id.import_appointments:
                if (serviceLevel >= 4) {
                    // Open activity with file list
                    Intent intent = new Intent(Model.getInstance().getContext(), ImportActivity.class);
                    if (Model.getInstance().getContext() != null) {
                        Model.getInstance().getContext().startActivity(intent);
                    }
                } else {
                    DialogManager.getInstance().showServiceLevelInsufficientDialog();
                }
                return true;
            case R.id.export_appointments:
                if (serviceLevel >= 6) {
                    // Open dialog for entering a file name
                    Dialog exportDialog = new ExportDialog(this);
                    exportDialog.show();
                } else {
                    DialogManager.getInstance().showServiceLevelInsufficientDialog();
                }
                
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
