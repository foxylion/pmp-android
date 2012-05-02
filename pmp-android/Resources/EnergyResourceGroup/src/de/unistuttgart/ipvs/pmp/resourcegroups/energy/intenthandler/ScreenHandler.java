package de.unistuttgart.ipvs.pmp.resourcegroups.energy.intenthandler;

import de.unistuttgart.ipvs.pmp.resourcegroups.energy.db.DBConnector;


/**
 * 
 * @author Marcus Vetter
 *
 */
public class ScreenHandler {

	public static void handle(boolean changedTo) {
		/*
		 * Store to database
		 */
		DBConnector.getInstance().storeScreenState(System.currentTimeMillis(), changedTo);
	}
	
}
