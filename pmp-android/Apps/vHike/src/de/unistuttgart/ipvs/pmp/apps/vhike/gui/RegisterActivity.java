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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;

/**
 * Users can register an account when register form is filled in correctly
 * 
 * @author Andre Nguyen
 * 
 */
public class RegisterActivity extends Activity {
    
    EditText et_username;
    EditText et_email;
    EditText et_password;
    EditText et_pw_confirm;
    EditText et_firstname;
    EditText et_lastname;
    EditText et_mobile;
    EditText et_desc;
    
    boolean cEmail = false;
    boolean cPw = false;
    boolean correctPw = false;
    boolean cMobile = false;
    boolean registrationSuccessfull = false;
    
    private final Pattern email_pattern = Pattern.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
            + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "." + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");
    private final Pattern mobile_pattern = Pattern.compile("^[+]?[0-9]{10,13}$");
    private final Pattern pw_pattern = Pattern.compile("(.{8,20})");
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        this.et_username = (EditText) findViewById(R.id.et_username);
        this.et_email = (EditText) findViewById(R.id.et_email);
        this.et_password = (EditText) findViewById(R.id.et_pw);
        this.et_pw_confirm = (EditText) findViewById(R.id.et_pw_confirm);
        this.et_firstname = (EditText) findViewById(R.id.et_firstname);
        this.et_lastname = (EditText) findViewById(R.id.et_lastname);
        this.et_mobile = (EditText) findViewById(R.id.et_mobile);
        this.et_desc = (EditText) findViewById(R.id.et_description);
        
        validator();
        register();
        
    }
    
    
    private void validator() {
        this.et_password.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void afterTextChanged(Editable arg0) {
                if (!RegisterActivity.this.pw_pattern.matcher(RegisterActivity.this.et_password.getText().toString())
                        .matches()) {
                    RegisterActivity.this.et_password.setError("Password length must be at least 8 characters");
                    RegisterActivity.this.correctPw = false;
                } else {
                    RegisterActivity.this.correctPw = true;
                }
            }
            
            
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            
            
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            
        });
        
        this.et_pw_confirm.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void afterTextChanged(Editable arg0) {
                if (RegisterActivity.this.et_pw_confirm.getText().toString()
                        .equals(RegisterActivity.this.et_password.getText().toString())
                        && RegisterActivity.this.correctPw) {
                    RegisterActivity.this.cPw = true;
                } else {
                    RegisterActivity.this.cPw = false;
                    RegisterActivity.this.et_pw_confirm.setError("Password wrong or do not match");
                }
            }
            
            
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            
            
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            
        });
        
        this.et_email.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void afterTextChanged(Editable arg0) {
                if (!RegisterActivity.this.email_pattern.matcher(RegisterActivity.this.et_email.getText().toString())
                        .matches()) {
                    RegisterActivity.this.et_email.setError("Invalid email");
                    RegisterActivity.this.cEmail = false;
                } else {
                    RegisterActivity.this.cEmail = true;
                }
                
            }
            
            
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                
            }
            
            
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                
            }
            
        });
        
        this.et_mobile.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void afterTextChanged(Editable s) {
                if (!RegisterActivity.this.mobile_pattern.matcher(RegisterActivity.this.et_mobile.getText().toString())
                        .matches()) {
                    RegisterActivity.this.et_mobile.setError("Invalid phone number");
                    RegisterActivity.this.cMobile = false;
                } else {
                    RegisterActivity.this.cMobile = true;
                }
            }
            
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
                
            }
            
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                
            }
            
        });
        
    }
    
    
    private void register() {
        Button register = (Button) findViewById(R.id.Button_Register);
        register.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                Controller ctrl = new Controller();
                Map<String, String> map = new HashMap<String, String>();
                
                EditText et_username = (EditText) findViewById(R.id.et_username);
                
                RegisterActivity.this.et_email = (EditText) findViewById(R.id.et_email);
                RegisterActivity.this.et_firstname = (EditText) findViewById(R.id.et_firstname);
                RegisterActivity.this.et_lastname = (EditText) findViewById(R.id.et_lastname);
                RegisterActivity.this.et_mobile = (EditText) findViewById(R.id.et_mobile);
                RegisterActivity.this.et_desc = (EditText) findViewById(R.id.et_description);
                
                map.put("username", et_username.getText().toString());
                map.put("password", RegisterActivity.this.et_password.getText().toString());
                map.put("email", RegisterActivity.this.et_email.getText().toString());
                map.put("firstname", RegisterActivity.this.et_firstname.getText().toString());
                map.put("lastname", RegisterActivity.this.et_lastname.getText().toString());
                map.put("tel", RegisterActivity.this.et_mobile.getText().toString());
                map.put("description", RegisterActivity.this.et_desc.getText().toString());
                
                if (validRegistrationForm(RegisterActivity.this.cMobile, RegisterActivity.this.cEmail,
                        RegisterActivity.this.cPw)) {
                    switch (ctrl.register(map)) {
                        case Constants.REG_STAT_USED_USERNAME:
                            Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                            break;
                        case Constants.REG_STAT_USED_MAIL:
                            Toast.makeText(RegisterActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                            break;
                        case Constants.REG_STAT_REGISTERED:
                            Toast.makeText(RegisterActivity.this,
                                    "Registration send.\nValidate your email to finish registration",
                                    Toast.LENGTH_SHORT).show();
                            RegisterActivity.this.finish();
                            break;
                        case Constants.REG_NOT_SUCCESSFUL:
                            Toast.makeText(RegisterActivity.this, "Registration failed. Please check input",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                    
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed. Please check input", Toast.LENGTH_LONG)
                            .show();
                }
                
            }
        });
    }
    
    
    /**
     * Check if users input is correct
     * 
     * @param mobile
     * @param email
     * @param pw
     * @return rather user input is correct or invalid
     */
    private boolean validRegistrationForm(boolean mobile, boolean email, boolean pw) {
        if (mobile && email && pw) {
            return true;
        } else {
            return false;
        }
    }
    
}
