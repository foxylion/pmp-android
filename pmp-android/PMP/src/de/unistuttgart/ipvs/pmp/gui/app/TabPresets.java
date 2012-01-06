package de.unistuttgart.ipvs.pmp.gui.app;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.preset.PresetActivity;
import de.unistuttgart.ipvs.pmp.gui.preset.PresetsAdapter;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.gui.util.GUITools;
import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;

/**
 * The {@link TabPresets} displays all Presets which are assigned to this App.
 * 
 * @author Jakob Jarosch
 */
public class TabPresets extends Activity {
    
    /**
     * The reference to the real App in the model.
     */
    private IApp app;
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        setContentView(R.layout.tab_app_presets);
        
        this.app = GUITools.handleAppIntent(getIntent());
        
        /* Switch between Expert Mode and Normal Mode */
        TextView tvDescriptionNormalMode = (TextView) findViewById(R.id.TextView_Description_Normal);
        LinearLayout tvDescriptionExpertMode = (LinearLayout) findViewById(R.id.TextView_Description_Expert);
        if (PMPPreferences.getInstance().isExpertMode()) {
            tvDescriptionNormalMode.setVisibility(View.GONE);
            tvDescriptionExpertMode.setVisibility(View.VISIBLE);
            
            initPresetList();
        } else {
            tvDescriptionNormalMode.setVisibility(View.VISIBLE);
            tvDescriptionExpertMode.setVisibility(View.GONE);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu_presets_tab, menu);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_app_add_preset:
                Toast.makeText(this, "Currently not implemented", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
    
    /**
     * Initiates the list of all assigned Presets.
     */
    private void initPresetList() {
        final List<IPreset> presetsList = Arrays.asList(this.app.getAssignedPresets());
        
        PresetsAdapter presetsAdapter = new PresetsAdapter(getApplicationContext(), presetsList);
        
        ListView presetListView = (ListView) findViewById(R.id.ListView_Presets);
        presetListView.setAdapter(presetsAdapter);
        
        presetListView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
                openPreset(presetsList.get(position));
            }
        });
        
        /* Determine if the Presets-List is empty, and display a text instead. */
        TextView tv = (TextView) findViewById(R.id.TextView_NoPresets);
        if (presetsList.size() > 0) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
        }
    }
    
    
    /**
     * Open the PresetActivity for one Preset
     * 
     * @param preset
     *            Preset to open
     */
    public void openPreset(IPreset preset) {
        Intent i = new Intent(TabPresets.this, PresetActivity.class);
        i.putExtra(GUIConstants.PRESET_IDENTIFIER, preset.getLocalIdentifier());
        startActivity(i);
    }
}
