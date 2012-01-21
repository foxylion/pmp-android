package de.unistuttgart.ipvs.pmp.gui.preset;

import java.util.ArrayList;
import java.util.Collections;
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
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
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
     * Array of all Presets
     */
    private IPreset[] presets;
    
    /**
     * ListView of all Presets
     */
    private ListView presetListView;
    
    /**
     * Flag for showing deleted presets
     */
    private boolean showDeleted = false;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_presets);
        
        init();
        
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
        inflater.inflate(R.menu.presets_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    
    /**
     * React on a selected menu item
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.presets_menu_add:
                PresetAddEditDialog dialog = new PresetAddEditDialog(PresetsActivity.this, this, null);
                dialog.setTitle(R.string.add_preset);
                dialog.show();
                break;
            case R.id.presets_menu_show_deleted:
                System.out.println(this.showDeleted);
                if (this.showDeleted) {
                    this.showDeleted = false;
                    item.setTitle(R.string.show_deleted);
                } else {
                    this.showDeleted = true;
                    item.setTitle(R.string.hide_deleted);
                }
                updateList();
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
        IPreset preset = this.presetList.get(menuInfo.position);
        
        if (preset.isDeleted()) {
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
            
        } else {
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
                case 2:
                    /*
                     * Clicked on "delete permanently"
                     */
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
        
        // Add a context menu listener for long clicks
        this.presetListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                AdapterContextMenuInfo menuInfoAdapter = (AdapterContextMenuInfo) menuInfo;
                IPreset preset = PresetsActivity.this.presetList.get(menuInfoAdapter.position);
                
                if (preset.isDeleted()) {
                    menu.setHeaderTitle(getString(R.string.edit_deleted_preset));
                    menu.add(0, 0, 0, R.string.restore_preset);
                    menu.add(1, 1, 1, R.string.delete_preset_permanently);
                } else {
                    menu.setHeaderTitle(getString(R.string.edit_preset));
                    menu.add(0, 0, 0, R.string.edit_name_and_description);
                    menu.add(1, 1, 1, R.string.delete_preset_trash_bin);
                    menu.add(2, 2, 2, R.string.delete_preset_permanently);
                }
            }
        });
        
        // React on clicked item
        this.presetListView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
                
                IPreset preset = PresetsActivity.this.presetList.get(pos);
                
                if (!preset.isDeleted()) {
                    openPreset(preset);
                } else {
                    openContextMenu(view);
                }
                
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
        
        // Fill the list
        for (IPreset preset : this.presets) {
            if (!this.showDeleted && preset.isDeleted()) {
                continue;
            }
            this.presetList.add(preset);
        }
        
        // Set adapter
        
        Collections.sort(this.presetList, new PresetComparator());
        PresetsAdapter presetsAdapter = new PresetsAdapter(this, this.presetList);
        this.presetListView.setAdapter(presetsAdapter);
        
    }
}
