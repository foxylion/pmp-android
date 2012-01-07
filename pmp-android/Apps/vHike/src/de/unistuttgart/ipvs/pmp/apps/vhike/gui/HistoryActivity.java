package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.widget.ListView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.HistoryAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.view.BasicTitleView;
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
			Log.i("ES IST DRIVER");
		} else {
			createPassengerActivity();
		}

	}

	private void createPassengerActivity() {
		title.setText(R.string.history_title_passenger);
		
		historyRides = ctrl.getHistory(Model.getInstance().getSid(), de.unistuttgart.ipvs.pmp.apps.vhike.Constants.ROLE_PASSENGER);
		if(historyRides!= null)
		Log.i("History grösse:" + historyRides.size());
		
		if(Model.getInstance().getHsitoryObjHolder()!=null)
		Log.i("Model History: " + Model.getInstance().getHsitoryObjHolder().size());
		
		this.adapter = new HistoryAdapter(this, historyRides);
		setListAdapter(this.adapter);
	}

	private void createDriverActivity() {
		title.setText(R.string.history_title_driver);
		
		historyRides = ctrl.getHistory(Model.getInstance().getSid(), de.unistuttgart.ipvs.pmp.apps.vhike.Constants.ROLE_DRIVER);

		this.adapter = new HistoryAdapter(this, historyRides);
		setListAdapter(this.adapter);
	}

}
