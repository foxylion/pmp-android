package de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestLocationRessourceGroup;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;

/**
 * This implements the {@link IPrivacyLevel} interface for testing only
 * 
 * @author Thorsten Berberich
 * 
 */
public class TestPrivacyLevelBadenWuerttemberg implements IPrivacyLevel {
    
    String identifier = "de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestLocationRessourceGroup.TestPrivacyLevelBadenWuerttemberg";
    String name = "TestPrivacyLevel BW";
    String description = "For testing ONLY";

    @Override
    public String getIdentifier() {
	return identifier;
    }

    @Override
    public IResourceGroup getResourceGroup() {
	return  new TestLocationResourceGroup();
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public String getDescription() {
	return description;
    }

    @Override
    public String getValue() {
	return "BadenWuerttemberg";
    }

    @Override
    public String getHumanReadableValue(String value) throws RemoteException {
	return "Nur in BW";
    }

    @Override
    public boolean permits(String reference, String value)
	    throws RemoteException {
	return new TestPrivacyLevelDeutschland().permits(reference, value);
    }

}
