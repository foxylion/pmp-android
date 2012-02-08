package de.unistuttgart.ipvs.pmp.apps.vhike.model;


public class FoundProfilePos {

    int userid;
    double lat;
    double lon;
    public FoundProfilePos(int userid, double lat, double lon) {
        super();
        this.userid = userid;
        this.lat = lat;
        this.lon = lon;
    }
    
    public int getUserid() {
        return userid;
    }
    
    public double getLat() {
        return lat;
    }
    
    public double getLon() {
        return lon;
    }
    
    
}

