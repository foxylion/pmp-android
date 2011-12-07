package de.unistuttgart.ipvs.pmp.gui.placeholder;

import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;

public class ServiceFeature implements IServiceFeature {
    
    private String name;
    private String description;
    private boolean active;
    private boolean available;
    
    
    public ServiceFeature(String name, String description, boolean active, boolean available) {
        this.name = name;
        this.description = description;
        this.active = active;
        this.available = available;
    }
    
    
    @Override
    public String getIdentifier() {
        return "de.unistuttgart.ipvs.pmp.apps.test::testSL";
    }
    
    
    @Override
    public String getLocalIdentifier() {
        return "testSL";
    }
    
    
    @Override
    public IApp getApp() {
        return null;
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
    public IPrivacySetting[] getRequiredPrivacySettings() {
        return SampleModel.privacySettings.toArray(new IPrivacySetting[SampleModel.privacySettings.size()]);
    }
    
    
    @Override
    public boolean isActive() {
        return this.active;
    }
    
    
    @Override
    public String getRequiredPrivacySettingValue(IPrivacySetting privacySetting) {
        return "true";
    }
    
    
    @Override
    public boolean isAvailable() {
        return this.available;
    }
    
}
