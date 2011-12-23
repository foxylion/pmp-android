package de.unistuttgart.ipvs.pmp.gui.main;

import de.unistuttgart.ipvs.pmp.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * The {@link ActivityAbout} displays informations about the who and why of PMP.
 * 
 * @author Jakob Jarosch
 */
public class ActivityAbout extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_about);
    }
}
