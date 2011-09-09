package de.unistuttgart.ipvs.pmp.apps.gui.dialogs;

import de.unistuttgart.ipvs.pmp.apps.R;
import de.unistuttgart.ipvs.pmp.apps.model.Date;
import de.unistuttgart.ipvs.pmp.apps.model.Model;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ChangeDateDialog extends Dialog {

    /**
     * Index of the date to edit
     */
    private int dateIndex;

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
     * Original date
     */
    private Date date;

    /**
     * The array adapter of the list to refresh the list after changing a date
     */
    private ArrayAdapter<Date> arrayAdapter;

    /**
     * Necessary constructor
     * 
     * @param context
     *            the context
     */
    public ChangeDateDialog(Context context, int id, ArrayAdapter<Date> adapter) {
	super(context);
	this.dateIndex = id;
	this.arrayAdapter = adapter;
    }

    /**
     * Called when the dialog is first created. Gets all elements of the gui
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	setContentView(R.layout.changedate);

	this.setTitle("Change date");

	date = Model.getInstance().getDateByIndex(dateIndex);
	String[] dates = date.getDate().split("\\.");

	int year = Integer.valueOf(dates[2]);
	int month = Integer.valueOf(dates[1]) - 1;
	int day = Integer.valueOf(dates[0]);

	dPicker = (DatePicker) findViewById(R.id.datePickerChange);
	dPicker.init(year, month, day, null);

	desc = (TextView) findViewById(R.id.descriptionChangeDate);
	desc.setText(date.getDescrpition());

	confirm = (Button) findViewById(R.id.ConfirmButtonChange);
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
	 * Stores the entered data.
	 */
	@Override
	public void onClick(View v) {
	    int month = +1;
	    Model.getInstance().changeDate(
		    date.getId(),
		    dPicker.getDayOfMonth() + "." + month + "."
			    + dPicker.getYear(), desc.getText().toString());
	    arrayAdapter.notifyDataSetChanged();
	    dismiss();

	}

    }
}
