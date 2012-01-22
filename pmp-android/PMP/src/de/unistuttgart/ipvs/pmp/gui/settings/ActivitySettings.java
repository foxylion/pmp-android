package de.unistuttgart.ipvs.pmp.gui.settings;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ListView;
import de.unistuttgart.ipvs.pmp.R;

/**
 * The {@link ActivitySettings} enables the user to select between the expert mode and the normal mode.
 * In the export mode the presets feature is enabled.
 * 
 * @author Jakob Jarosch, Marcus Vetter
 */
public class ActivitySettings extends Activity {
    
    /**
     * ListView of all Settings
     */
    private ListView settingsListView;
    
    /**
     * List of all Settings
     */
    private List<Setting> settingsList = new ArrayList<Setting>();
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set the Activity Layout
        setContentView(R.layout.activity_settings);
        
        // Get the ListView
        this.settingsListView = (ListView) findViewById(R.id.ListView_Settings);
        
        // Add all available Settings
        addSettings();
        
        // Instantiate and add the adapter
        SettingsAdapter settingsAdapter = new SettingsAdapter(this, this.settingsList);
        this.settingsListView.setAdapter(settingsAdapter);
    }
    
    
    /**
     * Add all Settings to the settingsList
     */
    private void addSettings() {
        // Get the resources
        Resources res = getResources();
        
        // Add the ExpertMode-Setting
        Drawable expertModeIcon = res.getDrawable(R.drawable.icon_expertmode);
        Setting expertMode = new Setting(SettingIdentifier.EXPERT_MODE, getString(R.string.expert_mode),
                getString(R.string.settings_expertmode_description), expertModeIcon);
        this.settingsList.add(expertMode);
        
        // Add the PresetTrashBinVisible-Setting
        Drawable presetTrashBinVisibleIcon = res.getDrawable(R.drawable.icon_expertmode);
        Setting presetTrashBinVisible = new Setting(SettingIdentifier.PRESET_TRASH_BIN_VISIBILITY,
                getString(R.string.settings_preset_trash_bin_visible),
                getString(R.string.settings_preset_trash_bin_description), presetTrashBinVisibleIcon);
        this.settingsList.add(presetTrashBinVisible);
    }
    
}
