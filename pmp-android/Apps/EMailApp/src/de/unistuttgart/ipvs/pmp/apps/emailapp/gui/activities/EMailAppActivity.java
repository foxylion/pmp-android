package de.unistuttgart.ipvs.pmp.apps.emailapp.gui.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import de.unistuttgart.ipvs.pmp.apps.emailapp.EMailProvider;
import de.unistuttgart.ipvs.pmp.apps.emailapp.R;
import de.unistuttgart.ipvs.pmp.apps.emailapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.emailapp.model.data.EMail;

public class EMailAppActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set the app context
        Model.getInstance().setAppContext(getApplicationContext());
        
        // TEST: Send an email!
        List<String> recipients = new ArrayList<String>();
        recipients.add("marcus@mvvt.de");
        recipients.add("m@mvvt.de");
        EMailProvider.getInstance().sendEmail(new EMail("test@test.com", recipients, "Betreff 123", "Text *g* was für ein Test ^^"));
        
        setContentView(R.layout.main);
    }
}