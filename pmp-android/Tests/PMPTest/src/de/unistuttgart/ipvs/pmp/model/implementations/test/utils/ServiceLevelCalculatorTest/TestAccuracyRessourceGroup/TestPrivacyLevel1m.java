package de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestAccuracyRessourceGroup;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;

public class TestPrivacyLevel1m implements IPrivacyLevel{

    private String identifier = "de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestAccuracyRessourceGroup.SericeLevel1m";
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
	return "1";
    }

    @Override
    public String getHumanReadableValue(String value) throws RemoteException {
	return "Accuracy one meter";
    }

    @Override
    public boolean permits(String reference, String value)
	    throws RemoteException {
	return new TestPrivacyLevel600m().permits(reference, value);
    }
    

}
