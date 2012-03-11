package de.unistuttgart.ipvs.pmp.apps.simpledialogregapp;

import android.app.Activity;
import android.os.Bundle;
import de.unistuttgart.ipvs.pmp.api.PMP;

public class SimpleDialogRegAppActivity extends Activity {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        PMP.get(getApplication()).register(this);
    }
}
