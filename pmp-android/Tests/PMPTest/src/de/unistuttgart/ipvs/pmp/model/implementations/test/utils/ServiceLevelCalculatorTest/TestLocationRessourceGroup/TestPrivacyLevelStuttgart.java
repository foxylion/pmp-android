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
public class TestPrivacyLevelStuttgart implements IPrivacyLevel {

    String identifier = "de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestLocationRessourceGroup.TestPrivacyLevelStuttgart";
    String name = "TestPrivacyLevel Stutt";
    String description = "For testing ONLY";

    @Override
    public String getIdentifier() {
	return identifier;
    }

    @Override
    public IResourceGroup getResourceGroup() {
	return new TestLocationResourceGroup();
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
	return "Stuttgart";
    }

    @Override
    public String getHumanReadableValue(String value) throws RemoteException {
	return "Nur in Stuttgart";
    }

    @Override
    public boolean permits(String reference, String value)
	    throws RemoteException {
	return new TestPrivacyLevelDeutschland().permits(reference, value);
    }

}
