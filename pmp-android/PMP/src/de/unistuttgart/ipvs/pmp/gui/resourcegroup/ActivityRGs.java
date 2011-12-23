package de.unistuttgart.ipvs.pmp.gui.resourcegroup;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import de.unistuttgart.ipvs.pmp.R;

/**
 * The {@link ActivityRGs} contains two tabs with the installed and the available Resourcegroups.
 * 
 * @author Jakob Jarosch
 */
public class ActivityRGs extends TabActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgs);
        
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        
        TabHost.TabSpec spec;
        Intent intent;
        
        intent = new Intent().setClass(this, TabRGsInstalled.class);
        spec = tabHost.newTabSpec("installed").setIndicator(getString(R.string.rg_installed)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, TabRGsAvailable.class);
        spec = tabHost.newTabSpec("available").setIndicator(getString(R.string.rg_available)).setContent(intent);
        tabHost.addTab(spec);
    }
}
