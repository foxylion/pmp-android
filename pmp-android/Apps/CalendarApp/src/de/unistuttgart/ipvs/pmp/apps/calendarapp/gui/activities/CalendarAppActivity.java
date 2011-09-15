package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities;

import de.unistuttgart.ipvs.pmp.app.DEPRECATED.utils.connector.PMPConnector;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.ChangeDateDialog;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.NewDateDialog;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Date;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
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

    /**
     * The arrayAdapter of the list
     */
    private static ArrayAdapter<Date> arrayAdapter;

    /**
     * The actual context
     */
    private Context actualContext;

    /**
     * Called when the activity is first created. Creates the list and shows the
     * dates.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	actualContext = this.getApplicationContext();
	PMPConnector.connectToPMP(actualContext, "de.unistuttgart.ipvs.pmp.apps.calendarapp");
	
	setContentView(R.layout.list_layout);
	arrayAdapter = new ArrayAdapter<Date>(this, R.layout.list_item, Model
		.getInstance().getDateList());
	setListAdapter(arrayAdapter);

	ListView listView = getListView();
	listView.setTextFilterEnabled(true);

	/*
	 * Listener for adding a new date. Opens a new dialog
	 */
	Button newDate = (Button) findViewById(R.id.AddDate);
	newDate.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Dialog dialog = new NewDateDialog(actualContext, arrayAdapter);
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
		Dialog changeDateDialog = new ChangeDateDialog(actualContext,
			position, arrayAdapter);
		changeDateDialog.show();
	    }
	});

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
