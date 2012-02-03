package de.unistuttgart.ipvs.pmp.resourcegroups.location;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.AbstractPrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;

/**
 * Simple PrivacySetting verification.
 * 
 * @author Jakob Jarosch
 */
public class PermissionValidator {
	
	private ResourceGroup rg;
	private String appIdentifier;
	
	
	public PermissionValidator(ResourceGroup rg, String appIdentifier) {
		this.rg = rg;
		this.appIdentifier = appIdentifier;
	}
	
	
	public void validate(String psIdentifier, String requiredValue) {
		AbstractPrivacySetting<?> ps = this.rg.getPrivacySetting(psIdentifier);
		String grantedValue = this.rg.getPMPPrivacySettingValue(psIdentifier, appIdentifier);
		
		try {
			if (!ps.permits(grantedValue, requiredValue)) {
				throw new SecurityException("The requested action requires at the PrivacySetting " + psIdentifier
						+ " with at least " + requiredValue + " as required value");
			}
		} catch (PrivacySettingValueException e) {
			Log.e("Something went wrong while validating the permissions for Privacy Settings.", e);
		}
	}
	
	
	public int getIntValue(String psIdentifier) {
		String value = this.rg.getPMPPrivacySettingValue(psIdentifier, appIdentifier);
		if (value == null) {
			return 100000;
		} else {
			return Integer.parseInt(value);
		}
	}
}
