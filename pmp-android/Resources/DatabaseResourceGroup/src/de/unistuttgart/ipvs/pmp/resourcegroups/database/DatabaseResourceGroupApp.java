/**
 * 
 */
package de.unistuttgart.ipvs.pmp.resourcegroups.database;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroupSingleApp;

/**
 * @author Dang Huynh
 *
 */
public class DatabaseResourceGroupApp extends
	ResourceGroupSingleApp<DatabaseResourceGroup> {
    
    static {
        Log.setTagSufix("DatabaseRG");
    }
    
    @Override
    protected DatabaseResourceGroup createResourceGroup() {
	return new DatabaseResourceGroup(getApplicationContext());
    }
}