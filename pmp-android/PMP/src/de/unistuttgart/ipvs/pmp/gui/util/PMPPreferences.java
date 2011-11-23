package de.unistuttgart.ipvs.pmp.gui.util;

import de.unistuttgart.ipvs.pmp.PMPApplication;
import android.content.Context;
import android.content.SharedPreferences;



public class PMPPreferences {
    
    private static final String PREFERENCES_NAME = "PMPSettings";
    
    private static final String KEY_EXPERT_MODE = "ExpertMode";
    
    private SharedPreferences settings;
    
    private static PMPPreferences instance = null;
    
    private PMPPreferences() {
        
        settings =  
                PMPApplication.getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
    
    
    public static PMPPreferences getInstanace() {
        if(PMPPreferences.instance == null) {
            PMPPreferences.instance = new PMPPreferences();
        }
        
        return PMPPreferences.instance;
    }
    
    public boolean isExpertMode() {
        return settings.getBoolean(KEY_EXPERT_MODE, false);
    }
    
    public void setExpertMode(boolean mode) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(KEY_EXPERT_MODE, mode);
        editor.commit();
    }
}
