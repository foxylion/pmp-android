package de.unistuttgart.ipvs.pmp.gui.mockup;

import java.util.HashMap;

import de.unistuttgart.ipvs.pmp.model.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.ServiceFeature;

/**
 * 
 * @author Tobias Kuhn
 * 
 */
public class MockupServiceFeature extends ServiceFeature {
    
    public MockupServiceFeature(MockupApp app, String identifier, boolean available) {
        super(app, identifier);
        this.containsUnknownPrivacySettings = !available;
        this.privacySettingValues = new HashMap<PrivacySetting, String>();
    }
    
    
    public void addPS(MockupPrivacySetting ps, String reqValue) {
        this.privacySettingValues.put(ps, reqValue);
    }
    
    
    @Override
    public void setPersistenceProvider(ElementPersistenceProvider<? extends ModelElement> persistenceProvider) {
        super.setPersistenceProvider(null);
    }
    
}
