/*
 * Copyright 2011 pmp-android development team
 * Project: SimpleApp
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
package de.unistuttgart.ipvs.pmp.apps.simpleapp.gui.activities;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.apps.simpleapp.model.Model;

public class SimpleAppActivity extends Activity {
    
    private SimpleAppActivity self = this;
    
    /**
     * The actual context
     */
    private Context appContext;
    
    
    /**
     * Called when the activity is first created. Creates the list and shows the dates.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Model.getInstance().setContext(this.self);
        
        //this.appContext = getApplicationContext();
        /*
        // Connector to check if the app is registered yet
        final PMPServiceConnector pmpconnector = new PMPServiceConnector(this.appContext);
        pmpconnector.addCallbackHandler(new AbstractConnectorCallback() {
            
            @Override
            public void onConnect(AbstractConnector connector) throws RemoteException {
                // Check if the service is registered yet
                if (!pmpconnector.getAppService().isRegistered(getPackageName())) {
                    Log.v("Registering");
                    DialogManager.getInstance().showWaitingDialog();
                    pmpconnector.getAppService().registerApp(getPackageName());
                    pmpconnector.unbind();
                } else {
                    Log.v("App registered");
                }
            }
            
            
            @Override
            public void onBindingFailed(AbstractConnector connector) {
                Looper.prepare();
                AlertDialog.Builder builder = new AlertDialog.Builder(self);
                builder.setMessage(R.string.no_register).setTitle(R.string.error).setCancelable(true)
                        .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                            
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                Looper.loop();
                
            }
        });
        // Connect to the service
        pmpconnector.bind();
        */
        TextView tv = new TextView(this);
        tv.setText("Hello, Android");
        setContentView(tv);
    }
    /*    
    @Override
    protected void onResume() {
        super.onResume();
        /*
         * Changes the functionality according to the service level that is set.
         * Will be called when the activity is started after on create and
         * called when the activity is shown again.
         */
        //((SimpleApp) getApplication()).changeFunctionalityAccordingToServiceLevel();
    /*    
	}
	*/
}
