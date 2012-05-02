package de.unistuttgart.ipvs.pmp.infoapp.filler;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.Event;

public abstract class Filler {
	
	public Service service = new Service(Service.DEFAULT_URL, "f2305a2fbef51bd82008c7cf3788250f");
	
	public void fill() {
		List<Event> events = new LinkedList<Event>();
		Calendar startC = Calendar.getInstance();
		startC.set(2012, 4, 2, 00, 00, 00);
		
		Calendar stopC = Calendar.getInstance();
		stopC.set(2012, 4, 10, 00, 00, 00);
		
		long timeMillis = startC.getTimeInMillis();
		
		while (timeMillis < stopC.getTimeInMillis()) {
			
			startC.setTimeInMillis(timeMillis);
			events.add(generateEvent(timeMillis));

			// Increment date/time randomly with a value between 0 ms and 100 minutes
			int inc = (int) (Math.random() * 6000000);
			timeMillis +=inc;
		}
		uploadEvents(events);
		
	}
	
	public abstract Event generateEvent(long time);
	
	public abstract void uploadEvents(List<Event> events);

}
