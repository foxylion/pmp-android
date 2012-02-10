package de.unistuttgart.ipvs.pmp.model.context.location;

/**
 * A geo point for the {@link LocationContextCondition}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class LocationContextGeoPoint {
    
    private double latitude, longitude;
    
    
    public LocationContextGeoPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    
    public double getLatitude() {
        return this.latitude;
    }
    
    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    
    public double getLongitude() {
        return this.longitude;
    }
    
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
}
