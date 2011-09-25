package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities;

import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemConnector;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.ImportDialog;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources.FileDetails;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
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
        
        /*
         * Listener for long clicking on one item. Opens a context menu where
         * the user can delete a file
         */
        listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                menu.setHeaderTitle(getString(R.string.menu));
                menu.add(0, 0, 0, R.string.delete);
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
