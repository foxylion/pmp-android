package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
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
    public void showServiceLevelInsufficientDialog() {
        final Dialog serviceLevelDialog = new Dialog(Model.getInstance().getContext());
        serviceLevelDialog.setContentView(R.layout.serviceleveldialog);
        
        serviceLevelDialog.setTitle(Model.getInstance().getContext().getString(R.string.insufficent_sl));
        serviceLevelDialog.show();
        
        /*
         * Closes the dialog
         */
        Button ok = (Button) serviceLevelDialog.findViewById(R.id.okButton);
        ok.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                serviceLevelDialog.dismiss();
            }
        });
        
        /*
         * Calls the PMP interface with an intent
         */
        Button changeSl = (Button) serviceLevelDialog.findViewById(R.id.changeSlButton);
        changeSl.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                /*
                 * Call Privacy Level Activity with the specified Intent
                 */
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("de.unistuttgart.ipvs.pmp",
                        "de.unistuttgart.ipvs.pmp.gui.activities.ServiceLvlActivity"));
                intent.putExtra("connection.identifier", "de.unistuttgart.ipvs.pmp.apps.calendarapp");
                
                Model.getInstance().getContext().startActivity(intent);
            }
        });
    }
}
