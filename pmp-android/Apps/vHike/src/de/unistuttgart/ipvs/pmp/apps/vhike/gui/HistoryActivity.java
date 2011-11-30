package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import de.unistuttgart.ipvs.pmp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * This Activity gives user freedom to act as a Driver or Passenger
 * 
 * @author Anton Makarov
 * 
 */
public class HistoryActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ride);

		registerListener();
	}

	private void registerListener() {
//		Button btnDrive = (Button) findViewById(R.id.Button_Drive);
//		Button btnSearch = (Button) findViewById(R.id.Button_Search);




	}

}
