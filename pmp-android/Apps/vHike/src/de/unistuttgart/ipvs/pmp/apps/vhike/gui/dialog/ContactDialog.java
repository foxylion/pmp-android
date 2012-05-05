package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.RouteOverlay;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewModel;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.PhoneCallListener;
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
    private String userName;
    private GeoPoint toGPS;
    private GeoPoint myGPS;
    
    
    public ContactDialog(Context context, MapView mapView, String userName, IContact iContact, GeoPoint myGPS) {
        super(context);
        setTitle(userName);
        setContentView(R.layout.dialog_contact);
        this.context = context;
        this.mapView = mapView;
        this.userName = userName;
        this.iContact = iContact;
        this.myGPS = myGPS;
        
        setButtons();
    }
    
    
    public void setToGPS(GeoPoint toGPS) {
        this.toGPS = toGPS;
    }
    
    
    private void setButtons() {
        
        // needed to return to activity after phone call
        PhoneCallListener phoneListener = new PhoneCallListener((Activity) this.context);
        TelephonyManager telephonyManager = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        
        this.phone = (Button) findViewById(R.id.btn_phone);
        this.phone.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                try {
                    if (ContactDialog.this.iContact == null) {
                        Log.i(this, "iContact null");
                    } else {
                        ContactDialog.this.iContact.call(5556);
                    }
                    
                } catch (RemoteException e) {
                    Log.i(this, "Failed to open dialer");
                }
                cancel();
            }
        });
        
        this.sms = (Button) findViewById(R.id.btn_sms);
        this.sms.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                try {
                    ContactDialog.this.iContact.sms(5554, "vHike Testmessage");
                } catch (RemoteException e) {
                    Log.i(this, "Failed to open sms app");
                }
            }
        });
        
        this.email = (Button) findViewById(R.id.btn_email);
        this.email.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
            }
        });
        
        this.route = (Button) findViewById(R.id.btn_route);
        if (ViewModel.getInstance().isRouteDrawn(this.userName)) {
            this.route.setBackgroundResource(R.drawable.btn_route);
        }
        this.route.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                if (ViewModel.getInstance().isRouteDrawn(ContactDialog.this.userName)) {
                    Log.i(this, ContactDialog.this.userName + " is drawn, removing DIALOG");
                    ViewModel.getInstance().removeRoute(
                            ViewModel.getInstance().getRouteOverlay(ContactDialog.this.userName));
                    ViewModel.getInstance().getRoutes.put(ContactDialog.this.userName, false);
                    ContactDialog.this.route.setBackgroundResource(R.drawable.btn_route_disabled);
                    cancel();
                } else {
                    
                    RouteOverlay routeOverlay = new RouteOverlay(ContactDialog.this.context, ContactDialog.this.myGPS,
                            ContactDialog.this.toGPS);
                    ViewModel.getInstance().getDriverOverlayList(ContactDialog.this.mapView).add(routeOverlay);
                    ViewModel.getInstance().getRoutes.put(ContactDialog.this.userName, true);
                    ViewModel.getInstance().getRouteHM.put(ContactDialog.this.userName, routeOverlay);
                    ContactDialog.this.route.setBackgroundResource(R.drawable.btn_route);
                    cancel();
                }
            }
        });
    }
    
}
