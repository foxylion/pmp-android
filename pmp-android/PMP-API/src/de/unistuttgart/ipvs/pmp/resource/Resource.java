package de.unistuttgart.ipvs.pmp.resource;

import de.unistuttgart.ipvs.pmp.service.ResourceGroupService;

import android.os.IInterface;

/**
 * An individual Resource of a {@link ResourceGroup}.
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class Resource {

    /**
     * Sets the {@link IInterface} for communicating over a Service.
     * 
     * @return The IInterface that shall be returned when an App binds against
     *         the {@link ResourceGroupService} requesting this resource.
     */
    public abstract IInterface getAndroidInterface();

}
