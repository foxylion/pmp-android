package de.unistuttgart.ipvs.pmp.resourcegroups.notification;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.BooleanPrivacySetting;
import de.unistuttgart.ipvs.pmp.resourcegroups.notification.resource.NotificationResource;

public class NotificationResourceGroup extends ResourceGroup {

	public static final String PACKAGE_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.notification";

	public static final String R_NOTIFICATION= "notificationResource";

	public static final String PS_USE_NOTIFY = "notify";

	public NotificationResourceGroup(IPMPConnectionInterface pmpci) {
		super(PACKAGE_NAME, pmpci);
		
		
		registerResource(R_NOTIFICATION, new NotificationResource(this));
		Log.i(this, "registerResource");
		registerPrivacySetting(PS_USE_NOTIFY, new BooleanPrivacySetting());
	}

}
