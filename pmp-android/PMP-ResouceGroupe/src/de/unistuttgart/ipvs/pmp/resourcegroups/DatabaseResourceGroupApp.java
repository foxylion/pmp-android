/**
 * 
 */
package de.unistuttgart.ipvs.pmp.resourcegroups;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroupSingleApp;
import de.unistuttgart.ipvs.pmp.resourcegroups.DatabaseResourceGroup;

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
	result.start(context, context, DatabaseService.class);
	return result;
    }
}