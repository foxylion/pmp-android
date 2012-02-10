package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.HistoryAdapter;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.HistoryRideObject;

public class HistoryActivity extends ListActivity {

	HistoryAdapter adapter;
	ListView lv;
	List<HistoryRideObject> historyRides;
	BasicTitleView btv;
	TextView title;
	Controller ctrl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);

		ctrl = new Controller();
		// Get and prepare Title
		btv = (BasicTitleView) findViewById(R.id.btv);
		title = (TextView) btv.findViewById(R.id.TextView_Title);

		// Read extras
		boolean isDriver = this.getIntent().getExtras().getBoolean("IS_DRIVER");

		if (isDriver) {
			createDriverActivity();
		} else {
			createPassengerActivity();
		}

	}

	private void createPassengerActivity() {
		title.setText(R.string.history_title_passenger);

		historyRides = ctrl.getHistory(Model.getInstance().getSid(), Constants.ROLE_PASSENGER);
		
		if(historyRides.size()==0){
			historyRides = new ArrayList<HistoryRideObject>();
			historyRides.add(new HistoryRideObject(0, 0, "no entries found", null, "no entries found", null));
		}

		this.adapter = new HistoryAdapter(this, historyRides, Constants.ROLE_PASSENGER);
		setListAdapter(this.adapter);
	}

	private void createDriverActivity() {
		title.setText(R.string.history_title_driver);
		

		historyRides = ctrl.getHistory(Model.getInstance().getSid(),Constants.ROLE_DRIVER);
		
		if(historyRides.size()==0){
			historyRides = new ArrayList<HistoryRideObject>();
			historyRides.add(new HistoryRideObject(0, 0, "no entries found", null, "no entries found", null));
		}
		
		this.adapter = new HistoryAdapter(this, historyRides, Constants.ROLE_DRIVER);
		setListAdapter(this.adapter);
	}

}
