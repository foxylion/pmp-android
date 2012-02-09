package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.List;
import java.util.TimerTask;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

import android.content.Context;
import android.os.Handler;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.FoundProfilePos;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.OfferObject;

/**
 * Check for incoming offers from drivers and draw found drivers in map and add them to slider list
 * 
 * @author andres
 * 
 */
public class Check4Offers extends TimerTask {
    
    private Handler handler;
    private Controller ctrl;
    private List<OfferObject> loo;
    
    
    public Check4Offers() {
        handler = new Handler();
        ctrl = new Controller();
    }
    
    
    @Override
    public void run() {
        handler.post(new Runnable() {
            
            @Override
            public void run() {
    
                loo = ctrl.viewOffers(Model.getInstance().getSid());
//                ViewModel.getInstance().updateLOO(loo);
                
            }
        });
    }
    
}
