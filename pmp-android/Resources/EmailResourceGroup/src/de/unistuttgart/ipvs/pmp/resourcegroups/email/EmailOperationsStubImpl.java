package de.unistuttgart.ipvs.pmp.resourcegroups.email;

import de.unistuttgart.ipvs.pmp.resource.privacylevel.BooleanPrivacyLevel;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;

public class EmailOperationsStubImpl extends IEmailOperations.Stub {

	private String appIdentifier;
	private EmailResource resource;
	private Context context;

	public EmailOperationsStubImpl(String appIdentifier,
			EmailResource resource, Context context) {
		this.appIdentifier = appIdentifier;
		this.resource = resource;
		this.context = context;
	}

	@Override
	public void sendEmail(String to, String subject, String body)
			throws RemoteException {
		BooleanPrivacyLevel bpl = (BooleanPrivacyLevel) this.resource
				.getPrivacyLevel(EmailResourceGroup.PRIVACY_LEVEL_SEND_EMAIL);

		if (!bpl.permits(this.appIdentifier, true)) {
			throw new IllegalAccessError();
		}

		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		emailIntent.setType("plain/text");

		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				new String[] { to });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);

		context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}

}
