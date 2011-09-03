package de.unistuttgart.ipvs.pmp.service.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class PMPServiceConnector extends AbstractConnector {

    public PMPServiceConnector(Context context) {
	super(context);
	// TODO Auto-generated constructor stub
    }

    @Override
    protected Intent createIntent() {
	// Todo, create the intent:
	/*
	 * 1. Create an intent with action=MAIN and category=LAUNCHER 2. Get the
	 * PackageManager from the current context using
	 * context.getPackageManager 3.
	 * packageManager.queryIntentActivity(<intent>, 0) where intent has
	 * category=LAUNCHER, action=MAIN or
	 * packageManager.resolveActivity(<intent>, 0) to get the first activity
	 * with main/launcher 4. Get the ActivityInfo you're interested in 5.
	 * From the ActivityInfo, get the packageName and name 6. Finally,
	 * create another intent with with category=LAUNCHER, action=MAIN,
	 * componentName = new ComponentName(packageName, name) and
	 * setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) 7. Finally,
	 * context.startActivity(newIntent)
	 */
	
	Intent intent = new Intent(Intent.ACTION_MAIN);
	intent.addCategory(Intent.CATEGORY_LAUNCHER);
	// how do I peek at project PMP without creating a circular reference?
	intent.setComponent(new ComponentName(
		"de.unistuttgart.ipvs.pmp.service", "PMPService"));
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// is this right?

	// shouldn't this be done in bind()?
	getContext().startActivity(intent);
	return intent;
    }
    
    @Override
    public IBinder getService() {
        return super.getService();
    }
    
    @Override
    protected void serviceConnected() {
	// TODO Auto-generated method stub
    }

    @Override
    protected void serviceDisconnected() {
	// TODO Auto-generated method stub
    }

}
