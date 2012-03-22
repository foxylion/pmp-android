package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.TimerTask;

import android.os.Handler;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.aidl.IvHikeWebservice;

/**
 * Check for ride queries every given time interval
 * 
 * 
 * @author Alexander Wassiljew, Andre Nguyen
 * 
 */
public class Check4AcceptedOffers extends TimerTask {
    
    private Handler handler;
    private Controller ctrl;
    private ViewObject object;
    private int offer_id;
    
    
    public Check4AcceptedOffers(IvHikeWebservice ws,ViewObject object, int offer_id) {
        this.handler = new Handler();
        this.ctrl = new Controller(ws);
        this.object = object;
        this.offer_id = offer_id;
        
    }
    
    @Override
    public void run() {
        this.handler.post(new Runnable() {
            
            @Override
            public void run() {
                
                switch (Check4AcceptedOffers.this.ctrl.offer_accepted(Model.getInstance().getSid(),
                        Check4AcceptedOffers.this.offer_id)) {
                    case Constants.STATUS_UNREAD:
                        Log.i(this, "UNREAD OFFER!");
                        break;
                    case Constants.STATUS_ACCEPTED:
                        Check4AcceptedOffers.this.object.setStatus(Constants.V_OBJ_SATUS_ACCEPTED);
                        ViewModel.getInstance().updateView(0);
                        Log.i(this, "ACCEPTED OFFER!");
                        cancel();
                        break;
                    case Constants.STATUS_DENIED:
                        Check4AcceptedOffers.this.object.setStatus(Constants.V_OBJ_SATUS_BANNED);
                        ViewModel.getInstance().addToBanned(Check4AcceptedOffers.this.object);
                        Log.i(this, "DENIED OFFER!");
                        ViewModel.getInstance().updateView(0);
                        cancel();
                        break;
                
                }
            }
        });
    }
    
}
