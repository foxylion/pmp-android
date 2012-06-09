/*
 * Copyright 2012 pmp-android development team
 * Project: vHikeApp
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
package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.TimerTask;

import android.os.Handler;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.aidl.IvHikeWebservice;

/**
 * Check for ride queries every given time interval
 * 
 * 
 * @author Alexander Wassiljew, Andre Nguyen
 * 
 */
public class Check4AcceptedOffers extends TimerTask {
    
    private Handler handler;
    private Controller ctrl;
    private ViewObject object;
    private int offer_id;
    
    
    public Check4AcceptedOffers(IvHikeWebservice ws, ViewObject object, int offer_id) {
        this.handler = new Handler();
        this.ctrl = new Controller(ws);
        this.object = object;
        this.offer_id = offer_id;
        
    }
    
    
    @Override
    public void run() {
        this.handler.post(new Runnable() {
            
            @Override
            public void run() {
                
                switch (Check4AcceptedOffers.this.ctrl.offer_accepted(Model.getInstance().getSid(),
                        Check4AcceptedOffers.this.offer_id)) {
                    case Constants.STATUS_UNREAD:
                        if (ViewModel.getInstance().acceptedIsCanceled()) {
                            Log.i(this, "UNREAD OFFER!");
                        }
                        break;
                    case Constants.STATUS_ACCEPTED:
                        Check4AcceptedOffers.this.object.setStatus(Constants.V_OBJ_SATUS_ACCEPTED);
                        ViewModel.getInstance().updateView(0);
                        Log.i(this, "ACCEPTED OFFER!");
                        cancel();
                        break;
                    case Constants.STATUS_DENIED:
                        Check4AcceptedOffers.this.object.setStatus(Constants.V_OBJ_SATUS_BANNED);
                        ViewModel.getInstance().addToBanned(Check4AcceptedOffers.this.object);
                        Log.i(this, "DENIED OFFER!");
                        ViewModel.getInstance().updateView(0);
                        cancel();
                        break;
                
                }
            }
        });
    }
    
}
