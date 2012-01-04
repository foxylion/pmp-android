package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

import java.util.List;

public class HistoryRideObject {

	int tripid;
	int avail_seats;

	String creation = "";
	String ending;
	String destination = "";
	List<HistoryPersonObject> persons;

	public HistoryRideObject(int tripid, int avail_seats, String creation,
			String ending, String destination, List<HistoryPersonObject> persons) {
		this.tripid = tripid;
		this.avail_seats = avail_seats;
		this.creation = creation;
		this.ending = ending;
		this.destination = destination;
		this.persons = persons;
	}
	
	public void addPerson(HistoryPersonObject person){
		persons.add(person);
	}
	
	public int getTripid() {
		return tripid;
	}

	public int getAvail_seats() {
		return avail_seats;
	}

	public String getCreation() {
		return creation;
	}

	public String getEnding() {
		return ending;
	}

	public String getDestination() {
		return destination;
	}

	public List<HistoryPersonObject> getPersons() {
		return persons;
	}

}
