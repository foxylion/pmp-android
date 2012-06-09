package de.unistuttgart.ipvs.pmp.apps.vhike.model;

/**
 * This class contains brief information of a trip and is used for display in the My Trips activity.
 * 
 * @author Dang Huynh
 */
public class CompactTrip {
    
    public int id;
    public String destination;
    public long startTime;
    public int numberOfPassengers;
    public int numberOfOffers;
    public int numberOfNewMessages;
    
    
    /**
     * Initiate a new compact trip
     * 
     * @param id
     *            Trip's ID
     * @param destination
     *            Destination of the trip
     * @param startTime
     *            Trip's start time
     * @param passengerNo
     *            Number of passengers
     * @param offerNo
     *            Number of offers
     * @param messageNo
     *            Number of new messages
     */
    public CompactTrip(int id, String destination, long startTime, int passengerNo, int offerNo, int messageNo) {
        this.id = id;
        String s = destination.trim().replaceAll("\\s*;\\s*", ";").replaceAll("(^;|;$)", "")
                .replaceAll(";.*", "");
        this.destination = s;
        this.startTime = startTime;
        this.numberOfPassengers = passengerNo;
        this.numberOfOffers = offerNo;
        this.numberOfNewMessages = messageNo;
    }
    
    
    public CompactTrip() {
    }
}
