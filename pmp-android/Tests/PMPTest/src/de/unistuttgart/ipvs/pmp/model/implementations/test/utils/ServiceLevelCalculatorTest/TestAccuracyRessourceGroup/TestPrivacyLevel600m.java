package de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestAccuracyRessourceGroup;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;

public class TestPrivacyLevel600m implements IPrivacyLevel {

    private String identifier = "de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestAccuracyRessourceGroup.SericeLevel600m";
    private String name = "Accuracy 1m";
    private String description = "For testing ONLY";

    @Override
    public String getIdentifier() {
	return identifier;
    }

    @Override
    public IResourceGroup getResourceGroup() {
	return new TestAccuracyResourceGroup();
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
	return "600";
    }

    @Override
    public String getHumanReadableValue(String value) throws RemoteException {
	return "Accuracy six hundred meter";
    }

    @Override
    public boolean permits(String reference, String value)
	    throws RemoteException {
	
	if (value != null && reference == null){
	    return true;
	}
	if (value == null && reference == null){
	    return true;
	}
	if (value == null && reference != null){
	    return false;
	}
	
	int ref = Integer.valueOf(reference);
	int val = Integer.valueOf(value);

	if (val >= ref) {
	    return true;
	}
	return false;
    }

}
