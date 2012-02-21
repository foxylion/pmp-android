package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.List;
import java.util.TimerTask;

import android.os.Handler;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.PositionObject;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.QueryObject;

/**
 * Check for ride queries every given time interval
 * 
 * 
 * @author Andre Nguyen, Alexander Wassiljew
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
     * Check for queries every given interval
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
                
                try {
                    PositionObject pObject = ctrl.getUserPosition(Model.getInstance().getSid(), me.getID());
                    lat = (float) pObject.getLat();
                    lng = (float) pObject.getLon();
                } catch (Exception ex) {
                    Log.i(this, "NULLPOINTER");
                }
                // retrieve all hitchhikers searching for a ride within my perimeter
                lqo = ctrl.searchQuery(Model.getInstance().getSid(), lat, lng, perimeter);
                
                // send ViewModel new list of hitchhikers
                ViewModel.getInstance().updateLQO(lqo);
            }
        });
    }
    
}
