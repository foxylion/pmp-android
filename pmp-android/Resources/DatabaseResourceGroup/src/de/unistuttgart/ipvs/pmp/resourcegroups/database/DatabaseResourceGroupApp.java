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
	
	// Create resource group
	DatabaseResourceGroup rg = new DatabaseResourceGroup(getApplicationContext());
		
	// Start resource group
	rg.start(getApplicationContext());

	return rg;
    }
}