package de.unistuttgart.ipvs.pmp.gui.preset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.ActivityKillReceiver;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;

/**
 * The overview of all created and deleted Presets
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetsActivity extends Activity {
    
    /**
     * List of all Presets
     */
    protected List<IPreset> presetList;
    
    /**
     * List of all deleted Presets
     */
    protected List<IPreset> presetTrashBinList;
    
    /**
     * Array of all Presets
     */
    private IPreset[] presets;
    
    /**
     * ListView of all Presets
     */
    private ListView presetListView;
    
    /**
     * ListView of all Preset in the trash bin
     */
    private ListView presetTrashBinListView;
    
    /**
     * The {@link ActivityKillReceiver}.
     */
    private ActivityKillReceiver akr;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_presets);
        
        init();
        
        /* Initiating the ActivityKillReceiver. */
        this.akr = new ActivityKillReceiver(this);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        unregisterReceiver(this.akr);
    }
    
    
    /**
     * Create the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.presets_menu, menu);
        MenuItem item = menu.getItem(1);
        if (PMPPreferences.getInstance().isPresetTrashBinVisible()) {
            item.setTitle(R.string.hide_trash_bin);
        } else {
            item.setTitle(R.string.show_trash_bin);
        }
        return super.onCreateOptionsMenu(menu);
    }
    
    
    /**
     * React on a selected menu item
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.presets_menu_add:
                /*
                 * Add a Preset
                 */
                PresetAddEditDialog dialog = new PresetAddEditDialog(PresetsActivity.this, this, null);
                dialog.setTitle(R.string.add_preset);
                dialog.show();
                break;
            case R.id.presets_menu_show_trash_bin:
                /*
                 * Show the trash bin
                 */
                if (PMPPreferences.getInstance().isPresetTrashBinVisible()) {
                    showTrashBin(false);
                    PMPPreferences.getInstance().setPresetTrashBinVisible(false);
                    item.setTitle(R.string.show_trash_bin);
                } else {
                    showTrashBin(true);
                    PMPPreferences.getInstance().setPresetTrashBinVisible(true);
                    item.setTitle(R.string.hide_trash_bin);
                }
                updateList();
                break;
            case R.id.presets_menu_clear_trash_bin:
                /*
                 * Clear the trash bin
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.presets_alert_clear_trash_bin)).setCancelable(false)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            
                            public void onClick(DialogInterface dialog, int id) {
                                for (IPreset preset : ModelProxy.get().getPresets()) {
                                    if (preset.isDeleted()) {
                                        ModelProxy.get().removePreset(preset.getCreator(), preset.getLocalIdentifier());
                                        Log.d("Deleted Preset \"" + preset.getName() + "\" (by cleaning the trash bin)");
                                    }
                                    
                                }
                                updateList();
                                dialog.dismiss();
                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
    
    
    /**
     * React on a clicked item of the context menu
     */
    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        // The menu information
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) menuItem.getMenuInfo();
        if (menuInfo.targetView == presetListView) {
            IPreset preset = this.presetList.get(menuInfo.position);
            switch (menuItem.getItemId()) {
                case 0:
                    /*
                     * Clicked on "Edit name and description"
                     */
                    PresetAddEditDialog dialog = new PresetAddEditDialog(PresetsActivity.this, this, preset);
                    dialog.show();
                    return true;
                case 1:
                    /*
                     * Clicked on "delete (trash bin)"
                     */
                    preset.setDeleted(true);
                    updateList();
                    return true;
            }
        } else if (menuInfo.targetView == presetTrashBinListView) {
            IPreset preset = this.presetTrashBinList.get(menuInfo.position);
            // Context menu of a deleted preset
            switch (menuItem.getItemId()) {
                case 0: // Clicked on "restore" 
                    preset.setDeleted(false);
                    updateList();
                    return true;
                case 1: // Clicked on "delete permanently"
                    ModelProxy.get().removePreset(null, preset.getLocalIdentifier());
                    updateList();
                    return true;
            }
        }
        
        return false;
        
    }
    
    
    /**
     * Initialize the components
     */
    private void init() {
        
        // Setup the presetsListView
        this.presetListView = (ListView) findViewById(R.id.ListView_Presets);
        this.presetListView.setClickable(true);
        this.presetListView.setLongClickable(false);
        registerForContextMenu(this.presetListView);
        
        // Setup the presetsTrashBinListView
        this.presetTrashBinListView = (ListView) findViewById(R.id.ListView_Presets_Trash_Bin);
        this.presetTrashBinListView.setClickable(true);
        this.presetTrashBinListView.setLongClickable(false);
        registerForContextMenu(this.presetTrashBinListView);
        
        // Add listener
        addListener();
        
        // Show trash bin (or not)
        showTrashBin(PMPPreferences.getInstance().isPresetTrashBinVisible());
    }
    
    
    /**
     * Add all listener two the presetListView and presetTrashBinListView
     */
    private void addListener() {
        // Add a context menu listener for long clicks to the presetListView
        this.presetListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                ((AdapterContextMenuInfo) menuInfo).targetView = PresetsActivity.this.presetListView;
                menu.setHeaderTitle(getString(R.string.edit_preset));
                menu.add(0, 0, 0, R.string.edit_name_and_description);
                menu.add(1, 1, 1, R.string.delete_preset_trash_bin);
            }
        });
        
        // React on clicked item on the presetListView
        this.presetListView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
                IPreset preset = PresetsActivity.this.presetList.get(pos);
                openPreset(preset);
            }
        });
        
        // Add a context menu listener for long clicks to the presetTrashBinListView
        this.presetTrashBinListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                ((AdapterContextMenuInfo) menuInfo).targetView = PresetsActivity.this.presetTrashBinListView;
                menu.setHeaderTitle(getString(R.string.edit_deleted_preset));
                menu.add(0, 0, 0, R.string.restore_preset);
                menu.add(1, 1, 1, R.string.delete_preset_permanently);
            }
        });
        
        // React on clicked item on the presetTrashBinListView
        this.presetTrashBinListView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
                openContextMenu(view);
            }
        });
    }
    
    
    /**
     * Open the PresetActivity for one Preset
     * 
     * @param preset
     *            Preset to open
     */
    public void openPreset(IPreset preset) {
        Intent i = new Intent(PresetsActivity.this, PresetActivity.class);
        i.putExtra(GUIConstants.PRESET_IDENTIFIER, preset.getLocalIdentifier());
        PresetsActivity.this.startActivity(i);
    }
    
    
    /**
     * Invoke method to show the presets
     * 
     */
    public void updateList() {
        // Get the presets
        this.presets = ModelProxy.get().getPresets();
        this.presetList = new ArrayList<IPreset>();
        this.presetTrashBinList = new ArrayList<IPreset>();
        
        // Fill the lists
        for (IPreset preset : this.presets) {
            if (!preset.isDeleted()) {
                this.presetList.add(preset);
            } else {
                this.presetTrashBinList.add(preset);
            }
        }
        
        // Set adapters
        Collections.sort(this.presetList, new PresetComparator());
        PresetsAdapter presetsAdapter = new PresetsAdapter(this, this.presetList);
        this.presetListView.setAdapter(presetsAdapter);
        
        Collections.sort(this.presetTrashBinList, new PresetComparator());
        PresetsAdapter presetsTrashBinAdapter = new PresetsAdapter(this, this.presetTrashBinList);
        this.presetTrashBinListView.setAdapter(presetsTrashBinAdapter);
        
        // Show label, if a list is empty
        TextView labelPresetList = (TextView) findViewById(R.id.Presets_Text_View_No_Presets_Existing);
        if (this.presetList.size() == 0) {
            labelPresetList.setVisibility(TextView.VISIBLE);
        } else {
            labelPresetList.setVisibility(TextView.GONE);
        }
        
        TextView labelTrashBinList = (TextView) findViewById(R.id.Presets_Text_View_Trash_Bin_Empty);
        if (this.presetTrashBinList.size() == 0 && PMPPreferences.getInstance().isPresetTrashBinVisible()) {
            labelTrashBinList.setVisibility(TextView.VISIBLE);
        } else {
            labelTrashBinList.setVisibility(TextView.GONE);
        }
        
    }
    
    
    /**
     * Show the trash bin (or not)
     * 
     * @param flag
     *            flag if the trash bin should be shown or not
     */
    private void showTrashBin(boolean flag) {
        LinearLayout label = (LinearLayout) findViewById(R.id.Presets_Trash_Bin_Label);
        ListView listView = (ListView) findViewById(R.id.ListView_Presets_Trash_Bin);
        TextView emptyLabel = (TextView) findViewById(R.id.Presets_Text_View_Trash_Bin_Empty);
        if (flag) {
            label.setVisibility(LinearLayout.VISIBLE);
            listView.setVisibility(ListView.VISIBLE);
            emptyLabel.setVisibility(TextView.VISIBLE);
        } else {
            label.setVisibility(LinearLayout.GONE);
            listView.setVisibility(ListView.GONE);
            emptyLabel.setVisibility(TextView.GONE);
        }
    }
}
