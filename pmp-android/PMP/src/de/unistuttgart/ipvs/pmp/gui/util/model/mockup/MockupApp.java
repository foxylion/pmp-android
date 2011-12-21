package de.unistuttgart.ipvs.pmp.gui.util.model.mockup;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.drawable.Drawable;
import de.unistuttgart.ipvs.pmp.model.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.App;
import de.unistuttgart.ipvs.pmp.model.element.preset.Preset;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.ServiceFeature;
import de.unistuttgart.ipvs.pmp.util.xml.app.AppInformationSet;

/**
 * 
 * @author Tobias Kuhn
 * 
 */
public class MockupApp extends App {
    
    private Drawable icon;
    
    
    public MockupApp(String appPackage, Drawable icon, AppInformationSet ais) {
        super(appPackage);
        
        this.icon = icon;
        this.ais = ais;
        this.serviceFeatures = new HashMap<String, ServiceFeature>();
        this.assignedPresets = new ArrayList<Preset>();
    }
    
    
    @Override
    public void setPersistenceProvider(ElementPersistenceProvider<? extends ModelElement> persistenceProvider) {
        super.setPersistenceProvider(null);
    }
    
    
    @Override
    public Drawable getIcon() {
        return this.icon;
    }
    
    
    public void addSF(String name, MockupServiceFeature sf) {
        this.serviceFeatures.put(name, sf);
    }
    
    
    @Override
    public void verifyServiceFeatures() {
    }
    
}
