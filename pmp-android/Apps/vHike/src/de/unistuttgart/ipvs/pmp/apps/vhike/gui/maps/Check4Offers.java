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

import java.util.List;
import java.util.TimerTask;

import android.os.Handler;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.OfferObject;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.aidl.IvHikeWebservice;

/**
 * Check for incoming offers from drivers
 * 
 * @author Alexander Wassiljew, Andre Nguyen
 * 
 */
public class Check4Offers extends TimerTask {
    
    private Handler handler;
    private Controller ctrl;
    private List<OfferObject> loo;
    
    
    public Check4Offers(IvHikeWebservice ws) {
        this.handler = new Handler();
        this.ctrl = new Controller(ws);
    }
    
    
    @Override
    public void run() {
        this.handler.post(new Runnable() {
            
            @Override
            public void run() {
                
                Check4Offers.this.loo = Check4Offers.this.ctrl.viewOffers(Model.getInstance().getSid());
                ViewModel.getInstance().updateLOO(Check4Offers.this.loo);
            }
        });
    }
    
}
