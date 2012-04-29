package de.unistuttgart.ipvs.pmp.infoapp.webservice.eventmanager;

import java.io.IOException;
import java.util.List;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.Event;

/**
 * Gives access to screen events
 * 
 * @author Patrick Strobel
 */
public class ScreenEventManager extends EventManager {
    
    public ScreenEventManager(Service service) {
        super(service);
    }
    
    
    @Override
    public void commitEvents(List<? extends Event> events) throws IOException {
        commitEvents(events, "upload_screen_events.php");
    }
    
    
    @Override
    public int getLastId() throws IOException {
        return getLastId("last_screen_event.php");
    }
    
}
