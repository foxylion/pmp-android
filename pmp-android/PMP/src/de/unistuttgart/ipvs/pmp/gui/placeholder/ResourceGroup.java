package de.unistuttgart.ipvs.pmp.gui.placeholder;

import android.graphics.drawable.Drawable;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;

public class ResourceGroup implements IResourceGroup {
    
    private String identifier;
    private String name;
    private String description;
    private Drawable icon;
    
    
    public ResourceGroup(String identifier, String name, String description, Drawable icon) {
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.icon = icon;
    }
    
    
    @Override
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    @Override
    public String getName() {
        return this.name;
    }
    
    
    @Override
    public String getDescription() {
        return this.description;
    }
    
    
    @Override
    public Drawable getIcon() {
        return this.icon;
    }
    
    
    @Override
    public IPrivacySetting[] getPrivacySettings() {
        return null;
    }
    
    
    @Override
    public IPrivacySetting getPrivacySetting(String privacySettingIdentifier) {
        return null;
    }
    
    
    @Override
    public IBinder getResource(String appPackage, String resource) {
        return null;
    }
    
}
