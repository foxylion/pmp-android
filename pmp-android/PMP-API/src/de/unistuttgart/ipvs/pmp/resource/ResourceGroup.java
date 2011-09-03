package de.unistuttgart.ipvs.pmp.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceRegistration;
import de.unistuttgart.ipvs.pmp.service.resource.ResourceGroupService;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * A resource group that bundles {@link Resource}s and {@link PrivacyLevel}s.
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class ResourceGroup {

    /**
     * Stores the associated service.
     */
    private ResourceGroupService service;

    /**
     * The resources present in that resource group.
     */
    private final Map<String, Resource> resources;

    /**
     * The privacy levels present in that resource group.
     */
    private final Map<String, PrivacyLevel> privacyLevels;

    /**
     * Stores the list of the privacy level values.
     */
    private final Map<String, Bundle> privacyLevelValues;

    /**
     * Creates a new {@link ResourceGroup}.
     */
    public ResourceGroup() {
	resources = new HashMap<String, Resource>();
	privacyLevels = new HashMap<String, PrivacyLevel>();
	privacyLevelValues = new HashMap<String, Bundle>();
    }

    /**
     * 
     * @param locale
     *            the ISO-639 locale string available from
     *            {@link Locale#getLanguage()}
     * @return the name of this resource group for the given locale
     */
    public abstract String getName(String locale);

    /**
     * 
     * @param locale
     *            the ISO-639 locale string available from
     *            {@link Locale#getLanguage()}
     * @return the description of this resource group for the given locale
     */
    public abstract String getDescription(String locale);

    /**
     * Internal call for the {@link ResourceGroupService}.
     * 
     * <b>Do not call this method.</b>
     */
    public void assignService(ResourceGroupService service) {
	this.service = service;
    }

    /**
     * Registers resource as resource "identifier" in this resource group.
     * 
     * @param identifier
     * @param resource
     */
    public void registerResource(String identifier, Resource resource) {
	resource.assignResourceGroup(this);
	resources.put(identifier, resource);
    }

    /**
     * 
     * @param identifier
     * @return the resource identified by "identifier", if present, null
     *         otherwise
     */
    public Resource getResource(String identifier) {
	return resources.get(identifier);
    }

    /**
     * 
     * @return a list of all the valid resource identifiers.
     */
    public List<String> getResources() {
	return new ArrayList<String>(resources.keySet());
    }

    /**
     * Registers privacyLevel as privacy level "identifier" in this resource
     * group.
     * 
     * @param identifier
     * @param privacyLevel
     */
    public void registerPrivacyLevel(String identifier,
	    PrivacyLevel privacyLevel) {
	privacyLevels.put(identifier, privacyLevel);
    }

    /**
     * 
     * @param identifier
     * @return the privacy level identified by "identifier", if present, null
     *         otherwise
     */
    public PrivacyLevel getPrivacyLevel(String identifier) {
	return privacyLevels.get(identifier);
    }

    /**
     * 
     * @return a list of all the valid resource identifiers.
     */
    public List<String> getPrivacyLevels() {
	return new ArrayList<String>(privacyLevels.keySet());
    }

    /**
     * Used for getting changed access rules from the
     * {@link ResourceGroupService}. <b>Do not call this method yourself or
     * allow a different context to call it.</b>
     * 
     * @param rga
     *            the {@link ResourceGroupAccess} to update
     */
    public final void updateAccess(ResourceGroupAccess rga) {
	privacyLevelValues.put(rga.getHeader().getIdentifier(),
		rga.getPrivacyLevelValues());
    }

    /**
     * 
     * @param appIdentifier
     *            the identifier of the accessing app
     * @param privacyLevel
     *            the identifier of the privacy level
     * @return the value privacy level identified by "privacyLevel" for the app
     *         "appIdentifier", if present, null otherwise
     */
    protected final String getPrivacyLevelValue(String appIdentifier,
	    String privacyLevel) {
	Bundle appPLs = privacyLevelValues.get(appIdentifier);
	if (appPLs == null) {
	    return null;
	} else {
	    return appPLs.getString(privacyLevel);
	}
    }

    /**
     * Effectively starts this resource group and registers it with PMP. Note
     * that it needs to be connected to a {@link ResourceGroupService} via an
     * {@link ResourceGroupApp}. You can implement reacting to the result of
     * this operation by implementing onRegistrationSuccess() or
     * onRegistrationFailed()
     * 
     * @param context
     *            {@link Context} to use for the connection
     * @throws MissingServiceException
     *             TODO fix this, this does not look good
     * 
     */
    public void start(Context context) throws MissingServiceException {
	if (service == null) {
	    throw new MissingServiceException();
	}

	// connect to PMP
	PMPServiceConnector pmpsc = new PMPServiceConnector(context);
	pmpsc.bind();
	IBinder binding = pmpsc.getService();

	if ((binding != null) && (binding instanceof IPMPServiceRegistration)) {
	    // register here
	    IPMPServiceRegistration ipmpsr = (IPMPServiceRegistration) binding;
	    try {
		byte[] pmpPublicKey = ipmpsr.registerResourceGroup(service
			.getSignature().getLocalPublicKey());
		// TODO: what there?
		service.setAndSaveRemotePublicKey(Constants.TYPE_PMP,
			"what to put here?", pmpPublicKey);
	    } catch (RemoteException e) {
		Log.e("RemoteException during registering resource group: "
			+ e.toString());
	    }
	} else {
	    Log.e("PMP does not work correctly.");
	}
    }

    /**
     * Callback called when the preceding call to start() registered this
     * resource group successfully with PMP.
     */
    public abstract void onRegistrationSuccess();

    /**
     * Callback called when the preceding call to start() could not register
     * this resource group with PMP due to errors.
     * 
     * @param message
     *            returned message from the PMP service
     */
    public abstract void onRegistrationFailed(String message);
}
