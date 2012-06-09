/*
 * Copyright 2012 pmp-android development team
 * Project: vHikeApp
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
package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.aidl.IContact;

/**
 * 
 * @author Andre Nguyen
 * 
 *         Dialog to send SMS or Email via ResourceGroup
 * 
 */
public class SMSDialog extends Dialog {
    
    private Controller ctrl;
    private Profile profile;
    
    private Button cancel_btn;
    private Button send_btn;
    
    private EditText et_recipient;
    private EditText et_body;
    
    private IContact contactRG;
    
    
    public SMSDialog(Context context, String tel, IContact contactRG, Controller ctrl, Profile profile) {
        super(context);
        setContentView(R.layout.dialog_sms);
        this.contactRG = contactRG;
        this.ctrl = ctrl;
        this.profile = profile;
        setTitle("Send SMS");
        
        this.et_recipient = (EditText) findViewById(R.id.et_recipient);
        this.et_body = (EditText) findViewById(R.id.et_sms_body);
        this.et_recipient.setText(String.valueOf(tel));
        
        setUpButtons();
    }
    
    
    private void setUpButtons() {
        this.cancel_btn = (Button) findViewById(R.id.btn_cancel);
        this.cancel_btn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                cancel();
            }
        });
        
        this.send_btn = (Button) findViewById(R.id.btn_send);
        this.send_btn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                try {
                    if (SMSDialog.this.et_recipient.getText().toString().length() > 0
                            && SMSDialog.this.et_body.getText().toString().length() > 0) {
                        if (SMSDialog.this.ctrl.isProfileAnonymous(Model.getInstance().getSid(),
                                SMSDialog.this.profile.getID())) {
                            Toast.makeText(
                                    getContext(),
                                    "The user has hidden his contact information. Contacting "
                                            + SMSDialog.this.profile.getUsername()
                                            + " is not possible", Toast.LENGTH_LONG).show();
                        } else {
                            SMSDialog.this.contactRG.sms(SMSDialog.this.et_recipient.getText().toString(),
                                    SMSDialog.this.et_body.getText().toString());
                        }
                    } else {
                        Toast.makeText(getContext(), "Please enter both phone number and message.",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                    
                } catch (RemoteException e) {
                    Toast.makeText(getContext(), "Unable to send SMS", Toast.LENGTH_SHORT).show();
                }
                
                cancel();
            }
        });
    }
    
}
