package de.unistuttgart.ipvs.pmp.api.handler._default;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import de.unistuttgart.ipvs.pmp.api.gui.registration.RegistrationDialog;
import de.unistuttgart.ipvs.pmp.api.gui.registration.RegistrationEventTypes;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRegistrationHandler;

public class PMPDefaultRegistrationHandler extends PMPRegistrationHandler {
    
    private RegistrationDialog dialog;
    
    private Context context;
    
    private Handler handler;
    
    
    public PMPDefaultRegistrationHandler(Activity activity) {
        this.context = activity;
        
        Looper.prepare();
        handler = new Handler();
        Looper.loop();
        Looper.myLooper().quit();
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
            handler.post(new Runnable() {
                
                @Override
                public void run() {
                    dialog = new RegistrationDialog(context);
                    dialog.show();
                }
            });
        }
    }
}
