package de.unistuttgart.ipvs.pmp.apps.activities;

import de.unistuttgart.ipvs.pmp.apps.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CalendarAppActivity extends Activity {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final Intent newDateIntent = new Intent(this, NewDateActivity.class);
        final Intent dateListIntent = new Intent(this, DateList.class);
        
        setContentView(R.layout.main);
        Button view = (Button) findViewById(R.id.ListDates);
        view.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		startActivity(dateListIntent);
	    }
	});
        
        Button newDate = (Button) findViewById(R.id.NewDates);
        newDate.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		startActivity(newDateIntent);
	    }
	});
    }
}