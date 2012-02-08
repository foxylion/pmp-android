package de.unistuttgart.ipvs.pmp.resourcegroups.location;

import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.BooleanPrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.IntegerPrivacySetting;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.resource.AbsoluteLocationResource;

public class LocationResourceGroup extends ResourceGroup {
	
	public static final String PACKAGE_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.location";
	
	public static final String R_ABSOLUTE_LOCATION = "absoluteLocationResource";
	
	public static final String PS_USE_ABSOLUTE_LOCATION = "useAbsoluteLocation";
	public static final String PS_LOCATION_PRECISION = "locationPrecision";
	
	public static final String PS_SHOW_ACCURACY = "showAccuracy";
	public static final String PS_SHOW_SPEED = "showSpeed";
	
	
	/**
	 * Creates a new {@link LocationResourceGroup}.
	 * 
	 * @param rgPackage
	 *            Packagename of the ResourceGroup
	 * @param pmpci
	 *            Connectioninterface to PMP.
	 */
	public LocationResourceGroup(IPMPConnectionInterface pmpci) {
		super(PACKAGE_NAME, pmpci);
		
		registerResource(R_ABSOLUTE_LOCATION, new AbsoluteLocationResource(this));
		
		registerPrivacySetting(PS_USE_ABSOLUTE_LOCATION, new BooleanPrivacySetting());
		registerPrivacySetting(PS_SHOW_ACCURACY, new BooleanPrivacySetting());
		registerPrivacySetting(PS_SHOW_SPEED, new BooleanPrivacySetting());
		registerPrivacySetting(PS_LOCATION_PRECISION, new IntegerPrivacySetting(true));
	}
}
