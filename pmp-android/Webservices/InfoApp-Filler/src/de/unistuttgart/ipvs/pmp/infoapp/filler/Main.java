package de.unistuttgart.ipvs.pmp.infoapp.filler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.eventmanager.ConnectionEventManager;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.ConnectionEvent;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Setup service url and device ID
		Service s = new Service("http://localhost/infoapp/src/json", "b7c2e4787e7f950c89909795907208d3");
		
		// Create some events and...
		ConnectionEvent e1 = new ConnectionEvent(2, 123, ConnectionEvent.Mediums.BLUETOOTH, true, true, "Stuttgart");
		ConnectionEvent e2 = new ConnectionEvent(1, 1337, ConnectionEvent.Mediums.WIFI, false, false, "Stuttgart");
		
		// ...bind them to a list
		List<ConnectionEvent> events = new ArrayList<ConnectionEvent>();
		events.add(e1);
		events.add(e2);
		
		// Contact the service and upload the events
		ConnectionEventManager cem = new ConnectionEventManager(s);
		try {
			System.out.println("LastID: "+cem.getLastId());
			cem.commitEvents(events);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
