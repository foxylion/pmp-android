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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import de.unistuttgart.ipvs.pmp.R;

/**
 * Dialog for a pontential passenger who accepted an offer is awaiting to be picked up and confirm that his query search
 * is over
 * 
 * @author Andre Nguyen
 * 
 */
public class Wait4PickUp extends Dialog {
    
    public Wait4PickUp(final Context context) {
        super(context);
        setContentView(R.layout.dialog_query_finito);
        
        Button btn = (Button) findViewById(R.id.btn_query_finish);
        btn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                cancel();
                ((Activity) context).finish();
            }
        });
    }
    
}
