package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;

/**
 * The main menu after user logged in
 * 
 * @author Andre Nguyen
 * 
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		registerListener();

		vhikeDialogs.getInstance().getLoginPD(MainActivity.this).dismiss();

		vhikeDialogs.getInstance().clearLoginPD();
	}

	private void registerListener() {
		Button btnRide = (Button) findViewById(R.id.Button_Ride);
		Button btnProfile = (Button) findViewById(R.id.Button_Profile);
		Button btnHistory = (Button) findViewById(R.id.Button_History);
		Button btnSettings = (Button) findViewById(R.id.Button_Settings);

		btnRide.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						RideActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});

		btnProfile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						ProfileActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});

		btnHistory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						HistoryActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});

		btnSettings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						SettingsActivity.class);
				v.getContext().startActivity(intent);
			}

		});
	}

}
