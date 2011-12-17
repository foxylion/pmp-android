package de.unistuttgart.ipvs.pmp.gui.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.PresetPrivacySettingsAdapter;
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
public class PresetPrivacySettingsTab extends Activity {
    
    /**
     * The preset instance
     */
    private IPreset preset;
    
    /**
     * The Priavcy Setting list
     */
    private List<IPrivacySetting> allPSList;
    
    /**
     * The list with all RGs and their PSs
     */
    private ArrayList<ArrayList<IPrivacySetting>> psList;
    
    /**
     * The Privacy Setting list view
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
     * Initialize the data structures
     */
    private void init() {
        // Setup the appsListView
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
                int rgPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
                int psPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
                
                // Open a context menu, if this was a click on a Privacy Setting (type=1)
                if (type == 1) {
                    
                    IPrivacySetting ps = PresetPrivacySettingsTab.this.psList.get(rgPos).get(psPos);
                    
                    menu.setHeaderTitle(ps.getName());
                    menu.setHeaderIcon(ps.getResourceGroup().getIcon());
                    menu.add(0, 0, 0, R.string.preset_tab_pss_context_menu_change_value);
                    menu.add(1, 1, 0, R.string.preset_tab_pss_context_menu_remove_pss);
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
        IPrivacySetting ps = PresetPrivacySettingsTab.this.psList.get(rgPos).get(psPos);
        
        // Handle
        switch (item.getItemId()) {
            case 0: // Change PS value
                return true;
            case 1: // Remove PS
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
        allPSList = new ArrayList<IPrivacySetting>();
        
        /* Build a hash map with the RGs and their PSs */
        HashMap<IResourceGroup, ArrayList<IPrivacySetting>> RGPSMap = new HashMap<IResourceGroup, ArrayList<IPrivacySetting>>();
        
        for (IPrivacySetting ps : preset.getGrantedPrivacySettings()) {
            IResourceGroup rg = ps.getResourceGroup();
            
            // Add the PS to the allPsList
            allPSList.add(ps);
            
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
        
        for (IResourceGroup rg : RGPSMap.keySet()) {
            rgList.add(rg);
            psList.add(RGPSMap.get(rg));
        }
        
        // Add the adapter
        PresetPrivacySettingsAdapter ppsAdapter = new PresetPrivacySettingsAdapter(this, rgList, psList);
        psExpandableListView.setAdapter(ppsAdapter);
        
        // Show or hide the text view about no pss assigned
        TextView noAssignedPSs = (TextView) findViewById(R.id.preset_tab_pss_no_assigned);
        if (allPSList.size() == 0) {
            noAssignedPSs.setVisibility(TextView.VISIBLE);
        } else {
            noAssignedPSs.setVisibility(TextView.GONE);
        }
    }
}
