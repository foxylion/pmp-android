package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs;

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemConnector;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.NewAppointmentDialog.ConfirmListener;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.util.DialogManager;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector.SqlConnector;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class ExportDialog extends Dialog {
    
    /**
     * The file name input
     */
    private TextView fileName;
    
    /**
     * The confirm button
     */
    private Button confirm;
    
    
    /**
     * Necessary constructor
     * 
     * @param context
     *            the context
     */
    public ExportDialog(Context context) {
        super(context);
    }
    
    /**
     * Called when the dialog is first created. Gets all elements of the gui
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.export_dialog);
        
        this.setTitle(R.string.export_appointments);
        
        fileName = (TextView) findViewById(R.id.export_file_name_input);
        
        confirm = (Button) findViewById(R.id.ExportConfirmButton);
        
        confirm.setOnClickListener(new ConfirmListener());
        
        /*
         * Neeeded to fill the width of the screen
         */
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        
    }
    
    /**
     * Listener class only needed for the confirm button
     * 
     */
    protected class ConfirmListener implements android.view.View.OnClickListener {
        
        /**
         * Called when the confirm button is pressed. Export the appointment list.
         */
        @Override
        public void onClick(View v) {
            
            if (!Model.getInstance().isFileNameExisting(fileName.getText().toString())) {
                Log.d("Exporting...");
                FileSystemConnector.getInstance().exportAppointments(Model.getInstance().getAppointmentList(),
                        fileName.getText().toString());
            } else {
                // TODO: Filename already exists
                Log.e("Filename already exists!");
            }
            
            dismiss();
        }
        
    }
    
}
