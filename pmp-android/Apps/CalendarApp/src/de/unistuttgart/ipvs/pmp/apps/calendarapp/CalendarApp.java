package de.unistuttgart.ipvs.pmp.apps.calendarapp;

import java.io.IOException;
import java.io.InputStream;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.util.DialogManager;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector.SqlConnector;
import de.unistuttgart.ipvs.pmp.service.utils.IConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;

public class CalendarApp extends App {
    
    static {
        Log.setTagSufix("CalendarApp");
    }
    
    
    @Override
    protected String getServiceAndroidName() {
        return "de.unistuttgart.ipvs.pmp.apps.calendarapp";
    }
    
    
    @Override
    public void setActiveServiceLevel(int level) {
        Log.v("ServiceLevel set to: " + String.valueOf(level));
        Model.getInstance().setServiceLevel(level);
    }
    
    
    @Override
    protected InputStream getXMLInputStream() {
        try {
            return getAssets().open("AppInformation.xml");
        } catch (IOException e) {
            Log.e("IOException during loading App XML", e);
            return null;
        }
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see de.unistuttgart.ipvs.pmp.app.App#onRegistrationSuccess() Is called
     * when the registration was successful. The method then tries to receive
     * the initial service level from the PMP service.
     */
    @Override
    public void onRegistrationSuccess() {
        Log.d("Registration succeed");
        
        // Connector to get the initial service level
        final PMPServiceConnector connector = new PMPServiceConnector(getApplicationContext(), getSignee());
        connector.addCallbackHandler(new IConnectorCallback() {
            
            @Override
            public void disconnected() {
                Log.e("Disconnected");
            }
            
            
            @Override
            public void connected() {
                // Try to get the initial service level
                try {
                    connector.getAppService().getInitialServiceLevel();
                } catch (RemoteException e) {
                    Log.e("RemoteException during getting initial ServiceLevel", e);
                }
                DialogManager.getInstance().dismissWaitingDialog();
                connector.unbind();
            }
            
            
            @Override
            public void bindingFailed() {
                Log.e("Binding failed during getting initial service level.");
            }
        });
        
        // Connect to the service
        connector.bind();
    }
    
    
    @Override
    public void onRegistrationFailed(String message) {
        Log.d("Registration failed:" + message);
        DialogManager.getInstance().dismissWaitingDialog();
    }
    
    
    /**
     * Changes the functionality of the app according to its set ServiceLevel
     */
    public void changeFunctionalityAccordingToServiceLevel() {
        Log.d("Changing ServiceLevel to level " + String.valueOf(Model.getInstance().getServiceLevel()));
        
        int level = Model.getInstance().getServiceLevel();
        
        switch (level) {
            case 0:
                Model.getInstance().clearLocalList();
                Model.getInstance().getNewDateButton().setEnabled(false);
                break;
            case 1:
                SqlConnector.getInstance().loadDates();
                Model.getInstance().getNewDateButton().setEnabled(false);
                break;
            case 2:
                SqlConnector.getInstance().loadDates();
                Model.getInstance().getNewDateButton().setEnabled(true);
                break;
            default:
                break;
        }
    }
    
}
