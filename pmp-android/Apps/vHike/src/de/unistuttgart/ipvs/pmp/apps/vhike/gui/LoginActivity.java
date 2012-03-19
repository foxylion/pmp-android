package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.vHikeService;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.aidl.IvHikeWebservice;

/**
 * LoginActivity: the startup activity for vHike and starts the registration on PMP to load
 * resource groups
 * 
 * @author Andre Nguyen, Dang Huynh
 * 
 */
public class LoginActivity extends Activity {
    
    private String username = "";
    private String pw = "";
    
    private EditText etUsername;
    private EditText etPW;
    private CheckBox cbAutologin;
    private ProgressBar pbLogin;
    
    private Controller ctrl;
    
    private static final String RG_vHike_NAME = "de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS";
    private static final String R_vHike_NAME = "vHikeWebserviceResource";
    
    private static final PMPResourceIdentifier R_ID = PMPResourceIdentifier.make(RG_vHike_NAME, R_vHike_NAME);
    
    Handler handler;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PMP.get(getApplication());
        handler = new Handler();
        // Show the main activity if already logged in
        if (Model.getInstance().isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivityIfNeeded(intent, Activity.RESULT_CANCELED);
            finish();
            return;
        }
        
        setContentView(R.layout.activity_login);
        
        cbAutologin = (CheckBox) findViewById(R.id.Checkbox_Remember);
        etUsername = (EditText) findViewById(R.id.edit_login);
        etPW = (EditText) findViewById(R.id.edit_password);
        pbLogin = (ProgressBar) findViewById(R.id.pb_login);
        
        registerListeners();
        
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        PMP.get().getResource(R_ID, new PMPRequestResourceHandler() {
            
            @Override
            public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder) {
                login();
            }
            
            
            @Override
            public void onBindingFailed() {
                Toast.makeText(LoginActivity.this, "Binding Resource failed", Toast.LENGTH_LONG).show();
            }
        });
        
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        username = settings.getString("USERNAME", "");
        pw = settings.getString("PASSWORD", "");
        
        if (settings.getBoolean("AUTOLOGIN", false) && !settings.getBoolean("ERROR", false)) {
            cbAutologin.setChecked(true);
            findViewById(R.id.layout_login).setVisibility(View.GONE);
            findViewById(R.id.layout_autologin).setVisibility(View.VISIBLE);
            etUsername.setEnabled(false);
            etPW.setEnabled(false);
            etUsername.setText(username);
            etPW.setText(pw);
            pbLogin.setVisibility(View.VISIBLE);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                
                @Override
                public void run() {
                    login();
                }
            }, 1000);
        } else {
            cbAutologin.setChecked(false);
            findViewById(R.id.layout_login).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_autologin).setVisibility(View.GONE);
            etUsername.setEnabled(true);
            etPW.setEnabled(true);
        }
    }
    
    
    /**
     * Set up Button for registration, starts RegisterActivity
     */
    private void registerListeners() {
        Button button_register = (Button) findViewById(R.id.button_register);
        button_register.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });
        
        Button btnCancel = (Button) findViewById(R.id.button_cancel);
        btnCancel.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                findViewById(R.id.layout_login).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_autologin).setVisibility(View.GONE);
                etUsername.setEnabled(true);
                etPW.setEnabled(true);
            }
        });
        
        Button btnLogin = (Button) findViewById(R.id.button_login);
        btnLogin.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                username = etUsername.getText().toString();
                pw = etPW.getText().toString();
                
                if (username.equals("") || pw.equals("")) {
                    Toast.makeText(LoginActivity.this, "Username or password field empty", Toast.LENGTH_LONG).show();
                } else {
                    login();
                }
            }
        });
    }
    
    
    private boolean login() {
        
        IBinder binder = PMP.get().getResourceFromCache(R_ID);
        
        if (binder == null) {
            
            this.handler.post(new Runnable() {
                
                public void run() {
                    Toast.makeText(LoginActivity.this,
                            "PMP said something like 'resource group does not exists'.", Toast.LENGTH_SHORT).show();
                }
            });
            
        }
        
        IvHikeWebservice ws = IvHikeWebservice.Stub.asInterface(binder);
        ctrl = new Controller(ws);
        try {
            findViewById(R.id.layout_login).setVisibility(View.GONE);
            findViewById(R.id.layout_autologin).setVisibility(View.VISIBLE);
            this.handler.post(new Runnable() {
                
                public void run() {
                    Toast.makeText(LoginActivity.this, "vHikeWS Resource loaded.", Toast.LENGTH_SHORT).show();
                }
            });
            
            boolean loggedin = ctrl.login(username, pw);
            
            SharedPreferences prefs = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = prefs.edit();
            
            if (loggedin) {
                prefsEditor.putBoolean("ERROR", false);
                
                Log.v(this, "LOGIN successfull");
                Intent intent2 = new Intent(this, vHikeService.class);
                startService(intent2);
                Intent intent = new Intent(this, MainActivity.class);
                startActivityIfNeeded(intent, Activity.RESULT_CANCELED);
                if (cbAutologin.isChecked()) {
                    prefsEditor.putBoolean("AUTOLOGIN", cbAutologin.isChecked());
                    prefsEditor.putString("USERNAME", etUsername.getText().toString());
                    prefsEditor.putString("PASSWORD", etPW.getText().toString());
                    prefsEditor.commit();
                } else {
                    prefsEditor.putBoolean("AUTOLOGIN", false);
                    prefsEditor.putString("USERNAME", "");
                    prefsEditor.putString("PASSWORD", "");
                    prefsEditor.commit();
                }
                LoginActivity.this.finish();
            } else {
                Toast.makeText(LoginActivity.this, "Login failed. Username or password not valid.",
                        Toast.LENGTH_LONG).show();
                prefsEditor.putBoolean("AUTOLOGIN", false);
                prefsEditor.putString("USERNAME", "");
                prefsEditor.putString("PASSWORD", "");
                prefsEditor.commit();
            }
            return loggedin;
        } catch (Exception e) {
            e.printStackTrace();
            findViewById(R.id.layout_login).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_autologin).setVisibility(View.GONE);
            return false;
        }
    }
    //    public boolean isConnected() {
    //        @SuppressWarnings("static-access")
    //        ConnectivityManager cm = (ConnectivityManager) getSystemService(LoginActivity.this.CONNECTIVITY_SERVICE);
    //        return cm.getActiveNetworkInfo().isConnected();
    //    }
}
