package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.aidl.IvHikeWebservice;

/**
 * After editing the rating bar user has to confirm is rating through this dialog
 * 
 * @author Andre Nguyen
 * 
 */
public class RateProfileConfirm extends Dialog {
    
    @SuppressWarnings("unused")
    private int profileID;
    @SuppressWarnings("unused")
    private int rating;
    @SuppressWarnings("unused")
    private int tripID;
    
    
    public RateProfileConfirm(IvHikeWebservice ws, Context context, final int profileID, final int rating,
            final int tripID) {
        super(context);
        setContentView(R.layout.dialog_rate_profile);
        setTitle("Confirm rating");
        this.profileID = profileID;
        this.rating = rating;
        this.tripID = tripID;
        final Controller ctrl = new Controller(ws);
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
                String rate = ctrl.rateUser(Model.getInstance().getSid(), profileID, tripID, rating);
                if (rate.equals("rated")) {
                    Toast.makeText(v.getContext(), "Rated with " + rating + " stars", Toast.LENGTH_LONG)
                            .show();
                } else if (rate.equals("already_rated")) {
                    Toast.makeText(v.getContext(), "You already rated this user!", Toast.LENGTH_LONG).show();
                } else if (rate.equals("not_ended")) {
                    
                }
                
                cancel();
            }
        });
    }
    
}
