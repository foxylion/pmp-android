package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemConnector;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemListActionType;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;

public class DialogManager {
    
    private static DialogManager instance = null;
    
    private Dialog waitingDialog;
    
    
    private DialogManager() {
    }
    
    
    public static DialogManager getInstance() {
        if (instance == null) {
            instance = new DialogManager();
        }
        return instance;
    }
    
    
    /**
     * Shows a {@link ProgressDialog} while registering the app
     */
    public void showWaitingDialog() {
        new Handler().post(new Runnable() {
            
            @Override
            public void run() {
                DialogManager.this.waitingDialog = ProgressDialog.show(Model.getInstance().getContext(), Model
                        .getInstance().getContext().getString(R.string.wait), Model.getInstance().getContext()
                        .getString(R.string.registration, true));
            }
        });
    }
    
    
    /**
     * Disposes the dialog that is shown while registering. This is called when the app is registered succesful and the
     * PMPService calls {@link CalendarApp#onRegistrationSuccess()} or {@link CalendarApp#onRegistrationFailed(String)
     * ()}
     */
    public void dismissWaitingDialog() {
        this.waitingDialog.dismiss();
    }
    
    
    /**
     * Shows a dialog when the user wants to do sth. that is not allowed in this service level
     */
    public void showServiceLevelInsufficientDialog(Context context) {
        
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.insufficent_sl))
                .setMessage(context.getString(R.string.insufficent_sl_message))
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    
                    // Close the dialog
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).setNegativeButton(context.getString(R.string.change_sl), new DialogInterface.OnClickListener() {
                    
                    public void onClick(DialogInterface dialog, int id) {
                        /*
                         * Call Privacy Level Activity with the specified Intent
                         */
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName("de.unistuttgart.ipvs.pmp",
                                "de.unistuttgart.ipvs.pmp.gui.activities.ServiceLvlActivity"));
                        intent.putExtra("connection.identifier", "de.unistuttgart.ipvs.pmp.apps.calendarapp");
                        
                        Model.getInstance().getContext().startActivity(intent);
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    
    /**
     * Show a dialog, if the appointment list is empty and the user want to export the list
     * 
     * @param context
     *            the context
     */
    public void showAppointmentsListEmptyDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.empty_list_title))
                .setMessage(context.getString(R.string.empty_list_message))
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    
                    // Close the dialog
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    
    /**
     * Show a dialog, if the entered file name is invalid
     * 
     * @param context
     *            the context
     */
    public void showInvalidFileNameDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.invalid_file_name_title))
                .setMessage(context.getString(R.string.invalid_file_name_message))
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    
                    // Close the dialog
                    public void onClick(DialogInterface dialog, int id) {
                        FileSystemConnector.getInstance().listStoredFiles(FileSystemListActionType.EXPORT);
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
