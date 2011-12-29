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

import android.app.Dialog;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.ChangeAppointmentDialog;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.util.DialogManager;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Appointment;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector.SqlConnector;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnector;
import de.unistuttgart.ipvs.pmp.service.utils.AbstractConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;

public class CalendarApp extends App {
    
    static {
        Log.setTagSufix("CalendarApp");
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see de.unistuttgart.ipvs.pmp.app.App#onRegistrationSuccess() Is called
     * when the registration was successful. The method then tries to receive
     * the initial service feature from the PMP service.
     */
    @Override
    public void onRegistrationSuccess() {
        Log.d("Registration succeed");
        
        // Connector to get the initial service feature
        final PMPServiceConnector pmpconnector = new PMPServiceConnector(getApplicationContext());
        pmpconnector.addCallbackHandler(new AbstractConnectorCallback() {
            
            @Override
            public void onConnect(AbstractConnector connector) throws RemoteException {
                pmpconnector.getAppService().getServiceFeatureUpdate(getPackageName());
                DialogManager.getInstance().dismissWaitingDialog();
                Toast.makeText(Model.getInstance().getContext(), R.string.registration_succeed, Toast.LENGTH_LONG)
                        .show();
            }
        });
        
        // Connect to the service
        pmpconnector.bind();
    }
    
    
    @Override
    public void onRegistrationFailed(String message) {
        Log.d("Registration failed:" + message);
        DialogManager.getInstance().dismissWaitingDialog();
    }
    
    
    @Override
    public IBinder getResourceBlocking(String resourceGroup, String resource) {
        return super.getResourceBlocking(resourceGroup, resource);
    }
    
    
    /**
     * Changes the functionality of the app according to its set ServiceFeature
     */
    public void changeFunctionalityAccordingToServiceFeature() {
        
        final Boolean read = this.isServiceFeatureEnabled("read");
        final Boolean write = this.isServiceFeatureEnabled("write");
        
        if (!read) {
            // no feature
            Model.getInstance().clearLocalList();
        } else {
            // Read files
            new SqlConnector().loadAppointments();
            
            //Scroll to the actual date
            Model.getInstance().getContext().getListView()
                    .setSelection(Model.getInstance().getArrayAdapter().getActualAppointmentPosition());
        }
        
        /*
         * Listener for clicking one item. Opens a new dialog where the user can
         * change the date.
         */
        Model.getInstance().getContext().getListView().setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object clicked = Model.getInstance().getArrayAdapter().getItem(position);
                if (clicked instanceof Appointment && write) {
                    Dialog changeDateDialog = new ChangeAppointmentDialog(Model.getInstance().getContext(),
                            (Appointment) clicked);
                    changeDateDialog.show();
                }
            }
        });
        
        // Update the "No appointments available" textview
        Model.getInstance().getContext().updateNoAvaiableAppointmentsTextView();
    }
    
}
