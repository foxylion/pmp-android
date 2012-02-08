package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.List;
import java.util.TimerTask;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.FoundProfilePos;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.PassengerObject;

/**
 * Checks in a given interval if a ride invitation was accepted. If an invitation is accepted notify driver by changing
 * button
 * from a question mark to "checked", count down available seats by one and add accepted passenger to "ban-list"
 * 
 * @author andres
 * 
 */
public class CheckAcceptedOffers extends TimerTask {
    
    private Button acceptButton;
    private TextView name;
    private int userID;
    private Handler handler;
    private Controller ctrl;
    
    
    /**
     * Handles time interval to check for accepted offers
     * 
     * @param acceptButton
     *            : Button which background is changed if offer is accepted
     * @param userID
     *            : userID to check for
     */
    public CheckAcceptedOffers(Button acceptButton, TextView name, int userID) {
        this.acceptButton = acceptButton;
        this.name = name;
        this.userID = userID;
        handler = new Handler();
        ctrl = new Controller();
    }
    
    
    @Override
    public void run() {
        
        handler.post(new Runnable() {
            
            public void run() {
                Log.i(this, "Checking");
                List<PassengerObject> lpo = ctrl.offer_accepted(Model.getInstance().getSid(), Model.getInstance()
                        .getTripId());
                // check if invitations were accepted
                if (lpo.size() > 0) {
                    
                    for (int i = 0; i < lpo.size(); i++) {
                        // set button/invitation as checked
                        if (lpo.get(i).getUser_id() == userID) {
                            acceptButton.setBackgroundResource(R.drawable.bg_check);
                            acceptButton.invalidate();
                            
                            Log.i(this, " Offer Accepted");
                            
                            acceptButton.setOnClickListener(new View.OnClickListener() {
                                
                                @Override
                                public void onClick(View v) {
                                    ctrl.pick_up(Model.getInstance().getSid(), userID);
                                    name.setTextColor(Color.BLUE);
                                    acceptButton.setBackgroundResource(R.drawable.bg_disabled);
                                    acceptButton.setEnabled(false);
                                    
                                    // add to list which contains all picked up users
                                    Model.getInstance().addToPickedUser(userID);
                                }
                            });
                            
                            // count down available seats
                            ctrl.tripUpdateData(Model.getInstance().getSid(), Model.getInstance().getTripId(), MapModel
                                    .getInstance().getNumSeats() - 1);
                            
                            // stop checking
                            cancel();
                        }
                    }
                }
            }
        });
    }
}
