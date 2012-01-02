package de.unistuttgart.ipvs.pmp.apps.vhike.tools;
/**
 * Query Objects will be generated when there is a call of searchQuery {@link JSonRequestReader}
 * @author Alexander Wassiljew
 *
 */
public class QueryObject{

	int id = 0;
	int passenger = 0;
	int seats = 0;
	float cur_lat =	0;
	float cur_lon = 0;
	String destination = "";
	float distance = 0;
	
	public QueryObject(int id, int passenger, int seats, float cur_lat,
			float cur_lon, String destination, float distance) {
		super();
		this.id = id;
		this.passenger = passenger;
		this.seats = seats;
		this.cur_lat = cur_lat;
		this.cur_lon = cur_lon;
		this.destination = destination;
		this.distance = distance;
	}

	/**
	 * 
	 * @return query_id
	 */
	public int getId() {
		return id;
	}
	/**
	 * 
	 * @return user_id of passenger
	 */
	public int getPassenger() {
		return passenger;
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

	public float getDistance() {
		return distance;
	}
	
	
	
	
}
