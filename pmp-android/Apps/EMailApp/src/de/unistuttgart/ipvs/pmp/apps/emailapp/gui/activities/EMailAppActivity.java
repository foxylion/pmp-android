package de.unistuttgart.ipvs.pmp.apps.emailapp.gui.activities;

import android.app.Activity;
import android.os.Bundle;
import de.unistuttgart.ipvs.pmp.apps.emailapp.R;
import de.unistuttgart.ipvs.pmp.apps.emailapp.model.Model;

public class EMailAppActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set the app context
        Model.getInstance().setAppContext(getApplicationContext());
        
        
        
        setContentView(R.layout.main);
    }
}