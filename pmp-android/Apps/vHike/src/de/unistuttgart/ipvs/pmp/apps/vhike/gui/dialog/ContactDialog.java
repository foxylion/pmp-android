package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import java.io.InputStream;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewModel;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.Route.Road;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.Route.RoadOverlay;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.Route.RoadProvider;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.PositionObject;
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
    private Profile foundUser;
    private Controller ctrl;
    
    private Road mRoad;
    
    
    public ContactDialog(Context context, MapView mapView, String userName, IContact iContact, Profile foundUser,
            Controller ctrl) {
        super(context);
        setTitle(userName);
        setContentView(R.layout.dialog_contact);
        this.context = context;
        this.mapView = mapView;
        this.userName = userName;
        this.iContact = iContact;
        this.foundUser = foundUser;
        this.ctrl = ctrl;
        
        setButtons();
    }
    
    
    private void setButtons() {
        
        // needed to return to activity after phone call
        //        PhoneCallListener phoneListener = new PhoneCallListener((Activity) this.context);
        //        TelephonyManager telephonyManager = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);
        //        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        
        this.phone = (Button) findViewById(R.id.btn_phone);
        this.phone.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                try {
                    if (ContactDialog.this.iContact == null) {
                        Log.i(this, "iContact is NULL");
                    } else {
                        boolean anonymous = ctrl.isProfileAnonymous(Model.getInstance().getSid(), foundUser.getID());
                        Log.i(this, foundUser.getID() + " is " + anonymous);
                        if (anonymous) {
                            Toast.makeText(
                                    getContext(),
                                    "The user has hidden his contact information. Contacting "
                                            + foundUser.getUsername() + " is not possible", Toast.LENGTH_LONG).show();
                        } else {
                            Log.i(this, foundUser.getID() + " is " + anonymous);
                            // iContact.call(foundUser.getTel());
                            iContact.call(String.valueOf(5556));
                        }
                        
                    }
                    
                } catch (RemoteException e) {
                    Log.i(this, "Failed to call tel");
                }
                cancel();
            }
        });
        
        this.sms = (Button) findViewById(R.id.btn_sms);
        this.sms.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                vhikeDialogs.getInstance().getSMSEmailDialog(context, true, "5556", "", iContact, ctrl, foundUser)
                        .show();
                cancel();
            }
        });
        
        this.email = (Button) findViewById(R.id.btn_email);
        this.email.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                vhikeDialogs.getInstance()
                        .getSMSEmailDialog(context, false, "", "nguyen.andres@gmail.com", iContact, ctrl, foundUser)
                        .show();
                cancel();
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
                    
                    new Thread() {
                        
                        @Override
                        public void run() {
                            PositionObject myPos = ctrl.getUserPosition(Model.getInstance().getSid(), Model
                                    .getInstance().getOwnProfile().getID());
                            PositionObject foundPos = ctrl.getUserPosition(Model.getInstance().getSid(),
                                    foundUser.getID());
                            //                            double fromLat = 37.402283, fromLon = -122.073524, toLat = 37.422, toLon = -122.084;
                            double fromLat = myPos.getLat(), fromLon = myPos.getLon(), toLat = foundPos.getLat(), toLon = foundPos
                                    .getLon();
                            
                            String url = RoadProvider.getUrl(fromLat, fromLon, toLat, toLon);
                            InputStream is = ViewModel.getInstance().getConnection(url);
                            mRoad = RoadProvider.getRoute(is);
                            mHandler.sendEmptyMessage(0);
                        }
                    }.start();
                    
                    ContactDialog.this.route.setBackgroundResource(R.drawable.btn_route);
                    cancel();
                }
            }
        });
    }
    
    Handler mHandler = new Handler() {
        
        public void handleMessage(android.os.Message msg) {
            Toast.makeText(context, mRoad.mName + " " + mRoad.mDescription, Toast.LENGTH_LONG).show();
            
            RoadOverlay roadOverlay = new RoadOverlay(mRoad, mapView);
            ViewModel.getInstance().getDriverOverlayList(mapView).add(roadOverlay);
            ViewModel.getInstance().getRoutes.put(ContactDialog.this.userName, true);
            ViewModel.getInstance().getRouteHM.put(ContactDialog.this.userName, roadOverlay);
            mapView.invalidate();
            //            routeOverlays.clear();
            //            routeOverlays.add(routeOverlay);
            //            mapView.invalidate();
        };
    };
    
}
