package de.unistuttgart.ipvs.pmp.gui.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.PresetPrivacySettingsAdapter;
import de.unistuttgart.ipvs.pmp.gui.dialog.PresetAssignPSsDialog;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;

/**
 * The "Assigned Privacy Settings" tab of a Preset
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetPSsTab extends Activity {
    
    /**
     * The preset instance
     */
    private IPreset preset;
    
    /**
     * The Priavcy Setting list
     */
    private List<IPrivacySetting> allassignedPSList;
    
    /**
     * The list with all RGs and their PSs
     */
    private ArrayList<ArrayList<IPrivacySetting>> psList;
    
    /**
     * The Privacy Setting expandable list view
     */
    private ExpandableListView psExpandableListView;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get the preset
        String presetIdentifier = super.getIntent().getStringExtra(GUIConstants.PRESET_IDENTIFIER);
        this.preset = ModelProxy.get().getPreset(null, presetIdentifier);
        
        // Set view
        setContentView(R.layout.tab_preset_pss);
        
        // Initialize the data structures and add listener
        init();
        
        // Update the list of RGs and PSs
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
        inflater.inflate(R.menu.preset_menu_pss_tab, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    /**
     * React to a selected menu item
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preset_tab_pss_assign_pss:
                PresetAssignPSsDialog dialog = new PresetAssignPSsDialog(PresetPSsTab.this, preset);
                
                // Check, if there are Apps available which are not assigned yet
                if (dialog.getSizeOfRGList() > 0) {
                    dialog.show();
                } else {
                    Toast.makeText(this, getString(R.string.preset_tab_pss_all_pss_already_assigned), Toast.LENGTH_LONG)
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
        // Setup the Preset ExpandableListView
        this.psExpandableListView = (ExpandableListView) findViewById(R.id.expandable_list_view_assigned_pss);
        
        this.psExpandableListView.setClickable(true);
        this.psExpandableListView.setLongClickable(false);
        registerForContextMenu(this.psExpandableListView);
        
        // Add a context menu listener for long clicks
        this.psExpandableListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                
                ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
                int type = ExpandableListView.getPackedPositionType(info.packedPosition);
                
                // Open a context menu, if this was a click on a Privacy Setting (type=1)
                if (type == 1) {
                    menu.setHeaderTitle(R.string.choose_your_action);
                    menu.add(0, 0, 0, R.string.show_details);
                    menu.add(1, 1, 1, R.string.change_value);
                    menu.add(2, 2, 2, R.string.remove);
                }
            }
        });
        
        // React on clicked Privacy Setting (child)
        this.psExpandableListView.setOnChildClickListener(new OnChildClickListener() {
            
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                openContextMenu(v);
                return true;
            }
        });
        
    }
    
    
    /**
     * React on a selected context menu item
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        
        // Parse the menu info
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item
                .getMenuInfo();
        int rgPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int psPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
        
        // Get the Privacy Setting
        IPrivacySetting ps = PresetPSsTab.this.psList.get(rgPos).get(psPos);
        
        // Handle
        switch (item.getItemId()) {
            case 0: // Show details of PS
                return true;
            case 1: // Change PS value
                return true;
            case 2: // Remove PS
                preset.removePrivacySetting(ps);
                updateList();
                return true;
        }
        
        return false;
    }
    
    
    /**
     * Update the list of apps
     * 
     */
    public void updateList() {
        allassignedPSList = new ArrayList<IPrivacySetting>();
        
        /* Build a hash map with the RGs and their PSs */
        HashMap<IResourceGroup, ArrayList<IPrivacySetting>> RGPSMap = new HashMap<IResourceGroup, ArrayList<IPrivacySetting>>();
        
        for (IPrivacySetting ps : preset.getGrantedPrivacySettings()) {
            IResourceGroup rg = ps.getResourceGroup();
            
            // Add the PS to the allPsList
            allassignedPSList.add(ps);
            
            if (!RGPSMap.containsKey(ps.getResourceGroup())) {
                // The map does not contain the RG: Add it as key and the PS as value
                ArrayList<IPrivacySetting> newList = new ArrayList<IPrivacySetting>();
                newList.add(ps);
                RGPSMap.put(rg, newList);
            } else {
                // The map already contains the RG: Add the PS
                ArrayList<IPrivacySetting> existingList = RGPSMap.get(rg);
                existingList.add(ps);
            }
            
        }
        
        /* Build two lists, separated into RGs and PSs out of the map */
        ArrayList<IResourceGroup> rgList = new ArrayList<IResourceGroup>();
        psList = new ArrayList<ArrayList<IPrivacySetting>>();

        for (Entry<IResourceGroup, ArrayList<IPrivacySetting>> entry : RGPSMap.entrySet()) {
            rgList.add(entry.getKey());
            psList.add(entry.getValue());
        }
        
        // Add the adapter
        PresetPrivacySettingsAdapter ppsAdapter = new PresetPrivacySettingsAdapter(this, preset, rgList, psList);
        psExpandableListView.setAdapter(ppsAdapter);
        
        // Show or hide the text view about no pss assigned
        TextView noAssignedPSs = (TextView) findViewById(R.id.preset_tab_pss_no_assigned);
        if (allassignedPSList.size() == 0) {
            noAssignedPSs.setVisibility(TextView.VISIBLE);
        } else {
            noAssignedPSs.setVisibility(TextView.GONE);
        }
    }
}
