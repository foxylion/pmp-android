package de.unistuttgart.ipvs.pmp.resource;

import java.util.concurrent.atomic.AtomicBoolean;

import de.unistuttgart.ipvs.pmp.Log;
import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.os.Bundle;

/**
 * A basic {@link Activity} for a {@link ResourceGroupApp} in iteration 1.
 * 
 * @author Tobias Kuhn
 * 
 */
public class ResourceGroupActivity extends Activity {

	private AtomicBoolean initialized;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initialized = new AtomicBoolean(false);

		// FIXME: Localization
		this.pd = ProgressDialog.show(this,
				"Registering with PMP",
				"Please wait while the resource group registers with PMP.");
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		if (initialized.get()) {
			finish();
		}

		pd.show();

		// resolve app to be ResourceGroupApp
		Application app = getApplication();
		if (!(app instanceof ResourceGroupApp)) {
			Log.e("ResourceGroupActivity started without ResourceGroupApp.");
		} else {
			final ResourceGroupApp rga = (ResourceGroupApp) app;

			// start a new thread that registers all the resource groups
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					for (ResourceGroup rg : rga.getAllResourceGroups()) {
						rg.start(getApplicationContext());
					}
					pd.dismiss();
					ResourceGroupActivity.this.initialized.set(true);
					ResourceGroupActivity.this.finish();
				}
			});

			t.start();
		}
	}
}
