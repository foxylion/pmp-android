package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

public class AWName {

	private String mCity;
	private String mDate;

	public AWName(String pCity, String pDate) {
		mCity = pCity;
		mDate = pDate;
	}

	public void setCity(String pCity) {
		mCity = pCity;
	}

	public String getCity() {
		return mCity;
	}

	public void setDate(String pDate) {
		mDate = pDate;
	}

	public String getDate() {
		return mDate;
	}

}
