package de.unistuttgart.ipvs.pmp.apps.vhike.model;

import de.unistuttgart.ipvs.pmp.apps.vhike.tools.TripPersonObject;

/**
 * @author Alexander Wassiljew
 */
public class Trip {
    
    // `id` int(8) NOT NULL AUTO_INCREMENT,
    // `driver` int(8) NOT NULL,
    // `avail_seats` int(8) NOT NULL,
    // `destination` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
    // `creation` datetime NOT NULL,
    // `ending` datetime NOT NULL,
    
    TripPersonObject driver;
    private int id;
    private int driverID;
    private int avail_seats;
    private String destination;
    private long creation;
    private long ending;
    
    public Trip() {
        
    }
    
    
    public Trip(int id, int driverID, int availSeats, String destination, long creation, long ending) {
        this.id = id;
        this.driverID = driverID;
        this.avail_seats = availSeats;
        this.destination = destination;
        this.creation = creation;
        this.ending = ending;
    }
    
    
    public int getId() {
        return this.id;
    }
    
    
    public void setId(int id) {
        // TODO Check ID
        this.id = id;
    }


    
    public int getDriverID() {
        return driverID;
    }


    
    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }


    
    public int getAvail_seats() {
        return avail_seats;
    }


    
    public void setAvail_seats(int avail_seats) {
        this.avail_seats = avail_seats;
    }


    
    public String getDestination() {
        return destination;
    }


    
    public void setDestination(String destination) {
        this.destination = destination;
    }


    
    public long getCreation() {
        return creation;
    }


    
    public void setCreation(long creation) {
        this.creation = creation;
    }


    
    public long getEnding() {
        return ending;
    }


    
    public void setEnding(long ending) {
        this.ending = ending;
    }
    
}
