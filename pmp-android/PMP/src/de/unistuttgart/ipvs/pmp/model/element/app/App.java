package de.unistuttgart.ipvs.pmp.model.element.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.Model;
import de.unistuttgart.ipvs.pmp.model.PersistenceConstants;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.preset.Preset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.ServiceFeature;
import de.unistuttgart.ipvs.pmp.util.xml.app.AppInformationSet;
import de.unistuttgart.ipvs.pmp.util.xml.app.AppInformationSetParser;

/**
 * @see IApp
 * @author Tobias Kuhn
 * 
 */
public class App extends ModelElement implements IApp {
    
    /**
     * localized values
     */
    protected AppInformationSet ais;
    
    /**
     * internal data & links
     */
    protected Map<String, ServiceFeature> serviceFeatures;
    protected List<Preset> assignedPresets;
    
    
    /* organizational */
    
    public App(String appPackage) {
        super(appPackage);
    }
    
    
    /* interface */
    
    @Override
    public String getName() {
        checkCached();
        String name = ais.getNames().get(Locale.getDefault());
        if (name == null) {
            name = ais.getNames().get(Locale.US);
        }
        return name;
    }
    
    
    @Override
    public String getDescription() {
        checkCached();
        String description = ais.getDescriptions().get(Locale.getDefault());
        if (description == null) {
            description = ais.getDescriptions().get(Locale.US);
        }
        return description;
    }
    
    
    @Override
    public IServiceFeature[] getServiceFeatures() {
        checkCached();
        return this.serviceFeatures.values().toArray(new IServiceFeature[0]);
    }
    
    @Override
    public IServiceFeature getServiceFeature(String serviceFeatureIdentifier) {
        checkCached();        
        return this.serviceFeatures.get(serviceFeatureIdentifier);
    }
    
    
    @Override
    public IServiceFeature[] getActiveServiceFeatures() {
        checkCached();
        List<ServiceFeature> actives = new ArrayList<ServiceFeature>();
        for (ServiceFeature sf : this.serviceFeatures.values()) {
            if (sf.isActive()) {
                actives.add(sf);
            }
        }
        return actives.toArray(new IServiceFeature[0]);
    }
    
    
    @Override
    public void verifyServiceFeatures() {
        checkCached();
        // TODO also here
        
    }
    
    
    @Override
    public IPreset[] getAssignedPresets() {
        checkCached();
        return this.assignedPresets.toArray(new IPreset[0]);
    }
    
    
    @Override
    public Drawable getIcon() {
        try {
            return PMPApplication.getContext().getPackageManager().getApplicationIcon(getIdentifier());
        } catch (NameNotFoundException e) {
            return null;
        }
    }


 
    
}
