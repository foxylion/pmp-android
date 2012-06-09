package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import java.io.InputStream;

import android.app.Activity;
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
import de.unistuttgart.ipvs.pmp.api.PMP;
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
    private Activity activity;
    
    private Road mRoad;
    private int driverOrpassenger;
    private boolean isDriver;
    
    
    public ContactDialog(Context context, MapView mapView, String userName, IContact iContact,
            Profile foundUser,
            Controller ctrl, int driverOrpassenger) {
        super(context);
        setTitle(userName);
        setContentView(R.layout.dialog_contact);
        this.context = context;
        this.mapView = mapView;
        this.userName = userName;
        this.iContact = iContact;
        this.foundUser = foundUser;
        this.ctrl = ctrl;
        this.driverOrpassenger = driverOrpassenger;
        this.activity = (Activity) context;
        
        if (driverOrpassenger == 0) {
            this.isDriver = true;
        } else {
            this.isDriver = false;
        }
        
        setButtons();
    }
    
    
    private void setButtons() {
        
        this.phone = (Button) findViewById(R.id.btn_phone);
        this.phone.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                try {
                    if (ContactDialog.this.iContact == null) {
                    } else {
                        boolean anonymous = ContactDialog.this.ctrl.isProfileAnonymous(Model.getInstance()
                                .getSid(),
                                ContactDialog.this.foundUser.getID());
                        if (anonymous) {
                            Toast.makeText(
                                    getContext(),
                                    "The user has hidden his contact information. Contacting "
                                            + ContactDialog.this.foundUser.getUsername() + " is not possible",
                                    Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            if (PMP.get(ContactDialog.this.activity.getApplication())
                                    .isServiceFeatureEnabled("contactResource")) {
                                ContactDialog.this.iContact.call(ContactDialog.this.foundUser.getTel());
                            } else {
                                PMP.get(ContactDialog.this.activity.getApplication()).requestServiceFeatures(
                                        ContactDialog.this.activity,
                                        "contactResource");
                            }
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
                if (PMP.get(ContactDialog.this.activity.getApplication()).isServiceFeatureEnabled(
                        "contactResource")) {
                    if (ContactDialog.this.ctrl.isProfileAnonymous(Model.getInstance().getSid(),
                            ContactDialog.this.foundUser.getID())) {
                        Toast.makeText(ContactDialog.this.context,
                                "The user has hidden his contact information. SMS can not be sent.",
                                Toast.LENGTH_LONG).show();
                    } else {
                        vhikeDialogs
                                .getInstance()
                                .getSMSDialog(ContactDialog.this.context,
                                        ContactDialog.this.foundUser.getTel(), ContactDialog.this.iContact,
                                        ContactDialog.this.ctrl, ContactDialog.this.foundUser)
                                .show();
                    }
                } else {
                    PMP.get(ContactDialog.this.activity.getApplication()).requestServiceFeatures(
                            ContactDialog.this.activity, "contactResource");
                }
                cancel();
            }
        });
        
        this.email = (Button) findViewById(R.id.btn_email);
        this.email.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                try {
                    String dest = ViewModel.getInstance().getDestination();
                    if (PMP.get(ContactDialog.this.activity.getApplication()).isServiceFeatureEnabled(
                            "contactResource")) {
                        if (ContactDialog.this.ctrl.isProfileAnonymous(Model.getInstance().getSid(),
                                ContactDialog.this.foundUser.getID())) {
                            Toast.makeText(ContactDialog.this.context,
                                    "The user has hidden his contact information. Email can not be sent.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            ContactDialog.this.iContact.email(ContactDialog.this.foundUser.getEmail(),
                                    "vHike Trip to " + dest,
                                    "Hello " + ContactDialog.this.foundUser.getUsername() + ",");
                        }
                    } else {
                        PMP.get(ContactDialog.this.activity.getApplication())
                                .requestServiceFeatures(ContactDialog.this.activity, "contactResource");
                    }
                } catch (RemoteException e) {
                    Toast.makeText(ContactDialog.this.context, "Unable to send email", Toast.LENGTH_SHORT)
                            .show();
                    e.printStackTrace();
                }
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
                
                // if route for user already drawn: remove
                if (ViewModel.getInstance().isRouteDrawn(ContactDialog.this.userName)) {
                    ViewModel.getInstance().setBtnInfoVisibility(false);
                    ViewModel.getInstance().removeRoute(
                            ViewModel.getInstance().getRouteOverlay(ContactDialog.this.userName),
                            ContactDialog.this.isDriver);
                    ViewModel.getInstance().getDrawnRoutes.put(ContactDialog.this.userName, false);
                    ContactDialog.this.route.setBackgroundResource(R.drawable.btn_route_disabled);
                    ViewModel.getInstance().setBtnInfoVisibility(false);
                    ViewModel.getInstance().setEtInfoVisibility(false);
                    cancel();
                } else {
                    ViewModel.getInstance().clearRoutes();
                    ViewModel.getInstance().initRouteList();
                    
                    try {
                        new Thread() {
                            
                            @Override
                            public void run() {
                                try {
                                    PositionObject myPos = ContactDialog.this.ctrl.getUserPosition(Model
                                            .getInstance().getSid(),
                                            Model
                                                    .getInstance().getOwnProfile().getID());
                                    PositionObject foundPos = ContactDialog.this.ctrl.getUserPosition(Model
                                            .getInstance()
                                            .getSid(),
                                            ContactDialog.this.foundUser.getID());
                                    //                            double fromLat = 37.402283, fromLon = -122.073524, toLat = 37.422, toLon = -122.084;
                                    double fromLat = myPos.getLat(), fromLon = myPos.getLon(), toLat = foundPos
                                            .getLat(), toLon = foundPos.getLon();
                                    
                                    String url = RoadProvider.getUrl(fromLat, fromLon, toLat, toLon);
                                    InputStream is = ViewModel.getInstance().getConnection(url);
                                    ContactDialog.this.mRoad = RoadProvider.getRoute(is);
                                    ContactDialog.this.mHandler.sendEmptyMessage(0);
                                } catch (IllegalStateException e) {
                                    e.printStackTrace();
                                }
                                
                            }
                        }.start();
                        
                        ContactDialog.this.route.setBackgroundResource(R.drawable.btn_route);
                        ViewModel.getInstance().setBtnInfoVisibility(true);
                    } catch (IllegalStateException ise) {
                        Log.i(this, ise.toString());
                        ise.printStackTrace();
                    }
                }
                cancel();
            }
            
        });
    }
    
    Handler mHandler = new Handler() {
        
        @Override
        public void handleMessage(android.os.Message msg) {
            Toast.makeText(ContactDialog.this.context,
                    ContactDialog.this.mRoad.mName + " " + ContactDialog.this.mRoad.mDescription,
                    Toast.LENGTH_LONG).show();
            
            RoadOverlay roadOverlay = new RoadOverlay(ContactDialog.this.mRoad, ContactDialog.this.mapView,
                    true);
            if (ContactDialog.this.driverOrpassenger == 0) {
                ViewModel.getInstance().getDriverOverlayList(ContactDialog.this.mapView).add(roadOverlay);
            } else {
                ViewModel.getInstance().getPassengerOverlayList(ContactDialog.this.mapView).add(roadOverlay);
            }
            
            ViewModel.getInstance().getDrawnRoutes.put(ContactDialog.this.userName, true);
            ViewModel.getInstance().getAddedRoutes.put(ContactDialog.this.userName, roadOverlay);
            ContactDialog.this.mapView.invalidate();
        };
    };
    
}
