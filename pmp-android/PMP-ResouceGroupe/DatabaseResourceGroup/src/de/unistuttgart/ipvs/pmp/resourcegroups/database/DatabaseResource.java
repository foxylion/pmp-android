/**
 * 
 */
package de.unistuttgart.ipvs.pmp.resourcegroups.database;

import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.resource.Resource;

/**
 * @author Dang Huynh
 *
 */
public class DatabaseResource extends Resource {

    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.resource.Resource#getAndroidInterface(java.lang.String)
     */
    @Override
    public IBinder getAndroidInterface(String appIdentifier) {
	return new DatabaseAccessImpl();
    }

}