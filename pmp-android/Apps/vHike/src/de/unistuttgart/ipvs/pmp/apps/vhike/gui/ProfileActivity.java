package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class ProfileActivity extends Activity {
	
	static final String[] RECENT_RIDES = new String[] {
			"01.01.2011, Stuttgart", "02.01.2011, Berlin",
			"03.01.2011, Vaihingen", "..." };
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		setUpMyProfile();
	}

	private void setUpMyProfile() {
		Profile myProfile = Model.getInstance().getOwnProfile();
		
		TextView tv_username = (TextView) findViewById(R.id.tv_username);
		tv_username.setText(myProfile.getUsername());

		EditText et_firstname = (EditText) findViewById(R.id.et_firstname);
		et_firstname.setText(myProfile.getFirstname());

		EditText et_lastname = (EditText) findViewById(R.id.et_lastname);
		et_lastname.setText(myProfile.getLastname());

		EditText et_email = (EditText) findViewById(R.id.et_email);
		et_email.setText(myProfile.getEmail());

		EditText et_mobile = (EditText) findViewById(R.id.et_mobile);
		et_mobile.setText(myProfile.getTel());

		// EditText et_car = (EditText) findViewById(R.id.et_car);
		// et_car.setText(myProfile.get);
	}

}
