package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.List;
import java.util.TimerTask;

import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.QueryObject;

import android.os.Handler;

/**
 * Check for ride queries every given time interval
 * 
 * 
 * @author andres
 * 
 */
public class Check4Queries extends TimerTask {
    
    private Handler handler;
    private Controller ctrl;
    private Profile me;
    private List<QueryObject> lqo;
    private float lat;
    private float lng;
    private int perimeter = 10000;
    
    
    /**
     * 
     */
    public Check4Queries() {
        handler = new Handler();
        ctrl = new Controller();
        
        // get my Profile to retrieve latitude and longitude later
        me = Model.getInstance().getOwnProfile();
    }
    
    
    @Override
    public void run() {
        handler.post(new Runnable() {
            
            @Override
            public void run() {
                
                // retrieve my latitude and longitude, my current location
                lat = (float) ctrl.getUserPosition(Model.getInstance().getSid(), me.getID()).getLat();
                lng = (float) ctrl.getUserPosition(Model.getInstance().getSid(), me.getID()).getLon();
                
                // retrieve all hitchhikers searching for a ride within my perimeter
                lqo = ctrl.searchQuery(Model.getInstance().getSid(), lat, lng, perimeter);
                
                // send ViewModel new list of hitchhikers
                ViewModel.getInstance().updateLVO(lqo);
                
            }
        });
    }
    
}
