package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemConnector;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.ExportDialog.ConfirmListener;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;

public class ImportDialog extends Dialog {
    
    /**
     * The confirm button
     */
    private Button confirm;
    
    /**
     * The file name
     */
    private String filename;
    
    
    /**
     * Necessary constructor
     * 
     * @param context
     *            the context
     */
    public ImportDialog(Context context) {
        super(context);
    }
    
    
    /**
     * Called when the dialog is first created. Gets all elements of the gui
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.import_dialog);
        
        this.setTitle(R.string.import_appointments);
        
        confirm = (Button) findViewById(R.id.ImportConfirmButton);
        
        confirm.setOnClickListener(new ConfirmListener());
        
        /*
         * Neeeded to fill the width of the screen
         */
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        
    }
    
    
    /**
     * Get the filename for importing
     * 
     * @return the filename for importing
     */
    public String getFilename() {
        return filename;
    }
    
    
    /**
     * Set the filename for importing
     * 
     * @param filename
     *            for importing
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    /**
     * Listener class only needed for the confirm button
     * 
     */
    protected class ConfirmListener implements android.view.View.OnClickListener {
        
        /**
         * Called when the confirm button is pressed. Import the file.
         */
        @Override
        public void onClick(View v) {
            
            // Import the file
            FileSystemConnector.getInstance().importAppointments(getFilename());
            
            dismiss();
        }
        
    }
    
}
