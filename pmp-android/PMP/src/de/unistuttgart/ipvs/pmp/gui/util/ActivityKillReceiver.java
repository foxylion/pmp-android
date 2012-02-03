package de.unistuttgart.ipvs.pmp.gui.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * The {@link ActivityKillReceiver} should be assigned to each activity.
 * 
 * @author Jakob Jarosch
 */
public class ActivityKillReceiver extends BroadcastReceiver {
    
    public static final String INTENT_MIME = "de.unistuttgart.ipvs.pmp/killevent";
    private Activity activity;
    
    
    /**
     * Creates a broadcast receiver and adds it directly as a receiver of events created by
     * {@link ActivityKillReceiver#sendKillBroadcast()}.
     * 
     * @param activity
     *            Activity which should be assigned to the broadcast instance.
     */
    public ActivityKillReceiver(Activity activity) {
        this.activity = activity;
        
        activity.registerReceiver(this, getIntentFilter());
    }
    
    
    @Override
    public void onReceive(Context arg0, Intent arg1) {
        this.activity.finish();
    }
    
    
    /**
     * Call this method to send a kill command to all activities.
     */
    public static void sendKillBroadcast(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setType(INTENT_MIME);
        intent.setPackage(context.getPackageName());
        context.sendBroadcast(intent);
    }
    
    
    /**
     * @return Returns an {@link IntentFilter} which reacts only on kill events.
     */
    private IntentFilter getIntentFilter() {
        IntentFilter filter = IntentFilter.create(Intent.ACTION_MAIN, INTENT_MIME);
        return filter;
    }
}
