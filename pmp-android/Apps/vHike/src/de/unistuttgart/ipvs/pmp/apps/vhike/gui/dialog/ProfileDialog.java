package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import java.io.InputStream;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
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
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.aidl.IvHikeWebservice;

public class ProfileDialog extends Dialog {
    
    private Profile profile;
    private int profileID;
    private Controller ctrl;
    
    private Button btn_phone;
    private Button btn_sms;
    private Button btn_email;
    private Button btn_route;
    
    private Road mRoad;
    private MapView mapView;
    private IContact iContact;
    private Profile foundUser;
    
    private Activity activity;
    
    
    @SuppressWarnings("static-access")
    public ProfileDialog(IvHikeWebservice ws, Context context, int profileID, MapView mapView, IContact iContact,
            Profile foundUser) {
        super(context);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_profile);
        this.profileID = profileID;
        this.ctrl = new Controller(ws);
        this.mapView = mapView;
        this.iContact = iContact;
        this.foundUser = foundUser;
        this.activity = (Activity) context;
        
        setUpProfile();
    }
    
    
    private void setUpProfile() {
        
        /**
         * PROFILE_ID: the profile id from some vHike-User
         * TRIP_ID: TRIP_ID which is needed to rate a user from a past ride
         * 
         */
        
        this.profile = this.ctrl.getProfile(Model.getInstance().getSid(), this.profileID);
        btn_phone = (Button) findViewById(R.id.btn_phone);
        btn_sms = (Button) findViewById(R.id.btn_sms);
        btn_email = (Button) findViewById(R.id.btn_email);
        btn_route = (Button) findViewById(R.id.btn_route);
        
        TextView tv_username = (TextView) findViewById(R.id.tv_username);
        tv_username.setText(this.profile.getUsername());
        
        EditText et_firstname = (EditText) findViewById(R.id.et_firstname);
        et_firstname.setText(this.profile.getFirstname());
        
        EditText et_lastname = (EditText) findViewById(R.id.et_lastname);
        et_lastname.setText(this.profile.getLastname());
        
        EditText et_email = (EditText) findViewById(R.id.et_email);
        et_email.setText(this.profile.getEmail());
        
        EditText et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_mobile.setText(this.profile.getTel());
        
        RatingBar rb = (RatingBar) findViewById(R.id.ratingbar_profile);
        rb.setRating((float) this.profile.getRating_avg());
        
        TextView tv_rating = (TextView) findViewById(R.id.tv_rating);
        tv_rating.setText(Float.toString((float) this.profile.getRating_avg()));
        
        btn_phone.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                boolean anonymous = ctrl.isProfileAnonymous(Model.getInstance().getSid(), foundUser.getID());
                Log.i(this, foundUser.getID() + " is " + anonymous);
                if (anonymous) {
                    Toast.makeText(
                            getContext(),
                            "The user has hidden his contact information. Contacting " + foundUser.getUsername()
                                    + " is not possible", Toast.LENGTH_LONG).show();
                } else {
                    if (PMP.get(activity.getApplication()).isServiceFeatureEnabled("contactResource")) {
                        try {
                            iContact.call(foundUser.getTel());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        PMP.get(activity.getApplication()).requestServiceFeatures(activity, "contactResource");
                    }
                }
                cancel();
            }
        });
        
        btn_sms.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (PMP.get(activity.getApplication()).isServiceFeatureEnabled("contactResource")) {
                    vhikeDialogs.getInstance()
                            .getSMSDialog(getContext(), foundUser.getTel(), iContact, ctrl, foundUser).show();
                } else {
                    PMP.get(activity.getApplication()).requestServiceFeatures(activity, "contactResource");
                }
                cancel();
            }
        });
        
        btn_email.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                try {
                    String dest = parseDestination(ViewModel.getInstance().getDestination());
                    if (PMP.get(activity.getApplication()).isServiceFeatureEnabled("contactResource")) {
                        iContact.email(foundUser.getEmail(), "vHike Trip to " + dest,
                                "Hello " + foundUser.getUsername() + ",");
                    } else {
                        PMP.get(activity.getApplication()).requestServiceFeatures(activity, "contactResource");
                    }
                } catch (RemoteException e) {
                    Toast.makeText(getContext(), "Unable to send email", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                cancel();
            }
        });
        
        if (ViewModel.getInstance().isRouteDrawn(foundUser.getUsername())) {
            btn_route.setBackgroundResource(R.drawable.btn_route);
        }
        btn_route.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // if route for user already drawn: remove
                if (ViewModel.getInstance().isRouteDrawn(foundUser.getUsername())) {
                    Log.i(this, foundUser.getUsername() + " is drawn, removing DIALOG");
                    ViewModel.getInstance().removeRoute(
                            ViewModel.getInstance().getRouteOverlay(foundUser.getUsername()));
                    ViewModel.getInstance().getDrawnRoutes.put(foundUser.getUsername(), false);
                    btn_route.setBackgroundResource(R.drawable.btn_route_disabled);
                    cancel();
                } else {
                    
                    ViewModel.getInstance().clearRoutes();
                    ViewModel.getInstance().initRouteList();
                    
                    try {
                        new Thread() {
                            
                            @Override
                            public void run() {
                                PositionObject myPos = ctrl.getUserPosition(Model.getInstance().getSid(), Model
                                        .getInstance().getOwnProfile().getID());
                                PositionObject foundPos = ctrl.getUserPosition(Model.getInstance().getSid(),
                                        foundUser.getID());
                                double fromLat = myPos.getLat(), fromLon = myPos.getLon(), toLat = foundPos.getLat(), toLon = foundPos
                                        .getLon();
                                
                                String url = RoadProvider.getUrl(fromLat, fromLon, toLat, toLon);
                                InputStream is = ViewModel.getInstance().getConnection(url);
                                mRoad = RoadProvider.getRoute(is);
                                mHandler.sendEmptyMessage(0);
                            }
                        }.start();
                        
                        btn_route.setBackgroundResource(R.drawable.btn_route);
                    } catch (IllegalStateException ise) {
                        Log.i(this, ise.toString());
                        ise.printStackTrace();
                    }
                }
                cancel();
            }
        });
        
    }
    
    
    private String parseDestination(String destination) {
        String[] temp;
        if (ViewModel.getInstance().getDestinationSpinners().size() > 1) {
            String dest = destination.replaceAll(";", "-");
            Log.i(this, "Split: " + destination + ", " + dest);
            return dest;
        } else {
            
            temp = destination.split(";");
            Log.i(this, "1.Split: " + temp[1]);
            temp = temp[1].split(";");
            Log.i(this, "2.Split: " + temp[0]);
            return temp[0];
        }
    }
    
    Handler mHandler = new Handler() {
        
        public void handleMessage(android.os.Message msg) {
            Toast.makeText(getContext(), mRoad.mName + " " + mRoad.mDescription, Toast.LENGTH_LONG).show();
            
            RoadOverlay roadOverlay = new RoadOverlay(mRoad, mapView, true);
            ViewModel.getInstance().getDriverOverlayList(mapView).add(roadOverlay);
            ViewModel.getInstance().getDrawnRoutes.put(foundUser.getUsername(), true);
            ViewModel.getInstance().getAddedRoutes.put(foundUser.getUsername(), roadOverlay);
            Log.i(this, "Added Routes After Add " + ViewModel.getInstance().getAddedRoutes.size());
            mapView.invalidate();
        };
    };
    
}
