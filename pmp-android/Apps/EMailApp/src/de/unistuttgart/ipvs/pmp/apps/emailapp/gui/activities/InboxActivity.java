package de.unistuttgart.ipvs.pmp.apps.emailapp.gui.activities;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.emailapp.R;
import de.unistuttgart.ipvs.pmp.apps.emailapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.emailapp.model.data.EMail;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class InboxActivity extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.list_layout);
	/*
	 * Sets the title
	 */
	TextView tv = (TextView) findViewById(R.id.header);
	tv.setText("Inbox");
	
	ArrayAdapter<EMail>arrayAdapter = new ArrayAdapter<EMail>(this, R.layout.list_item, Model.getInstance().getInboxEMails());
        setListAdapter(arrayAdapter);
        
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	/*
	 * Creates the menu that is shown when the hardware menu button is
	 * pressed
	 */
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.newmail_menu, menu);
	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.new_mail:
            Log.d("Do something");
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}