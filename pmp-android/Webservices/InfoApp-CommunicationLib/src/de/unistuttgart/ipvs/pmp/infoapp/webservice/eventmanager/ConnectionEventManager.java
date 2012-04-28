package de.unistuttgart.ipvs.pmp.infoapp.webservice.eventmanager;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.ConnectionEvent;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.Event;

public class ConnectionEventManager extends EventManager {
    
    public ConnectionEventManager(Service service) {
        super(service);
    }
    
    
    @Override
    public void commitEvents(Event[] events) throws IOException {
        if (!(events instanceof ConnectionEvent[])) {
            throw new IllegalArgumentException("Event array has to be of type \"ConnectionEvent[]\"");
        }
        
        JSONObject data = new JSONObject();
        
        JSONArray jsonEvents = new JSONArray();
        try {
            for (Event event : events) {
                
                jsonEvents.put(event.toJSONObject());
                
            }
            data.put("events", jsonEvents);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public int getLastId() throws IOException {
        return getLastId("last_id");
    }
    
}
