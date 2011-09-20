package de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestPresets;

import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestAccuracyRessourceGroup.TestAccuracyPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestLocationRessourceGroup.TestPrivacyLevelBadenWuerttemberg;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;

public class TestPreset4 implements IPreset{

    @Override
    public String getName() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public PMPComponentType getType() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getIdentifier() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getDescription() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public IPrivacyLevel[] getUsedPrivacyLevels() {
	IPrivacyLevel[] pls = new IPrivacyLevel[2];
	pls[0] = new TestPrivacyLevelBadenWuerttemberg();
	pls[1] = new TestAccuracyPrivacyLevel("600");
	return pls;
    }

    @Override
    public IApp[] getAssignedApps() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean isAppAssigned(IApp app) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public void addApp(IApp app) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void addApp(IApp app, boolean hidden) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void removeApp(IApp app) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void removeApp(IApp app, boolean hidden) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void setPrivacyLevel(IPrivacyLevel privacyLevel) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void setPrivacyLevel(IPrivacyLevel privacyLevel, boolean hidden) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void removePrivacyLevel(IPrivacyLevel privacyLevel) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void removePrivacyLevel(IPrivacyLevel privacyLevel, boolean hidden) {
	// TODO Auto-generated method stub
	
    }

}
