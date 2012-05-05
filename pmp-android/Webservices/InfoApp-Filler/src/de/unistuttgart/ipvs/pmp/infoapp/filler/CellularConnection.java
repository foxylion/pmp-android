package de.unistuttgart.ipvs.pmp.infoapp.filler;

import java.io.IOException;
import java.util.List;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.eventmanager.CellularConnectionEventManager;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.CellularConnectionEvent;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.Event;

public class CellularConnection extends Filler {
	
	public CellularConnection(Service service, long fromMillis, long toMillis) {
		super(service, fromMillis, toMillis);
	}
	
	
	@Override
	protected int maxSpreading() {
		return 24 * 60;
	}
	
	
	@Override
	public Event generateEvent(long time) {
		
		boolean airplane = false;
		boolean roaming = false;
		
		if (Math.random() < 0.2) {
			airplane = true;
		} else {
			if (Math.random() < 0.2) {
				roaming = true;
			}
		}
		CellularConnectionEvent event = new CellularConnectionEvent(++id, time, roaming, airplane);
		return event;
	}
	
	
	@Override
	public void uploadEvents(List<Event> events) {
		CellularConnectionEventManager ccem = new CellularConnectionEventManager(service);
		
		try {
			ccem.commitEvents(events);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
