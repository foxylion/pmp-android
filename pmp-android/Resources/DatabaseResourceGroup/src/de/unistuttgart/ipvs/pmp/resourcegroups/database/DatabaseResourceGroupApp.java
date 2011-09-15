/**
 * 
 */
package de.unistuttgart.ipvs.pmp.resourcegroups.database;

import de.unistuttgart.ipvs.pmp.resource.ResourceGroupSingleApp;
import de.unistuttgart.ipvs.pmp.resourcegroups.database.DatabaseResourceGroup;

/**
 * @author Dang Huynh
 *
 */
public class DatabaseResourceGroupApp extends
	ResourceGroupSingleApp<DatabaseResourceGroup> {

    @Override
    protected DatabaseResourceGroup createResourceGroup() {
	DatabaseResourceGroup result = new DatabaseResourceGroup(getApplicationContext(),
		DatabaseService.class);
	// TODO Service Context???
	result.start(getApplicationContext());
	return result;
    }
}