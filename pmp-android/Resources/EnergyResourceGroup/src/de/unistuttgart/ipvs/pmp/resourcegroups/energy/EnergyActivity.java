package de.unistuttgart.ipvs.pmp.resourcegroups.energy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

/**
 * 
 * @author Marcus Vetter
 *
 */
public class EnergyActivity extends Activity implements OnClickListener {

	private ToggleButton tb;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);

		tb = (ToggleButton) findViewById(R.id.toggleButton);
		tb.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (tb.isChecked()) {
			startService(new Intent(this, EnergyService.class));
		} else {
			stopService(new Intent(this, EnergyService.class));
		}
	}

}
