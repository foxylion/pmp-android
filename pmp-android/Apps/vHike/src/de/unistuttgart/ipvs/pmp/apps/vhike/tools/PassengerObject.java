package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

public class PassengerObject {
    
    int user_id;
    
    boolean picked_up;
    
    
    public PassengerObject(int user_id, boolean picked) {
        super();
        this.user_id = user_id;
        this.picked_up = picked;
    }
    
    
    public int getUser_id() {
        return this.user_id;
    }
    
    
    public boolean isPicked_up() {
        return this.picked_up;
    }
    
}
