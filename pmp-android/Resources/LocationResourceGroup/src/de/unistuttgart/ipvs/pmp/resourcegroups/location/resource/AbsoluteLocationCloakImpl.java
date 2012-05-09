package de.unistuttgart.ipvs.pmp.resourcegroups.location.resource;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.aidl.IAbsoluteLocation;

public class AbsoluteLocationCloakImpl extends IAbsoluteLocation.Stub {
    
    public void startLocationLookup(long minTime, float minDistance) throws RemoteException {
        // TODO Auto-generated method stub
        
    }
    
    
    public void endLocationLookup() throws RemoteException {
        // TODO Auto-generated method stub
        
    }
    
    
    public boolean isGpsEnabled() throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    public boolean isActive() throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    public boolean isFixed() throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    public boolean isUpdateAvailable() throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    public double getLongitude() throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    public double getLatitude() throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    public float getAccuracy() throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    public float getSpeed() throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    public String getCountryCode() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    public String getCountryName() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    public String getLocality() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    public String getPostalCode() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    public String getAddress() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
