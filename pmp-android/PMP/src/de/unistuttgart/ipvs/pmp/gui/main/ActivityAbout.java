package de.unistuttgart.ipvs.pmp.gui.main;

import android.app.Activity;
import android.os.Bundle;
import de.unistuttgart.ipvs.pmp.R;

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
