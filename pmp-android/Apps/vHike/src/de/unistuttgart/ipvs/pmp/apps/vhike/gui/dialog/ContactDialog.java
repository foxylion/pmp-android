package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.RouteOverlay;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewModel;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.PhoneCallListener;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.aidl.IContact;

/**
 * 
 * @author Andre Nguyen
 * 
 *         Dialog to call, send text message, email or draw path to a hitchhiker
 */
public class ContactDialog extends Dialog {
    
    private MapView mapView;
    private Context context;
    private Button phone;
    private Button sms;
    private Button email;
    private Button route;
    private IContact iContact;
    
    
    public ContactDialog(Context context, MapView mapView, String userName, IContact iContact) {
        super(context);
        setTitle(userName);
        setContentView(R.layout.dialog_contact);
        this.context = context;
        this.mapView = mapView;
        this.iContact = iContact;
        
        setButtons();
    }
    
    
    private void setButtons() {
        
        PhoneCallListener phoneListener = new PhoneCallListener((Activity) context);
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        
        phone = (Button) findViewById(R.id.btn_phone);
        phone.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                try {
                    if (iContact == null) {
                        Log.i(this, "iContact null");
                    } else {
                        iContact.call(5556);    
                    }
                    
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                cancel();
            }
        });
        
        sms = (Button) findViewById(R.id.btn_sms);
        sms.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                PendingIntent pi = PendingIntent.getActivity(v.getContext(), 0,
                        new Intent(v.getContext(), null), 0);
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage("5556", null, "Test", pi, null);
                cancel();
            }
        });
        
        email = (Button) findViewById(R.id.btn_email);
        email.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
            }
        });
        
        route = (Button) findViewById(R.id.btn_route);
        route.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                int lat1 = (int) (37.42221000 * 1E6);
                int lng1 = (int) (122.08398400 * 1E6);
                GeoPoint from = new GeoPoint(lat1, lng1);
                int lat2 = (int) (37.40550000 * 1E6);
                int lng2 = (int) (-122.08090000 * 1E6);
                GeoPoint to = new GeoPoint(lat2, lng2);
                
                ViewModel.getInstance().getDriverOverlayList(mapView).add(new RouteOverlay(from, to, 0));
                Log.i(this, "Draw route");
                cancel();
            }
        });
    }
    
}
