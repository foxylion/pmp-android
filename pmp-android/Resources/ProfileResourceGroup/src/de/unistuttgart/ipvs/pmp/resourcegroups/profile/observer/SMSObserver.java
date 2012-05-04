package de.unistuttgart.ipvs.pmp.resourcegroups.profile.observer;

import android.database.ContentObserver;
import android.os.Handler;
import de.unistuttgart.ipvs.pmp.resourcegroups.profile.ProfileService;

public class SMSObserver extends ContentObserver {
    
    ProfileService ps;
    
    
    public SMSObserver(ProfileService ps) {
        super(new Handler());
        this.ps = ps;
    }
    
    
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        this.ps.processSMSEvent();
    }
}
