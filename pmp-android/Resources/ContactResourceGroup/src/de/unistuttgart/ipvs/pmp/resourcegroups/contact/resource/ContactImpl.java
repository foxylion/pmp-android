package de.unistuttgart.ipvs.pmp.resourcegroups.contact.resource;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.ContactResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.PermissionValidator;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.aidl.IContact;

public class ContactImpl extends IContact.Stub {

	private ContactResourceGroup RG;
	private ContactResource RES;
	private String appIdentifier;

	private PermissionValidator psv;

	public ContactImpl(ContactResourceGroup RG, ContactResource RES,
			String appIdentifier) {
		this.RG = RG;
		this.RES = RES;
		this.appIdentifier = appIdentifier;
		this.psv = new PermissionValidator(this.RG, this.appIdentifier);
	}

	@Override
	public void call(int tel) throws RemoteException {
		this.psv.validate(ContactResourceGroup.PS_OPEN_DIALER, "true");
		this.RES.call(tel);
	}

	@Override
	public void sms(int tel, String message) throws RemoteException {
		this.psv.validate(ContactResourceGroup.PS_SEND_SMS, "true");
		this.RES.sms(tel, message);
	}

	@Override
	public void email(String recipient, String message) throws RemoteException {
		this.psv.validate(ContactResourceGroup.PS_SEND_EMAIL, "true");
		this.RES.email(recipient, message);
	}

}
