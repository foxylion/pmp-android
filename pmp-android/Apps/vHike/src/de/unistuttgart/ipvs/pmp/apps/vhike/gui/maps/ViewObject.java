package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.Timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.OfferObject;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.QueryObject;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.aidl.IvHikeWebservice;

public class ViewObject {
    
    private float lat;
    private float lon;
    private Profile profile;
    private QueryObject qObject;
    private OfferObject oObject;
    int status;
    ViewObject me;
    private Controller ctrl;
    IvHikeWebservice ws;
    
    
    public ViewObject(IvHikeWebservice ws, float lat, float lon, Profile profile) {
        super();
        this.ws = ws;
        this.ctrl = new Controller(ws);
        this.status = Constants.V_OBJ_SATUS_FOUND;
        this.lat = lat;
        this.lon = lon;
        this.profile = profile;
        this.me = this;
    }
    
    
    public void updatePos(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }
    
    
    public float getLat() {
        return this.lat;
    }
    
    
    public float getLon() {
        return this.lon;
    }
    
    
    public Profile getProfile() {
        return this.profile;
    }
    
    
    public QueryObject getqObject() {
        return this.qObject;
    }
    
    
    public void setqObject(QueryObject qObject) {
        this.qObject = qObject;
    }
    
    
    public OfferObject getoObject() {
        return this.oObject;
    }
    
    
    public void setoObject(OfferObject oObject) {
        this.oObject = oObject;
    }
    
    
    public int getStatus() {
        return this.status;
    }
    
    
    /**
     * Status: FOUND, INVITED, AWAIT_ACCEPTION, ACCEPTED, PICKED_UP, BANNED
     */
    public void setStatus(int status) {
        this.status = status;
    }
    
    
    public ViewObject getViewObjectToBann() {
        return this;
    }
    
    
    public OnClickListener getDenieOfferClickListener() {
        OnClickListener listener = null;
        
        listener = new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                ViewObject.this.ctrl.handleOffer(Model.getInstance().getSid(), ViewObject.this.oObject.getOffer_id(),
                        false);
                ViewObject.this.status = Constants.V_OBJ_SATUS_BANNED;
                ViewModel.getInstance().addToBanned(ViewObject.this.me);
                ViewModel.getInstance().updateView(1);
            }
        };
        
        return listener;
    }
    
    
    public OnClickListener getOnClickListener(int who) {
        OnClickListener listener = null;
        // 0 = driver , 1 = passenger
        final int whoAmI = who;
        
        switch (this.status) {
            case Constants.V_OBJ_SATUS_FOUND:
                listener = new OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        if (whoAmI == 0) {
                            listenerForDriver();
                        } else {
                            listenerForPassenger(v);
                        }
                    }
                    
                    
                    public void listenerForDriver() {
                        
                        //STATUS_SENT, STATUS_INVALID_TRIP, STATUS_INVALID_QUERY, STATUS_ALREADY_SENT
                        int result = ViewObject.this.ctrl.sendOffer(Model.getInstance().getSid(), Model.getInstance()
                                .getTripId(), ViewObject.this.qObject.getQueryid(), "I WANT TO TAKE YOU WITH ME!");
                        switch (result) {
                            case Constants.STATUS_INVALID_TRIP:
                                Log.i(this, "Invalid trip_id in sendOffer()");
                                break;
                            case Constants.STATUS_INVALID_QUERY:
                                Log.i(this, "Invalid query_id in sendOffer()");
                                break;
                            case Constants.STATUS_ALREADY_SENT:
                                Log.i(this, "Already sent offer!");
                                break;
                            default:
                                ViewObject.this.status = Constants.V_OBJ_SATUS_AWAIT_ACCEPTION;
                                // START TIMER HIER
                                Check4AcceptedOffers c4ao = new Check4AcceptedOffers(ViewObject.this.ws,
                                        getViewObjectToBann(), result);
                                Timer timer = new Timer();
                                timer.schedule(c4ao, 300, 10000);
                                Log.i(this, "Offer sent.");
                                ViewModel.getInstance().startAccepted();
                                ViewModel.getInstance().updateView(0);
                                break;
                        }
                    }
                    
                    
                    public void listenerForPassenger(final View v) {
                        if (ViewObject.this.oObject == null) {
                            Log.i(this, "oObject is Null");
                        }
                        switch (ViewObject.this.ctrl.handleOffer(Model.getInstance().getSid(),
                                ViewObject.this.oObject.getOffer_id(), true)) {
                        //                            STATUS_HANDLED, STATUS_INVALID_OFFER, STATUS_INVALID_USER, STATUS_ERROR
                            case Constants.STATUS_HANDLED:
                                ViewObject.this.status = Constants.V_OBJ_SATUS_ACCEPTED;
                                final AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                                alertDialog.setTitle("Exit");
                                alertDialog.setMessage("Please wait. The driver is on his way to pick you up...");
                                alertDialog.setIcon(R.drawable.waiting4driver);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    
                                    public void onClick(DialogInterface dialog, int which) {
                                        ViewModel.getInstance().cancelLocation();
                                        ViewModel.getInstance().cancelQuery();
                                        ViewModel.getInstance().clearDestinations();
                                        alertDialog.cancel();
                                        ((Activity) v.getContext()).finish();
                                    }
                                });
                                alertDialog.show();
                                //                                vhikeDialogs.getInstance().getW4PU(v.getContext()).show();
                                Log.i(this, "OFFER HANDLED");
                                break;
                            case Constants.STATUS_INVALID_OFFER:
                                Log.i(this, "INVALID OFFER");
                                break;
                            case Constants.STATUS_INVALID_USER:
                                Log.i(this, "INVALID USER");
                                break;
                            case Constants.STATUS_ERROR:
                                Log.i(this, "ERROR");
                                break;
                        }
                    }
                };
                break;
            case Constants.V_OBJ_SATUS_INVITED:
                listener = new OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        Log.i(this, "Already SENT");
                    }
                };
                break;
            case Constants.V_OBJ_SATUS_AWAIT_ACCEPTION:
                listener = new OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        Log.i(this, "Already SENT");
                    }
                };
                break;
            case Constants.V_OBJ_SATUS_ACCEPTED:
                listener = new OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        if (ViewObject.this.ctrl.pick_up(Model.getInstance().getSid(), ViewObject.this.profile.getID())) {
                            Log.i(this, "Picked up user: " + ViewObject.this.profile.getID());
                            ViewObject.this.status = Constants.V_OBJ_SATUS_PICKED_UP;
                            
                            // count down one available seats
                            ViewModel.getInstance().setNewNumSeats(ViewModel.getInstance().getNumSeats() - 1);
                            ViewObject.this.ctrl.tripUpdateData(Model.getInstance().getSid(), Model.getInstance()
                                    .getTripId(), ViewModel.getInstance().getNumSeats());
                        } else {
                            Log.i(this, "Not picked up user: " + ViewObject.this.profile.getID());
                        }
                        ViewModel.getInstance().updateView(0);
                    }
                };
                break;
            case Constants.V_OBJ_SATUS_PICKED_UP:
                listener = new OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        Log.i(this, "User is picked up!");
                    }
                };
                
                break;
            case Constants.V_OBJ_SATUS_BANNED:
                listener = new OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        Log.i(this, "User is banned!");
                    }
                };
                break;
        }
        return listener;
    }
}
