package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import de.unistuttgart.ipvs.pmp.R;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * This class provides access to all dialogs in vHike
 * 
 * @author andres
 * 
 */
public class vhikeDialogs {

	private static vhikeDialogs instance;

	private ProgressDialog dLogin;
	private ProgressDialog dAnnounce;
	private ProgressDialog dSearch;

	private UpdateData dUpdateData;

	public static vhikeDialogs getInstance() {
		if (instance == null) {
			instance = new vhikeDialogs();
		}
		return instance;
	}

	/**
	 * ProgressDialog when logging in
	 * 
	 * @param context
	 * @return login progress dialog
	 */
	public ProgressDialog getLoginPD(Context context) {
		if (dLogin == null) {
			dLogin = new ProgressDialog(context);
		}
		dLogin.setTitle("Login");
		dLogin.setMessage("Logging in...");
		dLogin.setIndeterminate(true);
		dLogin.setCancelable(false);
		return dLogin;
	}

	/**
	 * ProgressDialog for driver when announcing a trip and calculating current
	 * position
	 * 
	 * @param context
	 * @return announce progress dialog
	 */
	public ProgressDialog getAnnouncePD(Context context) {
		if (dAnnounce == null) {
			dAnnounce = new ProgressDialog(context);
		}
		dAnnounce.setTitle("Announcing trip");
		dAnnounce.setMessage("Getting current location...\nAnnouncing trip...");
		dAnnounce.setIndeterminate(true);
		dAnnounce.setCancelable(false);

		return dAnnounce;
	}

	/**
	 * ProgressDialog when searching for drivers and calculating current
	 * position
	 * 
	 * @param context
	 * @return search progress dialog
	 */
	public ProgressDialog getSearchPD(Context context) {
		if (dSearch == null) {
			dSearch = new ProgressDialog(context);
		}
		dSearch.setTitle("Thumbs up");
		dSearch.setMessage("Getting current location...\nHolding thumb up...");
		dSearch.setIndeterminate(true);
		dSearch.setCancelable(false);

		return dSearch;
	}

	public Dialog getUpdateDataDialog(Context mContext) {
		dUpdateData = new UpdateData(mContext);

		return dUpdateData;
	}
}
