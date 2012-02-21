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
