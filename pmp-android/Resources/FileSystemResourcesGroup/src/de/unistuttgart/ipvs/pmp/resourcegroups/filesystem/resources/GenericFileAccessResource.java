package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources;

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.resource.Resource;

/**
 * This resource gives access to every file or directory on the device. Accessing protected files, such as system files,
 * might require root-rights. Otherwise this resource-group will crash. For safety and security reasons it's recommended
 * to use this resource only if special file access is needed. Please use the other resources instead.
 * 
 * @author Patrick Strobel
 * @version 0.1.0
 */
public class GenericFileAccessResource extends Resource {
    
    @Override
    public IBinder getAndroidInterface(String appIdentifier) {
        return new GenericFileAccess(appIdentifier, this);
    }
    
}
