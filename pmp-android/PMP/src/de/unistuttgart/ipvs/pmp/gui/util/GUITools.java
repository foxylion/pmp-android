package de.unistuttgart.ipvs.pmp.gui.util;

import android.app.Activity;
import android.content.Intent;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.gui.activity.AppActivity;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;

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
    
    
    public static String handleIntentAction(Intent intent) {
        /* Intent should never be null */
        if (intent == null) {
            throw new IllegalArgumentException("Intent can't be null");
        }
        
        String action = intent.getExtras().getString(GUIConstants.ACTIVITY_ACTION);
        
        /* App Identifier should never be null */
        if (action == null) {
            throw new IllegalArgumentException("Intent should have the GUIConstants.ACTIVITY_ACTION packed with it");
        }
        
        return action;
    }
    
    
    /**
     * Opens the App details of the given App.
     * 
     * @param app
     *            the App which should be opened
     */
    public static Intent createAppActivityIntent(IApp app) {
        Intent intent = new Intent(PMPApplication.getContext(), AppActivity.class);
        intent.putExtra(GUIConstants.APP_IDENTIFIER, app.getIdentifier());
        return intent;
    }
    
    /**
     * Starts the given {@link Intent}.
     * 
     * @param intent {@link Intent} which should be started.
     */
    public static void startIntent(Intent intent) {
        PMPApplication.getContext().startActivity(intent);
    }
}
