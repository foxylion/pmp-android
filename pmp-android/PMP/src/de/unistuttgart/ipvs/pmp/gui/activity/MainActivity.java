package de.unistuttgart.ipvs.pmp.gui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.gui.util.AlwaysClickableButton;
import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;

/**
 * The {@link MainActivity} is the startup activity for PMP. It is also available in the App-Drawer.
 * 
 * @author Jakob Jarosch
 */
public class MainActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // TODO remove this once real model works
        ModelProxy.set(true, this);
        
        setContentView(R.layout.activity_main);
        
        registerListener();
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        checkExpertMode();
        
        updateStatistics(ModelProxy.get().getApps().length, ModelProxy.get().getResourceGroups().length, ModelProxy
                .get().getPresets().length);
    }
    
    
    /**
     * Updates the statistics displayed in the bottom part of the main activity.
     * 
     * @param appsCount
     *            Count of the registered apps.
     * @param rgsCount
     *            Count of the installed resource groups.
     * @param presetsCount
     *            Count of the created presets.
     */
    public void updateStatistics(int appsCount, int rgsCount, int presetsCount) {
        TextView textApps = (TextView) findViewById(R.id.TextView_Apps);
        textApps.setText(String.format(getResources().getString(R.string.main_statistics_apps), appsCount));
        
        TextView textRgs = (TextView) findViewById(R.id.TextView_RGs);
        textRgs.setText(String.format(getResources().getString(R.string.main_statistics_rgs), rgsCount));
        
        TextView textPresets = (TextView) findViewById(R.id.TextView_Presets);
        textPresets.setText(String.format(getResources().getString(R.string.main_statistics_presets), presetsCount));
    }
    
    
    /**
     * Registers all the listeners to the {@link Button}s.
     */
    private void registerListener() {
        Button buttonApps = (Button) findViewById(R.id.Button_Apps);
        Button buttonRgs = (Button) findViewById(R.id.Button_RGs);
        AlwaysClickableButton buttonPresets = (AlwaysClickableButton) findViewById(R.id.Button_Presets);
        Button buttonSettings = (Button) findViewById(R.id.Button_Settings);
        
        /* The Apps-Button OnClickListener */
        buttonApps.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AppsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        
        /* The RGs-Button OnClickListener */
        buttonRgs.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RGsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        
        /* The Presets-Button OnClickListener */
        buttonPresets.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PresetsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        // React when a touch occurs and the button is disabled.
        buttonPresets.setDisabledOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, R.string.main_presets_disabled, Toast.LENGTH_LONG).show();
            }
        });
        
        /* The Settings-Button OnClickListener */
        buttonSettings.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }
    
    
    /**
     * Hide the Presets if the Expert mode is disabled.
     */
    private void checkExpertMode() {
        Button buttonPresets = (Button) findViewById(R.id.Button_Presets);
        
        if (PMPPreferences.getInstance().isExpertMode()) {
            buttonPresets.setEnabled(true);
        } else {
            buttonPresets.setEnabled(false);
        }
    }
}
