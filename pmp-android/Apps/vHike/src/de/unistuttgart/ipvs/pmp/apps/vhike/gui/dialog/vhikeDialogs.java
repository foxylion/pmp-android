package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import java.util.Calendar;

import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.SpinnerDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * This class provides access to all dialogs in vHike
 * 
 * @author Andre Nguyen, Dang Huynh
 * 
 */
public class vhikeDialogs extends Activity {
    
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
    
    
    public ProfileDialog getProfileDialog(Context context, int profileID) {
        return new ProfileDialog(context, profileID);
    }

    /**
     * Returns a ready to use DateTimePicker-Dialog
     * 
     * @param inActivity
     *            The activity which invoke this Dialog. This Activity must implement the interface
     *            {@link IDialogFinishedCallBack}
     * @param ID
     *            The ID Dialog
     * @param c
     *            Initial date and time for the pickers
     * @return The Dialog, which must be show by calling the method show(). Title and Buttons can be set beforehand
     */
    public AlertDialog getDateTimePicker(final Activity inActivity, final int ID, Calendar cal) {
        final Context mContext = inActivity;
        
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_ride_datetime, null); //, (ViewGroup) findViewById(R.id.layout_root)
        AlertDialog.Builder builder = new Builder(mContext);

        // Set up the dialog
        if (cal == null)
            cal = Calendar.getInstance();
        DatePicker dPicker = (DatePicker) layout.findViewById(R.id.dpicker);
        dPicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        TimePicker tPicker = (TimePicker) layout.findViewById(R.id.tpicker);
        tPicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
        tPicker.setCurrentMinute(cal.get(Calendar.MINUTE));
        tPicker.setIs24HourView(DateFormat.is24HourFormat(mContext));
        
        builder.setView(layout).setTitle(R.string.dialog_pick_date_and_time)
                .setPositiveButton(mContext.getString(R.string.default_OK), new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            IDialogFinishedCallBack d = (IDialogFinishedCallBack)inActivity;
                            d.dialogFinished(ID, IDialogFinishedCallBack.POSITIVE_BUTTON);
                        } catch (Exception e) {
                            Toast.makeText(mContext, "ERROR", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                });
        
        return builder.create();
    }
}
