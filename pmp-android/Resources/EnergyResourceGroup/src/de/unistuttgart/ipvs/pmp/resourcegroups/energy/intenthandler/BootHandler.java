package de.unistuttgart.ipvs.pmp.resourcegroups.energy.intenthandler;

import de.unistuttgart.ipvs.pmp.resourcegroups.energy.db.DBConnector;

/**
 * 
 * @author Marcus Vetter
 *
 */
public class BootHandler {
	
	public static void handle() {
		/*
		 * Store to database
		 */
		DBConnector.getInstance().storeDeviceBoot(System.currentTimeMillis());
	}

}
