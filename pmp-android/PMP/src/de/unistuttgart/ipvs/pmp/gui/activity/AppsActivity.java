package de.unistuttgart.ipvs.pmp.gui.activity;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.AppsAdapter;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.gui.util.GUITools;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;

/**
 * The {@link AppsActivity} displays all at PMP registered Apps.
 * If the user may tab on one of the listed Apps he will get the {@link AppActivity}.
 * 
 * @author Jakob Jarosch
 */
public class AppsActivity extends Activity {
    
    /**
     * List of all registered Apps.
     */
    private List<IApp> appsList;
    
    /**
     * {@link ListView} is the view reference for the Apps list.
     */
    private ListView appsViewList;
    
    /**
     * {@link AppsAdapter} for displaying the appsList.
     */
    protected AppsAdapter appsAdapter;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_apps);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        this.appsViewList = (ListView) findViewById(R.id.ListView_Apps);
        this.appsViewList.setClickable(true);
        
        updateAppsList();
        
        this.appsViewList.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // recover App identifier
                IApp app = ((IApp) AppsActivity.this.appsAdapter.getItem(position));
                
                // create intent and start AppActivity
                Intent intent = GUITools.createAppActivityIntent(app);
                GUITools.startIntent(intent);
            }
        });
        
        this.appsViewList.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                menu.setHeaderTitle(getString(R.string.app_context_menu));
                menu.add(0, 0, 0, R.string.details_app);
                menu.add(1, 1, 0, R.string.remove_app);
            }
        });
    }
    
    
    /**
     * Is called when a long press on an App was done.
     */
    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        // The menu information
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) menuItem.getMenuInfo();
        IApp app = this.appsList.get(menuInfo.position);
        
        if (menuItem.getItemId() == 0) {
            // open details
            Intent intent = GUITools.createAppActivityIntent(app);
            GUITools.startIntent(intent);
            
        } else if (menuItem.getItemId() == 1) {
            // remove app from model
            ModelProxy.get().unregisterApp(app.getIdentifier());
            
            // update the app list (item just removed)
            updateAppsList();
            
            // inform the user
            Toast.makeText(this, getResources().getString(R.string.app_removed), Toast.LENGTH_LONG).show();
        }
        
        return true;
    }
    
    
    /**
     * Updates the AppsList, when a change occurred (like an App was installed or removed).
     */
    private void updateAppsList() {
        this.appsList = Arrays.asList(ModelProxy.get().getApps());
        this.appsAdapter = new AppsAdapter(this, this.appsList);
        this.appsViewList.setAdapter(this.appsAdapter);
    }
}
