package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

import java.util.List;

public class HistoryRideObject {
    
    int tripid;
    int avail_seats;
    
    String creation = "";
    String ending;
    String destination = "";
    List<HistoryPersonObject> persons;
    
    
    public HistoryRideObject(int tripid, int avail_seats, String creation, String ending, String destination,
            List<HistoryPersonObject> persons) {
        this.tripid = tripid;
        this.avail_seats = avail_seats;
        this.creation = creation;
        this.ending = ending;
        this.destination = destination;
        this.persons = persons;
    }
    
    
    public void addPerson(HistoryPersonObject person) {
        this.persons.add(person);
    }
    
    
    public int getTripid() {
        return this.tripid;
    }
    
    
    public int getAvail_seats() {
        return this.avail_seats;
    }
    
    
    public String getCreation() {
        return this.creation;
    }
    
    
    public String getEnding() {
        return this.ending;
    }
    
    
    public String getDestination() {
        return this.destination;
    }
    
    
    public List<HistoryPersonObject> getPersons() {
        return this.persons;
    }
    
}
