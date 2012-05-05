package de.unistuttgart.ipvs.pmp.infoapp.filler;

import java.io.IOException;
import java.util.List;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.eventmanager.ScreenEventManager;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.Event;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.ScreenEvent;

public class Screen extends Filler {
	
	public Screen(Service service, long fromMillis, long toMillis) {
		super(service, fromMillis, toMillis);
	}
	
	
	@Override
	protected int maxSpreading() {
		return 60;
	}
	
	
	@Override
	public Event generateEvent(long time) {
		
		boolean screen = false;
		
		if (Math.random() < 0.2) {
			screen = true;
		}
		
		ScreenEvent event = new ScreenEvent(++id, time, screen);
		return event;
	}
	
	
	@Override
	public void uploadEvents(List<Event> events) {
		ScreenEventManager sem = new ScreenEventManager(service);
		
		try {
			sem.commitEvents(events);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
