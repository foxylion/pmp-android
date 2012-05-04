package de.unistuttgart.ipvs.pmp.resourcegroups.contact.resource;

import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.ContactResourceGroup;

public class ContactResource extends Resource {
    
    ContactResourceGroup contactRG;
    
    
    public ContactResource(ContactResourceGroup contactRG) {
        this.contactRG = contactRG;
    }
    
    
    @Override
    public IBinder getAndroidInterface(String appIdentifier) {
        return new ContactImpl(this.contactRG, this, appIdentifier);
    }
    
    
    public void call(int tel) {
        //		String url = "tel:" + String.valueOf(tel);
        String url = "tel:5556";
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.contactRG.getContext().startActivity(callIntent);
    }
    
    
    public void sms(int tel, String message) {
        //		Uri uri = Uri.parse("smsto:" + tel);
        Uri uri = Uri.parse("smsto:5556");
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
        smsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        smsIntent.putExtra("sms_body", message);
        this.contactRG.getContext().startActivity(smsIntent);
    }
    
    
    public void email(String recipient, String message) {
        
    }
    
}
