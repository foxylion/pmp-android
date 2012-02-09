package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.List;
import java.util.TimerTask;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.FoundProfilePos;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.QueryObject;

public class Check4Queries extends TimerTask {
    
    private Handler handler;
    private Controller ctrl;
    private MapView mapView;
    private Context context;
    //    private Location location;
    
    private double lat;
    private double lng;
    
    
    public Check4Queries(MapView mapView, Context context, double lat, double lng) {
        this.mapView = mapView;
        this.context = context;
        //        this.location = location;
        this.lat = lat;
        this.lng = lng;
        
        handler = new Handler();
        ctrl = new Controller();
    }
    
    
    @Override
    public void run() {
        handler.post(new Runnable() {
            
            @Override
            public void run() {
            }
            
        });
    }
    
}
