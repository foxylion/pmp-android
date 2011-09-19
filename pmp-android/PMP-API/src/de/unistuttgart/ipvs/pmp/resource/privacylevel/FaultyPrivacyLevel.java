package de.unistuttgart.ipvs.pmp.resource.privacylevel;

/**
 * This privacy level only exists to be faulty for system testing.
 * 
 * @author Tobias Kuhn
 * 
 */
public class FaultyPrivacyLevel extends PrivacyLevel<IllegalArgumentException> {
    
    private boolean faultyNameDesc;
    
    public FaultyPrivacyLevel(boolean faultyNameDesc) {
	this.faultyNameDesc = faultyNameDesc;
    }

    @Override
    public String getName(String locale) {
	return (this.faultyNameDesc ? (String) new Object() : "");
    }

    @Override
    public String getDescription(String locale) {
	return (this.faultyNameDesc ? (String) new Object() : "");
    }

    @Override
    public String getHumanReadableValue(String locale, String value)
	    throws PrivacyLevelValueException {
	return (String) new Object();
    }

    @Override
    public boolean permits(IllegalArgumentException value,
	    IllegalArgumentException reference) {
	return true;
    }

    @Override
    public IllegalArgumentException parseValue(String value)
	    throws PrivacyLevelValueException {
	return (IllegalArgumentException) new Object();
    }

}
