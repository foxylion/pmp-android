package de.unistuttgart.ipvs.pmp.gui.settings;

import android.graphics.drawable.Drawable;
import de.unistuttgart.ipvs.pmp.gui.util.PMPPreferences;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class Setting {
    
    private SettingIdentifier identifier;
    private String name;
    private String description;
    private Drawable icon;
    
    
    public Setting(SettingIdentifier identifier, String name, String description, Drawable icon) {
        this.setIdentifier(identifier);
        this.setName(name);
        this.setDescription(description);
        this.setIcon(icon);
    }
    
    
    public SettingIdentifier getIdentifier() {
        return identifier;
    }
    
    
    public void setIdentifier(SettingIdentifier identifier) {
        this.identifier = identifier;
    }
    
    
    public String getName() {
        return name;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public String getDescription() {
        return description;
    }
    
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    
    public Drawable getIcon() {
        return icon;
    }
    
    
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
    
    
    public boolean isEnabled() {
        switch (identifier) {
            case EXPERT_MODE:
                return PMPPreferences.getInstance().isExpertMode();
            case PRESET_TRASH_BIN_VISIBILITY:
                return PMPPreferences.getInstance().isPresetTrashBinVisible();
        }
        return false;
    }
    
    
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
