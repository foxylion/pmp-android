package de.unistuttgart.ipvs.pmp.apps.emailapp;

import android.content.Intent;
import de.unistuttgart.ipvs.pmp.apps.emailapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.emailapp.model.data.EMail;

/**
 * This email provider connects to the server.
 * 
 * @author Marcus Vetter
 */
public class EMailProvider {

	/**
	 * Instance of this email provider (Singleton)
	 */
	private static EMailProvider instance = null;

	/**
	 * Private constructor (Singleton)
	 */
	private EMailProvider() {

	}

	/**
	 * This method returns the one and only instance of the email provider
	 * (Singleton)
	 */
	public static EMailProvider getInstance() {
		if (instance == null) {
			instance = new EMailProvider();
		}
		return instance;
	}

	/**
	 * This method retrieves all emails on the server
	 */
	public void retrieveEMails() {

	}

	/**
	 * This method sends the given email
	 * 
	 * @param email
	 *            the email to send
	 */
	public void sendEmail(EMail email) {
		String[] recipients = email.getRecipients().toArray(new String[email.getRecipients().size()]);
		
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, email.getSubject());
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, email.getContent());
		emailIntent.setType("text/plain");
		emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		Model.getInstance().getAppContext().startActivity(emailIntent);
	}

}
