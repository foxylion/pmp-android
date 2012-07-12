/*
 * Copyright 2012 pmp-android development team
 * Project: vHikeApp
 * Project-Site: http://code.google.com/p/pmp-android/
 * 
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    
    private ArrayList<CompactTrip> trips;
    private ListView listView;
    private MyTripAdapter adaper;
    private Controller ctrl;
    private Handler handler;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);
        
        this.listView = (ListView) findViewById(R.id.listView);
        this.listView.setOnItemClickListener(this);
        
        findViewById(R.id.btnNewTrip).setOnClickListener(this);
        
        this.handler = new Handler();
    }
    
    
    @Override
    public void onResourceGroupReady(IInterface resourceGroup, int resourceGroupId) {
        super.onResourceGroupReady(resourceGroup, resourceGroupId);
        if (resourceGroupId == Constants.RG_VHIKE_WEBSERVICE) {
            
            if (this.ctrl == null) {
                this.ctrl = new Controller(rgvHike);
            }
            
            loadData();
        }
    }
    
    
    @Override
    public void onResume() {
        super.onResume();
        
        if (rgvHike != null) {
            vHikeService.getInstance().updateServiceFeatures();
            if (this.ctrl == null) {
                this.ctrl = new Controller(rgvHike);
            }
            loadData();
        }
    }
    
    
    private void loadData() {
        try {
            this.trips = this.ctrl.getTrips(Model.getInstance().getSid());
        } catch (QueryException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        
        // update the GUI
        this.handler.post(new Runnable() {
            
            @Override
            public void run() {
                MyTripsActivity activity = MyTripsActivity.this;
                if (activity.trips == null)
                    activity.trips = new ArrayList<CompactTrip>(0);
                if (activity.adaper == null) {
                    activity.adaper = new MyTripAdapter(
                            activity, // context
                            activity.trips);
                    activity.listView.setAdapter(activity.adaper);
                } else {
                    activity.adaper.clear();
                    for (CompactTrip t : activity.trips)
                        activity.adaper.add(t);
                    activity.adaper.notifyDataSetChanged();
                    activity.listView.refreshDrawableState();
                }
            }
        });
    }
    
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.listView:
                Intent intent = new Intent(this, TripDetailActivity.class);
                intent.putExtra("tripId", this.trips.get(position).id);
                startActivity(intent);
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
