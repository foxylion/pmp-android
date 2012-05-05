package de.unistuttgart.ipvs.pmp.infoapp.filler;

import java.io.IOException;
import java.util.List;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.eventmanager.AwakeEventManager;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.AwakeEvent;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.Event;

public class Awake extends Filler {
	
	public Awake(Service service, long fromMillis, long toMillis) {
		super(service, fromMillis, toMillis);
	}
	
	
	@Override
	protected int maxSpreading() {
		return 60;
	}
	
	
	@Override
	public Event generateEvent(long time) {
		
		boolean awake = false;
		
		if (Math.random() < 0.2) {
			awake = true;
		}
		
		AwakeEvent event = new AwakeEvent(++id, time, awake);
		return event;
	}
	
	
	@Override
	public void uploadEvents(List<Event> events) {
		AwakeEventManager aem = new AwakeEventManager(service);
		
		try {
			aem.commitEvents(events);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
