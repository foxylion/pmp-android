package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class ProfileActivity extends Activity {

	private Profile profile;

	static final String[] RECENT_RIDES = new String[] {
			"01.01.2011, Stuttgart", "02.01.2011, Berlin",
			"03.01.2011, Vaihingen", "..." };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		setUpProfile();
	}

	private void setUpProfile() {
		int whoIsIt = getIntent().getExtras().getInt("MY_PROFILE");
		int passengerID = getIntent().getExtras().getInt("PASSENGER_ID");

		Log.i("ID: " + passengerID + ", WHO: " + whoIsIt);

		if (whoIsIt == 0) {
			profile = Model.getInstance().getOwnProfile();
		} else {
			Controller ctrl = new Controller();

			profile = ctrl
					.getProfile(Model.getInstance().getSid(), passengerID);
		}

		TextView tv_username = (TextView) findViewById(R.id.tv_username);
		tv_username.setText(profile.getUsername());

		EditText et_firstname = (EditText) findViewById(R.id.et_firstname);
		et_firstname.setText(profile.getFirstname());

		EditText et_lastname = (EditText) findViewById(R.id.et_lastname);
		et_lastname.setText(profile.getLastname());

		EditText et_email = (EditText) findViewById(R.id.et_email);
		et_email.setText(profile.getEmail());

		EditText et_mobile = (EditText) findViewById(R.id.et_mobile);
		et_mobile.setText(profile.getTel());

		RatingBar rb = (RatingBar) findViewById(R.id.ratingbar_profile);
		rb.setRating((float) profile.getRating_avg());

		TextView tv_rating = (TextView) findViewById(R.id.tv_rating);
		tv_rating.setText(String.valueOf(profile.getRating_avg()));

		EditText et_desc = (EditText) findViewById(R.id.et_description_profile);
		et_desc.setText(profile.getDescription());
		// // car = "";
		// et_car.setText(car);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.profile_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case (R.id.mi_save):
			// send to changes to server
			break;
		}
		return true;
	}

}
