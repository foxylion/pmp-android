package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import com.google.android.maps.MapActivity;

import de.unistuttgart.ipvs.pmp.R;

import android.os.Bundle;
/**
 * PassengerViewActivity displays passengers current location on google maps
 * 
 * @author Andre Nguyen
 * 
 */
public class PassengerViewActivity extends MapActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_passengerview);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
