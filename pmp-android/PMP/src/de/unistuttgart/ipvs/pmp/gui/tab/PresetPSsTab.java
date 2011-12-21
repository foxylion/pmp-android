package de.unistuttgart.ipvs.pmp.gui.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
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
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;

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
    private ArrayList<IResourceGroup> rgList;
    /**
     * The Privacy Setting expandable list view
     */
    private ExpandableListView psExpandableListView;
    
    /**
     * The Adapter
     */
    private PresetPrivacySettingsAdapter ppsAdapter;
    
    
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
                PresetAssignPSsDialog dialog = new PresetAssignPSsDialog(PresetPSsTab.this, this, preset);
                
                // Check, if there are Apps available which are not assigned yet
                if (dialog.getSizeOfRGList() > 0) {
                    dialog.show();
                    dialog.getWindow().setGravity(Gravity.TOP);
                } else {
                    Toast.makeText(this, getString(R.string.preset_tab_pss_all_pss_already_assigned), Toast.LENGTH_LONG)
                            .show();
                }
                
                break;
        }
        return super.onMenuItemSelected(featureId, item);
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
        final IPrivacySetting ps = PresetPSsTab.this.psList.get(rgPos).get(psPos);
        
        // Handle
        switch (item.getItemId()) {
            case 0:
                /*
                 * Show details of PS with an AlertDialog
                 */
                
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setMessage(getString(R.string.description) + ":\n" + ps.getDescription() + "\n\n"
                        + getString(R.string.resource_group) + ":\n" + ps.getResourceGroup().getName() + "\n\n"
                        + getString(R.string.identifier) + ":\n" + ps.getIdentifier());
                alertDialog.setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                    
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.setTitle(ps.getName());
                alert.show();
                
                return true;
            case 1:
                /*
                 *  Change PS value
                 */
                showChangeValueDialog(ps);
                
                return true;
            case 2:
                /*
                 *  Remove PS
                 */
                
                // Save expanded states
                ArrayList<Boolean> expandedStates = new ArrayList<Boolean>();
                for (int groupID = 0; groupID < this.ppsAdapter.getGroupCount(); groupID++) {
                    expandedStates.add(this.psExpandableListView.isGroupExpanded(groupID));
                }
                // Save index of RG, if this RG will disappear later
                int tmpIndexOfRG = this.rgList.indexOf(ps.getResourceGroup());
                
                // Remove the PS
                this.preset.removePrivacySetting(ps);
                
                // Update the list
                updateList();
                
                // Restore the expanded states of the groups
                if (!this.rgList.contains(ps.getResourceGroup())) {
                    propagateExpandedGroups(expandedStates, tmpIndexOfRG);
                }
                
                return true;
        }
        
        return false;
    }
    
    
    /**
     * Show a dialog to change the value of the given Privacy Setting
     * 
     * @param ps
     *            change value of the given Privacy Setting
     */
    public void showChangeValueDialog(final IPrivacySetting ps) {
        
        AlertDialog.Builder alertDialogChangeValue = new AlertDialog.Builder(this);
        
        final View psView = ps.getView(this);
        
        // Set currentValue, if the Privacy Setting is already assigned
        String currentValue = this.preset.getGrantedPrivacySettingValue(ps);
        String title = null;
        if (currentValue != null) {
            try {
                title = getString(R.string.change_value);
                ps.setViewValue(psView, currentValue);
            } catch (PrivacySettingValueException e) {
                // not possible
            }
        } else {
            title = getString(R.string.set_value);
        }
        alertDialogChangeValue.setView(psView);
        alertDialogChangeValue.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int id) {
                PresetPSsTab.this.preset.assignPrivacySetting(ps, ps.getViewValue(psView));
                PresetPSsTab.this.updateList();
                dialog.dismiss();
            }
        });
        alertDialogChangeValue.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertChangeValue = alertDialogChangeValue.create();
        alertChangeValue.setTitle(title);
        alertChangeValue.show();
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
        
        // Add the adapter
        ppsAdapter = new PresetPrivacySettingsAdapter(this, this.preset);
        this.psExpandableListView.setAdapter(ppsAdapter);
        
    }
    
    
    /**
     * Update the list of Apps and notify the addapter
     * 
     */
    public void updateList() {
        allassignedPSList = new ArrayList<IPrivacySetting>();
        
        /* Build a hash map with the RGs and their PSs */
        HashMap<IResourceGroup, ArrayList<IPrivacySetting>> RGPSMap = new HashMap<IResourceGroup, ArrayList<IPrivacySetting>>();
        
        for (IPrivacySetting ps : this.preset.getGrantedPrivacySettings()) {
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
        this.rgList = new ArrayList<IResourceGroup>();
        this.psList = new ArrayList<ArrayList<IPrivacySetting>>();
        
        for (Entry<IResourceGroup, ArrayList<IPrivacySetting>> entry : RGPSMap.entrySet()) {
            rgList.add(entry.getKey());
            this.psList.add(entry.getValue());
        }
        
        // Show or hide the text view about no pss assigned
        TextView noAssignedPSs = (TextView) findViewById(R.id.preset_tab_pss_no_assigned);
        
        if (allassignedPSList.size() == 0) {
            noAssignedPSs.setVisibility(TextView.VISIBLE);
        } else {
            noAssignedPSs.setVisibility(View.GONE);
        }
        
        ppsAdapter.setPsList(this.psList);
        ppsAdapter.setRgList(rgList);
        ppsAdapter.notifyDataSetChanged();
    }
    
    
    /**
     * Restore the expanded-state of the nodes by using a list with the expanded states before removing one.
     * 
     * @param oldExpandedStates
     *            a list with the old expanded states
     * @param startByGroupID
     *            the start index (start with propagating by this groupID)
     */
    private void propagateExpandedGroups(ArrayList<Boolean> oldExpandedStates, int startByGroupID) {
        for (int groupID = startByGroupID; groupID < ppsAdapter.getGroupCount(); groupID++) {
            if (oldExpandedStates.get(groupID + 1)) {
                this.psExpandableListView.expandGroup(groupID);
            } else {
                this.psExpandableListView.collapseGroup(groupID);
            }
        }
    }
}
