package de.unistuttgart.ipvs.pmp.model.context.location;

import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.util.MapsAPIKeyAsset;

public class LocationContextMapView extends MapActivity {
    
    private MapView map;
    
    
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        String mapKey = MapsAPIKeyAsset.getKey(this);
        if (mapKey == null) {
            // if not existing, I'm taking you down with me!
            throw new IllegalAccessError();
        }
        
        this.map = new MapView(this, mapKey);
        setContentView(this.map);
    }
    
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
}
