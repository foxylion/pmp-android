package de.unistuttgart.ipvs.pmp.gui.placeholder;

import android.graphics.drawable.Drawable;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;


public class App implements IApp {
    
    private String name;
    private String description;
    private Drawable icon;

    public App(String name, String description, Drawable icon) {
        this.name = name;
        this.description = description;
        this.icon = icon;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    public Drawable getIcon() {
        return icon;
    }

    @Override
    public String getIdentifier() {
        return "de.unistuttgart.ipvs.pmp.test";
    }

    @Override
    public IServiceFeature[] getServiceFeatures() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IServiceFeature getServiceFeature(String serviceFeatureIdentifier) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IServiceFeature[] getActiveServiceFeatures() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void verifyServiceFeatures() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public IPreset[] getAssignedPresets() {
        // TODO Auto-generated method stub
        return null;
    }
}
