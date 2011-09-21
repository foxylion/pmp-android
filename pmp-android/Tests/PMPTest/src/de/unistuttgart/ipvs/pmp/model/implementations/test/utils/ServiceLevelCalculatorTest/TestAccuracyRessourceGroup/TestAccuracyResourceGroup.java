package de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestAccuracyRessourceGroup;

import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;

public class TestAccuracyResourceGroup implements IResourceGroup {
    
    private String identifier = "de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestAccuracyRessourceGroup";
    
    
    @Override
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    @Override
    public String getName() {
        return null;
    }
    
    
    @Override
    public String getDescription() {
        return null;
    }
    
    
    @Override
    public IPrivacyLevel[] getPrivacyLevels() {
        return null;
    }
    
    
    @Override
    public IPrivacyLevel getPrivacyLevel(String privacyLevelIdentifier) {
        return null;
    }
    
    
    @Override
    public IApp[] getAllAppsUsingThisResourceGroup() {
        return null;
    }
    
}
