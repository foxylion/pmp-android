package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import de.unistuttgart.ipvs.pmp.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProfileActivity extends Activity {

	static final String[] RECENT_RIDES = new String[] {
			"01.01.2011, Stuttgart", "02.01.2011, Berlin", "03.01.2011, Vaihingen", "..." };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		ListView recentRides = (ListView) findViewById(R.id.lv_recent_rides);

		recentRides.setAdapter(new ArrayAdapter<String>(this,
				R.layout.list_profile_example, RECENT_RIDES));
		
	}
}
