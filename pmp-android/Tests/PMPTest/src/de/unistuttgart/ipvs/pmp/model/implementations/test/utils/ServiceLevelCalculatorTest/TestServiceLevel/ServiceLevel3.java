package de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestServiceLevel;

import de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestAccuracyRessourceGroup.TestAccuracyPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestLocationRessourceGroup.LocationEnum;
import de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestLocationRessourceGroup.TestLocationPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

public class ServiceLevel3 implements IServiceLevel {
    
    @Override
    public int getLevel() {
        return 3;
    }
    
    
    @Override
    public String getName() {
        return "Level 3";
    }
    
    
    @Override
    public String getDescription() {
        return "Third service level";
    }
    
    
    @Override
    public IPrivacyLevel[] getPrivacyLevels() {
        IPrivacyLevel[] pls = new IPrivacyLevel[2];
        pls[0] = new TestLocationPrivacyLevel(LocationEnum.BADEN_WUERTTEMBERG.toString());
        pls[1] = new TestAccuracyPrivacyLevel("50");
        return pls;
    }
    
    
    @Override
    public boolean isAvailable() {
        return true;
    }
    
}
