package de.unistuttgart.ipvs.pmp.resourcegroups.energy.intenthandler;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.db.DBConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.ScreenEvent;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class ScreenHandler {

	public static void handle(boolean changedTo, Context context) {
		/*
		 * Store to database
		 */
		ScreenEvent se = new ScreenEvent(-1, System.currentTimeMillis(),
				changedTo);
		DBConnector.getInstance(context).storeScreenEvent(se);
	}

}
