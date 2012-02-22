package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.SpinnerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

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
        if (dLogin == null) {
            dLogin = new ProgressDialog(context);
        }
        dLogin.setTitle("Login");
        dLogin.setMessage("Logging in...");
        dLogin.setIndeterminate(true);
        dLogin.setCancelable(false);
        return dLogin;
    }
    
    
    public void clearLoginPD() {
        dLogin = null;
    }
    
    
    /**
     * ProgressDialog for driver when announcing a trip and calculating current
     * position
     * 
     * @param context
     * @return announce progress dialog
     */
    public ProgressDialog getAnnouncePD(Context context) {
        if (dAnnounce == null) {
            dAnnounce = new ProgressDialog(context);
        }
        dAnnounce.setTitle("Announcing trip");
        dAnnounce.setMessage("Getting current location...\nAnnouncing trip...");
        dAnnounce.setIndeterminate(true);
        dAnnounce.setCancelable(false);
        
        return dAnnounce;
    }
    
    
    public void clearAnnouncPD() {
        dAnnounce = null;
    }
    
    
    /**
     * ProgressDialog when searching for drivers and calculating current
     * position
     * 
     * @param context
     * @return search progress dialog
     */
    public ProgressDialog getSearchPD(Context context) {
        if (dSearch == null) {
            dSearch = new ProgressDialog(context);
        }
        dSearch.setTitle("Thumbs up");
        dSearch.setMessage("Getting current location...\nHolding thumb up...");
        dSearch.setIndeterminate(true);
        dSearch.setCancelable(false);
        
        return dSearch;
    }
    
    
    public void clearSearchPD() {
        dSearch = null;
    }
    
    
    public Dialog getUpdateDataDialog(Context mContext) {
        dUpdateData = new UpdateData(mContext);
        
        return dUpdateData;
    }
    
    
    public RideDate getRideDate(Context context) {
        if (dRideDate == null) {
            dRideDate = new RideDate(context);
        }
        return dRideDate;
    }
    
    
    public RideTime getRideTime(Context context) {
        if (dRideTime == null) {
            dRideTime = new RideTime(context);
        }
        return dRideTime;
    }
    
    
    public Wait4PickUp getW4PU(Context context) {
        w4pu = new Wait4PickUp(context);
        
        return w4pu;
    }
    
    
    public RateProfileConfirm getRateProfileConfirmation(Context context, int profileID, int rating, int tripID) {
        rpc = new RateProfileConfirm(context, profileID, rating, tripID);
        return rpc;
    }
    
    
    public SpinnerDialog spDialog(Context context) {
        return new SpinnerDialog(context);
    }
}
