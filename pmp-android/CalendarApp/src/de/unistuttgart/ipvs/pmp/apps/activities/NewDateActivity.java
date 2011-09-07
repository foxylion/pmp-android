package de.unistuttgart.ipvs.pmp.apps.activities;

import de.unistuttgart.ipvs.pmp.apps.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

public class NewDateActivity extends Activity {
    

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.newdate);
	DatePicker dPicker = (DatePicker) findViewById(R.id.datePicker1);
    }

}
