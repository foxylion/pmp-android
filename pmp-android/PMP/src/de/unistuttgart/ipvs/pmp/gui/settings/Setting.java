package de.unistuttgart.ipvs.pmp.gui.settings;

import android.graphics.drawable.Drawable;
import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;

/**
 * Simple Setting
 * 
 * @author Marcus Vetter
 * 
 */
public class Setting {
    
    /**
     * Attributes of a Setting
     */
    private SettingIdentifier identifier;
    private String name;
    private String description;
    private Drawable icon;
    
    
    /**
     * Constructor to set the attributes
     * 
     * @param identifier
     *            the identifier of the Setting ({@link SettingIdentifier})
     * @param name
     *            name of the Setting
     * @param description
     *            description of the Setting
     * @param icon
     *            icon of the Setting (drawable)
     */
    public Setting(SettingIdentifier identifier, String name, String description, Drawable icon) {
        this.setIdentifier(identifier);
        this.setName(name);
        this.setDescription(description);
        this.setIcon(icon);
    }
    
    
    /**
     * @return Identifier of the Setting
     */
    public SettingIdentifier getIdentifier() {
        return identifier;
    }
    
    
    /**
     * @param identifier
     *            identifier of the Setting
     */
    public void setIdentifier(SettingIdentifier identifier) {
        this.identifier = identifier;
    }
    
    
    /**
     * @return name of the Setting
     */
    public String getName() {
        return name;
    }
    
    
    /**
     * @param name
     *            name of the Setting
     */
    public void setName(String name) {
        this.name = name;
    }
    
    
    /**
     * @return description of the Setting
     */
    public String getDescription() {
        return description;
    }
    
    
    /**
     * @param description
     *            description of the Setting
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    
    /**
     * @return icon of the Setting
     */
    public Drawable getIcon() {
        return icon;
    }
    
    
    /**
     * @param icon
     *            icon of the Setting
     */
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
    
    
    /**
     * Check, whether the Setting is enabled or not.
     * The value is returned from the PMPPreferences.
     * 
     * @return flag, whether the Setting is enabled or not.
     */
    public boolean isEnabled() {
        switch (identifier) {
            case EXPERT_MODE:
                return PMPPreferences.getInstance().isExpertMode();
            case PRESET_TRASH_BIN_VISIBILITY:
                return PMPPreferences.getInstance().isPresetTrashBinVisible();
        }
        return false;
    }
    
    
    /**
     * Set the Setting enabled or not. This flag will be stored to the PMPPreferences.
     * 
     * @param enabled
     *            flag
     */
    public void setEnabled(boolean enabled) {
        switch (identifier) {
            case EXPERT_MODE:
                PMPPreferences.getInstance().setExpertMode(enabled);
                break;
            case PRESET_TRASH_BIN_VISIBILITY:
                PMPPreferences.getInstance().setPresetTrashBinVisible(enabled);
                break;
        }
    }
    
}
