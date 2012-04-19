package de.unistuttgart.ipvs.pmp.resourcegroups.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

public class ProfileActivity extends Activity implements OnClickListener {

	private ToggleButton tb;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);

		tb = (ToggleButton) findViewById(R.id.toggleButton);
		tb.setOnClickListener(this);
	}

	public void onClick(View v) {
		Log.i(ProfileConstants.LOG_TAG, "Clicked! " + tb.isChecked());
		if (tb.isChecked()) {
			startService(new Intent(this, ProfileService.class));
		} else {
			stopService(new Intent(this, ProfileService.class));
		}
	}

}
