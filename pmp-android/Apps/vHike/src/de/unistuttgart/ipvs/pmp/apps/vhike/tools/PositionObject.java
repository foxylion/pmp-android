package de.unistuttgart.ipvs.pmp.apps.vhike.tools;


public class PositionObject {
 
    private double lat;
    private double lon;
    
    public PositionObject(double lat, double lon) {
        super();
        this.lat = lat;
        this.lon = lon;
    }

    
    public double getLat() {
        return lat;
    }

    
    public double getLon() {
        return lon;
    }
    
    
}
