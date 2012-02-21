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
        
        handler = new Handler();
        PMP.get(this.getApplication());
        
        setContentView(R.layout.activity_login);
        
        cb_remember = (CheckBox) findViewById(R.id.Checkbox_Remember);
        Button btnLogin = (Button) findViewById(R.id.button_login);
        et_username = (EditText) findViewById(R.id.edit_login);
        et_pw = (EditText) findViewById(R.id.edit_password);
        ctrl = new Controller();
        
        registerLink();
        
        username = "";
        pw = "";
        
        btnLogin.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                username = et_username.getText().toString();
                pw = et_pw.getText().toString();
                
                if (username.equals("") || pw.equals("")) {
                    Toast.makeText(LoginActivity.this, "Username or password field empty", Toast.LENGTH_LONG).show();
                } else {
                    
                    if (ctrl.login(username, pw)) {
                        
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
        remember = settings.getBoolean("REMEMBER", false);
        username = settings.getString("USERNAME", "");
        pw = settings.getString("PASSWORD", "");
        
        if (remember) {
            et_username.setText(username);
            et_pw.setText(pw);
            cb_remember.setChecked(remember);
        } else {
            cb_remember.setChecked(remember);
        }
    }
    
    
    private void resourceCached() {
        IBinder binder = PMP.get().getResourceFromCache(R_ID);
        
        if (binder == null) {
            
            this.handler.post(new Runnable() {
                
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
        
        SharedPreferences prefs = this.getSharedPreferences("vHikeLoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        
        if (cb_remember.isChecked()) {
            
            prefsEditor.putBoolean("REMEMBER", cb_remember.isChecked());
            prefsEditor.putString("USERNAME", et_username.getText().toString());
            prefsEditor.putString("PASSWORD", et_pw.getText().toString());
            
            prefsEditor.commit();
        } else {
            remember = false;
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
