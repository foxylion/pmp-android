package de.unistuttgart.ipvs.pmp.apps.vhike.model;


public class FoundProfilePos {

    int userid;
    float lat;
    float lon;
    int query_id;
    public FoundProfilePos(int userid, float lat, float lon, int query_id) {
        super();
        this.userid = userid;
        this.lat = lat;
        this.lon = lon;
        this.query_id = query_id;
    }
    
    public int getUserid() {
        return userid;
    }
    
    public int getQueyId(){
        return query_id;
    }
    public float getLat() {
        return lat;
    }
    
    public float getLon() {
        return lon;
    }
    
    
}

