package de.unistuttgart.ipvs.pmp.api.gui.registration;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.api.IPMP;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.gui.registration.RegistrationStateListItem.State;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestServiceFeaturesHandler;

public class RegistrationDialog extends Dialog implements IRegistrationUI {
    
    private Handler handler;
    
    private RegistrationElements elements;
    
    private IPMP pmp;
    
    
    public RegistrationDialog(Context context) {
        super(context);
        
        setContentView(R.layout.pmp_api_activity_registration);
        
        setCancelable(false);
        
        this.handler = new Handler();
        this.elements = new RegistrationElements(this);
        this.pmp = PMP.get();
    }
    
    
    @Override
    public void invokeEvent(final RegistrationEventTypes eventType, final Object... parameters) {
        handler.post(new Runnable() {
            
            @Override
            public void run() {
                switch (eventType) {
                    case NO_ACITIVTY_DEFINED:
                        elements.tvFailureMissingActivity.setVisibility(View.VISIBLE);
                        elements.buttonClose.setVisibility(View.VISIBLE);
                        
                        elements.setState(1, State.FAIL);
                        break;
                    
                    case PMP_NOT_INSTALLED:
                        elements.tvFailureMissingPMP.setVisibility(View.VISIBLE);
                        elements.buttonClose.setVisibility(View.VISIBLE);
                        
                        elements.setState(1, State.FAIL);
                        elements.setState(2, State.NONE);
                        elements.setState(3, State.NONE);
                        break;
                    
                    case START_REGISTRATION:
                        elements.setState(1, State.SUCCESS);
                        elements.setState(2, State.SUCCESS);
                        elements.setState(3, State.PROCESSING);
                        break;
                    
                    case REGISTRATION_SUCCEED:
                        elements.tvSelectInitialSF.setVisibility(View.VISIBLE);
                        elements.buttonSelectInitialSF.setVisibility(View.VISIBLE);
                        
                        elements.setState(2, State.SUCCESS);
                        elements.setState(3, State.SUCCESS);
                        elements.setState(4, State.NEW);
                        break;
                    
                    case REGISTRATION_FAILED:
                        elements.tvFailureError.setVisibility(View.VISIBLE);
                        elements.tvFailureErrorMessage.setText((String) parameters[0]);
                        elements.tvFailureErrorMessage.setVisibility(View.VISIBLE);
                        elements.buttonClose.setVisibility(View.VISIBLE);
                        
                        elements.setState(2, State.FAIL);
                        elements.setState(3, State.FAIL);
                        break;
                    
                    case SF_SCREEN_OPENED:
                        elements.setState(4, State.PROCESSING);
                        pmp.requestServiceFeatures(new ArrayList<String>(), new DialogServiceFeaturesHandler(
                                RegistrationDialog.this));
                        break;
                    
                    case SF_SCREEN_CLOSED:
                        elements.tvSelectInitialSF.setVisibility(View.GONE);
                        elements.buttonSelectInitialSF.setVisibility(View.GONE);
                        elements.tvOpenApp.setVisibility(View.VISIBLE);
                        elements.buttonOpenApp.setVisibility(View.VISIBLE);
                        
                        elements.setState(4, State.SUCCESS);
                        elements.setState(5, State.NEW);
                        break;
                    
                    case OPEN_APP:
                        elements.setState(5, State.PROCESSING);
                        RegistrationDialog.this.dismiss();
                        break;
                }
            }
        });
    }
    
    
    @Override
    public void close() {
        Toast.makeText(getContext(), "Please Close the Activity", Toast.LENGTH_SHORT).show();
        dismiss();
    }
    
}

class DialogServiceFeaturesHandler extends PMPRequestServiceFeaturesHandler {
    
    private RegistrationDialog dialog;
    
    
    public DialogServiceFeaturesHandler(RegistrationDialog dialog) {
        this.dialog = dialog;
    }
    
    
    @Override
    public void onFinalize() {
        super.onFinalize();
        
        dialog.invokeEvent(RegistrationEventTypes.SF_SCREEN_CLOSED);
    }
}
