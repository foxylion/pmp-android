package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.ChangeDateDialog;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.NewDateDialog;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.util.DialogManager;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Date;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector.SqlConnector;
import de.unistuttgart.ipvs.pmp.service.utils.IConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;

public class CalendarAppActivity extends ListActivity {
    
    private CalendarAppActivity self = this;
    
    /**
     * The arrayAdapter of the list
     */
    private static ArrayAdapter<Date> arrayAdapter;
    
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
                    ((App) getApplication()).start(getApplicationContext());
                    
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
        arrayAdapter = new ArrayAdapter<Date>(this, R.layout.list_item, Model.getInstance().getDateList());
        Model.getInstance().setArrayAdapter(arrayAdapter);
        setListAdapter(arrayAdapter);
        
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        
        /*
         * Listener for adding a new date. Opens a new dialog
         */
        Button newDate = (Button) findViewById(R.id.AddDate);
        Model.getInstance().setNewDateButton(newDate);
        newDate.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (Model.getInstance().getServiceLevel() == 1) {
                    Dialog dialog = new NewDateDialog(CalendarAppActivity.this.self);
                    dialog.setTitle("Create new date");
                    dialog.show();
                }
            }
        });
        
        /*
         * Listener for long clicking on one item. Opens a context menu where
         * the user can delete this date
         */
        listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("ContextMenu");
                menu.add(0, 0, 0, "Delete this date");
            }
        });
        
        /*
         * Listener for clicking one item. Opens a new dialog where the user can
         * change the date.
         */
        listView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Model.getInstance().getServiceLevel() == 1) {
                    Dialog changeDateDialog = new ChangeDateDialog(CalendarAppActivity.this.self, position);
                    changeDateDialog.show();
                }
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
        if (Model.getInstance().getServiceLevel() == 1) {
            AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) aItem.getMenuInfo();
            SqlConnector.getInstance().deleteDate(Model.getInstance().getDateByIndex(menuInfo.position).getId());
            arrayAdapter.notifyDataSetChanged();
        }
        return true;
    }
}
