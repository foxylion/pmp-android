package de.unistuttgart.ipvs.pmp.service;

import java.util.List;

import android.os.RemoteException;

/**
 * Implementation of the {@link IAppService.Stub} stub.
 * 
 * @author Jakob Jarosch
 */
public class IAppServiceStubImpl extends IAppService.Stub {

    @Override
    public String getName(String locale) throws RemoteException {

	// TODO Auto-generated method stub
	throw new UnsupportedOperationException();
    }

    @Override
    public String getDescription(String locale) throws RemoteException {

	// TODO Auto-generated method stub
	throw new UnsupportedOperationException();
    }

    @Override
    public int getServiceLevelCount() throws RemoteException {

	// TODO Auto-generated method stub
	throw new UnsupportedOperationException();
    }

    @Override
    public String getServiceLevelName(String locale, int serviceLevelId)
	    throws RemoteException {

	// TODO Auto-generated method stub
	throw new UnsupportedOperationException();
    }

    @Override
    public String getServiceLevelDescription(String locale, int serviceLevelId)
	    throws RemoteException {

	// TODO Auto-generated method stub
	throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List getServiceLevelPrivacyLevels(int serviceLevelId)
	    throws RemoteException {
	/**
	 * Note: The given List is described by the JavaDoc of
	 * {@link IAppService#getServiceLevelPrivacyLevels}
	 */

	// TODO Auto-generated method stub
	throw new UnsupportedOperationException();
    }

    @Override
    public void setServiceLevel(int serviceLevel) throws RemoteException {

	// TODO Auto-generated method stub
	throw new UnsupportedOperationException();
    }

    @Override
    public void setRegistrationSuccessful(boolean success)
	    throws RemoteException {
	// TODO Auto-generated method stub
	
    }

}
