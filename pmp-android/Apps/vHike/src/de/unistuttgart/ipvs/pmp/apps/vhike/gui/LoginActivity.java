package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.vHikeService;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.ResourceGroupReadyActivity;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;

/**
 * LoginActivity: the startup activity for vHike and starts the registration on PMP to load
 * resource groups
 * 
 * @author Andre Nguyen, Dang Huynh
 * 
 */
public class LoginActivity extends ResourceGroupReadyActivity {
    
    private String username = "";
    private String pw = "";
    
    private EditText etUsername;
    private EditText etPW;
    private CheckBox cbAutologin;
    private ProgressBar pbLogin;
    
    private Controller ctrl;
    private Timer loginTimer;
    
    Handler handler;
    protected boolean isCanceled;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        PMP.get(getApplication());
        handler = new Handler();
        
        // Show the main activity if already logged in
        if (Model.getInstance().isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivityIfNeeded(intent, Activity.RESULT_CANCELED);
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
    public void onResourceGroupReady(IInterface resourceGroup, int resourceGroupId) throws SecurityException {
        super.onResourceGroupReady(resourceGroup, resourceGroupId);
        Log.i(this, "RG ready: " + resourceGroup);
        if (rgvHike != null) {
            handler.post(new Runnable() {
                
                @Override
                public void run() {
                    login();
                }
            });
        }
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        username = settings.getString("USERNAME", "");
        pw = settings.getString("PASSWORD", "");
        
        if (settings.getBoolean("AUTOLOGIN", false) && !settings.getBoolean("ERROR", false)
                && vHikeService.isServiceFeatureEnabled(Constants.SF_VHIKE_WEB_SERVICE)) {
            cbAutologin.setChecked(true);
            findViewById(R.id.layout_login).setVisibility(View.GONE);
            findViewById(R.id.layout_autologin).setVisibility(View.VISIBLE);
            etUsername.setEnabled(false);
            etPW.setEnabled(false);
            etUsername.setText(username);
            etPW.setText(pw);
            pbLogin.setVisibility(View.VISIBLE);
            loginTimer = new Timer();
            loginTimer.schedule(new TimerTask() {
                
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        
                        @Override
                        public void run() {
                            login();
                        }
                    });
                }
            }, 1500);
        } else {
            cbAutologin.setChecked(false);
            findViewById(R.id.layout_login).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_autologin).setVisibility(View.GONE);
            etUsername.setEnabled(true);
            etPW.setEnabled(true);
            if (settings.getBoolean("AUTOLOGIN", false)) {
                etUsername.setText(username);
                etPW.setText(pw);
            }
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
                isCanceled = true;
                if (loginTimer != null)
                    loginTimer.cancel();
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
                    
                    isCanceled = false;
                    
                    // Save auto login state
                    SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = prefs.edit();
                    prefsEditor.putBoolean("AUTOLOGIN", cbAutologin.isChecked());
                    prefsEditor.putString("USERNAME", etUsername.getText().toString());
                    prefsEditor.putString("PASSWORD", etPW.getText().toString());
                    prefsEditor.commit();
                    
                    // Check service feature
                    if (!vHikeService.isServiceFeatureEnabled(Constants.SF_VHIKE_WEB_SERVICE)) {
                        vHikeService.requestServiceFeature(LoginActivity.this, Constants.SF_VHIKE_WEB_SERVICE);
                        return;
                    }
                    
                    login();
                }
            }
        });
    }
    
    
    private void login() {
        if (isCanceled)
            return;
        try {
            ((Button) findViewById(R.id.button_cancel)).setEnabled(false);
            // Get resource group 
            if (getvHikeRG(this) != null) {
                Log.v(this, "Logging in");
                ctrl = new Controller(rgvHike);
                LoginActivity.this.findViewById(R.id.layout_login).setVisibility(View.GONE);
                LoginActivity.this.findViewById(R.id.layout_autologin).setVisibility(View.VISIBLE);
                
                boolean loggedin = ctrl.login(username, pw);
                
                SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = prefs.edit();
                
                if (loggedin) {
                    // Do not login automatically if ERROR is true
                    prefsEditor.putBoolean("ERROR", false);
                    
                    Log.v(this, "Successfully logged in");
                    
                    // Start vHikeService
                    Intent intent2 = new Intent(this, vHikeService.class);
                    startService(intent2);
                    
                    // Start MainActivity
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivityIfNeeded(intent, Activity.RESULT_CANCELED);
                    
                    if (cbAutologin.isChecked()) { // Save password
                        prefsEditor.putBoolean("AUTOLOGIN", cbAutologin.isChecked());
                        prefsEditor.putString("USERNAME", etUsername.getText().toString());
                        prefsEditor.putString("PASSWORD", etPW.getText().toString());
                        prefsEditor.commit();
                    } else { // Clear saved password
                        prefsEditor.putBoolean("AUTOLOGIN", false);
                        prefsEditor.putString("USERNAME", "");
                        prefsEditor.putString("PASSWORD", "");
                        prefsEditor.commit();
                    }
                    LoginActivity.this.finish();
                } else {
                    // Login failed
                    Toast.makeText(LoginActivity.this, "Login failed. Username or password not valid.",
                            Toast.LENGTH_LONG).show();
                    findViewById(R.id.layout_login).setVisibility(View.VISIBLE);
                    findViewById(R.id.layout_autologin).setVisibility(View.GONE);
                    ((Button) findViewById(R.id.button_cancel)).setEnabled(false);
                    prefsEditor.putBoolean("AUTOLOGIN", false);
                    prefsEditor.putString("USERNAME", "");
                    prefsEditor.putString("PASSWORD", "");
                    prefsEditor.commit();
                }
            }
        } catch (SecurityException se) {
            se.printStackTrace();
            findViewById(R.id.layout_login).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_autologin).setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
            findViewById(R.id.layout_login).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_autologin).setVisibility(View.GONE);
        }
    }
}
