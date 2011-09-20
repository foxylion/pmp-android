package de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestServiceLevel;

import de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestAccuracyRessourceGroup.TestAccuracyPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestLocationRessourceGroup.LocationEnum;
import de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestLocationRessourceGroup.TestLocationPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

public class ServiceLevel2 implements IServiceLevel {
    
    @Override
    public int getLevel() {
        return 2;
    }
    
    
    @Override
    public String getName() {
        return "Level 2";
    }
    
    
    @Override
    public String getDescription() {
        return "Second service level";
    }
    
    
    @Override
    public IPrivacyLevel[] getPrivacyLevels() {
        IPrivacyLevel[] pls = new IPrivacyLevel[2];
        pls[0] = new TestLocationPrivacyLevel(LocationEnum.STUTTGART.toString());
        pls[1] = new TestAccuracyPrivacyLevel("50");
        return pls;
    }
    
    
    @Override
    public boolean isAvailable() {
        return true;
    }
    
}
