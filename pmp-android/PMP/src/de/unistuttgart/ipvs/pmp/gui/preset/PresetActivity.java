package de.unistuttgart.ipvs.pmp.gui.preset;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;

/**
 * Activity of one Preset. It contains two tabs: "Assigned Apps" and "Assigned Privacy Settings"
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetActivity extends Activity {
    
    /**
     * The preset instance
     */
    private IPreset preset;
    
    /**
     * Tab components
     */
    private LocalActivityManager lam;
    private TabHost mTabHost;
    private int currentTab = 0;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get the preset
        String presetIdentifier = super.getIntent().getStringExtra(GUIConstants.PRESET_IDENTIFIER);
        this.preset = ModelProxy.get().getPreset(null, presetIdentifier);
        
        // Set view
        setContentView(R.layout.activity_preset);
        
        // Set up tabs
        this.lam = new LocalActivityManager(this, true);
        this.lam.dispatchCreate(savedInstanceState);
        
        this.mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        this.mTabHost.setup(this.lam);
        
        setupTabs();
        
        // Set up title view
        BasicTitleView title = (BasicTitleView) findViewById(R.id.activity_title);
        
        title.setTitle(this.preset.getName());
        title.setIcon(R.drawable.icon_presets);
        
        updateDescriptionVisibility();
        
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        this.lam.dispatchResume();
        
        this.mTabHost.setCurrentTab(this.currentTab);
        
        updateDescriptionVisibility();
    }
    
    
    @Override
    protected void onPause() {
        super.onPause();
        
        this.lam.dispatchPause(isFinishing());
        
        this.currentTab = this.mTabHost.getCurrentTab();
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        this.lam.dispatchDestroy(isFinishing());
    }
    
    
    /**
     * Update the visibility of the description TextView and the strong divider.
     * If no description of the Preset is available, set those elements invisible (gone).
     */
    private void updateDescriptionVisibility() {
        // Set up description of the Preset
        TextView descr = (TextView) findViewById(R.id.TextView_Preset_Description);
        View divider = findViewById(R.id.View_Divider_Strong);
        if (!this.preset.getDescription().equals("")) {
            descr.setText(this.preset.getDescription());
            descr.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
        } else {
            descr.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        }
    }
    
    
    /**
     * Set up all tabs
     */
    private void setupTabs() {
        /* Assigned Apps Tab */
        TabSpec assignedApps = this.mTabHost.newTabSpec("tab_assigned_apps");
        assignedApps.setIndicator(getString(R.string.assigned_apps));
        
        // Create an Intent to start the inner activity
        Intent intentAssignedApps = new Intent(this, PresetAppsTab.class);
        intentAssignedApps.putExtra(GUIConstants.PRESET_IDENTIFIER, this.preset.getLocalIdentifier());
        
        assignedApps.setContent(intentAssignedApps);
        this.mTabHost.addTab(assignedApps);
        
        // Change the preferred size of the Tab-header
        View tab1 = this.mTabHost.getTabWidget().getChildAt(0);
        LayoutParams lp = tab1.getLayoutParams();
        lp.width = LayoutParams.WRAP_CONTENT;
        tab1.setLayoutParams(lp);
        
        /* Assigned Privacy Settings Tab */
        TabSpec pss = this.mTabHost.newTabSpec("tab_assigned_pss");
        pss.setIndicator(getString(R.string.assigned_privacy_settings));
        
        // Create an Intent to start the inner activity
        Intent intentPss = new Intent(this, PresetPSsTab.class);
        intentPss.putExtra(GUIConstants.PRESET_IDENTIFIER, this.preset.getLocalIdentifier());
        
        pss.setContent(intentPss);
        this.mTabHost.addTab(pss);
        
        // Change the preferred size of the Tab-header
        View tab2 = this.mTabHost.getTabWidget().getChildAt(1);
        lp = tab2.getLayoutParams();
        lp.width = LayoutParams.WRAP_CONTENT;
        tab2.setLayoutParams(lp);
    }
    
}
