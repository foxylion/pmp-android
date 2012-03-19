package de.unistuttgart.ipvs.pmp.resourcegroups.notification.resource;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resourcegroups.notification.NotificationResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.notification.PermissionValidator;
import de.unistuttgart.ipvs.pmp.resourcegroups.notification.aidl.INotification;

public class NotificationImpl extends INotification.Stub {
	NotificationResourceGroup RG;
	NotificationResource RES;
	String appIdentifier;

	PermissionValidator psv;

	public NotificationImpl(NotificationResourceGroup RG,
			NotificationResource RES, String appIdentifier) {
		this.RG = RG;
		this.RES = RES;
		this.appIdentifier = appIdentifier;
		this.psv = new PermissionValidator(RG, appIdentifier);
	}
	
	public void notify(String tickerText, String title, String message) throws RemoteException {
		this.psv.validate(NotificationResourceGroup.PS_USE_NOTIFY, "true");
		RES.notify(tickerText, title, message);
	}

}
