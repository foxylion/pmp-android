package de.unistuttgart.ipvs.pmp.gui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ToggleButton;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.PresetsAdapter;
import de.unistuttgart.ipvs.pmp.gui.placeholder.ModelProxy;
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
    private List<IPreset> presetList;
    
    /**
     * Array of all Presets
     */
    private IPreset[] presets;
    
    /**
     * ListView of all Presets
     */
    private ListView presetListView;
    
    /**
     * ToggleButton
     */
    private ToggleButton toggle;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_presets);
        
        init();
        
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        showPresets();
    }
    
    
    /**
     * Initialize the components
     */
    private void init() {
        /*
         * ToggleButton
         */
        toggle = (ToggleButton) findViewById(R.id.presets_deleted_toggle);
        toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showPresets();
            }
        });
        
        // Setup the presetsListView
        presetListView = (ListView) findViewById(R.id.ListView_Presets);
        presetListView.setClickable(true);
        presetListView.setLongClickable(false);
        registerForContextMenu(presetListView);
        
        // Add a context menu listener for long clicks
        presetListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                AdapterContextMenuInfo menuInfoAdapter = (AdapterContextMenuInfo) menuInfo;
                IPreset preset = presetList.get(menuInfoAdapter.position);
                
                if (preset.isDeleted()) {
                    menu.setHeaderTitle(getString(R.string.presets_deleted_context_menu));
                    menu.add(0, 0, 0, R.string.presets_restore);
                    menu.add(1, 1, 0, R.string.presets_delete_permanent);
                } else {
                    menu.setHeaderTitle(getString(R.string.presets_context_menu));
                    menu.add(1, 1, 0, R.string.presets_delete);
                }
            }
        });
        
        // React on clicked item
        presetListView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
                
                IPreset preset = presetList.get(pos);
                
                if (!preset.isDeleted()) {
                    Intent i = new Intent(PresetsActivity.this, PresetActivity.class);
                    PresetsActivity.this.startActivity(i);
                } else {
                    openContextMenu(view);
                }
                
            }
        });
        
    }
    
    
    /**
     * Invoke method to show the presets
     * 
     */
    private void showPresets() {
        // Get the presets
        this.presets = ModelProxy.get().getPresets();
        this.presetList = new ArrayList<IPreset>();
        
        // Show deleted presets or not
        boolean showDeleted = toggle.isChecked();
        
        // Fill the list
        for (IPreset preset : presets) {
            if (!showDeleted && preset.isDeleted())
                continue;
            presetList.add(preset);
        }
        
        // Set adapter
        PresetsAdapter presetsAdapter = new PresetsAdapter(this, presetList);
        presetListView.setAdapter(presetsAdapter);
        
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
            if (menuItem.getItemId() == 0) {
                // Clicked on "restore" 
                preset.setDeleted(false);
                showPresets();
                return true;
            } else if (menuItem.getItemId() == 1) {
                // Clicked on "delete permanently"
                ModelProxy.get().removePreset(null, preset.getIdentifier());
                showPresets();
                return true;
            }
        } else {
            // Context menu of a preset
            preset.setDeleted(true);
            showPresets();
            return true;
        }
        
        return false;
        
    }
}
