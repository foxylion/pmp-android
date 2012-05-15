package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;

public class SMS_Email_Dialog extends Dialog {
    
    private Context context;
    
    private Button cancel_btn;
    private Button send_btn;
    
    private EditText subject;
    private EditText recipient;
    
    
    public SMS_Email_Dialog(Context context, boolean isSMS, int tel, String email) {
        super(context);
        setContentView(R.layout.dialog_sms_email);
        
        subject = (EditText) findViewById(R.id.et_subject);
        recipient = (EditText) findViewById(R.id.et_recipient);
        
        if (isSMS) {
            setTitle("Send SMS");
            recipient.setText(String.valueOf(tel));
            subject.setEnabled(false);
        } else {
            setTitle("Send Email");
            recipient.setText(email);
            subject.setText("vHike Invitation");
        }
        
        this.context = context;
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
                //                String phoneNo = "5556";
                //                String message = "MyMessage";
                //                if (phoneNo.length() > 0 && message.length() > 0) {
                //                    TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                //                    int simState = telMgr.getSimState();
                //                    switch (simState) {
                //                        case TelephonyManager.SIM_STATE_ABSENT:
                //                            displayAlert();
                //                            break;
                //                        case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                //                            // do something
                //                            break;
                //                        case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                //                            // do something
                //                            break;
                //                        case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                //                            // do something
                //                            break;
                //                        case TelephonyManager.SIM_STATE_READY:
                //                            // do something
                //                            sendSMS(phoneNo, message); // method to send message
                //                            break;
                //                        case TelephonyManager.SIM_STATE_UNKNOWN:
                //                            // do something
                //                            break;
                //                    }
                //                    
                //                } else {
                //                    Toast.makeText(context, "Please enter both phone number and message.", Toast.LENGTH_SHORT).show();
                //                }
                //                
                cancel();
            }
        });
    }
    
    
    private void displayAlert() {
        // TODO Auto-generated method stub
        
        new AlertDialog.Builder(context).setMessage("Sim card not available").setCancelable(false)
        // .setIcon(R.drawable.alert)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("I am inside ok", "ok");
                        dialog.cancel();
                    }
                })
                
                .show();
        
    }
    
    
    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);
        
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);
        
        // ---when the SMS has been sent---
        context.registerReceiver(new BroadcastReceiver() {
            
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off", Toast.LENGTH_SHORT).show();
                        break;
                
                }
            }
        }, new IntentFilter(SENT));
        
        // ---when the SMS has been delivered---
        context.registerReceiver(new BroadcastReceiver() {
            
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS delivered", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
        
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        
    }
    
}
