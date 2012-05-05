package de.unistuttgart.ipvs.pmp.resourcegroups.profile.data;

public class Event {
    
    private int id;
    private long timestamp;
    private int type;
    private int special;
    
    
    public int getId() {
        return this.id;
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    
    public long getTimestamp() {
        return this.timestamp;
    }
    
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    
    public int getType() {
        return this.type;
    }
    
    
    public void setType(int type) {
        this.type = type;
    }
    
    
    public int getSpecial() {
        return this.special;
    }
    
    
    public void setSpecial(int special) {
        this.special = special;
    }
    
}
