package de.unistuttgart.ipvs.pmp.gui.util.model.mockup;

import java.util.ArrayList;
import java.util.HashMap;

import de.unistuttgart.ipvs.pmp.model.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.missing.MissingPrivacySettingValue;
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
        this.missingPrivacySettings = new ArrayList<MissingPrivacySettingValue>();
        if (!available) {
            this.missingPrivacySettings.add(new MissingPrivacySettingValue("", "", ""));
        }
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
