package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import de.unistuttgart.ipvs.pmp.R;
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
		// Controller c = new Controller();
		// c.getProfile(Model.getInstance().getSid(), user_id);

		TextView tv_username = (TextView) findViewById(R.id.tv_username);
		tv_username.setText(myProfile.getUsername());

		EditText et_firstname = (EditText) findViewById(R.id.et_firstname);
		et_firstname.setText(myProfile.getFirstname());

		EditText et_lastname = (EditText) findViewById(R.id.et_lastname);
		et_lastname.setText(myProfile.getLastname());

		EditText et_email = (EditText) findViewById(R.id.et_email);
		et_email.setText(myProfile.getEmail());

		EditText et_mobile = (EditText) findViewById(R.id.et_mobile);
		et_mobile.setText(myProfile.getEmail());

		RatingBar rb = (RatingBar) findViewById(R.id.ratingbar_profile);
		rb.setRating((float) myProfile.getRating_avg());

		// EditText et_car = (EditText) findViewById(R.id.et_car);
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
