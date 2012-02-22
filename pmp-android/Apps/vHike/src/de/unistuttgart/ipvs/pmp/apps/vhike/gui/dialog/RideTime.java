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
package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;

/**
 * Set time of ride
 * 
 * @author Andre Nguyen
 * 
 */
public class RideTime extends Dialog {
    
    private Context mContext;
    private Button btn_back;
    private Button btn_apply;
    
    
    public RideTime(Context context) {
        super(context);
        this.mContext = context;
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_time);
        setTitle("Pick time");
        
        registerButtons();
    }
    
    
    private void registerButtons() {
        this.btn_back = (Button) findViewById(R.id.dialog_time_back);
        this.btn_back.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                cancel();
                vhikeDialogs.getInstance().getRideDate(RideTime.this.mContext).show();
            }
        });
        
        this.btn_apply = (Button) findViewById(R.id.dialog_time_apply);
        this.btn_apply.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                /**
                 * TODO: apply picked date and time
                 */
                cancel();
            }
        });
    }
    
}
