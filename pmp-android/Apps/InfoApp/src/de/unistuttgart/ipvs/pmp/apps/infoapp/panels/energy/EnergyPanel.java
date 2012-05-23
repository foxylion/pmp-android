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
package de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy;

import java.util.concurrent.Semaphore;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.apps.infoapp.Constants;
import de.unistuttgart.ipvs.pmp.apps.infoapp.R;
import de.unistuttgart.ipvs.pmp.apps.infoapp.common.UploadRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.IPanel;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy.data.EnergyCurrentValues;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy.data.EnergyLastBootValues;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy.data.EnergyTotalValues;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.aidl.IEnergy;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class EnergyPanel implements IPanel {
    
    private LinearLayout view;
    
    private EnergyExtListViewAdapter adapter;
    
    private Application application;
    
    private Handler handler;
    
    
    public EnergyPanel(Context context, Activity activity) {
        
        // Set the application and the handler
        this.application = activity.getApplication();
        this.handler = new Handler();
        
        // load the layout from the xml file
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = (LinearLayout) inflater.inflate(R.layout.energy_panel, null);
        
        ExpandableListView listView = (ExpandableListView) this.view.findViewById(R.id.energyPanelExpandableListView);
        this.adapter = new EnergyExtListViewAdapter(context, new EnergyCurrentValues(), new EnergyLastBootValues(),
                new EnergyTotalValues());
        listView.setAdapter(this.adapter);
        
        // Get the data
        this.update();
    }
    
    
    public View getView() {
        return this.view;
    }
    
    
    public String getTitle() {
        return "Energy";
    }
    
    
    public void update() {
        
        final PMPResourceIdentifier id = PMPResourceIdentifier.make(Constants.ENERGY_RG_IDENTIFIER,
                Constants.ENERGY_RG_RESOURCE);
        
        this.adapter.setCvEnabled(false);
        this.adapter.setLbvEnabled(false);
        this.adapter.setTvEnabled(false);
        
        PMP.get(this.application).getResource(id,
                new RequestResourceHandler(this.adapter, this.handler, this.application));
    }
    
    
    public String upload() {
        if (PMP.get().isServiceFeatureEnabled(Constants.ENERGY_SF_UPLOAD_DATA)) {
            final PMPResourceIdentifier id = PMPResourceIdentifier.make(Constants.ENERGY_RG_IDENTIFIER,
                    Constants.ENERGY_RG_RESOURCE);
            
            Semaphore s = new Semaphore(0);
            UploadRequestResourceHandler urrh = new UploadRequestResourceHandler(s);
            PMP.get(this.application).getResource(id, urrh);
            try {
                s.acquire();
                return urrh.getURL();
            } catch (InterruptedException e) {
                return null;
            }
        }
        return null;
    }
}

/**
 * The request resource handler
 * 
 * @author Marcus Vetter
 * 
 */
class RequestResourceHandler extends PMPRequestResourceHandler {
    
    private EnergyExtListViewAdapter adapter;
    private Handler handler;
    private IBinder binder;
    private Application application;
    
    
    public RequestResourceHandler(EnergyExtListViewAdapter adapter, Handler handler, Application application) {
        this.adapter = adapter;
        this.handler = handler;
    }
    
    
    @Override
    public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder, boolean isMocked) {
        this.binder = binder;
        
        new Thread() {
            
            @Override
            public void run() {
                handler.post(new Runnable() {
                    
                    public void run() {
                        IEnergy energyRG = IEnergy.Stub.asInterface(RequestResourceHandler.this.binder);
                        /*
                         * Get the current values
                         */
                        if (PMP.get(RequestResourceHandler.this.application).isServiceFeatureEnabled(
                                Constants.ENERGY_SF_CURRENT_VALUES)) {
                            EnergyCurrentValues cv = new EnergyCurrentValues();
                            try {
                                cv.setLevel(energyRG.getCurrentLevel());
                                cv.setHealth(energyRG.getCurrentHealth());
                                cv.setStatus(energyRG.getCurrentStatus());
                                cv.setPlugged(energyRG.getCurrentPlugged());
                                cv.setStatusTime(energyRG.getCurrentStatus());
                                cv.setTemperature(energyRG.getCurrentTemperature());
                                
                                RequestResourceHandler.this.adapter.setCv(cv);
                                RequestResourceHandler.this.adapter.setCvEnabled(true);
                            } catch (RemoteException e) {
                                RequestResourceHandler.this.adapter.setCvEnabled(false);
                                e.printStackTrace();
                            }
                            
                        } else {
                            RequestResourceHandler.this.adapter.setCvEnabled(false);
                        }
                        
                        /*
                         * Get the values since last boot
                         */
                        if (PMP.get(RequestResourceHandler.this.application).isServiceFeatureEnabled(
                                Constants.ENERGY_SF_LAST_BOOT_VALUES)) {
                            EnergyLastBootValues lbv = new EnergyLastBootValues();
                            try {
                                lbv.setDate(energyRG.getLastBootDate());
                                lbv.setUptime(energyRG.getLastBootUptime());
                                lbv.setUptimeBattery(energyRG.getLastBootUptimeBattery());
                                lbv.setDurationOfCharging(energyRG.getLastBootDurationOfCharging());
                                lbv.setCountOfCharging(energyRG.getLastBootCountOfCharging());
                                lbv.setRatio(energyRG.getLastBootRatio());
                                lbv.setTemperaturePeak(energyRG.getLastBootTemperaturePeak());
                                lbv.setTemperatureAverage(energyRG.getLastBootTemperatureAverage());
                                lbv.setScreenOn(energyRG.getLastBootScreenOn());
                                
                                RequestResourceHandler.this.adapter.setLbv(lbv);
                                RequestResourceHandler.this.adapter.setLbvEnabled(true);
                            } catch (RemoteException e) {
                                RequestResourceHandler.this.adapter.setLbvEnabled(false);
                                e.printStackTrace();
                            }
                            
                        } else {
                            RequestResourceHandler.this.adapter.setLbvEnabled(false);
                        }
                        
                        /*
                         * Get the total values
                         */
                        if (PMP.get(RequestResourceHandler.this.application).isServiceFeatureEnabled(
                                Constants.ENERGY_SF_TOTAL_VALUES)) {
                            EnergyTotalValues tv = new EnergyTotalValues();
                            try {
                                tv.setDate(energyRG.getTotalBootDate());
                                tv.setUptime(energyRG.getTotalUptime());
                                tv.setUptimeBattery(energyRG.getTotalUptimeBattery());
                                tv.setDurationOfCharging(energyRG.getTotalDurationOfCharging());
                                tv.setCountOfCharging(energyRG.getTotalCountOfCharging());
                                tv.setRatio(energyRG.getTotalRatio());
                                tv.setTemperaturePeak(energyRG.getTotalTemperaturePeak());
                                tv.setTemperatureAverage(energyRG.getTotalTemperatureAverage());
                                tv.setScreenOn(energyRG.getTotalScreenOn());
                                
                                RequestResourceHandler.this.adapter.setTv(tv);
                                RequestResourceHandler.this.adapter.setTvEnabled(true);
                            } catch (RemoteException e) {
                                RequestResourceHandler.this.adapter.setTvEnabled(false);
                                e.printStackTrace();
                            }
                            
                        } else {
                            RequestResourceHandler.this.adapter.setTvEnabled(false);
                        }
                        
                        RequestResourceHandler.this.adapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
        
    }
}
