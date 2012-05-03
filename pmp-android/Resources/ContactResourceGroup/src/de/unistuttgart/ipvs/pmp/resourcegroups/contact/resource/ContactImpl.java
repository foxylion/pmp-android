package de.unistuttgart.ipvs.pmp.resourcegroups.contact.resource;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.ContactResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.PermissionValidator;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.aidl.IContact;

public class ContactImpl extends IContact.Stub {

	private ContactResourceGroup contactRG;
	private ContactResource contactR;
	private String appIdentifier;

	private PermissionValidator psv;

	public ContactImpl(ContactResourceGroup contactRG, ContactResource contactR,
			String appIdentifier) {
		this.contactRG = contactRG;
		this.contactR = contactR;
		this.appIdentifier = appIdentifier;
		this.psv = new PermissionValidator(this.contactRG, this.appIdentifier);
	}

	@Override
	public void call(int tel) throws RemoteException {
		this.psv.validate(ContactResourceGroup.PS_OPEN_DIALER, "true");
		this.contactR.call(tel);
	}

	@Override
	public void sms(int tel, String message) throws RemoteException {
		this.psv.validate(ContactResourceGroup.PS_SEND_SMS, "true");
		this.contactR.sms(tel, message);
	}

	@Override
	public void email(String recipient, String message) throws RemoteException {
		this.psv.validate(ContactResourceGroup.PS_SEND_EMAIL, "true");
		this.contactR.email(recipient, message);
	}

}