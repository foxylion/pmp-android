package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
     * The menu
     */
    private Menu menu;
    
    
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
        
        updateMenuVisibility();
    }
    
    
    @Override
    public boolean onContextItemSelected(MenuItem aItem) {
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) aItem.getMenuInfo();
        final Appointment clicked = Model.getInstance().getAppointmentByIndex(menuInfo.position);
        /*
         * Called when the user presses sth. in the menu that appears while long clicking
         */
        if (aItem.getItemId() == 0) {
            
            SqlConnector.getInstance().deleteAppointment(clicked.getId());
            return true;
        }
        if (aItem.getItemId() == 1) {
            
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
                            emailOP.sendEmail("", getString(R.string.subject),
                                    getString(R.string.appoint) + formatter.format(cal.getTime()) + "\n"
                                            + getString(R.string.desc) + ": " + clicked.getDescrpition());
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
        
        this.menu = menu;
        
        updateMenuVisibility();
        
        return true;
    }
    
    
    /**
     * Respond to user interaction with the menu
     * 
     * @author Marcus Vetter
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_appointment:
                Dialog dialog = new NewAppointmentDialog(Model.getInstance().getContext());
                dialog.setTitle("Create new appointment");
                dialog.show();
                return true;
            case R.id.delete_all_appointments:
                return true;
            case R.id.import_appointments:
                // Open activity with file list
                Intent intent = new Intent(Model.getInstance().getContext(), ImportActivity.class);
                if (Model.getInstance().getContext() != null) {
                    Model.getInstance().getContext().startActivity(intent);
                }
                return true;
            case R.id.export_appointments:
                // Open dialog for entering a file name
                Dialog exportDialog = new ExportDialog(this);
                exportDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    
    /**
     * Update the visibility of the menu
     */
    private void updateMenuVisibility() {
        // Update the menu visibility
        if (menu != null) {
            // Get the service level
            int serviceLevel = Model.getInstance().getServiceLevel();
            
            // Set new appointment functionality
            menu.getItem(0).setEnabled(serviceLevel >= 2);
            
            // Set delete all appointments functionality
            menu.getItem(1).setEnabled(serviceLevel >= 2);
            
            // Set import functionality
            menu.getItem(2).setEnabled(serviceLevel >= 4);
            
            // Set export functionality
            menu.getItem(3).setEnabled(serviceLevel >= 6);
        }
    }
}
