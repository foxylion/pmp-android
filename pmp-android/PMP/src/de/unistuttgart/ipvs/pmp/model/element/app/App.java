package de.unistuttgart.ipvs.pmp.model.element.app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.graphics.drawable.Drawable;

import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.Model;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.preset.Preset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.ServiceFeature;

/**
 * @see IApp
 * @author Tobias Kuhn
 * 
 */
public class App extends ModelElement implements IApp {
    
    /**
     * localized values
     */
    protected String name;
    protected String description;
    
    /**
     * internal data & links
     */
    protected List<ServiceFeature> serviceFeatures;
    protected List<Preset> assignedPresets;
    
    
    /* organizational */
    
    public App(String identifier) {
        super(identifier);
    }
    
    
    /* interface */
    
    @Override
    public String getName() {
        checkCached();
        return this.name;
    }
    
    
    @Override
    public String getDescription() {
        checkCached();
        return this.description;
    }
    
    
    @Override
    public IServiceFeature[] getServiceFeatures() {
        checkCached();
        return this.serviceFeatures.toArray(new IServiceFeature[0]);
    }
    
    
    @Override
    public IServiceFeature getServiceFeature(String level) {
        checkCached();
        //return this.serviceLevels.get(level);
        // TODO
        return null;
    }
    
    
    @Override
    public IServiceFeature[] getActiveServiceFeatures() {
        checkCached();
        //return this.serviceLevels.get(this.activeServiceLevel);
        // TODO
        return null;
    }
    
    
    @Override
    public void verifyServiceFeatures() {
        // TODO also here
        
    }
    
    
    @Override
    public IPreset[] getAssignedPresets() {
        return this.assignedPresets.toArray(new IPreset[0]);
    }


    @Override
    public Drawable getIcon() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
