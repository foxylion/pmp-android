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
import de.unistuttgart.ipvs.pmp.apps.vhike.bluetooth.gui.BluetoothActivity;
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
        this.handler = new Handler();
        
        // Show the main activity if already logged in
        if (Model.getInstance().isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivityIfNeeded(intent, Activity.RESULT_CANCELED);
            return;
        }
        
        setContentView(R.layout.activity_login);
        
        this.cbAutologin = (CheckBox) findViewById(R.id.Checkbox_Remember);
        this.etUsername = (EditText) findViewById(R.id.edit_login);
        this.etPW = (EditText) findViewById(R.id.edit_password);
        this.pbLogin = (ProgressBar) findViewById(R.id.pb_login);
        
        registerListeners();
    }
    
    
    @Override
    public void onResourceGroupReady(IInterface resourceGroup, int resourceGroupId) throws SecurityException {
        super.onResourceGroupReady(resourceGroup, resourceGroupId);
        Log.i(this, "RG ready: " + resourceGroup);
        if (rgvHike != null) {
            this.handler.post(new Runnable() {
                
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
        this.username = settings.getString("USERNAME", "");
        this.pw = settings.getString("PASSWORD", "");
        
        if (settings.getBoolean("AUTOLOGIN", false) && !settings.getBoolean("ERROR", false)
                && vHikeService.isServiceFeatureEnabled(Constants.SF_VHIKE_WEB_SERVICE)) {
            this.cbAutologin.setChecked(true);
            findViewById(R.id.layout_login).setVisibility(View.GONE);
            findViewById(R.id.layout_autologin).setVisibility(View.VISIBLE);
            this.etUsername.setEnabled(false);
            this.etPW.setEnabled(false);
            this.etUsername.setText(this.username);
            this.etPW.setText(this.pw);
            this.pbLogin.setVisibility(View.VISIBLE);
            this.loginTimer = new Timer();
            this.loginTimer.schedule(new TimerTask() {
                
                @Override
                public void run() {
                    LoginActivity.this.handler.post(new Runnable() {
                        
                        @Override
                        public void run() {
                            login();
                        }
                    });
                }
            }, 1500);
        } else {
            this.cbAutologin.setChecked(false);
            findViewById(R.id.layout_login).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_autologin).setVisibility(View.GONE);
            this.etUsername.setEnabled(true);
            this.etPW.setEnabled(true);
            if (settings.getBoolean("AUTOLOGIN", false)) {
                this.etUsername.setText(this.username);
                this.etPW.setText(this.pw);
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
                LoginActivity.this.isCanceled = true;
                if (LoginActivity.this.loginTimer != null) {
                    LoginActivity.this.loginTimer.cancel();
                }
                findViewById(R.id.layout_login).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_autologin).setVisibility(View.GONE);
                LoginActivity.this.etUsername.setEnabled(true);
                LoginActivity.this.etPW.setEnabled(true);
            }
        });
        
        Button btnLogin = (Button) findViewById(R.id.button_login);
        btnLogin.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                LoginActivity.this.username = LoginActivity.this.etUsername.getText().toString();
                LoginActivity.this.pw = LoginActivity.this.etPW.getText().toString();
                
                if (LoginActivity.this.username.equals("") || LoginActivity.this.pw.equals("")) {
                    Toast.makeText(LoginActivity.this, "Username or password field empty", Toast.LENGTH_LONG).show();
                } else {
                    
                    LoginActivity.this.isCanceled = false;
                    
                    // Save auto login state
                    SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = prefs.edit();
                    prefsEditor.putBoolean("AUTOLOGIN", LoginActivity.this.cbAutologin.isChecked());
                    prefsEditor.putString("USERNAME", LoginActivity.this.etUsername.getText().toString());
                    prefsEditor.putString("PASSWORD", LoginActivity.this.etPW.getText().toString());
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
        
        Button btnBluetooth = (Button) findViewById(R.id.button_bluetooth);
        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (username.equals("")) {
                    Toast.makeText(LoginActivity.this, "Username empty", Toast.LENGTH_SHORT).show();
                }
                
                Intent intent = new Intent(LoginActivity.this, BluetoothActivity.class);
                startActivityIfNeeded(intent, Activity.RESULT_CANCELED);
            }
        });
    }
    
    
    private void login() {
        if (this.isCanceled) {
            return;
        }
        try {
            try {
                ((Button) findViewById(R.id.button_cancel)).setEnabled(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Get resource group 
            if (getvHikeRG(this) != null) {
                Log.v(this, "Logging in");
                this.ctrl = new Controller(rgvHike);
                LoginActivity.this.findViewById(R.id.layout_login).setVisibility(View.GONE);
                LoginActivity.this.findViewById(R.id.layout_autologin).setVisibility(View.VISIBLE);
                
                boolean loggedin = this.ctrl.login(this.username, this.pw);
                
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
                    
                    if (this.cbAutologin.isChecked()) { // Save password
                        prefsEditor.putBoolean("AUTOLOGIN", this.cbAutologin.isChecked());
                        prefsEditor.putString("USERNAME", this.etUsername.getText().toString());
                        prefsEditor.putString("PASSWORD", this.etPW.getText().toString());
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
