package de.unistuttgart.ipvs.pmp.service.resource;

import java.util.List;

import android.os.IBinder;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.service.resource.IResourceGroupServiceApp;

/**
 * Implementation of the {@link IResourceGroupServiceApp.Stub} stub.
 * 
 * @author Tobias Kuhn
 */
public class ResourceGroupServiceAppStubImpl extends IResourceGroupServiceApp.Stub {

    @Override
    public List getResources() throws RemoteException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public IBinder getResource(String resourceIdentifier)
	    throws RemoteException {
	// TODO Auto-generated method stub
	return null;
    }

}
