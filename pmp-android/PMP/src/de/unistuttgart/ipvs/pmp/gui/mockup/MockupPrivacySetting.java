package de.unistuttgart.ipvs.pmp.gui.mockup;

import de.unistuttgart.ipvs.pmp.model.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.AbstractPrivacySetting;

/**
 * 
 * @author Tobias Kuhn
 * 
 */
public class MockupPrivacySetting extends PrivacySetting {
    
    public MockupPrivacySetting(MockupRG resourceGroup, String identifier, AbstractPrivacySetting<?> link) {
        super(resourceGroup, identifier);
        this.link = link;
    }
    
    
    @Override
    public void setPersistenceProvider(ElementPersistenceProvider<? extends ModelElement> persistenceProvider) {
        super.setPersistenceProvider(null);
    }
    
}
