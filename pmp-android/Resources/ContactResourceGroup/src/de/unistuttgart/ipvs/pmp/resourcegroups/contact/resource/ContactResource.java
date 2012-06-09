package de.unistuttgart.ipvs.pmp.resourcegroups.contact.resource;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.ContactResourceGroup;

/**
 * Allows communcation through phone dialer, sms or email
 * 
 * @author andres
 * 
 */
public class ContactResource extends Resource {
    
    ContactResourceGroup contactRG;
    
    
    public ContactResource(ContactResourceGroup contactRG) {
        this.contactRG = contactRG;
    }
    
    
    @Override
    public IBinder getAndroidInterface(String appIdentifier) {
        return new ContactImpl(this.contactRG, this, appIdentifier);
    }
    
    
    @Override
    public IBinder getMockedAndroidInterface(String appIdentifier) {
        return new ContactMockImpl(this.contactRG, this, appIdentifier);
    }
    
    
    @Override
    public IBinder getCloakedAndroidInterface(String appIdentifier) {
        return new ContactCloakImpl(this.contactRG, this, appIdentifier);
    }
    
    
    public void call(String appIdentifier, String tel) {
        try {
            String url = String.valueOf(tel);
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + url));
            
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.contactRG.getContext(appIdentifier).startActivity(callIntent);
        } catch (ActivityNotFoundException e) {
            Log.i(this, "ActivityNotFoundException");
            e.printStackTrace();
        }
    }
    
    
    public void sms(String appIdentifier, String tel, String message) throws RemoteException {
        Looper.prepare();
        
        TelephonyManager telMgr = (TelephonyManager) this.contactRG.getContext(appIdentifier).getSystemService(
                Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                displayAlert(appIdentifier);
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                // do something
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                // do something
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                // do something
                break;
            case TelephonyManager.SIM_STATE_READY:
                // do something
                sendSMS(appIdentifier, tel, message);
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                // do something
                break;
        
        }
    }
    
    
    public void email(String appIdentifier, String recipient, String subject, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", recipient, null));
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.contactRG.getContext(appIdentifier).startActivity(emailIntent);
        //        ((Activity) this.contactRG.getContext(appIdentifier)).startActivityForResult(emailIntent, 0);
    }
    
    
    private void displayAlert(String appIdentifier) {
        new AlertDialog.Builder(this.contactRG.getContext(appIdentifier)).setMessage("Sim card not available")
                .setCancelable(false)
                // .setIcon(R.drawable.alert)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(this, "Sim card inserted");
                        dialog.cancel();
                    }
                }).show();
    }
    
    
    private void sendSMS(final String appIdentifier, final String phoneNumber, final String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        
        PendingIntent sentPI = PendingIntent.getBroadcast(this.contactRG.getContext(appIdentifier), 0,
                new Intent(SENT), 0);
        
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this.contactRG.getContext(appIdentifier), 0, new Intent(
                DELIVERED), 0);
        
        // when the SMS has been sent
        this.contactRG.getContext(appIdentifier).registerReceiver(new BroadcastReceiver() {
            
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(ContactResource.this.contactRG.getContext(appIdentifier), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        Log.i(this, "Sent sms to: " + phoneNumber + ", " + message);
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(ContactResource.this.contactRG.getContext(appIdentifier), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(ContactResource.this.contactRG.getContext(appIdentifier), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(ContactResource.this.contactRG.getContext(appIdentifier), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(ContactResource.this.contactRG.getContext(appIdentifier), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                
                }
            }
        }, new IntentFilter(SENT));
        
        // when the SMS has been delivered
        this.contactRG.getContext(appIdentifier).registerReceiver(new BroadcastReceiver() {
            
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(ContactResource.this.contactRG.getContext(appIdentifier), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(ContactResource.this.contactRG.getContext(appIdentifier), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
        
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        
    }
    
}
