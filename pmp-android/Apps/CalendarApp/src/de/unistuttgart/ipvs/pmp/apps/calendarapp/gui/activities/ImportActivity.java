package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities;

import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemConnector;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.ImportDialog;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources.FileDetails;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

/**
 * This is the (list-)activity for the import. It shows all available files for importing.
 * 
 * @author Marcus Vetter
 * 
 */
public class ImportActivity extends ListActivity {
    
    /**
     * The arrayAdapter of the list
     */
    private static ArrayAdapter<FileDetails> importArrayAdapter;
    
    
    /**
     * Called, when the activity is created
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.import_list_layout);
        
        setTitle(R.string.import_appointments);
        
        /*
         * Fill the list of files for importing
         */
        FileSystemConnector.getInstance().listStoredFiles();
        
        // Array adapter that is needed to show the list of dates
        importArrayAdapter = new ArrayAdapter<FileDetails>(this, R.layout.import_list_item, Model.getInstance()
                .getFileList());
        Model.getInstance().setImportArrayAdapter(importArrayAdapter);
        setListAdapter(importArrayAdapter);
        
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        
        // Add the list view to the model
        Model.getInstance().setImportListView(listView);
        
        // Enable context menu if available
        ((CalendarApp) getApplication()).changeFunctionalityAccordingToServiceLevel();
        
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
    
    
    @Override
    public boolean onContextItemSelected(MenuItem aItem) {
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) aItem.getMenuInfo();
        final FileDetails clicked = Model.getInstance().getFileList().get(menuInfo.position);
        /*
         * Called when the user presses sth. in the menu that appears while long clicking
         */
        if (aItem.getItemId() == 0) {
            FileSystemConnector.getInstance().deleteFile(clicked);
            return true;
        }
        return false;
    }
    
}
