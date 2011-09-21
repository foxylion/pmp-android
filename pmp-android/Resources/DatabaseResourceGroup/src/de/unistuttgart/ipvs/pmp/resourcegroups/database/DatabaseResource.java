/**
 * 
 */
package de.unistuttgart.ipvs.pmp.resourcegroups.database;

import android.content.Context;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.resource.Resource;

/**
 * @author Dang Huynh
 * 
 */
public class DatabaseResource extends Resource {
    
    private Context context;
    
    
    public DatabaseResource(Context context) {
        this.context = context;
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * de.unistuttgart.ipvs.pmp.resource.Resource#getAndroidInterface(java.lang
     * .String)
     */
    @Override
    public IBinder getAndroidInterface(String appIdentifier) {
        return new DatabaseConnectionImpl(this.context, this, appIdentifier);
    }
}
