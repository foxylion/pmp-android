package de.unistuttgart.ipvs.pmp.apps.vhike.model;


public class FoundProfilePos {

    int userid;
    float lat;
    float lon;
    public FoundProfilePos(int userid, float lat, float lon) {
        super();
        this.userid = userid;
        this.lat = lat;
        this.lon = lon;
    }
    
    public int getUserid() {
        return userid;
    }
    
    public float getLat() {
        return lat;
    }
    
    public float getLon() {
        return lon;
    }
    
    
}

