/*
 * Copyright 2012 pmp-android development team
 * Project: vHike
 * Project-Site: http://code.google.com/p/pmp-android/
 * 
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.Timer;

import android.view.View;
import android.view.View.OnClickListener;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;
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
    ViewObject me;
    
    
    public ViewObject(float lat, float lon, Profile profile) {
        super();
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
            
            Controller ctrl = new Controller();
            
            
            @Override
            public void onClick(View v) {
                this.ctrl.handleOffer(Model.getInstance().getSid(), ViewObject.this.oObject.getOffer_id(), false);
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
                    
                    Controller ctrl = new Controller();
                    
                    
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
                        int result = this.ctrl.sendOffer(Model.getInstance().getSid(), Model.getInstance().getTripId(),
                                ViewObject.this.qObject.getQueryid(), "I WANT TO TAKE YOU WITH ME!");
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
                                Check4AcceptedOffers c4ao = new Check4AcceptedOffers(getViewObjectToBann(), result);
                                Timer timer = new Timer();
                                timer.schedule(c4ao, 300, 10000);
                                Log.i(this, "Offer sent.");
                                ViewModel.getInstance().updateView(0);
                                break;
                        }
                    }
                    
                    
                    public void listenerForPassenger(View v) {
                        if (ViewObject.this.oObject == null) {
                            Log.i(this, "oObject is Null");
                        }
                        switch (this.ctrl.handleOffer(Model.getInstance().getSid(),
                                ViewObject.this.oObject.getOffer_id(), true)) {
                        //                            STATUS_HANDLED, STATUS_INVALID_OFFER, STATUS_INVALID_USER, STATUS_ERROR
                            case Constants.STATUS_HANDLED:
                                ViewObject.this.status = Constants.V_OBJ_SATUS_ACCEPTED;
                                vhikeDialogs.getInstance().getW4PU(v.getContext()).show();
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
                        if (ctrl.pick_up(Model.getInstance().getSid(), ViewObject.this.profile.getID())) {
                            Log.i(this, "Picked up user: " + ViewObject.this.profile.getID());
                            ViewObject.this.status = Constants.V_OBJ_SATUS_PICKED_UP;
                            
                            // count down one available seats
                            ViewModel.getInstance().setNewNumSeats(ViewModel.getInstance().getNumSeats() - 1);
                            ctrl.tripUpdateData(Model.getInstance().getSid(), Model.getInstance().getTripId(),
                                    ViewModel.getInstance().getNumSeats());
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
