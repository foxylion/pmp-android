package de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy;

import android.app.Application;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.apps.infoapp.Constants;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy.data.EnergyCurrentValues;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy.data.EnergyLastBootValues;
import de.unistuttgart.ipvs.pmp.apps.infoapp.panels.energy.data.EnergyTotalValues;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.aidl.IEnergy;

/**
 * The request resource handler
 * 
 * @author Marcus Vetter
 * 
 */
public class EnergyResourceRequestHandler extends PMPRequestResourceHandler {
    
    private EnergyExtListViewAdapter adapter;
    private Handler handler;
    private IBinder binder;
    private Application application;
    
    
    public EnergyResourceRequestHandler(EnergyExtListViewAdapter adapter, Handler handler, Application application) {
        this.adapter = adapter;
        this.handler = handler;
        this.application = application;
    }
    
    
    @Override
    public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder, boolean isMocked) {
        this.binder = binder;
        
        new Thread() {
            
            @Override
            public void run() {
                handler.post(new Runnable() {
                    
                    public void run() {
                        IEnergy energyRG = IEnergy.Stub.asInterface(EnergyResourceRequestHandler.this.binder);
                        /*
                         * Get the current values
                         */
                        if (PMP.get(EnergyResourceRequestHandler.this.application).isServiceFeatureEnabled(
                                Constants.ENERGY_SF_CURRENT_VALUES)) {
                            EnergyCurrentValues cv = new EnergyCurrentValues();
                            try {
                                cv.setLevel(energyRG.getCurrentLevel());
                                cv.setHealth(energyRG.getCurrentHealth());
                                cv.setStatus(energyRG.getCurrentStatus());
                                cv.setPlugged(energyRG.getCurrentPlugged());
                                cv.setStatusTime(energyRG.getCurrentStatusTime());
                                cv.setTemperature(energyRG.getCurrentTemperature());
                                
                                EnergyResourceRequestHandler.this.adapter.setCv(cv);
                                EnergyResourceRequestHandler.this.adapter.setCvEnabled(true);
                            } catch (RemoteException e) {
                                EnergyResourceRequestHandler.this.adapter.setCvEnabled(false);
                                e.printStackTrace();
                            }
                            
                        } else {
                            EnergyResourceRequestHandler.this.adapter.setCvEnabled(false);
                        }
                        
                        /*
                         * Get the values since last boot
                         */
                        if (PMP.get(EnergyResourceRequestHandler.this.application).isServiceFeatureEnabled(
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
                                
                                EnergyResourceRequestHandler.this.adapter.setLbv(lbv);
                                EnergyResourceRequestHandler.this.adapter.setLbvEnabled(true);
                            } catch (RemoteException e) {
                                EnergyResourceRequestHandler.this.adapter.setLbvEnabled(false);
                                e.printStackTrace();
                            }
                            
                        } else {
                            EnergyResourceRequestHandler.this.adapter.setLbvEnabled(false);
                        }
                        
                        /*
                         * Get the total values
                         */
                        if (PMP.get(EnergyResourceRequestHandler.this.application).isServiceFeatureEnabled(
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
                                
                                EnergyResourceRequestHandler.this.adapter.setTv(tv);
                                EnergyResourceRequestHandler.this.adapter.setTvEnabled(true);
                            } catch (RemoteException e) {
                                EnergyResourceRequestHandler.this.adapter.setTvEnabled(false);
                                e.printStackTrace();
                            }
                            
                        } else {
                            EnergyResourceRequestHandler.this.adapter.setTvEnabled(false);
                        }
                        
                        EnergyResourceRequestHandler.this.adapter.notifyDataSetChanged();
                        EnergyResourceRequestHandler.this.adapter.notifyDataSetInvalidated();
                    }
                });
            }
        }.start();
        
    }
}
