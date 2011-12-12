package de.unistuttgart.ipvs.pmp.gui.util;

import android.content.Intent;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;

public class GUITools {
    
    public static IApp handleIntent(Intent intent) {
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
        
        /* App should exists in the model */
        if (app == null) {
            throw new IllegalArgumentException("The given App (" + appIdentifier
                    + ") in the Intent does not exist in the model");
        }
        
        return app;
    }
}
