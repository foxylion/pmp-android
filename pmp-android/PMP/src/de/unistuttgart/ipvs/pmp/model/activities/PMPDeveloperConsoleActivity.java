/*
 * Copyright 2011 pmp-android development team
 * Project: PMP
 * Project-Site: http://code.google.com/p/pmp-android/
 * 
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.model.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug.MemoryInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.activities.StartActivity;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IModel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;
import de.unistuttgart.ipvs.pmp.model2.Model;
import de.unistuttgart.ipvs.pmp.model2.PersistenceProvider;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroupActivity;

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
     *            Message which should be displayed, if null, a default message is used.
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
         * Open PMP Button
         */
        Button openPMP = (Button) findViewById(R.id.pmp_developer_console_open_pmp_button);
        openPMP.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PMPDeveloperConsoleActivity.this.self.getApplicationContext(),
                        StartActivity.class));
            }
        });
        
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
        
        /**
         * Register ResourceGroup.
         */
        Button registerRg = (Button) findViewById(R.id.pmp_developer_console_button_register_rg);
        registerRg.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                final List<Intent> intents = new ArrayList<Intent>();
                
                final PackageManager pm = getPackageManager();
                List<ApplicationInfo> pkgs = pm.getInstalledApplications(0);
                for (ApplicationInfo info : pkgs) {
                    try {
                        PackageInfo pi = pm.getPackageInfo(info.packageName, PackageManager.GET_ACTIVITIES
                                | PackageManager.GET_INTENT_FILTERS);
                        if (pi.activities != null) {
                            for (ActivityInfo ai : pi.activities) {
                                if (ai.name.equals(ResourceGroupActivity.class.getName())) {
                                    Log.d(info.packageName + " :: " + ai.name);
                                    Intent i = new Intent();
                                    i.setClassName(info.packageName, ai.name);
                                    i.setPackage(info.packageName);
                                    i.putExtra("rgName", info.loadLabel(pm));
                                    Log.d(info.name);
                                    intents.add(i);
                                }
                            }
                        }
                    } catch (NameNotFoundException e) {
                        Log.e(info.packageName + " not found.", e);
                    }
                    
                }
                
                CharSequence[] items = new CharSequence[intents.size()];
                for (int i = 0; i < items.length; i++) {
                    items[i] = intents.get(i).getStringExtra("rgName");
                    Log.d("" + items[i]);
                }
                
                AlertDialog.Builder builder = new AlertDialog.Builder(PMPDeveloperConsoleActivity.this.self);
                builder.setTitle("Pick a ResourceGroup");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {
                        startActivity(intents.get(item));
                        return;
                    }
                });
                builder.create().show();
            }
        });
        
        /* *** performance tests ***/
        Button modelPerformance = (Button) findViewById(R.id.pmp_developer_console_button_performance_model);
        modelPerformance.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                performanceTests(ModelSingleton.getInstance().getModel(), null);
            }
        });
        
        Button model2Performance = (Button) findViewById(R.id.pmp_developer_console_button_performance_model2);
        model2Performance.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                performanceTests(Model.getInstance(), null);
            }
        });
        
        Button model2pcPerformance = (Button) findViewById(R.id.pmp_developer_console_button_performance_model2_pc);
        model2pcPerformance.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                performanceTests(Model.getInstance(), PersistenceProvider.getInstance());
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
                                status.setText("Service has connected to ServiceConnection: null");
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
    
    
    /**
     * @param useModel
     *            {@link IModel} to be used
     * @param persistenceProvider
     *            if available, will be called to pre-cache
     * 
     */
    private void performanceTests(IModel useModel, PersistenceProvider persistenceProvider) {
        long[] indexTimes = new long[5];
        long[] pathTimes = new long[5];
        
        // memory stuff
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        MemoryInfo[] miBefore = am.getProcessMemoryInfo(new int[] { Process.myPid() });
        
        long pPinit = 0L;
        if (persistenceProvider != null) {
            long start = System.nanoTime();
            persistenceProvider.cacheEverythingNow();
            pPinit = System.nanoTime() - start;
        }
        
        // index run = get all apps + all rgs
        for (int i = 0; i < 5; i++) {
            long start = System.nanoTime();
            
            IApp[] apps = useModel.getApps();
            for (IApp app : apps) {
                app.getName();
                app.getActiveServiceLevel();
            }
            IResourceGroup[] rgs = useModel.getResourceGroups();
            for (IResourceGroup rg : rgs) {
                rg.getDescription();
                rg.getPrivacyLevels();
            }
            
            indexTimes[i] = System.nanoTime() - start;
        }
        // path run = wild calls running through every class
        for (int i = 0; i < 5; i++) {
            long start = System.nanoTime();
            
            IResourceGroup rg = useModel.getResourceGroup("de.unistuttgart.ipvs.pmp.resourcegroups.database");
            if (rg == null) {
                throw new IllegalArgumentException();
            }
            IPrivacyLevel pl = rg.getPrivacyLevel("read");
            pl.getValue();
            rg = pl.getResourceGroup();
            IApp app = rg.getAllAppsUsingThisResourceGroup()[0];
            IPreset p = app.getAssignedPresets()[0];
            p.getType();
            p.getUsedPrivacyLevels();
            p.isAppAssigned(app);
            IServiceLevel sl = app.getServiceLevel(1);
            pl = sl.getPrivacyLevels()[0];
            rg = pl.getResourceGroup();
            
            pathTimes[i] = System.nanoTime() - start;
        }
        
        MemoryInfo[] miAfter = am.getProcessMemoryInfo(new int[] { Process.myPid() });
        
        // create the message
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append("Index-Run #");
            sb.append(i + 1);
            sb.append(" took ");
            sb.append(indexTimes[i] / 1E6);
            sb.append(" ms");
            sb.append(System.getProperty("line.separator"));
        }
        sb.append(System.getProperty("line.separator"));
        for (int i = 0; i < 5; i++) {
            sb.append("Path-Run #");
            sb.append(i + 1);
            sb.append(" took ");
            sb.append(pathTimes[i] / 1E6);
            sb.append(" ms");
            sb.append(System.getProperty("line.separator"));
        }
        sb.append(System.getProperty("line.separator"));
        if (persistenceProvider != null) {
            sb.append("Precaching: ");
            sb.append(pPinit / 1E6);
            sb.append(" ms");
            sb.append(System.getProperty("line.separator"));
        }
        
        // memory info
        int dpb = miBefore[0].dalvikPrivateDirty;
        int dpa = miAfter[0].dalvikPrivateDirty;
        sb.append("Dalvik private dirty memory: ");
        sb.append(System.getProperty("line.separator"));
        sb.append("before ");
        sb.append(dpb);
        sb.append(" kB / after ");
        sb.append(dpa);
        sb.append(" kB");
        
        // show the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(PMPDeveloperConsoleActivity.this);
        builder.setMessage(sb.toString()).setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
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
