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
    private MapView mapView;
    private Context context;
    
    
    public Check4Offers(MapView mapView, Context context) {
        this.mapView = mapView;
        this.context = context;
        
        handler = new Handler();
        ctrl = new Controller();
    }
    
    
    @Override
    public void run() {
        handler.post(new Runnable() {
            
            @Override
            public void run() {
                
                Profile me = Model.getInstance().getOwnProfile();
                
                if (Model.getInstance().isPicked(me.getID())) {
                    cancel();
                } else {
                    MapModel.getInstance().clearPassengerOverlayList();
                    MapModel.getInstance().getHitchDrivers().clear();
                    
                    int lati = (int) (Model.getInstance().getLatitude() * 1E6);
                    int lngi = (int) (Model.getInstance().getLongtitude() * 1E6);
                    GeoPoint gps = new GeoPoint(lati, lngi);
                    MapModel.getInstance().add2PassengerOverlay(context, gps, me, mapView, 0, 1);
                    
                    List<OfferObject> loo = ctrl.viewOffers(Model.getInstance().getSid());
                    if (loo != null && loo.size() > 0) {
                        for (int i = 0; i < loo.size(); i++) {
                            Profile driver = ctrl.getProfile(Model.getInstance().getSid(), loo.get(i).getUser_id());
                            int lat = (int) (loo.get(i).getLat() * 1E6);
                            int lng = (int) (loo.get(i).getLon() * 1E6);
                            GeoPoint gpsDriver = new GeoPoint(lat, lng);
                            
                            MapModel.getInstance().add2PassengerOverlay(context, gpsDriver, driver, mapView, 1, 0);
                            MapModel.getInstance().getHitchDrivers().add(driver);
                            
                            if (!Model.getInstance().isFinded(loo.get(i).getUser_id())) {
                                MapModel.getInstance().fireNotification(context, driver, loo.get(i).getUser_id(), 1,
                                        mapView);
                                MapModel.getInstance().getDriverAdapter(context, mapView).notifyDataSetChanged();
                            }
                            
                            MapModel.getInstance().getDriverAdapter(context, mapView).notifyDataSetChanged();
                            mapView.invalidate();
                            mapView.postInvalidate();
                            Model.getInstance().addToFoundUsers(
                                    new FoundProfilePos(loo.get(i).getUser_id(), loo.get(i).getLat(), loo.get(i)
                                            .getLon(), -1));
                        }
                    }
                }
            }
            
        });
    }
    
}
