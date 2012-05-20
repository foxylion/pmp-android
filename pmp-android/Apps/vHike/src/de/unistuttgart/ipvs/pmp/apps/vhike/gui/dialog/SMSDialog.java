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
        
        et_recipient = (EditText) findViewById(R.id.et_recipient);
        et_body = (EditText) findViewById(R.id.et_sms_body);
        et_recipient.setText(String.valueOf(tel));
        
        setUpButtons();
    }
    
    
    private void setUpButtons() {
        cancel_btn = (Button) findViewById(R.id.btn_cancel);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                cancel();
            }
        });
        
        send_btn = (Button) findViewById(R.id.btn_send);
        send_btn.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                try {
                    if (et_recipient.getText().toString().length() > 0 && et_body.getText().toString().length() > 0) {
                        if (ctrl.isProfileAnonymous(Model.getInstance().getSid(), profile.getID())) {
                            Toast.makeText(
                                    getContext(),
                                    "The user has hidden his contact information. Contacting " + profile.getUsername()
                                            + " is not possible", Toast.LENGTH_LONG).show();
                        } else {
                            contactRG.sms(et_recipient.getText().toString(), et_body.getText().toString());
                        }
                    } else {
                        Toast.makeText(getContext(), "Please enter both phone number and message.", Toast.LENGTH_SHORT)
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
