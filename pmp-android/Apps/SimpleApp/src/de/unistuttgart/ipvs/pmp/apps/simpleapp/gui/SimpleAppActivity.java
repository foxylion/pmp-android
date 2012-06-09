/*
 * Copyright 2012 pmp-android development team
 * Project: SimpleApp
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
package de.unistuttgart.ipvs.pmp.apps.simpleapp.gui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.apps.simpleapp.R;
import de.unistuttgart.ipvs.pmp.apps.simpleapp.provider.Model;

public class SimpleAppActivity extends Activity {
    
    public Handler handler;
    
    private Button buttonServiceFeatures;
    
    private Button wirelessRefreshButton;
    private TextView wirelessStateTextView;
    private ToggleButton wirelessToggleButton;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.handler = new Handler();
        
        setContentView(R.layout.main);
        
        this.buttonServiceFeatures = (Button) findViewById(R.id.Button_ChangeServiceFeatures);
        
        this.wirelessRefreshButton = (Button) findViewById(R.id.Button_WifiRefresh);
        this.wirelessStateTextView = (TextView) findViewById(R.id.TextView_WirelessState);
        this.wirelessToggleButton = (ToggleButton) findViewById(R.id.ToggleButton_Wifi);
        
        PMP.get(getApplication());
        
        Model.getInstance().setActivity(this);
        
        addListener();
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        refresh();
    }
    
    
    private void addListener() {
        this.buttonServiceFeatures.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                PMP.get().requestServiceFeatures(SimpleAppActivity.this, Model.SF_WIFI_STATE, Model.SF_WIFI_CHANGE);
            }
        });
        
        this.wirelessRefreshButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                refreshWifi();
            }
        });
        
        this.wirelessToggleButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                Model.getInstance().setWifi(SimpleAppActivity.this.wirelessToggleButton.isChecked());
            }
        });
    }
    
    
    public void refresh() {
        refreshWifi();
        
        String string = "Current active SF:\n";
        string += Model.SF_WIFI_STATE + ": " + PMP.get().isServiceFeatureEnabled(Model.SF_WIFI_STATE) + "\n";
        string += Model.SF_WIFI_CHANGE + ": " + PMP.get().isServiceFeatureEnabled(Model.SF_WIFI_CHANGE);
        
        Toast.makeText(SimpleAppActivity.this, string, Toast.LENGTH_LONG).show();
    }
    
    
    public void refreshWifi() {
        
        if (!PMP.get().isServiceFeatureEnabled(Model.SF_WIFI_STATE)) {
            // Wireless State SF is disabled
            this.wirelessStateTextView.setText("missingSF");
        } else {
            Model.getInstance().getWifi(this.handler, this.wirelessStateTextView, this.wirelessToggleButton);
        }
        
        if (!PMP.get().isServiceFeatureEnabled(Model.SF_WIFI_CHANGE)) {
            // Wireless State SF is disabled
            this.wirelessToggleButton.setEnabled(false);
        } else {
            this.wirelessToggleButton.setEnabled(true);
        }
    }
    
}
