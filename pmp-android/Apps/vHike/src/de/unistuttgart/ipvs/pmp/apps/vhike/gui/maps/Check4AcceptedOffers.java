package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.List;
import java.util.TimerTask;

import android.os.Handler;
import de.unistuttgart.ipvs.pmp.Log;
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
    private int offer_id;
    
    /**
     * 
     */
    public Check4AcceptedOffers(ViewObject object, int offer_id) {
        handler = new Handler();
        ctrl = new Controller();
        this.object = object;
        this.offer_id = offer_id;
        
    }
    
    
    @Override
    public void run() {
        handler.post(new Runnable() {
            
            @Override
            public void run() {
                
                switch (ctrl.offer_accepted(Model.getInstance().getSid(), offer_id)) {
                    case Constants.STATUS_UNREAD:
                        Log.i(this, "UNREAD OFFER!");
                        break;
                    case Constants.STATUS_ACCEPTED:
                        object.setStatus(Constants.V_OBJ_SATUS_ACCEPTED);
                        ViewModel.getInstance().updateView(0);
                        Log.i(this, "ACCEPTED OFFER!");
                        cancel();
                        break;
                    case Constants.STATUS_DENIED:
                        object.setStatus(Constants.V_OBJ_SATUS_BANNED);
                        Log.i(this, "DENIED OFFER!");
                        ViewModel.getInstance().updateView(0);
                        cancel();
                        break;
                        
                }
            }
        });
    }
    
}
