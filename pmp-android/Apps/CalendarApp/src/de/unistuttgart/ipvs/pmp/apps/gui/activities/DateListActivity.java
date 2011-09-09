package de.unistuttgart.ipvs.pmp.apps.gui.activities;

import de.unistuttgart.ipvs.pmp.apps.R;
import de.unistuttgart.ipvs.pmp.apps.gui.dialogs.ChangeDateDialog;
import de.unistuttgart.ipvs.pmp.apps.model.Date;
import de.unistuttgart.ipvs.pmp.apps.model.Model;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DateListActivity extends ListActivity {

    /**
     * The arrayAdapter of the list
     */
    private static ArrayAdapter<Date> arrayAdapter;

    /**
     * The actual context
     */
    private Context actualContext = this;

    /**
     * Called when the activity is first created. Creates the list and shows the
     * dates.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	arrayAdapter = new ArrayAdapter<Date>(this, R.layout.list_item, Model
		.getInstance().getDateList());

	setListAdapter(arrayAdapter);

	ListView listView = getListView();
	listView.setTextFilterEnabled(true);

	listView.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
		Dialog changeDateDialog = new ChangeDateDialog(actualContext,
			position, arrayAdapter);
		changeDateDialog.show();
	    }
	});

    }

}
