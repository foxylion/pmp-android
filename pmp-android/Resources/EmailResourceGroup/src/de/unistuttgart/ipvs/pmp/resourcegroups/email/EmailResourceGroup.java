package de.unistuttgart.ipvs.pmp.resourcegroups.email;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.BooleanPrivacyLevel;

public class EmailResourceGroup extends ResourceGroup {

	private Context context;
	
	public static final String PRIVACY_LEVEL_SEND_EMAIL = "canSendEmail";
	public static final String RESOURCE_EMAIL_OPERATIONS = "emailOperations";
	
	public EmailResourceGroup(Context serviceContext) {
		super(serviceContext);
		this.context = serviceContext;
		
		registerPrivacyLevel(PRIVACY_LEVEL_SEND_EMAIL, new BooleanPrivacyLevel("Send Email",
                "Is allowed to send emails."));
        
        registerResource(RESOURCE_EMAIL_OPERATIONS, new EmailResource());
	}

	@Override
	public String getName(String locale) {
		return "Email ResourceGroup";
	}

	@Override
	public String getDescription(String locale) {
		return "Allows some basic interactions with Androids Mail app.";
	}

	@Override
	protected String getServiceAndroidName() {
		return "de.unistuttgart.ipvs.pmp.resourcegroups.email";
	}

	@Override
	public void onRegistrationSuccess() {
		Log.d("Registration success.");
	}

	@Override
	public void onRegistrationFailed(String message) {
		Log.e("Registration failed with \"" + message + "\"");
	}
	
	public Context getContext() {
		return context;
	}

}
