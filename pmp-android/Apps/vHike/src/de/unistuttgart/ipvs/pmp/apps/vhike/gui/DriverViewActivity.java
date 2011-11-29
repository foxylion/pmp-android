package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import android.os.Bundle;

import com.google.android.maps.MapActivity;

import de.unistuttgart.ipvs.pmp.R;
/**
 * DriverViewActivity displays drivers current location on google maps
 * 
 * @author Andre Nguyen
 * 
 */
public class DriverViewActivity extends MapActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driverview);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
