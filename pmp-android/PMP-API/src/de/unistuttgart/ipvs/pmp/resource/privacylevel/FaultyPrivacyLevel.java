package de.unistuttgart.ipvs.pmp.resource.privacylevel;

/**
 * This privacy level only exists to be faulty for system testing.
 * 
 * @author Tobias Kuhn
 * 
 */
public class FaultyPrivacyLevel extends PrivacyLevel<IllegalArgumentException> {
    
    private boolean faultyNameDesc;
    private boolean throwExcep;
    
    public FaultyPrivacyLevel(boolean faultyNameDesc, boolean throwExcep) {
	this.faultyNameDesc = faultyNameDesc;
	this.throwExcep = throwExcep;
    }

    @Override
    public String getName(String locale) {
	return (this.faultyNameDesc ? null : "");
    }

    @Override
    public String getDescription(String locale) {
	return (this.faultyNameDesc ? null : "");
    }

    @Override
    public String getHumanReadableValue(String locale, String value)
	    throws PrivacyLevelValueException {
	if (this.throwExcep) {
	    throw new PrivacyLevelValueException();
	} else {
	    return null;
	}
    }

    @Override
    public boolean permits(IllegalArgumentException value,
	    IllegalArgumentException reference) {
	return true;
    }

    @Override
    public IllegalArgumentException parseValue(String value)
	    throws PrivacyLevelValueException {
	if (this.throwExcep) {
	    throw new PrivacyLevelValueException();
	} else {
	    return null;
	}
    }

}
