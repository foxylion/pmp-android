package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Appointment;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector.SqlConnector;

/**
 * Opens a new dialog where the user can add a new {@link Appointment}.
 * 
 * @author Thorsten Berberich
 * 
 */
public class NewAppointmentDialog extends Dialog {
    
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
    public NewAppointmentDialog(Context context) {
        super(context);
    }
    
    
    /**
     * Called when the dialog is first created. Gets all elements of the gui
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.newdate);
        
        this.setTitle(R.string.add_todo_dialog);
        
        this.dPicker = (DatePicker) findViewById(R.id.datePickerNew);
        this.desc = (TextView) findViewById(R.id.descriptionNew);
        this.confirm = (Button) findViewById(R.id.ConfirmButton);
        
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
         * Called when the confirm button is pressed. Stores the entered data and closes the dialog.
         */
        @Override
        public void onClick(View v) {
            // The chosen month
            int month = NewAppointmentDialog.this.dPicker.getMonth();
            int year = NewAppointmentDialog.this.dPicker.getYear();
            int day = NewAppointmentDialog.this.dPicker.getDayOfMonth();
            
            Calendar cal = new GregorianCalendar(year, month, day);
            
            // Stores the date
            SqlConnector.getInstance().storeNewAppointment(cal.getTime(),
                    NewAppointmentDialog.this.desc.getText().toString());
            dismiss();
        }
        
    }
    
}
