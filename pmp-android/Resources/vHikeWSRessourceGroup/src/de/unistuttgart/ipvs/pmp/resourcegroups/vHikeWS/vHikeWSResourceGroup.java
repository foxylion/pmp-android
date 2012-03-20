package de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.BooleanPrivacySetting;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.resource.vHikeWebserviceResource;

public class vHikeWSResourceGroup extends ResourceGroup {

	public static final String PACKAGE_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS";

	public static final String R_vHIKE_WEBSERVICE= "vHikeWebserviceResource";

	public static final String PS_USE_vHIKE_WEBSERVICE = "usevHikeWS";

	public vHikeWSResourceGroup(IPMPConnectionInterface pmpci) {
		super(PACKAGE_NAME, pmpci);
		
		
		registerResource(R_vHIKE_WEBSERVICE, new vHikeWebserviceResource(this));
		Log.i(this, "registerResource");
		registerPrivacySetting(PS_USE_vHIKE_WEBSERVICE, new BooleanPrivacySetting());
	}

}
