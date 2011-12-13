package de.unistuttgart.ipvs.pmp.gui.tab;

import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.PresetAppsAdapter;
import de.unistuttgart.ipvs.pmp.gui.dialog.PresetAssignAppsDialog;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;

/**
 * The "Assigned Apps" tab of a Preset
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetAppsTab extends Activity {
    
    /**
     * The preset instance
     */
    private IPreset preset;
    
    /**
     * The Assign Apps Button
     */
    private Button assignAppButton;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get the preset
        String presetIdentifier = super.getIntent().getStringExtra(GUIConstants.PRESET_IDENTIFIER);
        this.preset = ModelProxy.get().getPreset(null, presetIdentifier);
        
        // Set view
        setContentView(R.layout.tab_preset_apps);
        
        // Fill the list
        updateList();
        
        // Setup the add preset button
        this.assignAppButton = (Button) findViewById(R.id.preset_tab_apps_assign_app_button);
        this.assignAppButton.setOnClickListener(new android.view.View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                PresetAssignAppsDialog dialog = new PresetAssignAppsDialog(PresetAppsTab.this);
                dialog.setTitle("bla");
                dialog.setActivity(PresetAppsTab.this);
                dialog.setPreset(preset);
                dialog.show();
            }

        });
    }
    
    
    /**
     * Update the list of apps
     * 
     */
    private void updateList() {
        final IApp[] apps = preset.getAssignedApps();
        
        ListView appsList = (ListView) findViewById(R.id.listview_assigned_apps);
        appsList.setClickable(true);
        
        PresetAppsAdapter presetAppsAdapter = new PresetAppsAdapter(this, Arrays.asList(apps));
        appsList.setAdapter(presetAppsAdapter);
        
        // Add the listner and dialog
        appsList.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                Builder alertBuilder = new Builder(PresetAppsTab.this);
                alertBuilder.setMessage(getString(R.string.preset_tab_apps_remove_app_msg));
                alertBuilder.setCancelable(false);
                alertBuilder.setPositiveButton(R.string.presets_dialog_confirm, new OnClickListener() {
                    
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'Yes' Button
                        preset.removeApp(apps[arg2]);
                        updateList();
                        dialog.dismiss();
                    }
                });
                alertBuilder.setNegativeButton(R.string.presets_dialog_cancel, new DialogInterface.OnClickListener() {
                    
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
                AlertDialog alert = alertBuilder.create();
                // Title for AlertDialog
                alert.setTitle(getString(R.string.preset_tab_apps_remove_app_title));
                // Icon for AlertDialog
                alert.setIcon(R.drawable.icon_delete_32);
                alert.show();
            }
        });
    }
    
}
