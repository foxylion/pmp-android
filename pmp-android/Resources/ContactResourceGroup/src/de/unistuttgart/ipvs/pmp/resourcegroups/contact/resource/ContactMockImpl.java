package de.unistuttgart.ipvs.pmp.resourcegroups.contact.resource;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.ContactResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.aidl.IContact;


public class ContactMockImpl extends IContact.Stub {

    public ContactMockImpl(ContactResourceGroup contactRG, ContactResource contactResource, String appIdentifier) {
        
    }
    
    @Override
    public void call(int tel) throws RemoteException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sms(String tel, String message) throws RemoteException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void email(String recipient, String subject, String message) throws RemoteException {
        // TODO Auto-generated method stub
        
    }
    
}
