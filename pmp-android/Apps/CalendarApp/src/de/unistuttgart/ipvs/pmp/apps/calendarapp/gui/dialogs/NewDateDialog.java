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
import de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector.SqlConnector;

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
            int month = NewDateDialog.this.dPicker.getMonth() + 1;
            
            // Stores the date
            SqlConnector.getInstance().storeNewDate(
                    NewDateDialog.this.dPicker.getDayOfMonth() + "." + month + "."
                            + NewDateDialog.this.dPicker.getYear(), NewDateDialog.this.desc.getText().toString());
            dismiss();
        }
        
    }
    
}
