package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.List;
import java.util.TimerTask;

import android.os.Handler;
import android.widget.Button;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
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
    private int userID;
    private Handler handler;
    
    
    /**
     * Handles time interval to check for accepted offers
     * 
     * @param acceptButton
     *            : Button which background is changed if offer is accepted
     * @param userID
     *            : userID to check for
     */
    public CheckAcceptedOffers(Button acceptButton, int userID) {
        this.acceptButton = acceptButton;
        this.userID = userID;
        handler = new Handler();
    }
    
    
    @Override
    public void run() {
        
        handler.post(new Runnable() {
            
            public void run() {
                Log.i(this, "Checking");
                Controller ctrl = new Controller();
                List<PassengerObject> lpo = ctrl.offer_accepted(Model.getInstance().getSid(), Model.getInstance()
                        .getTripId());
                // check if invitations were accepted
                if (lpo.size() > 0) {
                    
                    for (int i = 0; i < lpo.size(); i++) {
                        // set button/invitation as checked
                        if (lpo.get(i).getUser_id() == userID) {
                            acceptButton.setBackgroundResource(R.drawable.bg_check);
                            Log.i(this, "OFFER ACCEPTED");
                            // count down available seats
                            // ctrl.tripUpdateData(Model.getInstance().getSid(), Model.getInstance().getTripId(), MapModel
                            // .getInstance().getNumSeats() - 1);
                            
                            // stop checking
                            cancel();
                            
                            // in Bannliste
                        }
                    }
                }
            }
        });
    }
    
}
