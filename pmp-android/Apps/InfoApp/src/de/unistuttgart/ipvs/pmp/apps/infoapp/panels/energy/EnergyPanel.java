/*
 * Copyright 2012 pmp-android development team
 * Project: InfoApp
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
package de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.apps.infoapp.Constants;
import de.unistuttgart.ipvs.pmp.apps.infoapp.R;
import de.unistuttgart.ipvs.pmp.apps.infoapp.common.EneryUploadResourceHandler;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.IPanel;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy.data.EnergyCurrentValues;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy.data.EnergyLastBootValues;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy.data.EnergyTotalValues;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class EnergyPanel implements IPanel {
    
    private LinearLayout view;
    
    private EnergyExtListViewAdapter adapter;
    
    private Application application;
    
    private Handler handler;
    
    private Activity activity;
    
    private EneryUploadResourceHandler resHandler;
    
    
    public EnergyPanel(Context context, Activity activity) {
        
        // Set the application, the handler and the activity
        this.activity = activity;
        this.application = activity.getApplication();
        this.handler = new Handler();
        
        // load the layout from the xml file
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = (LinearLayout) inflater.inflate(R.layout.energy_panel, null);
        
        ExpandableListView listView = (ExpandableListView) this.view.findViewById(R.id.energyPanelExpandableListView);
        this.adapter = new EnergyExtListViewAdapter(context, activity, new EnergyCurrentValues(),
                new EnergyLastBootValues(), new EnergyTotalValues());
        listView.setAdapter(this.adapter);
        
        // Get the data
        update();
    }
    
    
    public View getView() {
        return this.view;
    }
    
    
    public String getTitle() {
        return "Energy";
    }
    
    
    public void update() {
        
        final PMPResourceIdentifier id = PMPResourceIdentifier.make(Constants.ENERGY_RG_IDENTIFIER,
                Constants.ENERGY_RG_RESOURCE);
        
        PMP.get(this.application).getResource(id,
                new EnergyResourceRequestHandler(this.adapter, this.handler, this.application));
    }
    
    
    public void upload(ProgressDialog dialog) {
        if (PMP.get(this.application).isServiceFeatureEnabled(Constants.ENERGY_SF_UPLOAD_DATA)) {
            final PMPResourceIdentifier id = PMPResourceIdentifier.make(Constants.ENERGY_RG_IDENTIFIER,
                    Constants.ENERGY_RG_RESOURCE);
            this.resHandler = new EneryUploadResourceHandler(dialog, this.activity);
            PMP.get(EnergyPanel.this.application).getResource(id, EnergyPanel.this.resHandler);
        } else {
            dialog.dismiss();
            List<String> sfs = new ArrayList<String>();
            sfs.add("energy-upload");
            PMP.get(this.activity.getApplication()).requestServiceFeatures(this.activity, sfs);
        }
    }
}
