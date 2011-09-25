package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.ChangeAppointmentDialog;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.ExportDialog;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.ImportDialog;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Appointment;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources.FileDetails;
import android.app.Dialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ImportActivity extends ListActivity {
    
    /**
     * The arrayAdapter of the list
     */
    private static ArrayAdapter<FileDetails> arrayAdapter;
    
    
    /**
     * Called, when the activity is created
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.import_list_layout);
        
        setTitle(R.string.import_appointments);
        
        // Array adapter that is needed to show the list of dates
        arrayAdapter = new ArrayAdapter<FileDetails>(this, R.layout.import_list_item, Model.getInstance().getFileList());
        Model.getInstance().setImportArrayAdapter(arrayAdapter);
        setListAdapter(arrayAdapter);
        
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        
        /*
         * Listener for clicking one item. Opens a new dialog where the user can
         * change the date.
         */
        listView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileDetails file = Model.getInstance().getFileList().get(position);
                // Open dialog for importing
                ImportDialog importDialog = new ImportDialog(ImportActivity.this);
                importDialog.setFilename(file.getName());
                importDialog.show();
            }
        });
    }
    
}
