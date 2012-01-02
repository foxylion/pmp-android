package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

public class OfferObject {

	int offer_id; int user_id; String username;
	float rating; float rating_num;


	public OfferObject(int offer_id, int user_id, String username,
			float rating, float rating_num) {
		super();
		this.offer_id = offer_id;
		this.user_id = user_id;
		this.username = username;
		this.rating = rating;
		this.rating_num = rating_num;
	}


	public int getOffer_id() {
		return offer_id;
	}


	public int getUser_id() {
		return user_id;
	}


	public String getUsername() {
		return username;
	}


	public float getRating() {
		return rating;
	}


	public float getRating_num() {
		return rating_num;
	}
	
	
}
