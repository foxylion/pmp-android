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
public class TestPrivacyLevelDeutschland implements IPrivacyLevel {

    String identifier = "de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestLocationRessourceGroup.TestPrivacyLevelDeutschland";
    String name = "TestPrivacyLevel DE";
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
	return "Deutschland";
    }

    @Override
    public String getHumanReadableValue(String value) throws RemoteException {
	return "Nur in Deutschland";
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
	
	if (value.equals("Deutschland")){
	    return true;
	}
	
	if (value.equals("BadenWuerttemberg") && reference.equals("Stuttgart")){
	    return true;
	}
	
	if (value.equals("BadenWuerttemberg") && reference.equals("BadenWuerttemberg")){
	    return true;
	}
	
	if (value.equals("Stuttgart") && reference.equals("Stuttgart")){
	    return true;
	}
	return false;
    }

}
