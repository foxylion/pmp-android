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
    
    
    @Override
    public String toString() {
        return String.format("%.4f %s, %.4f %s", Math.abs(this.latitude), this.latitude > 0 ? "N" : "S",
                Math.abs(this.longitude), this.longitude > 0 ? "E" : "W");
    }
    
    
    @Override
    public int hashCode() {
        return Double.valueOf(this.latitude).hashCode() ^ Double.valueOf(this.longitude).hashCode();
    }
    
    
    @Override
    public boolean equals(Object o) {
        return o.hashCode() == hashCode();
    }
    
}
