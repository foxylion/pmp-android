package de.unistuttgart.ipvs.pmp.resourcegroups.contact;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.BooleanPrivacySetting;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.resource.ContactResource;

public class ContactResourceGroup extends ResourceGroup {
    
    public static final String PACKAGE_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.contact";
    
    public static final String R_CONTACT = "contactResource";
    
    public static final String PS_OPEN_DIALER = "openDialer";
    public static final String PS_SEND_SMS = "sendSMS";
    public static final String PS_SEND_EMAIL = "sendEmail";
    
    
    public ContactResourceGroup(IPMPConnectionInterface pmpci) {
        super(PACKAGE_NAME, pmpci);
        
        registerResource(R_CONTACT, new ContactResource(this));
        
        Log.i(this, "registerResource");
        registerPrivacySetting(PS_OPEN_DIALER, new BooleanPrivacySetting());
        registerPrivacySetting(PS_SEND_SMS, new BooleanPrivacySetting());
        registerPrivacySetting(PS_SEND_EMAIL, new BooleanPrivacySetting());
    }
    
}
