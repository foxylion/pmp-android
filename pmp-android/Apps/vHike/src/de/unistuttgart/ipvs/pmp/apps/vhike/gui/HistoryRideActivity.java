package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.HistoryRideAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.HistoryPersonObject;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.HistoryRideObject;

public class HistoryRideActivity extends ListActivity {
    
    HistoryRideAdapter adapter;
    List<HistoryPersonObject> hPersonObjects;
    Controller ctrl;
    int tripid;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        int id = getIntent().getExtras().getInt("ID");
        String role = getIntent().getExtras().getString("ROLE");
        this.ctrl = new Controller();
        setContentView(de.unistuttgart.ipvs.pmp.apps.vhike.R.layout.activity_history_profile_list);
        List<HistoryRideObject> historyRides = this.ctrl.getHistory(Model.getInstance().getSid(), role);
        if (historyRides.size() == 0) {
            this.hPersonObjects = new ArrayList<HistoryPersonObject>();
            this.hPersonObjects.add(new HistoryPersonObject(0, "no user found", 0, 0, false));
        } else {
            this.hPersonObjects = historyRides.get(id).getPersons();
            this.tripid = historyRides.get(id).getTripid();
        }
        
        Log.i(this, "Größe von Persons: " + this.hPersonObjects.size() + ", ID: " + id);
        this.adapter = new HistoryRideAdapter(HistoryRideActivity.this, this.hPersonObjects, this.tripid);
        setListAdapter(this.adapter);
    }
}
