package de.unistuttgart.ipvs.pmp.model.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.PMPApplication;
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
     *            Message which should be displayed, if NULL, a default message is used.
     */
    protected void openWaitDialog(String message) {
        if (message == null) {
            message = "Executing the requested operations.";
        }
        
        this.dialog = ProgressDialog.show(this, "Please wait...", message, true);
        
    }
    
    
    /**
     * Close the wait dialog.
     */
    protected void hideWaitDialog() {
        if (this.dialog != null) {
            this.dialog.hide();
            this.dialog = null;
        }
    }
    
    
    protected void registerListener() {
        /*
         * Sample Data installation.
         */
        Button addSampleData = (Button) findViewById(R.id.pmp_developer_console_button_add_sample_data);
        addSampleData.setEnabled(!DatabaseSingleton.getInstance().isSampleDataInstalled());
        addSampleData.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                new SimpleAsyncTask(PMPDeveloperConsoleActivity.this.self, "Inserting sample datas.") {
                    
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
        removeSampleData.setEnabled(DatabaseSingleton.getInstance().isSampleDataInstalled());
        removeSampleData.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                new SimpleAsyncTask(PMPDeveloperConsoleActivity.this.self, "Cleaning sample datas") {
                    
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
                new SimpleAsyncTask(PMPDeveloperConsoleActivity.this.self, "Cleaning database tables") {
                    
                    @Override
                    protected void toBeExecuted() {
                        DatabaseSingleton.getInstance().getDatabaseHelper().cleanTables();
                        PMPApplication.getSignee().clearRemotePublicKeys();
                    }
                }.execute();
            }
        });
        
        /**
         * Bind Service
         */
        Button bindService = (Button) findViewById(R.id.pmp_developer_console_button_bind_service);
        bindService.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                EditText serviceName = (EditText) findViewById(R.id.pmp_developer_console_edit_service_name);
                PMPDeveloperConsoleActivity.this.self.bindService(serviceName.getText().toString());
            }
        });
    }
    
    
    private void bindService(String serviceName) {
        final ProgressBar progress = (ProgressBar) findViewById(R.id.pmp_developer_console_cicle_bind);
        final TextView status = (TextView) findViewById(R.id.pmp_developer_console_bind_status);
        
        if (serviceName == null || serviceName.length() == 0) {
            
        } else {
            progress.setVisibility(View.VISIBLE);
            status.setText("Now trying to bind " + serviceName);
            
            ServiceConnection sc = new ServiceConnection() {
                
                @Override
                public void onServiceDisconnected(ComponentName name) {
                    
                }
                
                
                @Override
                public void onServiceConnected(ComponentName name, final IBinder service) {
                    new Handler().post(new Runnable() {
                        
                        @Override
                        public void run() {
                            if (service == null) {
                                status.setText("Service has connected to ServiceConnection: NULL");
                            } else {
                                status.setText("Service has connected to ServiceConnection: "
                                        + service.getClass().getName());
                            }
                            progress.setVisibility(View.INVISIBLE);
                        }
                    });
                    
                    unbindService(this);
                }
            };
            
            Intent intent = new Intent(serviceName);
            if (bindService(intent, sc, Context.BIND_AUTO_CREATE)) {
                status.setText("Service bound was sent successfully, waiting for connection...");
            } else {
                status.setText("Service bound was sent unsuccessfully. (see LogCat)");
                progress.setVisibility(View.INVISIBLE);
            }
        }
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
        
        this.activity.openWaitDialog(this.message);
    }
    
    
    @Override
    protected Void doInBackground(Void... params) {
        toBeExecuted();
        
        return null;
    }
    
    
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        
        this.activity.registerListener();
        this.activity.hideWaitDialog();
    }
    
}
