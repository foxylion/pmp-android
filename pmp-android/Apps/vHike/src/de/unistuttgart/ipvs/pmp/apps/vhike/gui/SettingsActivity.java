package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import de.unistuttgart.ipvs.pmp.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

public class SettingsActivity extends Activity {

	private EditText perimeter;
	private CheckBox unanimous;

	private String sPerimeter;
	private boolean bUnanimous;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		perimeter = (EditText) findViewById(R.id.et_perimeter);
		unanimous = (CheckBox) findViewById(R.id.cb_unanimous);

	}

	@Override
	protected void onResume() {
		super.onResume();

		SharedPreferences settings = getSharedPreferences("vHikePrefs",
				MODE_PRIVATE);
		sPerimeter = settings.getString("PERIMETER", "10");
		bUnanimous = settings.getBoolean("UNANIMOUS", false);
		
		if (sPerimeter.equals("")) {
			sPerimeter = "10";
		}
		
		perimeter.setText(sPerimeter);
		unanimous.setChecked(bUnanimous);

	}

	@Override
	protected void onStop() {
		super.onStop();

		SharedPreferences prefs = this.getSharedPreferences("vHikePrefs",
				MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = prefs.edit();
		prefsEditor.putString("PERIMETER", perimeter.getText().toString());
		prefsEditor.putBoolean("UNANIMOUS", unanimous.isChecked());
		prefsEditor.commit();
	}

}
