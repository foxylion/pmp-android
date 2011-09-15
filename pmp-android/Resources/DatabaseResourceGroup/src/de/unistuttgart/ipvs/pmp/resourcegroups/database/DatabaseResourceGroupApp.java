/**
 * 
 */
package de.unistuttgart.ipvs.pmp.resourcegroups.database;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroupSingleApp;
import de.unistuttgart.ipvs.pmp.resourcegroups.database.DatabaseResourceGroup;

/**
 * @author Dang Huynh
 *
 */
public class DatabaseResourceGroupApp extends
	ResourceGroupSingleApp<DatabaseResourceGroup> {

    private Context context = this.getApplicationContext();
//    private DatabaseService dbService = new DatabaseService();

    @Override
    protected DatabaseResourceGroup createResourceGroup() {
	DatabaseResourceGroup result = new DatabaseResourceGroup(context,
		DatabaseService.class);
	// TODO Service Context???
	result.start(context);
	return result;
    }
}