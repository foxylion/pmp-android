package de.unistuttgart.ipvs.pmp.infoapp.filler;

import java.io.IOException;
import java.util.List;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.eventmanager.ConnectionEventManager;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.ConnectionEvent;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.Event;

public class Connection extends Filler {
	
	public Connection(Service service, long fromMillis, long toMillis) {
		super(service, fromMillis, toMillis);
	}
	
	private String[] cities = { "Stuttgart", "Vaihingen", "Böblingen", "Esslingen" };
	
	
	@Override
	protected int maxSpreading() {
		return 120;
	}
	
	
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
		
		int cityNum = (int) (Math.random() * cities.length);
		
		ConnectionEvent event = new ConnectionEvent(++id, time, medium, connected, enabled, cities[cityNum]);
		return event;
	}
	
	
	@Override
	public void uploadEvents(List<Event> events) {
		ConnectionEventManager cem = new ConnectionEventManager(service);
		
		try {
			cem.commitEvents(events);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
