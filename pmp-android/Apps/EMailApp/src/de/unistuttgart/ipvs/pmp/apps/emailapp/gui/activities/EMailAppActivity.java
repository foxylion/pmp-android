package de.unistuttgart.ipvs.pmp.apps.emailapp.gui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.unistuttgart.ipvs.pmp.apps.emailapp.R;
import de.unistuttgart.ipvs.pmp.apps.emailapp.model.Model;

public class EMailAppActivity extends Activity {
    private EMailAppActivity self;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        
        // Set the app context
        Model.getInstance().setAppContext(getApplicationContext());
        
        setContentView(R.layout.main);
        
        Button inbox = (Button) findViewById(R.id.inbox);
        inbox.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		Intent intent = new Intent(self, InboxActivity.class);
		startActivity(intent);
	    }
	});
    }
}