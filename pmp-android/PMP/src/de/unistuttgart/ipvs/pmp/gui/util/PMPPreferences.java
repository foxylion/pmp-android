package de.unistuttgart.ipvs.pmp.gui.util;

import android.content.Context;
import android.content.SharedPreferences;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.simple.SimpleModel;

/**
 * The {@link PMPPreferences} do provide informations like the current state of the expert mode.
 * 
 * @author Jakob Jarosch
 */
public class PMPPreferences {
    
    private static final String PREFERENCES_NAME = "PMPSettings";
    
    private static final String KEY_EXPERT_MODE = "ExpertMode";
    
    private SharedPreferences settings;
    
    /* singleton pattern */
    private static PMPPreferences instance = null;
    
    
    private PMPPreferences() {
        this.settings = PMPApplication.getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
    
    
    /**
     * @return Returns an instance of the {@link PMPPreferences}.
     */
    public static PMPPreferences getInstance() {
        if (PMPPreferences.instance == null) {
            PMPPreferences.instance = new PMPPreferences();
        }
        
        return PMPPreferences.instance;
    }
    
    
    /* End of singleton pattern */
    
    /**
     * @return The current state of the expert mode.
     */
    public boolean isExpertMode() {
        return this.settings.getBoolean(KEY_EXPERT_MODE, false);
    }
    
    
    /**
     * Sets the new state of the expert mode.
     * This does also convert the Model from a normal model to a simple one if new state is set to false. This is an
     * irreversible action.
     * 
     * @param mode
     */
    public void setExpertMode(boolean mode) {
        SharedPreferences.Editor editor = this.settings.edit();
        editor.putBoolean(KEY_EXPERT_MODE, mode);
        editor.commit();
        
        if (!mode) {
            SimpleModel.getInstance().convertExpertToSimple(ModelProxy.get());
        }
    }
}
