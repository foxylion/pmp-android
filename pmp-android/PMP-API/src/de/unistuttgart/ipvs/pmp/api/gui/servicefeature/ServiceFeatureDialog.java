package de.unistuttgart.ipvs.pmp.api.gui.servicefeature;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.api.handler._default.PMPDefaultRequestSFHandler;

public class ServiceFeatureDialog extends Dialog {
    
    private PMPDefaultRequestSFHandler defaultRegistrationHandler;
    
    
    public ServiceFeatureDialog(Activity activity, PMPDefaultRequestSFHandler defaultRegistrationHandler) {
        super(activity);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.pmp_api_dialog_servicefeature);
        
        setCancelable(false);
        
        this.defaultRegistrationHandler = defaultRegistrationHandler;
        
        addListener();
    }
    
    
    private void addListener() {
        ((Button) findViewById(R.id.Button_Continue)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                ServiceFeatureDialog.this.dismiss();
                ServiceFeatureDialog.this.defaultRegistrationHandler.unblockHandler();
            }
        });
        
        ((Button) findViewById(R.id.Button_Close)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                ServiceFeatureDialog.this.dismiss();
                ServiceFeatureDialog.this.defaultRegistrationHandler.killServiceFeatureRequest();
                ServiceFeatureDialog.this.defaultRegistrationHandler.unblockHandler();
            }
        });
    }
}
