package de.unistuttgart.ipvs.pmp.service.app;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.app.AppInformationSetParcelable;
import de.unistuttgart.ipvs.pmp.service.RegistrationState;

/**
 * Implementation of the {@link IAppService.Stub} stub.
 * 
 * @author Jakob Jarosch
 */
public class IAppServiceStubImpl extends IAppService.Stub {

    @Override
    public AppInformationSetParcelable getAppInformationSet()
	    throws RemoteException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void setActiveServiceLevel(int level) throws RemoteException {
	// TODO Auto-generated method stub

    }

    @Override
    public void setRegistrationSuccessful(RegistrationState state)
	    throws RemoteException {
	// TODO Auto-generated method stub

    }

}
