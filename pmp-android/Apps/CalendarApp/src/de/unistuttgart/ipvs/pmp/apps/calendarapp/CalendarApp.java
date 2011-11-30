/*
 * Copyright 2011 pmp-android development team
 * Project: CalendarApp
 * Project-Site: http://code.google.com/p/pmp-android/
 * 
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.apps.calendarapp;

import java.io.IOException;
import java.io.InputStream;

import android.app.Dialog;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.ChangeAppointmentDialog;
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
    public void updateServiceFeatures(Bundle features) {
        // TODO Auto-generated method stub
        
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
        final PMPServiceConnector connector = new PMPServiceConnector(getApplicationContext());
        connector.addCallbackHandler(new IConnectorCallback() {
            
            @Override
            public void disconnected() {
                Log.e("Disconnected");
            }
            
            
            @Override
            public void connected() {
                // Try to get the initial service level
//                try {
//                    connector.getAppService().getInitialServiceLevel();
//                } catch (RemoteException e) {
//                    Log.e("RemoteException during getting initial ServiceLevel", e);
//                }
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
        
        // Get the service level
        final int serviceLevel = Model.getInstance().getServiceLevel();
        
        if (serviceLevel == 0) {
            // null level
            Model.getInstance().clearLocalList();
        } else {
            // Read files
            SqlConnector.getInstance().loadAppointments();
        }
        
        /*
         * Listener for clicking one item. Opens a new dialog where the user can
         * change the date.
         */
        Model.getInstance().getContext().getListView().setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (serviceLevel >= 2) {
                    Dialog changeDateDialog = new ChangeAppointmentDialog(Model.getInstance().getContext(), position);
                    changeDateDialog.show();
                }
            }
        });
        
    }

}
