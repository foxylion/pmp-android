package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

public class RateProfileConfirm extends Dialog {
    
    private int profileID;
    private int rating;
    private int tripID;
    
    
    public RateProfileConfirm(Context context, final int profileID, final int rating, final int tripID) {
        super(context);
        setContentView(R.layout.dialog_rate_profile);
        setTitle("Confirm rating");
        this.profileID = profileID;
        this.rating = rating;
        this.tripID = tripID;
        
        Button cancel = (Button) findViewById(R.id.btn_cancel_rate);
        cancel.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        
        Button confirm = (Button) findViewById(R.id.btn_ok_rate);
        confirm.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // rate
                Controller ctrl = new Controller();
                String rate = ctrl.rateUser(Model.getInstance().getSid(), profileID, tripID, rating);
                if (rate.equals("rated")) {
                    Log.i(this, "RATED " + profileID + ", WITH " + rating + ", TripID " + tripID);
                } else if (rate.equals("already_rated")) {
                    Log.i(this, "Not RATED " + profileID + ", WITH " + rating + ", TripID " + tripID);
                } else if (rate.equals("not_ended")) {
                    
                }
                
                cancel();
            }
        });
    }
    
}