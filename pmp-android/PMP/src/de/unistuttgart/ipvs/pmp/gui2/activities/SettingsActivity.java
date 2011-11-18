package de.unistuttgart.ipvs.pmp.gui2.activities;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui2.utils.PMPPreferences;
import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

/**
 * The {@link SettingsActivity} enables the user to select between the expert mode and the normal mode.
 * In the export mode the presets feature is enabled.
 * 
 * @author Jakob Jarosch
 */
public class SettingsActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.pmp_settings);
        
        registerListener();
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        loadSettings();
    }
    
    /**
     * Registers the Listener for changes on the checkbox of the Expert Mode.
     */
    private void registerListener() {
        CheckBox checkboxExpertMode = (CheckBox) findViewById(R.id.CheckBox_ExpertMode);
        
        /* The ExpertMode-Checkbox OnCheckedChangeListener */
        checkboxExpertMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PMPPreferences.getInstanace().setExpertMode(isChecked);
                
                /* Show a toast that the expert mode state has been changed */
                String text;
                if (isChecked) {
                    text = SettingsActivity.this.getString(R.string.settings_expertmode_enabled);
                } else {
                    text = SettingsActivity.this.getString(R.string.settings_expertmode_disabled);
                }
                Toast.makeText(SettingsActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Load the Settings from {@link PMPPreferences} to ensure that consistency is guaranteed.
     */
    private void loadSettings() {
        CheckBox checkboxExpertMode = (CheckBox) findViewById(R.id.CheckBox_ExpertMode);
        checkboxExpertMode.setChecked(PMPPreferences.getInstanace().isExpertMode());
    }
}
