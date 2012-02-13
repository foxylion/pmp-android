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
		String grantedValue = this.rg.getPMPPrivacySettingValue(psIdentifier, this.appIdentifier);
		
		boolean failed = true;
		try {
			if (ps.permits(grantedValue, requiredValue)) {
				failed = false;
			}
		} catch (PrivacySettingValueException e) {
			Log.e(this, "Something went wrong while validating the permissions for Privacy Settings.", e);
		}
		
		if (failed) {
			throw new SecurityException("The requested action requires at the PrivacySetting " + psIdentifier
					+ " with at least " + requiredValue + " as required value");
		}
	}
	
	
	public void validate(UseLocationDescriptionEnum country) {
		String value = this.rg.getPMPPrivacySettingValue(LocationResourceGroup.PS_USE_LOCATION_DESCRIPTION,
				this.appIdentifier);
		
		if (value == null) {
			value = UseLocationDescriptionEnum.NONE.name();
		}
		
		AbstractPrivacySetting<?> ps = this.rg.getPrivacySetting(LocationResourceGroup.PS_USE_LOCATION_DESCRIPTION);
		boolean failed = true;
		try {
			if (ps.permits(country.name(), value)) {
				failed = false;
			}
		} catch (PrivacySettingValueException e) {
			Log.e(this, "Something went wrong while validating the permissions for Privacy Settings.", e);
		}
		
		if (failed) {
			throw new SecurityException("The requested action requires at the PrivacySetting "
					+ LocationResourceGroup.PS_USE_LOCATION_DESCRIPTION + " with at least " + country.name()
					+ " as required value");
		}
	}
	
	
	public int getIntValue(String psIdentifier) {
		String value = this.rg.getPMPPrivacySettingValue(psIdentifier, this.appIdentifier);
		if (value == null) {
			return 100000;
		} else {
			return Integer.parseInt(value);
		}
	}
}
