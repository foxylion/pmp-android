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
package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.vHikeService;

public class ChangeServiceFeature extends Dialog {
    
    private Button cancel;
    private Button change_SF;
    private Context context;
    
    
    public ChangeServiceFeature(Context context) {
        super(context);
        setContentView(R.layout.dialog_change_sf);
        this.context = context;
        
        setLayout();
    }
    
    
    private void setLayout() {
        this.cancel = (Button) findViewById(R.id.btn_cancel_change);
        this.change_SF = (Button) findViewById(R.id.btn_open_pmp);
        
        this.cancel.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        
        this.change_SF.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                vHikeService.getInstance();
                // Open PMP
                vHikeService.requestServiceFeature((Activity) ChangeServiceFeature.this.context, 0);
            }
        });
    }
    
}
