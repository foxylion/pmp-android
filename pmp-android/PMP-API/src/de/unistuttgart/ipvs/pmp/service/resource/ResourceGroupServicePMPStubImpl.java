package de.unistuttgart.ipvs.pmp.service.resource;

import java.util.List;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroupAccess;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.PrivacyLevel;
import de.unistuttgart.ipvs.pmp.service.RegistrationState;
import de.unistuttgart.ipvs.pmp.service.SerializableContainer;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

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
     * referenced signee
     */
    private PMPSignee refSignee;

    public void setSignee(PMPSignee resgrpSignee) {
	refSignee = resgrpSignee;
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
	    return pl.getHumanReadableValue(locale, value);
	}
    }

    @Override
    public void setAccesses(SerializableContainer accesses)
	    throws RemoteException {
	ResourceGroupAccess[] rgas = (ResourceGroupAccess[]) accesses.getSerializable();
	for (ResourceGroupAccess rga : rgas) {
	    refSignee.setRemotePublicKey(PMPComponentType.APP, rga.getHeader()
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
	    return pl.isQualified(reference, value);
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
    public void setRegistrationState(RegistrationState state)
	    throws RemoteException {
	if (state.getState()) {
	    rg.onRegistrationSuccess();
	} else {
	    rg.onRegistrationFailed(state.getMessage());
	}
    }

}
