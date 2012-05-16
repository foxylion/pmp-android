package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.aidl.IContact;

/**
 * 
 * @author Andre Nguyen
 * 
 *         Dialog to send SMS or Email via ResourceGroup
 * 
 */
public class SMS_Email_Dialog extends Dialog {
    
    private Button cancel_btn;
    private Button send_btn;
    
    private EditText et_recipient;
    private EditText et_subject;
    private EditText et_body;
    
    private IContact contactRG;
    
    
    public SMS_Email_Dialog(Context context, boolean isSMS, String tel, String email, IContact contactRG) {
        super(context);
        setContentView(R.layout.dialog_sms_email);
        this.contactRG = contactRG;
        
        et_recipient = (EditText) findViewById(R.id.et_recipient);
        et_subject = (EditText) findViewById(R.id.et_subject);
        et_body = (EditText) findViewById(R.id.et_sms_body);
        
        if (isSMS) {
            setTitle("Send SMS");
            et_recipient.setText(String.valueOf(tel));
            et_subject.setEnabled(false);
        } else {
            setTitle("Send Email");
            et_recipient.setText(email);
            et_subject.setText("vHike Invitation");
        }
        
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
                        contactRG.sms(et_recipient.getText().toString(), et_body.getText().toString());
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
