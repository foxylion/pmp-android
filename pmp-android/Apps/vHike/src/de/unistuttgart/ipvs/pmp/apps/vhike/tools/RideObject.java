package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

public class RideObject {

	int tripid =0;
	int seats=0;
	float cur_lat=0;
	float cur_lon=0;
	String destination="";
	int driverid=0;
	String username ="";
	float rating=0;
	float distance=0;
	public RideObject(int tripid, int seats, float cur_lat, float cur_lon,
			String destination, int driverid, String username, float rating,
			float distance) {
		super();
		this.tripid = tripid;
		this.seats = seats;
		this.cur_lat = cur_lat;
		this.cur_lon = cur_lon;
		this.destination = destination;
		this.driverid = driverid;
		this.username = username;
		this.rating = rating;
		this.distance = distance;
	}
	public int getTripid() {
		return tripid;
	}
	public int getSeats() {
		return seats;
	}
	public float getCur_lat() {
		return cur_lat;
	}
	public float getCur_lon() {
		return cur_lon;
	}
	public String getDestination() {
		return destination;
	}
	public int getDriverid() {
		return driverid;
	}
	public String getUsername() {
		return username;
	}
	public float getRating() {
		return rating;
	}
	public float getDistance() {
		return distance;
	}


}
