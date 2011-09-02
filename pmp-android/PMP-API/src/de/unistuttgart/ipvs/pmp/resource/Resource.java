package de.unistuttgart.ipvs.pmp.resource;

import de.unistuttgart.ipvs.pmp.service.resource.ResourceGroupService;

import android.os.IBinder;

/**
 * An individual Resource of a {@link ResourceGroup}.
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class Resource {

    /**
     * The resource group that this resource is assigned to.
     */
    private ResourceGroup resourceGroup;

    /**
     * Assigns the resource group during registration.
     * 
     * <b>Do not call this method.</b>
     * 
     * @param resourceGroup
     */
    protected final void assignResourceGroup(ResourceGroup resourceGroup) {
	this.resourceGroup = resourceGroup;
    }

    /**
     * Retrieves the setting for a privacy level.
     * 
     * @param appIdentifier
     *            the identifier for the accessing app
     * @param privacyLevel
     *            the name of the privacy level
     * @return the value of the privacy level
     */
    protected final String getPrivacyLevelValue(String appIdentifier,
	    String privacyLevel) {
	return resourceGroup.getPrivacyLevelValue(appIdentifier, privacyLevel);
    }

    /**
     * Sets the {@link IBinder} defined in AIDL for communicating over a
     * Service.
     * 
     * @see http://developer.android.com/guide/developing/tools/aidl.html
     * 
     * @param appIdentifier
     *            the identifier for the app accessing the interface.
     * 
     * @return The IBinder that shall be returned when an App binds against the
     *         {@link ResourceGroupService} requesting this resource.
     */
    public abstract IBinder getAndroidInterface(String appIdentifier);

}
