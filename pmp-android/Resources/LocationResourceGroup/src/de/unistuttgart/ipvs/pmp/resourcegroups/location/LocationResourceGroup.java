package de.unistuttgart.ipvs.pmp.resourcegroups.location;

import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.BooleanPrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.EnumPrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.IntegerPrivacySetting;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.resource.AbsoluteLocationResource;

public class LocationResourceGroup extends ResourceGroup {
	
	public static final String PACKAGE_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.location";
	
	public static final String R_ABSOLUTE_LOCATION = "absoluteLocationResource";
	
	public static final String PS_USE_ABSOLUTE_LOCATION = "useAbsoluteLocation";
	public static final String PS_USE_COORDINATES = "useCoordinates";
	public static final String PS_USE_LOCATION_DESCRIPTION = "useLocationDescription";
	
	public static final String PS_USE_ACCURACY = "useAccuracy";
	public static final String PS_USE_SPEED = "useSpeed";
	
	public static final String PS_LOCATION_PRECISION = "locationPrecision";
	
	
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
		registerPrivacySetting(PS_USE_COORDINATES, new BooleanPrivacySetting());
		registerPrivacySetting(PS_USE_LOCATION_DESCRIPTION, new EnumPrivacySetting<UseLocationDescriptionEnum>(
				UseLocationDescriptionEnum.class));
		registerPrivacySetting(PS_USE_ACCURACY, new BooleanPrivacySetting());
		registerPrivacySetting(PS_USE_SPEED, new BooleanPrivacySetting());
		registerPrivacySetting(PS_LOCATION_PRECISION, new IntegerPrivacySetting(true));
	}
}
