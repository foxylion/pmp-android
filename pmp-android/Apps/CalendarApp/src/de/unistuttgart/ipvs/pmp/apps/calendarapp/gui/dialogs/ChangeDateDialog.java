package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Date;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector.SqlConnector;

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
     * Necessary constructor
     * 
     * @param context
     *            the context
     */
    public ChangeDateDialog(Context context, int id) {
        super(context);
        this.dateIndex = id;
    }
    
    
    /**
     * Called when the dialog is first created. Gets all elements of the gui
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.changedate);
        
        this.setTitle("Change date");
        
        this.date = Model.getInstance().getDateByIndex(this.dateIndex);
        String[] dates = this.date.getDate().split("\\.");
        
        int year = Integer.valueOf(dates[2]);
        int month = Integer.valueOf(dates[1]) - 1;
        int day = Integer.valueOf(dates[0]);
        
        this.dPicker = (DatePicker) findViewById(R.id.datePickerChange);
        this.dPicker.init(year, month, day, null);
        
        this.desc = (TextView) findViewById(R.id.descriptionChangeDate);
        this.desc.setText(this.date.getDescrpition());
        
        this.confirm = (Button) findViewById(R.id.ConfirmButtonChange);
        this.confirm.setOnClickListener(new ConfirmListener());
        
        /*
         * Neeeded to fill the width of the screen
         */
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    
    /**
     * Listener class only needed for the confirm button
     * 
     * @author Thorsten Berberich
     * 
     */
    protected class ConfirmListener implements android.view.View.OnClickListener {
        
        /**
         * Stores the entered data.
         */
        @Override
        public void onClick(View v) {
            int month = ChangeDateDialog.this.dPicker.getMonth() + 1;
            
            SqlConnector.getInstance().changeDate(
                    ChangeDateDialog.this.date.getId(),
                    ChangeDateDialog.this.dPicker.getDayOfMonth() + "." + month + "."
                            + ChangeDateDialog.this.dPicker.getYear(), ChangeDateDialog.this.desc.getText().toString());
            
            dismiss();
        }
        
    }
}
