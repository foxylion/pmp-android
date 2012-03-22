package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.IInterface;
import android.widget.ListView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.HistoryAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.ResourceGroupReadyListActivity;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.HistoryRideObject;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;

public class HistoryActivity extends ResourceGroupReadyListActivity {
    
    HistoryAdapter adapter;
    ListView lv;
    List<HistoryRideObject> historyRides;
    BasicTitleView btv;
    TextView title;
    Controller ctrl;
    Handler handler;
    boolean isD;
    @Override
    public void onResourceGroupReady(IInterface resourceGroup, int resourceGroupId) {
        super.onResourceGroupReady(resourceGroup, resourceGroupId);
        Log.i(this, "RG ready: " + resourceGroup);
        if (rgvHike != null) {
            handler.post(new Runnable() {
                
                @Override
                public void run() {
                    ctrl = new Controller(rgvHike);
                    if (isD) {
                        createDriverActivity();
                    } else {
                        createPassengerActivity();
                    } 
                }
            });
        }
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        handler = new Handler();
        // Get and prepare Title
        this.btv = (BasicTitleView) findViewById(R.id.btv);
        this.title = (TextView) this.btv.findViewById(R.id.TextView_Title);
        
        // Read extras
        boolean isDriver = getIntent().getExtras().getBoolean("IS_DRIVER");
        isD = isDriver;
        if(getvHikeRG(this)!= null)
            ctrl = new Controller(rgvHike);
        
        if (isDriver) {
            createDriverActivity();
        } else {
            createPassengerActivity();
        }
        
    }
    
    
    private void createPassengerActivity() {
        this.title.setText(R.string.history_title_passenger);
        
        this.historyRides = this.ctrl.getHistory(Model.getInstance().getSid(), Constants.ROLE_PASSENGER);
        
        if (this.historyRides.size() == 0) {
            this.historyRides = new ArrayList<HistoryRideObject>();
            this.historyRides.add(new HistoryRideObject(0, 0, "no entries found", null, "no entries found", null));
        }
        
        this.adapter = new HistoryAdapter(this, this.historyRides, Constants.ROLE_PASSENGER);
        setListAdapter(this.adapter);
    }
    
    
    private void createDriverActivity() {
        this.title.setText(R.string.history_title_driver);
        
        this.historyRides = this.ctrl.getHistory(Model.getInstance().getSid(), Constants.ROLE_DRIVER);
        
        if (this.historyRides.size() == 0) {
            this.historyRides = new ArrayList<HistoryRideObject>();
            this.historyRides.add(new HistoryRideObject(0, 0, "no entries found", null, "no entries found", null));
        }
        
        this.adapter = new HistoryAdapter(this, this.historyRides, Constants.ROLE_DRIVER);
        setListAdapter(this.adapter);
    }
    
}
