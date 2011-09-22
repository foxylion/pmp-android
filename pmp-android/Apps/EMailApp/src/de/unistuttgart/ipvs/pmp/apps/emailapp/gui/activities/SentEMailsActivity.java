package de.unistuttgart.ipvs.pmp.apps.emailapp.gui.activities;

import de.unistuttgart.ipvs.pmp.apps.emailapp.R;
import de.unistuttgart.ipvs.pmp.apps.emailapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.emailapp.model.data.EMail;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SentEMailsActivity extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.list_layout);
	/*
	 * Sets the title
	 */
	TextView tv = (TextView) findViewById(R.id.header);
	tv.setText("Sent E-mails");

	ArrayAdapter<EMail> arrayAdapter = new ArrayAdapter<EMail>(this,
		R.layout.list_item, Model.getInstance().getSentEMails());
	setListAdapter(arrayAdapter);

	ListView listView = getListView();
	listView.setTextFilterEnabled(true);

    }
}