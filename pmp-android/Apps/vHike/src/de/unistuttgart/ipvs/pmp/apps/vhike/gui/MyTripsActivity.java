package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.vHikeService;
import de.unistuttgart.ipvs.pmp.apps.vhike.exception.QueryException;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.MyTripAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.ResourceGroupReadyActivity;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.CompactTrip;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;

/**
 * 
 * @author Dang Huynh
 * 
 */
public class MyTripsActivity extends ResourceGroupReadyActivity implements OnItemClickListener,
        OnClickListener {
    
    private ArrayList<CompactTrip> t;
    private ListView listView;
    private MyTripAdapter adaper;
    private Controller ctrl;
    private Handler handler;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);
        
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        
        findViewById(R.id.btnNewTrip).setOnClickListener(this);
        
        // TODO Remove this line
        //        PMP.get(getApplication()).register(new PMPRegistrationHandler());
        //        vHikeService.getInstance().updateServiceFeatures();
        //        if (vHikeService.isServiceFeatureEnabled(Constants.SF_VHIKE_WEB_SERVICE))
        //            getvHikeRG(this);
        //        else {
        //            System.out.println("NO!");
        //            PMP.get().requestServiceFeatures(this, "de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS");
        //        }
        handler = new Handler();
    }
    
    
    @Override
    public void onResourceGroupReady(IInterface resourceGroup, int resourceGroupId) {
        super.onResourceGroupReady(resourceGroup, resourceGroupId);
        if (resourceGroupId == Constants.RG_VHIKE_WEBSERVICE) {
            
            if (ctrl == null)
                ctrl = new Controller(rgvHike);
            
            // TODO Test code
            //            if (Model.getInstance().getSid().equals("")) {
            //                ctrl.login("driver", "test12345");
            //            }
            
            loadData();
        }
    }
    
    
    @Override
    public void onResume() {
        super.onResume();
        
        if (rgvHike != null) {
            vHikeService.getInstance().updateServiceFeatures();
            if (ctrl == null)
                ctrl = new Controller(rgvHike);
            loadData();
        }
    }
    
    
    private void loadData() {
        try {
            t = ctrl.getTrips(Model.getInstance().getSid());
        } catch (QueryException e) {
            Toast.makeText(this, e.getMessage(), 2000);
            e.printStackTrace();
        }
        
        //        t = new ArrayList<CompactTrip>(10);
        //        t.add(new CompactTrip(1, "Berlin", 1000000000, 2, 3, 1));
        //        t.add(new CompactTrip(2, "Stuttgart", 1000000000, 0, 3, 1));
        //        t.add(new CompactTrip(3, "Muenchen", 1000000000, 2, 0, 1));
        //        t.add(new CompactTrip(4, "Koeln", 1000000000, 2, 3, 0));
        //        t.add(new CompactTrip(5, "Duesseldorf", 1000000000, 2, 3, 1));
        //        t.add(new CompactTrip(6, "Hamburg", 1000000000, 0, 0, 0));
        //        t.add(new CompactTrip(7, "Dortmund", 1000000000, 2, 3, 1));
        //        t.add(new CompactTrip(8, "Bremen", 1000000000, 2, 3, 1));
        //        t.add(new CompactTrip(9, "Hanover", 1000000000, 2, 3, 1));
        //        t.add(new CompactTrip(10, "Leipzig", 1000000000, 2, 3, 1));
        
        // update the GUI
        handler.post(new Runnable() {
            
            @Override
            public void run() {
                MyTripsActivity activity = MyTripsActivity.this;
                if (MyTripsActivity.this.adaper == null) {
                    MyTripsActivity.this.adaper = new MyTripAdapter(MyTripsActivity.this,
                            t == null ? t = new ArrayList<CompactTrip>(0) : t);
                    MyTripsActivity.this.listView.setAdapter(adaper);
                } else {
                    MyTripsActivity.this.adaper.notifyDataSetChanged();
                }
                System.out.println(activity.adaper.getCount());
                Toast.makeText(activity, String.valueOf(activity.adaper.getCount()), 2000);
            }
        });
    }
    
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.listView:
                Intent intent = new Intent(this, TripDetailActivity.class);
                intent.putExtra("tripId", t.get(position).id);
                this.startActivity(intent);
                break;
            
            default:
                break;
        }
    }
    
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNewTrip:
                Intent intent = new Intent(this, PlanTripActivity.class);
                startActivityForResult(intent, 0);
                break;
            
            default:
                break;
        }
    }
}
