package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.Timer;

import android.view.View;
import android.view.View.OnClickListener;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.OfferObject;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.QueryObject;

public class ViewObject {
    
    private float lat;
    private float lon;
    private Profile profile;
    private QueryObject qObject;
    private OfferObject oObject;
    int status;
    
    
    public ViewObject(float lat, float lon, Profile profile) {
        super();
        this.status = Constants.V_OBJ_SATUS_FOUND;
        this.lat = lat;
        this.lon = lon;
        this.profile = profile;
    }
    
    
    public void updatePos(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }
    
    
    public float getLat() {
        return lat;
    }
    
    
    public float getLon() {
        return lon;
    }
    
    
    public Profile getProfile() {
        return profile;
    }
    
    
    public QueryObject getqObject() {
        return qObject;
    }
    
    
    public void setqObject(QueryObject qObject) {
        this.qObject = qObject;
    }
    
    
    public OfferObject getoObject() {
        return oObject;
    }
    
    
    public void setoObject(OfferObject oObject) {
        this.oObject = oObject;
    }
    
    
    public int getStatus() {
        return status;
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
    
    
    public OnClickListener getOnClickListener(int who) {
        OnClickListener listener = null;
        // 0 = driver , 1 = passenger
        final int whoAmI = who;
        
        switch (status) {
            case Constants.V_OBJ_SATUS_FOUND:
                listener = new OnClickListener() {
                    Controller ctrl = new Controller();
                    @Override
                    public void onClick(View v) {
                        if(whoAmI == 0){
                            listenerForDriver();
                        }else{
                            listenerForPassenger();
                        }
                    }
                    
                    public void listenerForDriver(){
                        
                        //STATUS_SENT, STATUS_INVALID_TRIP, STATUS_INVALID_QUERY, STATUS_ALREADY_SENT 
                        switch (ctrl.sendOffer(Model.getInstance().getSid(), Model.getInstance().getTripId(),
                                qObject.getQueryid(), "I WANT TO TAKE YOU WITH ME!")) {
                            case Constants.STATUS_SENT:
                                status = Constants.V_OBJ_SATUS_AWAIT_ACCEPTION;
                                // START TIMER HIER
                                Check4AcceptedOffers c4ao = new Check4AcceptedOffers(getViewObjectToBann());
                                Timer timer = new Timer();
                                timer.schedule(c4ao, 300, 10000);
                                Log.i(this, "Offer sent.");
                                ViewModel.getInstance().updateView();
                                break;
                            case Constants.STATUS_INVALID_TRIP:
                                Log.i(this, "Invalid trip_id in sendOffer()");
                                break;
                            case Constants.STATUS_INVALID_QUERY:
                                Log.i(this, "Invalid query_id in sendOffer()");
                                break;
                            case Constants.STATUS_ALREADY_SENT:
                                Log.i(this, "Already sent offer!");
                                break;
                        }
                    }
                    public void listenerForPassenger(){
                        if(oObject== null){
                            Log.i(this, "oObject is Null");
                        }
                        switch(ctrl.handleOffer(Model.getInstance().getSid(), oObject.getOffer_id(), true)){
//                            STATUS_HANDLED, STATUS_INVALID_OFFER, STATUS_INVALID_USER, STATUS_ERROR
                            case Constants.STATUS_HANDLED:
                                status = Constants.V_OBJ_SATUS_ACCEPTED;
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
                        Controller ctrl = new Controller();
                        if (ctrl.pick_up(Model.getInstance().getSid(), profile.getID())) {
                            Log.i(this, "Picked up user: " + profile.getID());
                            status = Constants.V_OBJ_SATUS_PICKED_UP;
                            
                            // count down one available seats
                            ViewModel.getInstance().setNewNumSeats(ViewModel.getInstance().getNumSeats() - 1);
                            ctrl.tripUpdateData(Model.getInstance().getSid(), Model.getInstance().getTripId(),
                                    ViewModel.getInstance().getNumSeats());
                        } else {
                            Log.i(this, "Not picked up user: " + profile.getID());
                        }
                        ViewModel.getInstance().updateView();
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
