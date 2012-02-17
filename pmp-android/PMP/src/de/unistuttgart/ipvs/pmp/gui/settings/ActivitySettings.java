package de.unistuttgart.ipvs.pmp.gui.settings;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.ActivityKillReceiver;
import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;

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
    private List<SettingAbstract<?>> settingsList = new ArrayList<SettingAbstract<?>>();
    
    /**
     * The {@link ActivityKillReceiver}.
     */
    private ActivityKillReceiver akr;
    
    
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
        
        /* Initiating the ActivityKillReceiver. */
        this.akr = new ActivityKillReceiver(this);
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        unregisterReceiver(this.akr);
    }
    
    
    /**
     * Add all Settings to the settingsList
     */
    private void addSettings() {
        // add the ExpertMode-SettingCheckBox
        this.settingsList.add(new SettingCheckBox(R.string.expert_mode, R.string.settings_expertmode_description,
                R.drawable.icon_expertmode, new ISettingEvaluator<Boolean>() {
                    
                    @Override
                    public void setValue(Boolean newValue) {
                        PMPPreferences.getInstance().setExpertMode(newValue);
                    }
                    
                    
                    @Override
                    public Boolean getValue() {
                        return PMPPreferences.getInstance().isExpertMode();
                    }
                }));
        
        // add the preset trash bin SettingCheckBox 
        this.settingsList.add(new SettingCheckBox(R.string.settings_preset_trash_bin_visible,
                R.string.settings_preset_trash_bin_description, R.drawable.icon_expertmode,
                new ISettingEvaluator<Boolean>() {
                    
                    @Override
                    public void setValue(Boolean newValue) {
                        PMPPreferences.getInstance().setPresetTrashBinVisible(newValue);
                    }
                    
                    
                    @Override
                    public Boolean getValue() {
                        return PMPPreferences.getInstance().isPresetTrashBinVisible();
                    }
                }));
    }
}
