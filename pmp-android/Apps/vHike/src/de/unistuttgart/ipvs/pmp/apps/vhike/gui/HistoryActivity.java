/*
 * Copyright 2012 pmp-android development team
 * Project: vHike
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
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.HistoryAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.HistoryRideObject;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;

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
        
        this.ctrl = new Controller();
        // Get and prepare Title
        this.btv = (BasicTitleView) findViewById(R.id.btv);
        this.title = (TextView) this.btv.findViewById(R.id.TextView_Title);
        
        // Read extras
        boolean isDriver = getIntent().getExtras().getBoolean("IS_DRIVER");
        
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
