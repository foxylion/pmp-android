package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

public class OfferObject {
    
    int offer_id;
    int user_id;
    String username;
    float rating;
    float rating_num;
    float lat;
    float lon;
    
    
    public OfferObject(int offer_id, int user_id, String username, float rating, float rating_num, float lat, float lon) {
        super();
        this.offer_id = offer_id;
        this.user_id = user_id;
        this.username = username;
        this.rating = rating;
        this.rating_num = rating_num;
        this.lat = lat;
        this.lon = lon;
    }
    
    
    public float getLat() {
        return this.lat;
    }
    
    
    public float getLon() {
        return this.lon;
    }
    
    
    public int getOffer_id() {
        return this.offer_id;
    }
    
    
    public int getUser_id() {
        return this.user_id;
    }
    
    
    public String getUsername() {
        return this.username;
    }
    
    
    public float getRating() {
        return this.rating;
    }
    
    
    public float getRating_num() {
        return this.rating_num;
    }
    
}
