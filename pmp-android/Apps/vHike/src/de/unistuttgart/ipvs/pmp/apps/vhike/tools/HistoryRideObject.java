package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

public class HistoryRideObject {

	String creation ="";
	String destination="";
	
	
	public HistoryRideObject(String creation, String destination) {
		this.creation = creation;
		this.destination = destination;
	}
	

	public String getCreation() {
		return creation;
	}

	public String getDestination() {
		return destination;
	}

}
