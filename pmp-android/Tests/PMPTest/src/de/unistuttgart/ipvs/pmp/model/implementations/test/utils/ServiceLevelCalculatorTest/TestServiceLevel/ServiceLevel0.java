package de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestServiceLevel;

import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

public class ServiceLevel0 implements IServiceLevel {
    @Override
    public boolean isAvailable() {
	return true;
    }

    @Override
    public IPrivacyLevel[] getPrivacyLevels() {
	return new IPrivacyLevel[0];
    }

    @Override
    public String getName() {
	return "null level";
    }

    @Override
    public int getLevel() {
	return 0;
    }

    @Override
    public String getDescription() {
	return "Null level that can do nothing";
    }

}
