package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.List;
import java.util.TimerTask;

import android.os.Handler;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.OfferObject;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.aidl.IvHikeWebservice;

/**
 * Check for incoming offers from drivers
 * 
 * @author Alexander Wassiljew, Andre Nguyen
 * 
 */
public class Check4Offers extends TimerTask {
    
    private Handler handler;
    private Controller ctrl;
    private List<OfferObject> loo;
    
    
    public Check4Offers(IvHikeWebservice ws) {
        this.handler = new Handler();
        this.ctrl = new Controller(ws);
    }
    
    
    @Override
    public void run() {
        this.handler.post(new Runnable() {
            
            @Override
            public void run() {
                
                Check4Offers.this.loo = Check4Offers.this.ctrl.viewOffers(Model.getInstance().getSid());
                ViewModel.getInstance().updateLOO(Check4Offers.this.loo);
            }
        });
    }
    
}
