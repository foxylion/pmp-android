package de.unistuttgart.ipvs.pmp.infoapp.webservice.eventmanager;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.Event;

/**
 * Abstract base class for all event managers that might be used to upload data to a webservice.
 * 
 * @author Patrick Strobel
 * 
 */
public abstract class EventManager {
    
    protected Service service;
    
    
    public EventManager(Service service) {
        this.service = service;
    }
    
    
    /**
     * Uploads events to the webservice
     * 
     * @param events
     *            Events to upload
     */
    public abstract void commitEvents(Event[] events) throws IOException;
    
    
    /**
     * Gets the ID of the event that has been uploaded to the webservice the last time
     * 
     * @return The last event's ID
     */
    public abstract int getLastId() throws IOException;
    
    
    protected int getLastId(String service) throws IOException {
        try {
            JSONObject json = this.service.requestGetService(service);
            return json.getInt("last_id");
        } catch (JSONException e) {
            throw new IOException("Server returned no valid JSON object");
        }
    }
    
}
