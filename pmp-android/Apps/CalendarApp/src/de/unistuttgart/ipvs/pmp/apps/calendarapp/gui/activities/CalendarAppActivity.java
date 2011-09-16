package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.ChangeDateDialog;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.NewDateDialog;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.util.DialogManager;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Date;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.service.utils.IConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
     * Called when the activity is first created. Creates the list and shows the
     * dates.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	Model.getInstance().setContext(self);

	appContext = this.getApplicationContext();

	// Connector to check if the app is registered yet
	final PMPServiceConnector connector = new PMPServiceConnector(
		appContext, ((CalendarApp) appContext).getSignee());
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
		    ((App) getApplication()).start(appContext);

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
	arrayAdapter = new ArrayAdapter<Date>(this, R.layout.list_item, Model
		.getInstance().getDateList());
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
		Dialog dialog = new NewDateDialog(self);
		dialog.setTitle("Create new date");
		dialog.show();
	    }
	});

	/*
	 * Listener for long clicking the item. Opens a context menu where the
	 * user can delete this date
	 */
	listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

	    @Override
	    public void onCreateContextMenu(ContextMenu menu, View v,
		    ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("ContextMenu");
		menu.add(0, 0, 0, "Delete this date");
	    }
	});

	/*
	 * Listener for clicking one item. Opens a new dialog where the user can
	 * change the date.
	 */
	listView.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
		Dialog changeDateDialog = new ChangeDateDialog(self, position);
		changeDateDialog.show();
	    }
	});
	
	((CalendarApp) getApplication()).changeFunctionalityAccordingToServiceLevel();
    }

    @Override
    protected void onResume() {
	super.onResume();
    }

    @Override
    public boolean onContextItemSelected(MenuItem aItem) {
	AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) aItem
		.getMenuInfo();
	Model.getInstance().deleteDateByIndex(menuInfo.position);
	arrayAdapter.notifyDataSetChanged();
	return true;
    }
}
