package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import android.app.ProgressDialog;
import android.content.Context;

public class vhikeDialogs {

	private static vhikeDialogs instance;

	private ProgressDialog dLogin;
	private ProgressDialog dAnnounce;

	public static vhikeDialogs getInstance() {
		if (instance == null) {
			instance = new vhikeDialogs();
		}
		return instance;
	}

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

}
