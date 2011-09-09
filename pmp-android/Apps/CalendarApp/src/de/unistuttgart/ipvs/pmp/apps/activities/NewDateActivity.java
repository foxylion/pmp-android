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

/**
 * Let the user store a date and a description. This date will be stored in the
 * model and the SQL database.
 * 
 * @author Thorsten Berberich
 * 
 */
public class NewDateActivity extends Activity {

    /**
     * Called when first created. Stores the date at the model if the confirm
     * button is pressed
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.newdate);

	final DatePicker dPicker = (DatePicker) findViewById(R.id.datePickerNew);
	final TextView desc = (TextView) findViewById(R.id.descriptionNew);
	Button confirm = (Button) findViewById(R.id.ConfirmButton);

	// Sets the onClickListener to the confirm button
	confirm.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// The chosen month
		int month =  + 1;

		// Stores the date
		Model.getInstance().addDate(
			new Date(Model.getInstance().getNewId(), desc.getText()
				.toString(), dPicker.getDayOfMonth() + "."
				+ month + "." + dPicker.getYear()));
		finish();
	    }
	});

    }

}
