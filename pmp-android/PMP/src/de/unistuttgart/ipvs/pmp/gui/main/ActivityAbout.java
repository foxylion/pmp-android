package de.unistuttgart.ipvs.pmp.gui.main;

import android.app.Activity;
import android.os.Bundle;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.ActivityKillReceiver;

/**
 * The {@link ActivityAbout} displays informations about the who and why of PMP.
 * 
 * @author Jakob Jarosch
 */
public class ActivityAbout extends Activity {
    
    /**
     * The {@link ActivityKillReceiver}.
     */
    private ActivityKillReceiver akr;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_about);
        
        /* Initiating the ActivityKillReceiver. */
        this.akr = new ActivityKillReceiver(this);
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        unregisterReceiver(this.akr);
    }
}
