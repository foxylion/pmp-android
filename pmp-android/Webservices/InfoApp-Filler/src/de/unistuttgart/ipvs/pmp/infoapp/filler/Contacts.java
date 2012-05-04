package de.unistuttgart.ipvs.pmp.infoapp.filler;

import java.io.IOException;
import java.util.List;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.eventmanager.ConnectionEventManager;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.ConnectionEvent;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.Event;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InternalDatabaseException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidEventIdException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidEventOrderException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidParameterException;

public class Contacts extends Filler {
    
    private int id = 0;
    private String[] cities = { "Stuttgart", "Vaihingen", "Böblingen", "Esslingen" };
    
    
    @Override
    public Event generateEvent(long time) {
        ConnectionEvent.Mediums medium = ConnectionEvent.Mediums.BLUETOOTH;
        
        if (Math.random() < 0.5) {
            medium = ConnectionEvent.Mediums.WIFI;
        }
        
        boolean connected = false;
        boolean enabled = false;
        
        if (Math.random() < 0.7) {
            enabled = true;
            if (Math.random() < 0.5) {
                connected = true;
            }
        }
        
        int cityNum = (int) (Math.random() * this.cities.length);
        
        ConnectionEvent event = new ConnectionEvent(++this.id, time, medium, connected, enabled, this.cities[cityNum]);
        return event;
    }
    
    
    @Override
    public void uploadEvents(List<Event> events) {
        ConnectionEventManager cem = new ConnectionEventManager(this.service);
        
        try {
            cem.commitEvents(events);
        } catch (InternalDatabaseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidEventIdException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidEventOrderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}
