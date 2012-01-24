package de.unistuttgart.ipvs.pmp.gui.util;

import android.app.Activity;
import android.content.Intent;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.gui.app.ActivityApp;
import de.unistuttgart.ipvs.pmp.gui.resourcegroup.ActivityRGs;
import de.unistuttgart.ipvs.pmp.gui.resourcegroup.TabRGsAvailable;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.util.StringUtil;

public class GUITools {
    
    /**
     * Handles an intent which is called when a specific App should be referenced in the {@link Activity}.
     * 
     * @param intent
     *            which invoked the {@link Activity}
     * @return the corresponding {@link IApp}
     */
    public static IApp handleAppIntent(Intent intent) {
        /* Intent should never be null */
        if (intent == null) {
            throw new IllegalArgumentException("Intent can't be null");
        }
        
        String appIdentifier = intent.getExtras().getString(GUIConstants.APP_IDENTIFIER);
        
        /* App Identifier should never be null */
        if (appIdentifier == null) {
            throw new IllegalArgumentException("Intent should have the GUIConstants.APP_IDENTIFIER packed with it");
        }
        
        IApp app = ModelProxy.get().getApp(appIdentifier);
        
        /* App does not exists in the model */
        if (app == null) {
            throw new IllegalArgumentException("The given App (" + appIdentifier
                    + ") in the Intent does not exist in the model");
        }
        
        return app;
    }
    
    
    /**
     * @return Returns the action as a String, or an empty String if no action is given.
     */
    public static String handleIntentAction(Intent intent) {
        /* Intent should never be null */
        if (intent == null) {
            throw new IllegalArgumentException("Intent can't be null");
        }
        
        String action = null;
        
        if (intent.getExtras() != null) {
            action = intent.getExtras().getString(GUIConstants.ACTIVITY_ACTION);
        }
        
        if (action == null) {
            action = "";
        }
        
        return action;
    }
    
    
    /**
     * @return Returns the RGs filter as a String, or an empty String if no filter is given.
     */
    public static String getAvailableRGsFilter(Intent intent) {
        /* Intent should never be null */
        if (intent == null) {
            throw new IllegalArgumentException("Intent can't be null");
        }
        
        String rgsFilter = null;
        
        if (intent.getExtras() != null) {
            rgsFilter = intent.getExtras().getString(GUIConstants.RGS_FILTER);
        }
        
        if (rgsFilter == null) {
            rgsFilter = "";
        }
        
        return rgsFilter;
    }
    
    
    /**
     * Opens the App details of the given App.
     * 
     * @param app
     *            the App which should be opened
     */
    public static Intent createAppActivityIntent(IApp app) {
        Intent intent = new Intent(PMPApplication.getContext(), ActivityApp.class);
        intent.putExtra(GUIConstants.APP_IDENTIFIER, app.getIdentifier());
        return intent;
    }
    
    
    /**
     * Opens the {@link TabRGsAvailable} tab and views all the listed Resourcegroups.
     * 
     * @param filteredRGIdentifiers
     *            Only the given Resourcegroups will be displayed.
     */
    public static Intent createFilterAvailableRGsIntent(String[] filteredRGIdentifiers) {
        Intent intent = new Intent(PMPApplication.getContext(), ActivityRGs.class);
        intent.putExtra(GUIConstants.ACTIVITY_ACTION, GUIConstants.FILTER_AVAILABLE_RGS);
        intent.putExtra(GUIConstants.RGS_FILTER, StringUtil.join(",", filteredRGIdentifiers));
        
        return intent;
    }
    
    
    /**
     * Starts the given {@link Intent}.
     * 
     * @param intent
     *            {@link Intent} which should be started.
     */
    public static void startIntent(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PMPApplication.getContext().startActivity(intent);
    }
}
