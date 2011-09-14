package de.unistuttgart.ipvs.pmp.model.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;

public class PMPDeveloperConsoleActivity extends Activity {

    private PMPDeveloperConsoleActivity self = this;

    /**
     * The dialog which overlays the UI while executing.
     */
    private ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.pmp_developer_console_activity);
	setTitle("PMP Developer Console");
	registerListener();
    }

    /**
     * Open the Wait dialog with a specific message.
     * 
     * @param message
     *            Message which should be displayed, if NULL, a default message
     *            is used.
     */
    protected void openWaitDialog(String message) {
	if (message == null) {
	    message = "Executing the requested operations.";
	}

	dialog = ProgressDialog.show(this, "Please wait...", message, true);

    }

    /**
     * Close the wait dialog.
     */
    protected void hideWaitDialog() {
	if (dialog != null) {
	    dialog.hide();
	    dialog = null;
	}
    }

    protected void registerListener() {
	/*
	 * Sample Data installation.
	 */
	Button addSampleData = (Button) findViewById(R.id.pmp_developer_console_button_add_sample_data);
	addSampleData.setEnabled(!DatabaseSingleton.getInstance()
		.isSampleDataInstalled());
	addSampleData.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		new SimpleAsyncTask(self, "Inserting sample datas.") {

		    @Override
		    protected void toBeExecuted() {
			DatabaseSingleton.getInstance().createSampleData();
		    }
		}.execute();
	    }
	});

	/*
	 * Sample Data cleaning.
	 */
	Button removeSampleData = (Button) findViewById(R.id.pmp_developer_console_button_remove_sample_data);
	removeSampleData.setEnabled(DatabaseSingleton.getInstance()
		.isSampleDataInstalled());
	removeSampleData.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		new SimpleAsyncTask(self, "Cleaning sample datas") {

		    @Override
		    protected void toBeExecuted() {
			DatabaseSingleton.getInstance().removeSampleData();
		    }
		}.execute();
	    }
	});

	/**
	 * Truncate database (cleaning)
	 */
	Button truncateDatabase = (Button) findViewById(R.id.pmp_developer_console_button_truncate_database);
	truncateDatabase.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		new SimpleAsyncTask(self, "Cleaning database tables") {

		    @Override
		    protected void toBeExecuted() {
			DatabaseSingleton.getInstance().getDatabaseHelper()
				.cleanTables();
		    }
		}.execute();
	    }
	});
    }
}

/**
 * An abstract implementation of the AsyncTask.
 * 
 * @author Jakob Jarosch
 */
abstract class SimpleAsyncTask extends AsyncTask<Void, Void, Void> {

    private String message;
    private PMPDeveloperConsoleActivity activity;

    public SimpleAsyncTask(PMPDeveloperConsoleActivity activity, String message) {
	this.message = message;
	this.activity = activity;
    }

    protected abstract void toBeExecuted();

    @Override
    protected void onPreExecute() {
	super.onPreExecute();

	activity.openWaitDialog(message);
    }

    @Override
    protected Void doInBackground(Void... params) {
	toBeExecuted();

	return null;
    }

    @Override
    protected void onPostExecute(Void result) {
	super.onPostExecute(result);

	activity.registerListener();
	activity.hideWaitDialog();
    }

}