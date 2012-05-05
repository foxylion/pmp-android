package de.unistuttgart.ipvs.pmp.infoapp.filler;

import java.util.LinkedList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.Event;

public abstract class Filler {
	
	protected Service service;
	protected int id = 0;
	private long fromMillis;
	private long toMillis;
	
	
	public Filler(Service service, long fromMillis, long toMillis) {
		this.service = service;
		this.fromMillis = fromMillis;
		this.toMillis = toMillis;
	}
	
	
	public void fill() {
		System.out.print("Generating... ");
		List<Event> events = new LinkedList<Event>();
		
		long timeMillis = this.fromMillis;
		int spreading = this.maxSpreading() * 60000;
		
		while (timeMillis < this.toMillis) {
			
			events.add(generateEvent(timeMillis));
			
			// Increment date/time randomly with a value between 0 ms and 100 minutes
			int inc = (int) (Math.random() * spreading);
			timeMillis += inc;
		}
		System.out.print("Uploading (" + events.size() + " Events)... ");
		uploadEvents(events);
		System.out.println("Done!");
		
	}
	
	
	/**
	 * Maximum time between two events.
	 * 
	 * @return The time between two events is computed randomly between 0 and the returned value in
	 *         minutes
	 */
	protected abstract int maxSpreading();
	
	
	public abstract Event generateEvent(long time);
	
	
	public abstract void uploadEvents(List<Event> events);
	
}
