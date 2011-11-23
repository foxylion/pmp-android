package de.unistuttgart.ipvs.pmp.gui.activity;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.placeholder.App;
import de.unistuttgart.ipvs.pmp.gui.tab.AppDetailsTab;
import de.unistuttgart.ipvs.pmp.gui.tab.AppPresetsTab;
import de.unistuttgart.ipvs.pmp.gui.tab.AppServiceFeaturesTab;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;
import de.unistuttgart.ipvs.pmp.model.Model;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class AppActivity extends Activity {
    
    private App app;
    
    private LocalActivityManager lam;
    
    private TabHost mTabHost;
    
    private int currentTab = 0;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.pmp_app);
        
        lam = new LocalActivityManager(this, true);
        lam.dispatchCreate(savedInstanceState);
        
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(lam);
        
        //app = handleIntent(getIntent());
        app = new App(
                "Barcode Scanner",
                "With the Barcode Scanner you can scan your products and get more informations about them. "
                        + "Especially you can find the best price for products. If you enable the Location Feature you "
                        + "can also get the direction to the next store where the item is available. Facebook Feature "
                        + "allows you to share the product with your friends.", BitmapFactory.decodeResource(
                        getResources(), R.drawable.test_icon1));
        
        setupTabs();
        
        BasicTitleView title = (BasicTitleView) findViewById(R.id.activity_title);
        
        title.setName(app.getName());
        title.setIcon(app.getIcon());
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        lam.dispatchResume();
        
        mTabHost.setCurrentTab(currentTab);
    }
    
    
    @Override
    protected void onPause() {
        super.onPause();
        
        lam.dispatchPause(isFinishing());
        
        currentTab = mTabHost.getCurrentTab();
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        lam.dispatchDestroy(isFinishing());
    }
    
    
    private IApp handleIntent(Intent intent) {
        /* Intent should never be null */
        if (intent == null) {
            throw new IllegalArgumentException("Intent can't be null");
        }
        
        String appIdentifier = intent.getExtras().getString(GUIConstants.APP_IDENTIFIER);
        
        /* App Identifier should never be null */
        if (appIdentifier == null) {
            throw new IllegalArgumentException("Intent should have the GUIConstants.APP_IDENTIFIER packed with it");
        }
        
        IApp app = Model.getInstance().getApp(appIdentifier);
        
        /* App should exists in the model */
        if (app == null) {
            throw new IllegalArgumentException("The given App (" + appIdentifier
                    + ") in the Intent does not exist in the model");
        }
        
        return app;
    }
    
    
    private void setupTabs() {
        /* Details Tab */
        TabSpec details = mTabHost.newTabSpec("tab_details");
        details.setIndicator("Details");
        
        // Create an Intent to start the inner activity
        Intent intentDetails = new Intent(this, AppDetailsTab.class);
        intentDetails.putExtra(GUIConstants.APP_IDENTIFIER, app.getIdentifier());
        
        details.setContent(intentDetails);
        mTabHost.addTab(details);
        
        // Change the preferred size of the Tab-header
        View tab1 = mTabHost.getTabWidget().getChildAt(0);
        LayoutParams lp = tab1.getLayoutParams();
        lp.width = LayoutParams.WRAP_CONTENT;
        tab1.setLayoutParams(lp);
        
        /* Presets Tab */
        TabSpec presets = mTabHost.newTabSpec("tab_details");
        presets.setIndicator("Presets");
        
        // Create an Intent to start the inner activity
        Intent intentPresets = new Intent(this, AppPresetsTab.class);
        intentPresets.putExtra(GUIConstants.APP_IDENTIFIER, app.getIdentifier());
        
        presets.setContent(intentPresets);
        mTabHost.addTab(presets);
        
        // Change the preferred size of the Tab-header
        View tab2 = mTabHost.getTabWidget().getChildAt(1);
        lp = tab2.getLayoutParams();
        lp.width = LayoutParams.WRAP_CONTENT;
        tab2.setLayoutParams(lp);
        
        /* Service Features Tab */
        TabSpec sfs = mTabHost.newTabSpec("tab_sfs");
        sfs.setIndicator("Service Features");
        
        // Create an Intent to start the inner activity
        Intent intentSfs = new Intent(this, AppServiceFeaturesTab.class);
        intentSfs.putExtra(GUIConstants.APP_IDENTIFIER, app.getIdentifier());
        
        sfs.setContent(intentSfs);
        mTabHost.addTab(sfs);
        
        // Change the preferred size of the Tab-header
        View tab3 = mTabHost.getTabWidget().getChildAt(2);
        lp = tab3.getLayoutParams();
        lp.width = LayoutParams.WRAP_CONTENT;
        tab3.setLayoutParams(lp);
    }
}
