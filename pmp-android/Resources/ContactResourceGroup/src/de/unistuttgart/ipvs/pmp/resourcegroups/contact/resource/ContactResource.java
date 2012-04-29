package de.unistuttgart.ipvs.pmp.resourcegroups.contact.resource;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsManager;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.ContactResourceGroup;

public class ContactResource extends Resource {

	ContactResourceGroup RG;

	public ContactResource(ContactResourceGroup contactRG) {
		this.RG = contactRG;
	}

	@Override
	public IBinder getAndroidInterface(String appIdentifier) {
		return new ContactImpl(this.RG, this, appIdentifier);
	}

	public void call(int tel) {
//		String url = "tel:" + String.valueOf(tel);
		String url = "tel:5556";
	    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
	    this.RG.getContext().startActivity(callIntent);
	}

	public void sms(int tel, String message) {
		PendingIntent pi = PendingIntent.getActivity(RG.getContext(), 0,
	            new Intent(RG.getContext(), null), 0);                
	        SmsManager sms = SmsManager.getDefault();
	        sms.sendTextMessage(String.valueOf(tel), null, message, pi, null);  
	}

	public void email(String recipient, String message) {
		
	}

}
