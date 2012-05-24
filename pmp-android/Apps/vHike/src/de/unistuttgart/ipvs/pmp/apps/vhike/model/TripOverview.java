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
    public ArrayList<CompactUser> passengers;
    public ArrayList<CompactMessage> messages;
    
    
    public TripOverview(int id, String destination, String stopovers, ArrayList<CompactUser> passengers,
            Date startTime, ArrayList<CompactMessage> newMessages) {
        
        if (id < 0) {
            throw new IllegalArgumentException("Invalid Trip ID");
        }
        this.id = id;
        this.destination = destination;
        this.startTime = startTime;
        this.messages = newMessages;
        this.passengers = passengers;
        this.stopovers = stopovers.replaceAll("(^;|;$)", "").replace(";", ", ").replaceAll("\\s+", " ");
    }
}
