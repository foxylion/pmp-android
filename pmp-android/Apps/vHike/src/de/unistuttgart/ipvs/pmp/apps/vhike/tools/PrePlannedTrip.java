package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

public class PrePlannedTrip {
    
    int tid;
    String destination;
    int time;
    int passengers;
    int invites;
    
    
    public PrePlannedTrip(int tid, String destination, int time, int passengers, int invites) {
        this.tid = tid;
        this.destination = destination;
        this.passengers = passengers;
        this.invites = invites;
    }
    
    
    public int getTid() {
        return this.tid;
    }
    
    
    public String getDestination() {
        return this.destination;
    }
    
    
    public int getDate() {
        // TODO: Time parsen zu Date
        return this.time;
    }
    
    
    public int getPassengers() {
        return this.passengers;
    }
    
    
    public int getInvites() {
        return this.invites;
    }
}
