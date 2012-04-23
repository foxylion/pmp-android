package de.unistuttgart.ipvs.pmp.resourcegroups.profile;

import android.content.Context;
import android.widget.Toast;

public class Debug {

	public static final boolean DEBUG = true; 
	
	public static void e(String msg, Context context) {
		if (DEBUG) {
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		}
	}
}
