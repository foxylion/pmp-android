package de.unistuttgart.ipvs.pmp.apps.activities;

import de.unistuttgart.ipvs.pmp.apps.R;
import de.unistuttgart.ipvs.pmp.apps.model.Date;
import de.unistuttgart.ipvs.pmp.apps.model.Model;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class ChangeDateActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.changedate);

	int dateIndex = getIntent().getExtras().getInt("id");
	final Date date = Model.getInstance().getDateByIndex(dateIndex);
	String[] dates = date.getDate().split("\\.");

	int year = Integer.valueOf(dates[2]);
	int month = Integer.valueOf(dates[1]) - 1;
	int day = Integer.valueOf(dates[0]);

	final DatePicker dPicker = (DatePicker) findViewById(R.id.datePickerChange);
	dPicker.init(year, month, day, null);

	final TextView desc = (TextView) findViewById(R.id.descriptionChangeDate);
	desc.setText(date.getDescrpition());

	Button confirm = (Button) findViewById(R.id.ConfirmButtonChange);
	confirm.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		int month =  + 1;
		Model.getInstance().changeDate(date.getId(), dPicker.getDayOfMonth() + "."
			+ month + "." + dPicker.getYear(), desc.getText().toString());
		
		setResult(0);
		finish();
	    }
	});
    }

}
