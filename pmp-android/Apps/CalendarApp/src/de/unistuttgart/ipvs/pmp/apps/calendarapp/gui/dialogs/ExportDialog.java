package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemConnector;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;

/**
 * This is the dialog for exporting. You can enter a file name for exporting.
 * 
 * @author Marcus Vetter
 * 
 */
public class ExportDialog extends Dialog {
    
    /**
     * The file name input
     */
    private TextView fileTextView;
    
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
        
        fileTextView = (TextView) findViewById(R.id.export_file_name_input);
        
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
            
            final String fileName = fileTextView.getText().toString();
            
            if (!Model.getInstance().isFileNameExisting(fileName)) {
                Log.d("Exporting...");
                FileSystemConnector.getInstance()
                        .exportAppointments(Model.getInstance().getAppointmentList(), fileName);
            } else {
                Log.d("Filename already exists!");
                
                // Show the confirm dialog for overwriting the file
                new AlertDialog.Builder(Model.getInstance().getContext()).setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.export_override_question).setMessage(R.string.export_override_attention)
                        .setPositiveButton(R.string.export_override_conf, new DialogInterface.OnClickListener() {
                            
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Override the file
                                Log.d("Exporting... Filename: " + fileName);
                                FileSystemConnector.getInstance().exportAppointments(
                                        Model.getInstance().getAppointmentList(), fileName);
                            }
                            
                        }).setNegativeButton(R.string.export_override_cancel, new DialogInterface.OnClickListener() {
                            
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Model.getInstance().getContext(), R.string.export_toast_cancel,
                                        Toast.LENGTH_SHORT).show();
                                Log.d("Exporting canceled.");
                            }
                        }).show();
                
            }
            
            dismiss();
        }
        
    }
    
}
