package de.unistuttgart.ipvs.pmp.apps.gui.dialogs;

import de.unistuttgart.ipvs.pmp.apps.R;
import de.unistuttgart.ipvs.pmp.apps.model.Date;
import de.unistuttgart.ipvs.pmp.apps.model.Model;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * Opens a new dialog where the user can add a new {@link Date}.
 * 
 * @author Thorsten Berberich
 * 
 */
public class NewDateDialog extends Dialog {

    /**
     * The date picker
     */
    private DatePicker dPicker;

    /**
     * The TextView with the description
     */
    private TextView desc;

    /**
     * The button to confirm the dialog
     */
    private Button confirm;

    /**
     * Necessary constructor
     * 
     * @param context
     *            the context
     */
    public NewDateDialog(Context context) {
	super(context);
    }

    /**
     * Called when the dialog is first created. Gets all elements of the gui
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	setContentView(R.layout.newdate);

	dPicker = (DatePicker) findViewById(R.id.datePickerNew);
	desc = (TextView) findViewById(R.id.descriptionNew);
	confirm = (Button) findViewById(R.id.ConfirmButton);

	confirm.setOnClickListener(new ConfirmListener());

	/*
	 * Neeeded to fill the width of the screen
	 */
	getWindow().setLayout(LayoutParams.FILL_PARENT,
		LayoutParams.WRAP_CONTENT);

    }

    /**
     * Listener class only needed for the confirm button
     * 
     * @author Thorsten Berberich
     * 
     */
    protected class ConfirmListener implements
	    android.view.View.OnClickListener {

	/**
	 * Called when the confirm button is pressed. Stores the entered data
	 * and closes the dialog.
	 */
	@Override
	public void onClick(View v) {
	    // The chosen month
	    int month = dPicker.getMonth() + 1;

	    // Stores the date
	    Model.getInstance().addDate(
		    new Date(Model.getInstance().getNewId(), desc.getText()
			    .toString(), dPicker.getDayOfMonth() + "." + month
			    + "." + dPicker.getYear()));
	    dismiss();
	}

    }

}
