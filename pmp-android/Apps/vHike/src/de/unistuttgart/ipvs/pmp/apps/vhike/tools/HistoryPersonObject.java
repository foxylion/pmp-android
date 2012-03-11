package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

public class HistoryPersonObject {
    
    int userid;
    String username;
    float rating;
    int rating_num;
    boolean rated;
    
    
    public HistoryPersonObject(int userid, String username, float rating, int rating_num, boolean rated) {
        super();
        this.userid = userid;
        this.username = username;
        this.rating = rating;
        this.rating_num = rating_num;
        this.rated = rated;
    }
    
    
    public int getUserid() {
        return this.userid;
    }
    
    
    public String getUsername() {
        return this.username;
    }
    
    
    public float getRating() {
        return this.rating;
    }
    
    
    public int getRating_num() {
        return this.rating_num;
    }
    
    
    public boolean isRated() {
        return this.rated;
    }
    
}
