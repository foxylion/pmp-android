package de.unistuttgart.ipvs.pmp.gui.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.tab.RGsAvailableTab;
import de.unistuttgart.ipvs.pmp.gui.tab.RGsInstalledTab;

public class RGsActivity extends TabActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgs);
        
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        
        TabHost.TabSpec spec; // Resusable TabSpec for each tab
        Intent intent; // Reusable Intent for each tab
        
        // Do the same for the other tabs
        intent = new Intent().setClass(this, RGsAvailableTab.class);
        spec = tabHost.newTabSpec("available").setIndicator(getString(R.string.rgs_available))
                .setContent(intent);
        tabHost.addTab(spec);
        tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = 40;        
        
        intent = new Intent().setClass(this, RGsInstalledTab.class);
        spec = tabHost.newTabSpec("installed").setIndicator(getString(R.string.rgs_installed))
                .setContent(intent);
        tabHost.addTab(spec);
        tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = 40;
        tabHost.setCurrentTab(0);
    }
}
