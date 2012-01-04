package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

public class HistoryPersonObject {

	int userid;
	String username;
	float rating;
	int rating_num;
	boolean rated;
	public HistoryPersonObject(int userid, String username, float rating,
			int rating_num, boolean rated) {
		super();
		this.userid = userid;
		this.username = username;
		this.rating = rating;
		this.rating_num = rating_num;
		this.rated = rated;
	}
	public int getUserid() {
		return userid;
	}
	public String getUsername() {
		return username;
	}
	public float getRating() {
		return rating;
	}
	public int getRating_num() {
		return rating_num;
	}
	public boolean isRated() {
		return rated;
	}
	
	
}
