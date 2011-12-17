package de.unistuttgart.ipvs.pmp.gui.tab;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.PresetPrivacySettingsAdapter;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;

/**
 * The "Assigned Privacy Settings" tab of a Preset
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetPrivacySettingsTab extends Activity {
    
    /**
     * The preset instance
     */
    private IPreset preset;
    
    /**
     * The Priavcy Setting list
     */
    private List<IPrivacySetting> psList;
    
    /**
     * The Privacy Setting list view
     */
    private ListView psListView;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get the preset
        String presetIdentifier = super.getIntent().getStringExtra(GUIConstants.PRESET_IDENTIFIER);
        this.preset = ModelProxy.get().getPreset(null, presetIdentifier);
        
        // Set view
        setContentView(R.layout.tab_preset_pss);
        
        init();
        
        updateList();
        
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
        inflater.inflate(R.menu.preset_menu_pss_tab, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    /**
     * Initialize the data structures
     */
    private void init() {
        // Setup the appsListView
        this.psListView = (ListView) findViewById(R.id.listview_assigned_pss);
    }
    
    
    /**
     * Update the list of apps
     * 
     */
    public void updateList() {
        
        psList = new ArrayList<IPrivacySetting>();
        
        for (IPrivacySetting ps : preset.getGrantedPrivacySettings()) {
            psList.add(ps);
        }
         
        
        PresetPrivacySettingsAdapter ppsAdapter = new PresetPrivacySettingsAdapter(this, psList);
        psListView.setAdapter(ppsAdapter);
        
        // Show or hide the text view about no pss assigned
        TextView noAssignedPSs = (TextView) findViewById(R.id.preset_tab_pss_no_assigned);
        if (psList.size() == 0) {
            noAssignedPSs.setVisibility(TextView.VISIBLE);
        } else {
            noAssignedPSs.setVisibility(TextView.GONE);
        }
    }
    
}
