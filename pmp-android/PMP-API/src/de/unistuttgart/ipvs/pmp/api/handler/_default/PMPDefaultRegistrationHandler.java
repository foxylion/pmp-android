package de.unistuttgart.ipvs.pmp.api.handler._default;

import java.util.concurrent.Semaphore;

import android.app.Activity;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.api.gui.registration.RegistrationDialog;
import de.unistuttgart.ipvs.pmp.api.gui.registration.RegistrationEventTypes;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRegistrationHandler;

public class PMPDefaultRegistrationHandler extends PMPRegistrationHandler {
    
    private RegistrationDialog dialog;
    
    private Activity activity;
    
    
    public PMPDefaultRegistrationHandler(Activity activity) {
        this.activity = activity;
    }
    
    
    @Override
    public void onRegistration() {
        super.onRegistration();
        
        createDialogIfNotExists();
        dialog.invokeEvent(RegistrationEventTypes.START_REGISTRATION);
    }
    
    
    @Override
    public void onBindingFailed() {
        super.onBindingFailed();
        createDialogIfNotExists();
        
        dialog.invokeEvent(RegistrationEventTypes.PMP_NOT_INSTALLED);
    }
    
    
    @Override
    public void onSuccess() {
        super.onSuccess();
        
        dialog.invokeEvent(RegistrationEventTypes.REGISTRATION_SUCCEED);
    }
    
    
    @Override
    public void onFailure(String message) {
        super.onFailure(message);
        
        dialog.invokeEvent(RegistrationEventTypes.REGISTRATION_FAILED, message);
    }
    
    
    private void createDialogIfNotExists() {
        if (dialog == null) {
            final Semaphore s = new Semaphore(0);
            
            this.activity.runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    dialog = new RegistrationDialog(activity);
                    dialog.show();
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
