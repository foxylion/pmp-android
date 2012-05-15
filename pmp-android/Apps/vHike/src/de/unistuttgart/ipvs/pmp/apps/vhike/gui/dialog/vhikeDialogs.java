package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.SpinnerDialog;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.aidl.IvHikeWebservice;

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
    
    private Wait4PickUp w4pu;
    private RateProfileConfirm rpc;
    
    private ChangeServiceFeature csf;
    
    
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
    
    
    public ChangeServiceFeature getChangeSF(Context context) {
        this.csf = new ChangeServiceFeature(context);
        return this.csf;
    }
    
    
    /**
     * ProgressDialog for driver when announcing a trip and calculating current position
     * 
     * @param context
     * @return announce progress dialog
     */
    public ProgressDialog getAnnouncePD(Context context) {
        if (this.dAnnounce == null) {
            this.dAnnounce = new ProgressDialog(context);
        }
        this.dAnnounce.setTitle("Announcing trip");
        this.dAnnounce.setMessage("Getting current location...");
        this.dAnnounce.setIndeterminate(true);
        this.dAnnounce.setCancelable(false);
        
        return this.dAnnounce;
    }
    
    
    public void clearAnnouncPD() {
        this.dAnnounce = null;
    }
    
    
    /**
     * ProgressDialog when searching for drivers and calculating current position
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
    
    
    public Dialog getUpdateDataDialog(IvHikeWebservice ws, Context mContext) {
        this.dUpdateData = new UpdateData(mContext, ws);
        
        return this.dUpdateData;
    }
    
    
    public Wait4PickUp getW4PU(Context context) {
        this.w4pu = new Wait4PickUp(context);
        
        return this.w4pu;
    }
    
    
    public RateProfileConfirm getRateProfileConfirmation(IvHikeWebservice ws, Context context, int profileID,
            int rating, int tripID) {
        this.rpc = new RateProfileConfirm(ws, context, profileID, rating, tripID);
        return this.rpc;
    }
    
    
    public SpinnerDialog spDialog(Context context) {
        return new SpinnerDialog(context);
    }
    
    
    public ProfileDialog getProfileDialog(IvHikeWebservice ws, Context context, int profileID) {
        return new ProfileDialog(ws, context, profileID);
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
     * @return The Dialog, which must be show by calling the method show(). Title and Buttons can be
     *         set beforehand
     */
    public AlertDialog getDateTimePicker(final Activity inActivity, final int ID, Calendar cal) {
        final Context mContext = inActivity;
        
        // TODO If dialog already exists
        
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_ride_datetime, null); // , (ViewGroup)
                                                                             // findViewById(R.id.layout_root)
        AlertDialog.Builder builder = new Builder(mContext);
        
        // Set up the dialog
        if (cal == null) {
            cal = Calendar.getInstance();
        }
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
                            IDialogFinishedCallBack d = (IDialogFinishedCallBack) inActivity;
                            d.dialogFinished(ID, IDialogFinishedCallBack.POSITIVE_BUTTON);
                        } catch (Exception e) {
                            Toast.makeText(mContext, "ERROR", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                });
        
        return builder.create();
    }
    
    private static AlertDialog confirm;
    
    
    public static AlertDialog getConfirmationDialog(final Activity inActivity, int titleId, int messageId,
            int positiveTextId, int negativeTextId, final int callBackFunctionID) {
        final Context mContext = inActivity;
        
        if (confirm == null) {
            Builder builder = new Builder(mContext);
            confirm = builder.create();
        }
        confirm.setTitle(titleId);
        confirm.setMessage(mContext.getText(messageId));
        confirm.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getText(positiveTextId),
                new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IConfirmDialogFinishedCallBack callback = (IConfirmDialogFinishedCallBack) inActivity;
                        callback.confirmDialogPositive(callBackFunctionID);
                    }
                });
        confirm.setButton(DialogInterface.BUTTON_NEGATIVE, mContext.getText(negativeTextId),
                new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IConfirmDialogFinishedCallBack callback = (IConfirmDialogFinishedCallBack) inActivity;
                        callback.confirmDialogNegative(callBackFunctionID);
                    }
                });
        
        return confirm;
    }
    
    
    public SMS_Email_Dialog getSMSEmailDialog(Context context, boolean isSMS, int tel, String email) {
        return new SMS_Email_Dialog(context, isSMS, tel, email);
    }
    
}
