package de.unistuttgart.ipvs.pmp.resourcegroups.profile.observer;

import android.database.ContentObserver;
import android.os.Handler;
import de.unistuttgart.ipvs.pmp.resourcegroups.profile.ProfileService;

public class CallObserver extends ContentObserver {
	ProfileService ps;

	public CallObserver(ProfileService ps) {
		super(new Handler());
		this.ps = ps;
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		ps.processCallEvent();
	}
}