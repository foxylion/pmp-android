package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemConnector;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemListActionType;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.util.DialogManager;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources.FileDetails;

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
        
        // Store the context
        Model.getInstance().setImportContext(this);
        
        /*
         * Fill the list of files for importing.
         * It is also used to check for exporting, if a file already exists.
         */
        FileSystemConnector.getInstance().listStoredFiles(FileSystemListActionType.NONE);
        
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
                final FileDetails file = Model.getInstance().getFileList().get(position);

                // Show the confirm dialog for importing and deleting all current appointments
                new AlertDialog.Builder(ImportActivity.this).setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.import_question)
                        .setMessage(R.string.import_attention)
                        .setPositiveButton(R.string.conf, new DialogInterface.OnClickListener() {
                            
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Import the file
                                FileSystemConnector.getInstance().importAppointments(file.getName());
                                finish();
                            }
                            
                        }).show();
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
            if (Model.getInstance().getServiceLevel() >= 6) {
                FileSystemConnector.getInstance().deleteFile(clicked);
            } else {
                DialogManager.getInstance().showServiceLevelInsufficientDialog(this);
            }
            return true;
        }
        return false;
    }
    
}
