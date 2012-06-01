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
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.aidl.IvHikeWebservice;

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
    public Check4Queries(IvHikeWebservice ws, Handler handler) {
        this.handler = handler;
        this.ctrl = new Controller(ws);
        
        // get my Profile to retrieve latitude and longitude later
        this.me = Model.getInstance().getOwnProfile();
    }
    
    
    @Override
    public void run() {
        this.handler.post(new Runnable() {
            
            @Override
            public void run() {
                
                if (ViewModel.getInstance().allowStartSearch4Query()) {
                    try {
                        PositionObject pObject = Check4Queries.this.ctrl.getUserPosition(Model.getInstance().getSid(),
                                Check4Queries.this.me.getID());
                        Check4Queries.this.lat = (float) pObject.getLat();
                        Check4Queries.this.lng = (float) pObject.getLon();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    // retrieve all hitchhikers searching for a ride within my perimeter
                    Check4Queries.this.lqo = Check4Queries.this.ctrl.searchQuery(Model.getInstance().getSid(),
                            Check4Queries.this.lat, Check4Queries.this.lng, Check4Queries.this.perimeter);
                    
                    // send ViewModel new list of hitchhikers
                    ViewModel.getInstance().updateLQO(Check4Queries.this.lqo);
                    
                    if (ViewModel.getInstance().queryIsCanceled()) {
                        cancel();
                        Log.i(this, "Canceled Check4Queries");
                    }
                } else {
                    Log.i(this, "Waiting for location");
                }
                
            }
        });
    }
    
}
