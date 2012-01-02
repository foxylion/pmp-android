package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

/**
 * Query Objects will be generated when there is a call of searchQuery
 * {@link JSonRequestReader}
 * 
 * @author Alexander Wassiljew
 * 
 */
public class QueryObject {
	int queryid = 0;
	int userid = 0;
	String username = "";
	float cur_lat = 0;
	float cur_lon = 0;
	int seats = 0;
	float distance = 0;
	public QueryObject(int queryid, int userid, String username, float cur_lat,
			float cur_lon, int seats, float distance) {
		super();
		this.queryid = queryid;
		this.userid = userid;
		this.username = username;
		this.cur_lat = cur_lat;
		this.cur_lon = cur_lon;
		this.seats = seats;
		this.distance = distance;
	}
	public int getQueryid() {
		return queryid;
	}
	public int getUserid() {
		return userid;
	}
	public String getUsername() {
		return username;
	}
	public float getCur_lat() {
		return cur_lat;
	}
	public float getCur_lon() {
		return cur_lon;
	}
	public int getSeats() {
		return seats;
	}
	public float getDistance() {
		return distance;
	}


}
