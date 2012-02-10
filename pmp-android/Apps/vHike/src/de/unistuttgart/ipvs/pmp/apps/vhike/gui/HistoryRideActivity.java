package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.HistoryRideAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.HistoryPersonObject;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.HistoryRideObject;
import android.R;
import android.app.ListActivity;
import android.os.Bundle;

public class HistoryRideActivity extends ListActivity{
    HistoryRideAdapter adapter;
    List<HistoryPersonObject> hPersonObjects;
    Controller ctrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        int id = this.getIntent().getExtras().getInt("ID");
        String role = this.getIntent().getExtras().getString("ROLE");
        ctrl = new Controller();
        setContentView(de.unistuttgart.ipvs.pmp.apps.vhike.R.layout.activity_history_profile_list);
        List<HistoryRideObject> historyRides = ctrl.getHistory(Model.getInstance().getSid(), role);
        if(historyRides.size()==0){
            hPersonObjects = new ArrayList<HistoryPersonObject>();
            hPersonObjects.add(new HistoryPersonObject(0, "no user found", 0, 0, false));
        }else{
            hPersonObjects = historyRides.get(id).getPersons();    
        }
        
        Log.i(this, "Größe von Persons: "+hPersonObjects.size()+", ID: "+ id);
        this.adapter = new HistoryRideAdapter(HistoryRideActivity.this, hPersonObjects);
        setListAdapter(adapter);
    }
}
