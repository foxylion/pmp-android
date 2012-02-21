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
package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.SpinnerDialog;

/**
 * This class provides access to all dialogs in vHike
 * 
 * @author Andre Nguyen
 * 
 */
public class vhikeDialogs {
    
    private static vhikeDialogs instance;
    
    private ProgressDialog dLogin;
    private ProgressDialog dAnnounce;
    private ProgressDialog dSearch;
    
    private UpdateData dUpdateData;
    private RideDate dRideDate;
    private RideTime dRideTime;
    
    private Wait4PickUp w4pu;
    private RateProfileConfirm rpc;
    
    
    public static vhikeDialogs getInstance() {
        if (instance == null) {
            instance = new vhikeDialogs();
        }
        return instance;
    }
    
    
    /**
     * ProgressDialog when logging in
     * 
     * @param context
     * @return login progress dialog
     */
    public ProgressDialog getLoginPD(Context context) {
        if (this.dLogin == null) {
            this.dLogin = new ProgressDialog(context);
        }
        this.dLogin.setTitle("Login");
        this.dLogin.setMessage("Logging in...");
        this.dLogin.setIndeterminate(true);
        this.dLogin.setCancelable(false);
        return this.dLogin;
    }
    
    
    public void clearLoginPD() {
        this.dLogin = null;
    }
    
    
    /**
     * ProgressDialog for driver when announcing a trip and calculating current
     * position
     * 
     * @param context
     * @return announce progress dialog
     */
    public ProgressDialog getAnnouncePD(Context context) {
        if (this.dAnnounce == null) {
            this.dAnnounce = new ProgressDialog(context);
        }
        this.dAnnounce.setTitle("Announcing trip");
        this.dAnnounce.setMessage("Getting current location...\nAnnouncing trip...");
        this.dAnnounce.setIndeterminate(true);
        this.dAnnounce.setCancelable(false);
        
        return this.dAnnounce;
    }
    
    
    public void clearAnnouncPD() {
        this.dAnnounce = null;
    }
    
    
    /**
     * ProgressDialog when searching for drivers and calculating current
     * position
     * 
     * @param context
     * @return search progress dialog
     */
    public ProgressDialog getSearchPD(Context context) {
        if (this.dSearch == null) {
            this.dSearch = new ProgressDialog(context);
        }
        this.dSearch.setTitle("Thumbs up");
        this.dSearch.setMessage("Getting current location...\nHolding thumb up...");
        this.dSearch.setIndeterminate(true);
        this.dSearch.setCancelable(false);
        
        return this.dSearch;
    }
    
    
    public void clearSearchPD() {
        this.dSearch = null;
    }
    
    
    public Dialog getUpdateDataDialog(Context mContext) {
        this.dUpdateData = new UpdateData(mContext);
        
        return this.dUpdateData;
    }
    
    
    public RideDate getRideDate(Context context) {
        if (this.dRideDate == null) {
            this.dRideDate = new RideDate(context);
        }
        return this.dRideDate;
    }
    
    
    public RideTime getRideTime(Context context) {
        if (this.dRideTime == null) {
            this.dRideTime = new RideTime(context);
        }
        return this.dRideTime;
    }
    
    
    public Wait4PickUp getW4PU(Context context) {
        if (this.w4pu == null) {
            this.w4pu = new Wait4PickUp(context);
        }
        return this.w4pu;
    }
    
    
    public RateProfileConfirm getRateProfileConfirmation(Context context, int profileID, int rating, int tripID) {
        this.rpc = new RateProfileConfirm(context, profileID, rating, tripID);
        return this.rpc;
    }
    
    
    public SpinnerDialog spDialog(Context context) {
        return new SpinnerDialog(context);
    }
}
