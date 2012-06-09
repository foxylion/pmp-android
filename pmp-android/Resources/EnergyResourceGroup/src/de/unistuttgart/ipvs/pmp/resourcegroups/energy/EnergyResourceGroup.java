/*
 * Copyright 2012 pmp-android development team
 * Project: EnergyResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.energy;

import android.content.Context;
import android.content.IntentFilter;
import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.BooleanPrivacySetting;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.broadcastreceiver.EnergyBroadcastReceiver;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.intenthandler.DeviceBootHandler;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.EnergyResource;

/**
 * This is the energy resource group
 * 
 * @author Marcus Vetter
 * 
 */
public class EnergyResourceGroup extends ResourceGroup {
    
    @SuppressWarnings("deprecation")
    public EnergyResourceGroup(IPMPConnectionInterface pmpci) {
        super(EnergyConstants.RG_PACKAGE_NAME, pmpci);
        
        // Register the resource
        registerResource(EnergyConstants.RES_ENERGY, new EnergyResource(this));
        
        initPrivacySettings();
        initReceiver();
        
        // Boot-Event received
        // Deprecated: Work-around, but works
        if (pmpci.getContext(null) instanceof Context) {
            DeviceBootHandler.handle(pmpci.getContext(null), true);
        }
    }
    
    
    /**
     * Register the privacy settings
     */
    private void initPrivacySettings() {
        registerPrivacySetting(EnergyConstants.PS_BATTERY_LEVEL, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_BATTERY_HEALTH, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_BATTERY_STATUS, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_BATTERY_PLUGGED, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_BATTERY_STATUS_TIME, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_BATTERY_TEMPERATURE, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_BATTERY_CHARGING_RATIO, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_BATTERY_CHARGING_COUNT, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_DEVICE_BATTERY_CHARGING_UPTIME, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_DEVICE_DATES, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_DEVICE_DATES_TOTAL, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_DEVICE_SCREEN, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_UPLOAD_DATA, new BooleanPrivacySetting());
    }
    
    
    /**
     * Register the broadcast receiver
     */
    private void initReceiver() {
        // Instantiate the broadcast receiver
        EnergyBroadcastReceiver ebr = new EnergyBroadcastReceiver();
        
        // Instantiate the intent filters
        IntentFilter ebrFilter = new IntentFilter();
        ebrFilter.addAction(EnergyConstants.ACTION_BATTERY_CHANGED);
        ebrFilter.addAction(EnergyConstants.ACTION_SCREEN_ON);
        ebrFilter.addAction(EnergyConstants.ACTION_SCREEN_OFF);
        ebrFilter.addAction(EnergyConstants.ACTION_SHOW_DOWN);
        
        // Add broadcast receiver
        registerReceiver(ebr, ebrFilter);
    }
}
