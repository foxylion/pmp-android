package de.unistuttgart.ipvs.pmp.gui.activity;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.tab.AppDetailsTab;
import de.unistuttgart.ipvs.pmp.gui.tab.AppPresetsTab;
import de.unistuttgart.ipvs.pmp.gui.tab.AppServiceFeaturesTab;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.gui.util.GUITools;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;

/**
 * The {@link AppActivity} displays a at PMP registered App.
 * For Details, Service Features and Presets is a tab available to display it.
 * 
 * @author Jakob Jarosch
 */
public class AppActivity extends Activity {
    
    /**
     * The reference to the real App in the model.
     */
    private IApp app;
    
    /**
     * Activity manager is used to setup the {@link TabHost}.
     */
    private LocalActivityManager lam;
    
    /**
     * {@link TabHost} for the displayed tabs in the GUI.
     */
    private TabHost mTabHost;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_app);
        
        this.lam = new LocalActivityManager(this, true);
        this.lam.dispatchCreate(savedInstanceState);
        
        this.mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        this.mTabHost.setup(this.lam);
        
        this.app = GUITools.handleAppIntent(getIntent());
        
        setupTabs();
        
        BasicTitleView title = (BasicTitleView) findViewById(R.id.activity_title);
        
        title.setTitle(this.app.getName());
        title.setIcon(this.app.getIcon());
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        this.lam.dispatchResume();
    }
    
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    
    
    @Override
    protected void onPause() {
        super.onPause();
        
        this.lam.dispatchPause(isFinishing());
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        this.lam.dispatchDestroy(isFinishing());
    }
    
    
    private void setupTabs() {
        /* Details Tab */
        TabSpec details = this.mTabHost.newTabSpec("tab_detail");
        details.setIndicator(getResources().getString(R.string.details));
        
        // Create an Intent to start the inner activity
        Intent intentDetails = new Intent(this, AppDetailsTab.class);
        intentDetails.putExtra(GUIConstants.APP_IDENTIFIER, this.app.getIdentifier());
        
        details.setContent(intentDetails);
        this.mTabHost.addTab(details);
        
        // Change the preferred size of the Tab-header
        View tab1 = this.mTabHost.getTabWidget().getChildAt(0);
        LayoutParams lp = tab1.getLayoutParams();
        lp.width = LayoutParams.WRAP_CONTENT;
        tab1.setLayoutParams(lp);
        
        /* Service Features Tab */
        TabSpec sfs = this.mTabHost.newTabSpec("tab_sf");
        sfs.setIndicator(getResources().getString(R.string.service_features));
        
        // Create an Intent to start the inner activity
        Intent intentSfs = new Intent(this, AppServiceFeaturesTab.class);
        intentSfs.putExtra(GUIConstants.APP_IDENTIFIER, this.app.getIdentifier());
        
        sfs.setContent(intentSfs);
        this.mTabHost.addTab(sfs);
        
        // Change the preferred size of the Tab-header
        View tab2 = this.mTabHost.getTabWidget().getChildAt(1);
        lp = tab2.getLayoutParams();
        lp.width = LayoutParams.WRAP_CONTENT;
        tab2.setLayoutParams(lp);
        
        /* Presets Tab */
        TabSpec presets = this.mTabHost.newTabSpec("tab_preset");
        presets.setIndicator(getResources().getString(R.string.presets));
        
        // Create an Intent to start the inner activity
        Intent intentPresets = new Intent(this, AppPresetsTab.class);
        intentPresets.putExtra(GUIConstants.APP_IDENTIFIER, this.app.getIdentifier());
        
        presets.setContent(intentPresets);
        this.mTabHost.addTab(presets);
        
        // Change the preferred size of the Tab-header
        View tab3 = this.mTabHost.getTabWidget().getChildAt(2);
        lp = tab3.getLayoutParams();
        lp.width = LayoutParams.WRAP_CONTENT;
        tab3.setLayoutParams(lp);
    }
    
}
