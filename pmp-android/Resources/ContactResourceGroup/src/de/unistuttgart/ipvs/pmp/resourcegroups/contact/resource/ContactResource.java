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
    
    
    @Override
    public IBinder getMockedAndroidInterface(String appIdentifier) {
        return new ContactMockImpl(this.contactRG, this, appIdentifier);
    }
    
    
    @Override
    public IBinder getCloakedAndroidInterface(String appIdentifier) {
        return new ContactCloakImpl(this.contactRG, this, appIdentifier);
    }
    
    
    public void call(String appIdentifier, int tel) {
        String url = "tel:5556";
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
        
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.contactRG.getContext(appIdentifier).startActivity(callIntent);
    }
    
    
    public void sms(String appIdentifier, int tel, String message) {
        //		Uri uri = Uri.parse("smsto:" + tel);
        Uri uri = Uri.parse("smsto:5556");
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
        smsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        smsIntent.putExtra("sms_body", message);
        this.contactRG.getContext(appIdentifier).startActivity(smsIntent);
    }
    
    
    public void email(String appIdentifier, String recipient, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "to@email.com", null));
        // emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new
        // String[]{mailId});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "subject");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "body");
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        this.contactRG.getContext(appIdentifier).startActivity(emailIntent);
    }
    
}
