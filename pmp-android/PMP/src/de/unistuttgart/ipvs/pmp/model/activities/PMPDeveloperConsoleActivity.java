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

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ToggleButton;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.activity.MainActivity;
import de.unistuttgart.ipvs.pmp.gui.placeholder.ModelProxy;
import de.unistuttgart.ipvs.pmp.gui.util.LongTaskProgressDialog;
import de.unistuttgart.ipvs.pmp.model.DatabaseOpenHelper;
import de.unistuttgart.ipvs.pmp.model.PersistenceProvider;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnector;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.AppServiceConnector;

public class PMPDeveloperConsoleActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_developer_console);
        setTitle("PMP Developer Console");
        registerListener();
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        ToggleButton mockup = (ToggleButton) findViewById(R.id.pdc_switch_mockup_btn);
        mockup.setChecked(ModelProxy.isMockup());
        setRealModelBtnStates(mockup.isChecked());
    }
    
    
    /**
     * dis/enables the buttons that only operate on the real model
     * 
     * @param isMockup
     */
    private void setRealModelBtnStates(boolean isMockup) {
        int[] ids = new int[] { R.id.pdc_precache_btn, R.id.pdc_clearcache_btn, R.id.pdc_clean_tables_btn };
        
        for (int id : ids) {
            Button b = (Button) findViewById(id);
            b.setEnabled(!isMockup);
        }
    }
    
    
    protected void registerListener() {
        /*
         * Launcher Activity
         */
        Button launcherActivity = (Button) findViewById(R.id.pdc_main_activity_btn);
        launcherActivity.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        
        /*
         * Switch mockup
         */
        ToggleButton mockup = (ToggleButton) findViewById(R.id.pdc_switch_mockup_btn);
        mockup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ModelProxy.set(isChecked, PMPDeveloperConsoleActivity.this);
                setRealModelBtnStates(isChecked);
            }
        });
        
        /*
         * precache real model
         */
        Button precache = (Button) findViewById(R.id.pdc_precache_btn);
        precache.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                PersistenceProvider.getInstance().cacheEverythingNow();
            }
        });
        
        /*
         * clearcache real model
         */
        Button clearcache = (Button) findViewById(R.id.pdc_clearcache_btn);
        clearcache.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                PersistenceProvider.getInstance().releaseCache();
            }
        });
        
        /*
         * cleantables real model
         */
        Button cleantables = (Button) findViewById(R.id.pdc_clean_tables_btn);
        cleantables.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                PersistenceProvider pp = PersistenceProvider.getInstance();
                try {
                    // it's a developer console, what'ya expect?
                    
                    Field dohf = pp.getClass().getDeclaredField("doh");
                    dohf.setAccessible(true);
                    DatabaseOpenHelper doh = (DatabaseOpenHelper) dohf.get(pp);
                    doh.cleanTables();
                    
                } catch (Throwable t) {
                    Log.e("While cleaning tables: ", t);
                }
            }
        });
        
        /*
         * bind app service
         */
        Button bindService = (Button) findViewById(R.id.pdc_appservice_bind_btn);
        bindService.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                EditText nameEdit = (EditText) findViewById(R.id.pdc_appservice_name_edit);
                testBindService(nameEdit.getText().toString());
            }
        });
    }
    
    
    private void testBindService(final String serviceName) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Binding service");
        pd.setMessage("Please wait while Android tries to talk to the service.");
        
        LongTaskProgressDialog<Void, Void, Void> ltpd = new LongTaskProgressDialog<Void, Void, Void>(pd) {
            
            @Override
            public Void run(Void... params) {
                AppServiceConnector asc = new AppServiceConnector(getApplicationContext(), serviceName);
                asc.addCallbackHandler(new AbstractConnectorCallback() {
                    
                    @Override
                    public void onConnect(AbstractConnector connector) throws RemoteException {
                        new AlertDialog.Builder(PMPDeveloperConsoleActivity.this).setMessage("Connection successful.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).setCancelable(true).show();
                    }
                    
                    
                    @Override
                    public void onBindingFailed(AbstractConnector connector) {
                        new AlertDialog.Builder(PMPDeveloperConsoleActivity.this).setMessage("Connection failed.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).setCancelable(true).show();
                    }
                });
                asc.bind(true);
                return null;
            }
        };
        ltpd.execute();
    }
}
