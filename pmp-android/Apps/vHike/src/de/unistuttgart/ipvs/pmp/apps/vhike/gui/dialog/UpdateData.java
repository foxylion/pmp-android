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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewModel;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;

/**
 * Through this dialog a rides available/needed seat count can be updated
 * 
 * @author Andre Nguyen
 * 
 */
public class UpdateData extends Dialog {
    
    private Context mContext;
    private Controller ctrl;
    private Spinner spinner_destination;
    private Spinner spinner_numSeats;
    
    private Button apply;
    private Button cancel;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_data);
        setTitle("Change trip");
        
        this.ctrl = new Controller();
        this.spinner_destination = (Spinner) findViewById(R.id.update_spinner);
        this.spinner_numSeats = (Spinner) findViewById(R.id.update_spinner_numSeats);
        this.apply = (Button) findViewById(R.id.dialog_update_apply);
        this.cancel = (Button) findViewById(R.id.dialog_update_cancel);
        
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.mContext, R.array.array_cities,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner_destination.setAdapter(adapter);
        
        adapter = ArrayAdapter.createFromResource(this.mContext, R.array.array_numSeats,
                android.R.layout.simple_spinner_item);
        this.spinner_numSeats.setAdapter(adapter);
        
        this.apply.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                ViewModel.getInstance().setDestination(UpdateData.this.spinner_destination);
                ViewModel.getInstance().setNumSeats(UpdateData.this.spinner_numSeats);
                
                switch (UpdateData.this.ctrl.tripUpdateData(Model.getInstance().getSid(), Model.getInstance()
                        .getTripId(), ViewModel.getInstance().getNumSeats())) {
                    case (Constants.STATUS_UPDATED):
                        Toast.makeText(v.getContext(), "Updated", Toast.LENGTH_SHORT).show();
                        
                        Log.i(this, "Destination:" + ViewModel.getInstance().getDestination() + ", Seats: "
                                + ViewModel.getInstance().getNumSeats());
                        cancel();
                        break;
                    case Constants.STATUS_UPTODATE:
                        Toast.makeText(v.getContext(), "Up to date", Toast.LENGTH_SHORT).show();
                        cancel();
                        break;
                    case Constants.STATUS_NOTRIP:
                        Toast.makeText(v.getContext(), "No trip", Toast.LENGTH_SHORT).show();
                        cancel();
                        break;
                    case Constants.STATUS_HASENDED:
                        Toast.makeText(v.getContext(), "Has ended", Toast.LENGTH_SHORT).show();
                        cancel();
                    case Constants.STATUS_INVALID_USER:
                        Toast.makeText(v.getContext(), "Invalid user", Toast.LENGTH_SHORT).show();
                        cancel();
                        break;
                }
            }
        });
        
        this.cancel.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }
    
    
    public UpdateData(Context context) {
        super(context);
        this.mContext = context;
    }
}
