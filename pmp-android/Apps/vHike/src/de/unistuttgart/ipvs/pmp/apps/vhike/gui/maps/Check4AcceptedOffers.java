package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.List;
import java.util.TimerTask;

import android.os.Handler;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.PassengerObject;

/**
 * Check for ride queries every given time interval
 * 
 * 
 * @author andres
 * 
 */
public class Check4AcceptedOffers extends TimerTask {
    
    private Handler handler;
    private Controller ctrl;
    private ViewObject object;
    
    
    /**
     * 
     */
    public Check4AcceptedOffers(ViewObject object) {
        handler = new Handler();
        ctrl = new Controller();
        this.object = object;
        
    }
    
    
    @Override
    public void run() {
        handler.post(new Runnable() {
            
            @Override
            public void run() {
                
                List<PassengerObject> pasObjects = ctrl.offer_accepted(Model.getInstance().getSid(), Model
                        .getInstance().getTripId());
                for (PassengerObject passengerObject : pasObjects) {
                    if (passengerObject.getUser_id() == object.getqObject().getUserid()) {
                        object.setStatus(Constants.V_OBJ_SATUS_ACCEPTED);
                        ViewModel.getInstance().updateView();
                        cancel();
                    }
                }
            }
        });
    }
    
}
