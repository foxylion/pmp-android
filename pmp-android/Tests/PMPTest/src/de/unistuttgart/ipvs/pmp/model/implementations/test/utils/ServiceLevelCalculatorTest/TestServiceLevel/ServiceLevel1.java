package de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestServiceLevel;

import de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestAccuracyRessourceGroup.TestAccuracyPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

public class ServiceLevel1 implements IServiceLevel {
    @Override
    public boolean isAvailable() {
	return true;
    }

    @Override
    public IPrivacyLevel[] getPrivacyLevels() {
	IPrivacyLevel[] pls = new IPrivacyLevel[1];
	pls[0] = new TestAccuracyPrivacyLevel("50");
	return pls;
    }

    @Override
    public String getName() {
	return "Level 1";
    }

    @Override
    public int getLevel() {
	return 1;
    }

    @Override
    public String getDescription() {
	return "The first service Level";
    }
}
