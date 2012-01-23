package de.unistuttgart.ipvs.pmp.gui.resourcegroup;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.ActivityKillReceiver;

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
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgs);
        
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        
        TabHost.TabSpec spec;
        Intent intent;
        
        intent = new Intent().setClass(this, TabRGsInstalled.class);
        spec = tabHost.newTabSpec("installed").setIndicator(getString(R.string.installed)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, TabRGsAvailable.class);
        spec = tabHost.newTabSpec("available").setIndicator(getString(R.string.available)).setContent(intent);
        tabHost.addTab(spec);
        
        /* Initiating the ActivityKillReceiver. */
        this.akr = new ActivityKillReceiver(this);
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        unregisterReceiver(this.akr);
    }
}
