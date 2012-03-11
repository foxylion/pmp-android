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
    
    
    public Trip() {
        
    }
    
    
    public Trip(int id, int driverID, int availSeats, String destination, long creation, long ending) {
        
    }
    
    
    public int getId() {
        return this.id;
    }
    
    
    public void setId(int id) {
        // TODO Check ID
        this.id = id;
    }
    
}
