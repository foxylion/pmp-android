/*
 * Copyright 2012 pmp-android development team
 * Project: vHike
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
package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;

/**
 * LoginActivity: the startup activity for vHike and starts the registration on PMP to load resource groups
 * 
 * @author Andre Nguyen
 * 
 */
public class LoginActivity extends Activity {
    
    private static final String RG_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.location";
    private static final String R_NAME = "absoluteLocationResource";
    
    private static final PMPResourceIdentifier R_ID = PMPResourceIdentifier.make(RG_NAME, R_NAME);
    private Handler handler;
    
    private String username;
    private String pw;
    private boolean remember;
    
    private EditText et_username;
    private EditText et_pw;
    private CheckBox cb_remember;
    
    private Controller ctrl;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.handler = new Handler();
        PMP.get(getApplication());
        
        setContentView(R.layout.activity_login);
        
        this.cb_remember = (CheckBox) findViewById(R.id.Checkbox_Remember);
        Button btnLogin = (Button) findViewById(R.id.button_login);
        this.et_username = (EditText) findViewById(R.id.edit_login);
        this.et_pw = (EditText) findViewById(R.id.edit_password);
        this.ctrl = new Controller();
        
        registerLink();
        
        this.username = "";
        this.pw = "";
        
        btnLogin.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                LoginActivity.this.username = LoginActivity.this.et_username.getText().toString();
                LoginActivity.this.pw = LoginActivity.this.et_pw.getText().toString();
                
                if (LoginActivity.this.username.equals("") || LoginActivity.this.pw.equals("")) {
                    Toast.makeText(LoginActivity.this, "Username or password field empty", Toast.LENGTH_LONG).show();
                } else {
                    
                    if (LoginActivity.this.ctrl.login(LoginActivity.this.username, LoginActivity.this.pw)) {
                        
                        vhikeDialogs.getInstance().getLoginPD(LoginActivity.this).show();
                        
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                        v.getContext().startActivity(intent);
                        
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed. Username or password not valid.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        /* Request resources from PMP*/
        PMP.get().getResource(R_ID, new PMPRequestResourceHandler() {
            
            @Override
            public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder) {
                resourceCached();
            }
            
            
            @Override
            public void onBindingFailed() {
                Toast.makeText(LoginActivity.this, "Binding Resource failed", Toast.LENGTH_LONG).show();
            }
        });
        
        SharedPreferences settings = getSharedPreferences("vHikeLoginPrefs", MODE_PRIVATE);
        this.remember = settings.getBoolean("REMEMBER", false);
        this.username = settings.getString("USERNAME", "");
        this.pw = settings.getString("PASSWORD", "");
        
        if (this.remember) {
            this.et_username.setText(this.username);
            this.et_pw.setText(this.pw);
            this.cb_remember.setChecked(this.remember);
        } else {
            this.cb_remember.setChecked(this.remember);
        }
    }
    
    
    private void resourceCached() {
        IBinder binder = PMP.get().getResourceFromCache(R_ID);
        
        if (binder == null) {
            
            this.handler.post(new Runnable() {
                
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, "PMP said something like 'resource group does not exists'.",
                            Toast.LENGTH_SHORT).show();
                }
            });
            
            return;
        }
        
    };
    
    
    @Override
    protected void onStop() {
        super.onStop();
        
        IBinder binder = PMP.get().getResourceFromCache(R_ID);
        
        if (binder == null) {
            return;
        }
        
        SharedPreferences prefs = getSharedPreferences("vHikeLoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        
        if (this.cb_remember.isChecked()) {
            
            prefsEditor.putBoolean("REMEMBER", this.cb_remember.isChecked());
            prefsEditor.putString("USERNAME", this.et_username.getText().toString());
            prefsEditor.putString("PASSWORD", this.et_pw.getText().toString());
            
            prefsEditor.commit();
        } else {
            this.remember = false;
            prefsEditor.putBoolean("REMEMBER", false);
            
            prefsEditor.commit();
        }
    }
    
    
    /**
     * Set up Button for registration, starts RegisterActivity
     */
    private void registerLink() {
        Button button_register = (Button) findViewById(R.id.button_register);
        button_register.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });
    }
    
    
    public boolean isConnected() {
        @SuppressWarnings("static-access")
        ConnectivityManager cm = (ConnectivityManager) getSystemService(LoginActivity.this.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo().isConnected();
    }
    
}
