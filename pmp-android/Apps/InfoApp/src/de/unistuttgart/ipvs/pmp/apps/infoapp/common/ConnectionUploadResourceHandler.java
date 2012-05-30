/*
 * Copyright 2012 pmp-android development team
 * Project: InfoApp
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
package de.unistuttgart.ipvs.pmp.apps.infoapp.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.IBinder;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.resourcegroups.connection.IConnection;

/**
 * Upload Handler for the connetion rg
 * 
 * @author Thorsten Berberich
 * 
 */
public class ConnectionUploadResourceHandler extends AbstractRequestRessourceHandler {
    
    /**
     * Constructor
     */
    public ConnectionUploadResourceHandler(ProgressDialog dialog, Activity activity) {
        super(dialog, activity);
    }
    
    
    @Override
    public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder, boolean isMocked) {
        IConnection connectionRG = IConnection.Stub.asInterface(binder);
        try {
            String url = connectionRG.uploadData();
            this.setURL(url);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        super.dialog.dismiss();
        super.openURLwithBrowser();
    }
    
}
