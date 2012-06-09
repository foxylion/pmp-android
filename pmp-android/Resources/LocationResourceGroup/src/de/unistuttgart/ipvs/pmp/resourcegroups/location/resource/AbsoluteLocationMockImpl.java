package de.unistuttgart.ipvs.pmp.resourcegroups.location.resource;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.aidl.IAbsoluteLocation;

public class AbsoluteLocationMockImpl extends IAbsoluteLocation.Stub {
    
    @Override
    public void startLocationLookup(long minTime, float minDistance) throws RemoteException {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void endLocationLookup() throws RemoteException {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public boolean isGpsEnabled() throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public boolean isActive() throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public boolean isFixed() throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public boolean isUpdateAvailable() throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public double getLongitude() throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    @Override
    public double getLatitude() throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    @Override
    public float getAccuracy() throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    @Override
    public float getSpeed() throws RemoteException {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    @Override
    public String getCountryCode() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String getCountryName() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String getLocality() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String getPostalCode() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String getAddress() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
