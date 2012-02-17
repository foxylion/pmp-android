package de.unistuttgart.ipvs.pmp.gui.util;

import android.content.Context;
import android.content.SharedPreferences;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.simple.SimpleModel;

/**
 * The {@link PMPPreferences} do provide informations like the current state of the expert mode or the visibility of the
 * preset trash bin.
 * 
 * @author Jakob Jarosch, Marcus Vetter
 */
public class PMPPreferences {
    
    private static final String PREFERENCES_NAME = "PMPSettings";
    
    /**
     * The keys
     */
    private static final String KEY_EXPERT_MODE = "ExpertMode";
    private static final String KEY_PRESET_TRASH_BIN_VISIBLE = "PresetTrashBin";
    private static final String KEY_LOGGING_GRANULARITY = "LoggingGranularity";
    
    private SharedPreferences settings;
    
    /* Singleton pattern */
    private static volatile PMPPreferences instance = null;
    
    
    private PMPPreferences() {
        this.settings = PMPApplication.getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
    
    
    /**
     * @return an instance of the {@link PMPPreferences}.
     */
    public static PMPPreferences getInstance() {
        if (PMPPreferences.instance == null) {
            PMPPreferences.instance = new PMPPreferences();
        }
        
        return PMPPreferences.instance;
    }
    
    
    /* End of Singleton pattern */
    
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
    
    
    /**
     * @return The current visibility of the preset trash bin
     */
    public boolean isPresetTrashBinVisible() {
        return this.settings.getBoolean(KEY_PRESET_TRASH_BIN_VISIBLE, true);
    }
    
    
    /**
     * Set the visibility of the preset trash bin
     * 
     * @param visible
     *            flag of the visibility
     */
    public void setPresetTrashBinVisible(boolean visible) {
        SharedPreferences.Editor editor = this.settings.edit();
        editor.putBoolean(KEY_PRESET_TRASH_BIN_VISIBLE, visible);
        editor.commit();
    }
    
    
    /**
     * @return the logging granularities that are active
     */
    public String[] getLoggingGranularity() {
        return this.settings.getString(KEY_LOGGING_GRANULARITY, "").split(",");
    }
    
    
    /**
     * Sets the logging granularities to granularities.
     * 
     * @param granularities
     */
    public void setLoggingGranularity(String[] granularities) {
        SharedPreferences.Editor editor = this.settings.edit();
        
        StringBuilder logGran = new StringBuilder();
        for (String gran : granularities) {
            logGran.append(gran);
            logGran.append(",");
        }
        
        editor.putString(KEY_LOGGING_GRANULARITY, logGran.substring(0, logGran.length() - 1));
        editor.commit();
    }
}
