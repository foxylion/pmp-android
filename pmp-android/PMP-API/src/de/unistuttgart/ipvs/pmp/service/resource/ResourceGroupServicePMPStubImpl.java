package de.unistuttgart.ipvs.pmp.service.resource;

import java.util.List;

import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.resource.PrivacyLevel;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroupAccess;
import de.unistuttgart.ipvs.pmp.service.RegistrationState;
import de.unistuttgart.ipvs.pmp.service.resource.IResourceGroupServicePMP;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignature;

import android.os.RemoteException;

/**
 * Implementation of the {@link IResourceGroupServicePMP.Stub} stub.
 * 
 * @author Tobias Kuhn
 */
public class ResourceGroupServicePMPStubImpl extends
	IResourceGroupServicePMP.Stub {

    /**
     * referenced resource group
     */
    private ResourceGroup rg;

    /**
     * referenced signature
     */
    private PMPSignature refSig;

    public void setSignature(PMPSignature resgrpSig) {
	refSig = resgrpSig;
    }

    public void setResourceGroup(ResourceGroup rg) {
	this.rg = rg;
    }

    @Override
    public String getName(String locale) throws RemoteException {
	return rg.getName(locale);
    }

    @Override
    public String getDescription(String locale) throws RemoteException {
	return rg.getDescription(locale);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List getPrivacyLevelIdentifiers() throws RemoteException {
	return rg.getPrivacyLevels();
    }

    @Override
    public String getPrivacyLevelName(String locale, String identifier)
	    throws RemoteException {
	PrivacyLevel pl = rg.getPrivacyLevel(identifier);
	if (pl == null) {
	    return null;
	} else {
	    return pl.getName(locale);
	}
    }

    @Override
    public String getPrivacyLevelDescription(String locale, String identifier)
	    throws RemoteException {
	PrivacyLevel pl = rg.getPrivacyLevel(identifier);
	if (pl == null) {
	    return null;
	} else {
	    return pl.getDescription(locale);
	}
    }

    @Override
    public String getHumanReadablePrivacyLevelValue(String locale,
	    String identifier, String value) throws RemoteException {
	PrivacyLevel pl = rg.getPrivacyLevel(identifier);
	if (pl == null) {
	    return null;
	} else {
	    return pl.getHumanReadablePrivacyLevelValue(locale, value);
	}
    }

    @Override
    public void setAccesses(@SuppressWarnings("rawtypes") List accesses)
	    throws RemoteException {
	@SuppressWarnings("unchecked")
	List<ResourceGroupAccess> castedAccesses = accesses;

	for (ResourceGroupAccess rga : castedAccesses) {
	    refSig.setRemotePublicKey(Constants.TYPE_APP, rga.getHeader()
		    .getIdentifier(), rga.getHeader().getPublicKey());
	    rg.updateAccess(rga);
	}
    }

    @Override
    public boolean satisfiesPrivacyLevel(String privacyLevel, String reference,
	    String value) throws RemoteException {
	PrivacyLevel pl = rg.getPrivacyLevel(privacyLevel);
	if (pl == null) {
	    return false;
	} else {
	    return pl.satisfies(reference, value);
	}
    }

    @Override
    public void changePrivacyLevels(String appIdentifier)
	    throws RemoteException {
	/*
	 * TODO: some magic happening here I assume we have to call
	 * ResourceGroup.createActivity() that presents a standard
	 * representation of all privacylevels or something.
	 * 
	 * NOTICE: THIS WOULD BE GUI!
	 */

    }

    @Override
    public void setRegistrationSuccessful(RegistrationState state)
	    throws RemoteException {
	if (state.getState()) {
	    rg.onRegistrationSuccess();
	} else {
	    rg.onRegistrationFailed(state.getMessage());
	}
    }

}
