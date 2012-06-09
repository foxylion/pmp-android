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
    
    int sNumber;
    
    
    public SpinnerDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_remove_dest);
        setTitle("Remove destination");
        
        Button remove = (Button) findViewById(R.id.btn_remove_dest);
        remove.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                if (ViewModel.getInstance().getDestinationSpinners().size() > 1) {
                    
                    //                    int sNumber = ViewModel.getInstance().getClickedSpinner().getSelectedItemPosition();
                    for (int i = 0; i < ViewModel.getInstance().getDestinationSpinners().size(); i++) {
                        if (ViewModel.getInstance().getClickedSpinner() == ViewModel.getInstance()
                                .getDestinationSpinners().get(i)) {
                            SpinnerDialog.this.sNumber = i;
                        }
                    }
                    Log.i(this, "Selcted Item Pos: "
                            + ViewModel.getInstance().getClickedSpinner().getSelectedItemPosition());
                    ViewModel.getInstance().getDestinationSpinners().remove(SpinnerDialog.this.sNumber);
                    ViewModel.getInstance().getClickedSpinner().setVisibility(View.GONE);
                    Log.i(this, "Added spinner, Size"
                            + ViewModel.getInstance().getDestinationSpinners().size()
                            + ", Clicked ");
                    v.getParent().requestLayout();
                } else {
                    Toast.makeText(v.getContext(), "At least one destination must be given",
                            Toast.LENGTH_SHORT).show();
                }
                cancel();
            }
        });
    }
    
}
