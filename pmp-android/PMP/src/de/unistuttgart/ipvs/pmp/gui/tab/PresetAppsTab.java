package de.unistuttgart.ipvs.pmp.gui.tab;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.PresetAppsAdapter;
import de.unistuttgart.ipvs.pmp.gui.dialog.PresetAssignAppsDialog;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.gui.util.GUITools;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;

/**
 * The "Assigned Apps" tab of a Preset
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetAppsTab extends Activity {
    
    /**
     * The preset instance
     */
    private IPreset preset;
    
    /**
     * ListView of all Apps
     */
    private ListView appsListView;
    
    /**
     * List of all Apps
     */
    private List<IApp> appList;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get the preset
        String presetIdentifier = super.getIntent().getStringExtra(GUIConstants.PRESET_IDENTIFIER);
        this.preset = ModelProxy.get().getPreset(null, presetIdentifier);
        
        // Set view
        setContentView(R.layout.tab_preset_apps);
        
        // Initialize
        init();
        
        // Fill the list
        updateList();
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }
    
    
    /**
     * Create the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.preset_menu_apps_tab, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    
    /**
     * React to a selected menu item
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preset_tab_apps_assign_apps:
                PresetAssignAppsDialog dialog = new PresetAssignAppsDialog(PresetAppsTab.this);
                dialog.setActivity(PresetAppsTab.this);
                dialog.setPreset(preset);
                
                // Check, if there are Apps available which are not assigned yet
                if (dialog.calcDisplayApps().size() > 0) {
                    dialog.show();
                } else {
                    Toast.makeText(this, getString(R.string.preset_tab_apps_all_apps_assigned), Toast.LENGTH_LONG)
                            .show();
                }
                
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
    
    
    /**
     * Initialize the data structures
     */
    private void init() {
        
        // Setup the appsListView
        this.appsListView = (ListView) findViewById(R.id.listview_assigned_apps);
        this.appsListView.setClickable(true);
        this.appsListView.setLongClickable(false);
        registerForContextMenu(this.appsListView);
        
        // Add a context menu listener for long clicks
        this.appsListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                menu.setHeaderTitle(R.string.preset_tab_apps_context_menu_title); 
                menu.add(0, 0, 0, R.string.preset_tab_apps_context_menu_show_details);
                menu.add(1, 1, 0, R.string.preset_tab_apps_context_menu_remove_app);
            }
        });
        
        // React on clicked item
        this.appsListView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
                
                openContextMenu(view);
                
            }
        });
    }
    
    
    /**
     * Update the list of apps
     * 
     */
    public void updateList() {
        
        appList = new ArrayList<IApp>();
        
        for (IApp app : preset.getAssignedApps()) {
            appList.add(app);
        }
        
        PresetAppsAdapter presetAppsAdapter = new PresetAppsAdapter(this, appList);
        appsListView.setAdapter(presetAppsAdapter);
        
        // Show or hide the text view about no apps assigned
        TextView noAssignedApps = (TextView) findViewById(R.id.preset_tab_apps_no_assigned);
        if (appList.size() == 0) {
            noAssignedApps.setVisibility(TextView.VISIBLE);
        } else {
            noAssignedApps.setVisibility(TextView.GONE);
        }
    }
    
    
    /**
     * React on a clicked item of the context menu
     */
    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        // The menu information
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) menuItem.getMenuInfo();
        IApp app = this.appList.get(menuInfo.position);
        
        // Context menu of a deleted preset
        switch (menuItem.getItemId()) {
            case 0: // Clicked on "Show App details" 
                
                Intent intent = GUITools.createAppActivityIntent(app);
                GUITools.startIntent(intent);
                
                return true;
            case 1: // Clicked on "Delete App"
                preset.removeApp(app);
                updateList();
                return true;
        }
        
        return false;
    }
    
}
