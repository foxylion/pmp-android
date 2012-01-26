package de.unistuttgart.ipvs.pmp.gui.resourcegroup;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.ActivityKillReceiver;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.gui.util.GUITools;

/**
 * The {@link ActivityRGs} contains two tabs with the installed and the available Resourcegroups.
 * 
 * @author Jakob Jarosch
 */
public class ActivityRGs extends TabActivity {
    
    /**
     * The {@link ActivityKillReceiver}.
     */
    private ActivityKillReceiver akr;
    
    private TabHost tabHost;
    
    private String activeTab;
    
    /**
     * Tab-Tags
     */
    private static final String TAB_INSTALLED = "installed";
    private static final String TAB_AVAILABLE = "available";
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_rgs);
        
        this.activeTab = checkExtendedIntentActions();
        
        setupTabs();
        
        this.tabHost.setCurrentTabByTag(this.activeTab);
        
        /* Initiating the ActivityKillReceiver. */
        this.akr = new ActivityKillReceiver(this);
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        unregisterReceiver(this.akr);
    }
    
    
    /**
     * Checks if the Activity has been started with extended parameters like requested service features.
     */
    private void setupTabs() {
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec spec;
        Intent intent;
        
        intent = getIntent().setClass(this, TabRGsInstalled.class);
        spec = tabHost.newTabSpec(TAB_INSTALLED).setIndicator(getString(R.string.installed)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = getIntent().setClass(this, TabRGsAvailable.class);
        spec = tabHost.newTabSpec(TAB_AVAILABLE).setIndicator(getString(R.string.available)).setContent(intent);
        tabHost.addTab(spec);
    }
    
    
    private String checkExtendedIntentActions() {
        if (GUITools.handleIntentAction(getIntent()) != null
                && GUITools.handleIntentAction(getIntent()).equals(GUIConstants.FILTER_AVAILABLE_RGS)) {
            return TAB_AVAILABLE;
        }
        
        return TAB_INSTALLED;
    }
}
