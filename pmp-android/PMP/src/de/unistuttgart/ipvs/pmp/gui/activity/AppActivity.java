package de.unistuttgart.ipvs.pmp.gui.activity;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.placeholder.App;
import de.unistuttgart.ipvs.pmp.gui.tab.AppDetailsTab;
import de.unistuttgart.ipvs.pmp.gui.tab.AppPresetsTab;
import de.unistuttgart.ipvs.pmp.gui.tab.AppServiceFeaturesTab;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;

public class AppActivity extends Activity {
    
    private App app;
    
    private LocalActivityManager lam;
    
    private TabHost mTabHost;
    
    private int currentTab = 0;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_app);
        
        this.lam = new LocalActivityManager(this, true);
        this.lam.dispatchCreate(savedInstanceState);
        
        this.mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        this.mTabHost.setup(this.lam);
        
        //app = handleIntent(getIntent());
        this.app = new App(
                "Barcode Scanner",
                "With the Barcode Scanner you can scan your products and get more informations about them. "
                        + "Especially you can find the best price for products. If you enable the Location Feature you "
                        + "can also get the direction to the next store where the item is available. Facebook Feature "
                        + "allows you to share the product with your friends.", getResources().getDrawable(
                        R.drawable.test_icon1));
        
        setupTabs();
        
        BasicTitleView title = (BasicTitleView) findViewById(R.id.activity_title);
        
        title.setTitle(this.app.getName());
        title.setIcon(this.app.getIcon());
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        this.lam.dispatchResume();
        
        this.mTabHost.setCurrentTab(this.currentTab);
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
    
    
    private void setupTabs() {
        /* Details Tab */
        TabSpec details = this.mTabHost.newTabSpec("tab_details");
        details.setIndicator("Details");
        
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
        TabSpec sfs = this.mTabHost.newTabSpec("tab_sfs");
        sfs.setIndicator("Service Features");
        
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
        TabSpec presets = this.mTabHost.newTabSpec("tab_details");
        presets.setIndicator("Presets");
        
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
