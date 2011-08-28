package de.unistuttgart.ipvs.pmp.service;

import java.util.List;
import java.util.Map;

import android.os.RemoteException;

public class ResourceGroupServicePMPStubImpl extends IResourceGroupServicePMP.Stub {

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
	@SuppressWarnings("rawtypes")
	public List getPrivacyLevelIdentifiers() throws RemoteException {
		/** Note: Has to return a List of Strings. */

		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	

	@Override
	public String getPrivacyLevelName(String locale, String identifier)
			throws RemoteException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getPrivacyLevelDescription(String locale, String identifier)
			throws RemoteException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getHumanReadablePrivacyLevelValue(String locale,
			String identifier, String value) throws RemoteException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAccesses(@SuppressWarnings("rawtypes") Map accesses)
			throws RemoteException {
		/**
		 * Note: The given Map is described by the JavaDoc of
		 * {@link IResourceGroupSerivce.Stub#setAccesses}
		 */
		@SuppressWarnings("unchecked")
		Map<String, List<String>> castedAccesses = (Map<String, List<String>>) accesses;

		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean satisfiesPrivacyLevel(String privacyLevel, String reference, String value)
			throws RemoteException {
		
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
}
