package de.unistuttgart.ipvs.pmp.gui.tab;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.activity.PresetActivity;
import de.unistuttgart.ipvs.pmp.gui.adapter.PresetsAdapter;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.gui.util.GUITools;
import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;

/**
 * The {@link AppPresetsTab} displays all Presets which are assigned to this App.
 * 
 * @author Jakob Jarosch
 */
public class AppPresetsTab extends Activity {
    
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
    
    /**
     * Initiates the list of all assigned Presets.
     */
    private void initPresetList() {
        final List<IPreset> presetsList = Arrays.asList(app.getAssignedPresets());
        
        PresetsAdapter presetsAdapter = new PresetsAdapter(getApplicationContext(), presetsList);
        
        ListView presetListView = (ListView) findViewById(R.id.ListView_Presets);
        presetListView.setAdapter(presetsAdapter);
        
        presetListView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
                openPreset(presetsList.get(position));
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
        Intent i = new Intent(AppPresetsTab.this, PresetActivity.class);
        i.putExtra(GUIConstants.PRESET_IDENTIFIER, preset.getLocalIdentifier());
        startActivity(i);
    }
}
