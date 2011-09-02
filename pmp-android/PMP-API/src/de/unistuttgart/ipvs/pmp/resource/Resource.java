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
     * Sets the {@link IBinder} for communicating over a Service.
     * 
     * @return The IBinder that shall be returned when an App binds against
     *         the {@link ResourceGroupService} requesting this resource.
     */
    public abstract IBinder getAndroidInterface();

}
