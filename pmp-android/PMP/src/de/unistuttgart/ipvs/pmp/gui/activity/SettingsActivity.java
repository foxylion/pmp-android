package de.unistuttgart.ipvs.pmp.gui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;

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
        
        setContentView(R.layout.activity_settings);
        
        registerListener();
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        loadFromPMPPrefernces();
    }
    
    
    @Override
    protected void onPause() {
        super.onPause();
        
        saveToPMPPreferences();
    }
    
    
    /**
     * Registers the Listener for changes on the checkbox of the Expert Mode.
     */
    private void registerListener() {
        CheckBox checkboxExpertMode = (CheckBox) findViewById(R.id.CheckBox_ExpertMode);
        
        /*
         * React on a tap and save the changes directly to the PMPPreferences.
         */
        checkboxExpertMode.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                changedStateExpertMode();
            }
            
        });
        
        LinearLayout ll = (LinearLayout) findViewById(R.id.LinearLayout_ExpertMode);
        ll.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                CheckBox checkboxExpertMode = (CheckBox) findViewById(R.id.CheckBox_ExpertMode);
                checkboxExpertMode.setChecked(!checkboxExpertMode.isChecked());
                changedStateExpertMode();
            }
        });
    }
    
    
    /**
     * Load the Settings from {@link PMPPreferences} to ensure that consistency is guaranteed.
     */
    private void loadFromPMPPrefernces() {
        CheckBox checkboxExpertMode = (CheckBox) findViewById(R.id.CheckBox_ExpertMode);
        checkboxExpertMode.setChecked(PMPPreferences.getInstance().isExpertMode());
    }
    
    
    /**
     * Save the form values of the Activity to the {@link PMPPreferences}.
     * It ensures that consistency is guaranteed.
     */
    private void saveToPMPPreferences() {
        CheckBox checkboxExpertMode = (CheckBox) findViewById(R.id.CheckBox_ExpertMode);
        PMPPreferences.getInstance().setExpertMode(checkboxExpertMode.isChecked());
    }
    
    
    /**
     * Can be called when the changed state of the Expert Mode should be displayed as a toast message.
     */
    protected void changedStateExpertMode() {
        CheckBox checkboxExpertMode = (CheckBox) findViewById(R.id.CheckBox_ExpertMode);
        
        /* Show a toast that the expert mode state has been changed */
        String text;
        if (checkboxExpertMode.isChecked()) {
            text = SettingsActivity.this.getString(R.string.settings_expertmode_enabled);
        } else {
            text = SettingsActivity.this.getString(R.string.settings_expertmode_disabled);
        }
        Toast.makeText(SettingsActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}
