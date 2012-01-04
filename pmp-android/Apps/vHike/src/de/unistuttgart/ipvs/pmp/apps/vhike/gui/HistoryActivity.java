package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.HistoryAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.HistoryRideObject;



public class HistoryActivity extends ListActivity {

	HistoryAdapter adapter;
	ListView lv ;
	List<HistoryRideObject> historyRides;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		
		historyRides = new ArrayList<HistoryRideObject>();
		
		historyRides.add(new HistoryRideObject("DATUM", "STUTTGART"));
		historyRides.add(new HistoryRideObject("DATUM", "BERLIN"));
		historyRides.add(new HistoryRideObject("DATUM", "MÜNCHEN"));
		
		
        this.adapter = new HistoryAdapter(this, historyRides);
        setListAdapter(this.adapter);
        
        boolean isDriver = this.getIntent().getExtras().getBoolean("IS_DRIVER");
        
    }

	
}
