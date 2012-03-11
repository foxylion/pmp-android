package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;

/**
 * LoginActivity: the startup activity for vHike and starts the registration on PMP to load resource groups
 * 
 * @author Andre Nguyen
 * 
 */
public class LoginActivity extends Activity {
    
    private String username;
    private String pw;
    private boolean remember = false;
    
    private EditText et_username;
    private EditText et_pw;
    private CheckBox cb_remember;
    
    private Controller ctrl;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
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
                        
                        Log.i(this, "LOGIN successfull");
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
    
    
    @Override
    protected void onStop() {
        super.onStop();
        
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
    
    
    @Override
    protected void onPause() {
        super.onStop();
        
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
