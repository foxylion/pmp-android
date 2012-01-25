package de.unistuttgart.ipvs.pmp.api.handler._default;

import java.util.concurrent.Semaphore;

import android.app.Activity;
import android.app.Dialog;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.api.gui.registration.RegistrationDialog;
import de.unistuttgart.ipvs.pmp.api.gui.registration.RegistrationEventTypes;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRegistrationHandler;

/**
 * The {@link PMPDefaultRegistrationHandler} is used to display a {@link Dialog} during the registration process. The
 * whole registration including the request for initial Service Features is done by this handler.
 * 
 * @author Jakob Jarosch
 */
public class PMPDefaultRegistrationHandler extends PMPRegistrationHandler {
    
    /**
     * The corresponding registration {@link Dialog}.
     */
    private RegistrationDialog dialog;
    
    /**
     * The {@link Activity} which initiated the registration.
     */
    private Activity activity;
    
    
    /**
     * Creates a new {@link PMPDefaultRegistrationHandler}.
     * 
     * @param activity
     *            The initiating {@link Activity}
     */
    public PMPDefaultRegistrationHandler(Activity activity) {
        this.activity = activity;
    }
    
    
    @Override
    public void onRegistration() {
        super.onRegistration();
        
        createDialogIfNotExists();
        this.dialog.invokeEvent(RegistrationEventTypes.START_REGISTRATION);
    }
    
    
    @Override
    public void onBindingFailed() {
        super.onBindingFailed();
        createDialogIfNotExists();
        
        this.dialog.invokeEvent(RegistrationEventTypes.PMP_NOT_INSTALLED);
    }
    
    
    @Override
    public void onSuccess() {
        super.onSuccess();
        
        this.dialog.invokeEvent(RegistrationEventTypes.REGISTRATION_SUCCEED);
    }
    
    
    @Override
    public void onFailure(String message) {
        super.onFailure(message);
        
        this.dialog.invokeEvent(RegistrationEventTypes.REGISTRATION_FAILED, message);
    }
    
    
    /**
     * Creates the {@link Dialog} if not already one exists.
     */
    private void createDialogIfNotExists() {
        if (this.dialog == null) {
            /* 
             * Define a semaphore for locking the thread until
             * the UI-event for dialog creation is processed.
             */
            final Semaphore s = new Semaphore(0);
            
            this.activity.runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    PMPDefaultRegistrationHandler.this.dialog = new RegistrationDialog(
                            PMPDefaultRegistrationHandler.this.activity);
                    PMPDefaultRegistrationHandler.this.dialog.show();
                    s.release();
                }
            });
            
            try {
                s.acquire();
            } catch (InterruptedException e) {
                Log.e("Interrupted the RegistrationHandler", e);
            }
        }
    }
}
