package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.HistoryAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.view.BasicTitleView;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.HistoryRideObject;

public class HistoryActivity extends ListActivity {

	HistoryAdapter adapter;
	ListView lv;
	List<HistoryRideObject> historyRides;
	BasicTitleView btv;
	TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);

		// Get and prepare Title
		btv = (BasicTitleView) findViewById(R.id.btv);
		title = (TextView) btv.findViewById(R.id.TextView_Title);

		// Read extras
		boolean isDriver = this.getIntent().getExtras().getBoolean("IS_DRIVER");

		if (isDriver) {
			createDriverActivity();
			Log.i("ES IST DRIVER");
		} else {
			createPassengerActivity();
		}

	}

	private void createPassengerActivity() {
		title.setText(R.string.history_title_passenger);
		historyRides = new ArrayList<HistoryRideObject>();

		historyRides.add(new HistoryRideObject(0, 0, "DATUM", "STUTTGART", null, null));
		historyRides.add(new HistoryRideObject(0, 0, "DATUM", "BERLIN", null, null));
		historyRides.add(new HistoryRideObject(0, 0, "DATUM", "MÜNCHEN", null, null));

		this.adapter = new HistoryAdapter(this, historyRides);
		setListAdapter(this.adapter);
	}

	private void createDriverActivity() {
		title.setText(R.string.history_title_driver);
		historyRides = new ArrayList<HistoryRideObject>();

		historyRides.add(new HistoryRideObject(0, 0, "DATUM", "STUTTGART", null, null));
		historyRides.add(new HistoryRideObject(0, 0, "DATUM", "BERLIN", null, null));
		historyRides.add(new HistoryRideObject(0, 0, "DATUM", "MÜNCHEN", null, null));

		this.adapter = new HistoryAdapter(this, historyRides);
		setListAdapter(this.adapter);
	}

}
