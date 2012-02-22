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
package de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewModel;

/**
 * SpinnerDialog contains only one button to remove a stop over
 * 
 * @author Andre Nguyen
 * 
 */
public class SpinnerDialog extends Dialog {
    
    public SpinnerDialog(Context context) {
        super(context);
        setTitle("Remove destination");
        setContentView(R.layout.dialog_remove_dest);
        
        Button remove = (Button) findViewById(R.id.btn_remove_dest);
        remove.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (ViewModel.getInstance().getSpinners().size() > 1) {
                    ViewModel.getInstance().getClickedSpinner().setVisibility(View.GONE);
                    ViewModel.getInstance().getSpinners().remove(ViewModel.getInstance().getClickedSpinner());
                    Log.i(this, "Spinners size: " + ViewModel.getInstance().getSpinners().size());
                } else {
                    Toast.makeText(v.getContext(), "At least one destination must be given", Toast.LENGTH_SHORT).show();
                }
                cancel();
            }
        });
    }
    
}
