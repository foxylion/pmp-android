package de.unistuttgart.ipvs.pmp.infoapp.webservice.eventmanager;

import java.io.IOException;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.Event;

public class ConnectionEventManager extends EventManager {
    
    public ConnectionEventManager(Service service) {
        super(service);
    }
    
    
    @Override
    public void commitEvents(Event[] events) throws IOException {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public int getLastId() throws IOException {
        HttpParams params = new BasicHttpParams();
        params.setParameter("device", service.getDeviceId());
        try {
            JSONObject json = this.service.requestGetService("last_connection_event.php", params);
            
            return json.getInt("last_id");
        } catch (JSONException e) {
            throw new IOException("Server returned no valid JSON object");
        }
    }
    
}
