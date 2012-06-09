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
package de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewModel;

/**
 * Listener to add multiple stopovers(destinations)
 * 
 * @author Andre Nguyen, Dang Huynh
 * 
 */
public class AddStopOverListener implements OnClickListener {
    
    private Spinner spinner;
    private LinearLayout layout;
    
    
    @Override
    public void onClick(View v) {
        
        if (ViewModel.getInstance().getDestinationSpinners().size() == 0
                || ViewModel.getInstance().getDestinationSpinners().size() >= ViewModel.getInstance()
                        .getDestinationSpinners().get(0).getCount()) {
            Toast.makeText(v.getContext(), R.string.destination_max_reached, Toast.LENGTH_LONG).show();
            Log.i(this, "Size3: " + ViewModel.getInstance().getDestinationSpinners().size());
            return;
        } else {
            
            View root = v.getRootView();
            this.layout = (LinearLayout) root.findViewById(R.id.layout_dest);
            
            this.spinner = new Spinner(v.getContext());
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                    R.array.array_cities,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spinner.setAdapter(adapter);
            
            // add Spinner to "StopOver-List"/Spinner-List
            ViewModel.getInstance().getDestinationSpinners().add(this.spinner);
            Log.i(this, "Added spinner, Size" + ViewModel.getInstance().getDestinationSpinners().size()
                    + ", Clicked ");
            
            this.spinner.setOnLongClickListener(new OnLongClickListener() {
                
                @Override
                public boolean onLongClick(View v) {
                    
                    for (Spinner s : ViewModel.getInstance().getDestinationSpinners()) {
                        if (s == (Spinner) v) {
                            ViewModel.getInstance().setClickedSpinner(s);
                        }
                    }
                    vhikeDialogs.getInstance().spDialog(v.getContext()).show();
                    return false;
                }
                
            });
            
            Log.i(this, "Size1: " + ViewModel.getInstance().getDestinationSpinners().size());
            // add to layout 
            this.layout.addView(this.spinner);
        }
    }
}
