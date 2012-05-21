package de.unistuttgart.ipvs.pmp.apps.vhike.model;

import java.util.ArrayList;
import java.util.Date;

public class TripOverview {
    
    public int id;
    public String destination;
    public String stopovers;
    public Date startTime;
    public int numberOfPassengers;
    public int numberOfOffers;
    public int numberOfNewMessages;
    public ArrayList<String> persons;
    
    
    public TripOverview(int id, String destination, String stopovers, Date startTime, ArrayList<String> persons) {
        this.id = id;
        this.destination = destination;
        this.stopovers = stopovers;
        this.startTime = startTime;
        this.persons = persons;
    }
}
