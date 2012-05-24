package de.unistuttgart.ipvs.pmp.resourcegroups.energy;

import android.content.IntentFilter;
import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.BooleanPrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.EnumPrivacySetting;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.broadcastreceiver.EnergyBroadcastReceiver;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.EnergyResource;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.privacysettingenum.PSBatteryTemperatureEnum;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.privacysettingenum.PSDeviceBatteryChargingUptimeEnum;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.privacysettingenum.PSDeviceDatesEnum;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class EnergyResourceGroup extends ResourceGroup {
    
    public EnergyResourceGroup(IPMPConnectionInterface pmpci) {
        super(EnergyConstants.RG_PACKAGE_NAME, pmpci);
        
        // Register the resource
        registerResource(EnergyConstants.RES_ENERGY, new EnergyResource(this));
        
        // Register the privacy settings
        registerPrivacySetting(EnergyConstants.PS_BATTERY_LEVEL, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_BATTERY_HEALTH, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_BATTERY_STATUS, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_BATTERY_PLUGGED, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_BATTERY_STATUS_TIME, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_BATTERY_TEMPERATURE,
                new EnumPrivacySetting<PSBatteryTemperatureEnum>(PSBatteryTemperatureEnum.class));
        registerPrivacySetting(EnergyConstants.PS_BATTERY_CHARGING_RATIO, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_BATTERY_CHARGING_COUNT, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_DEVICE_BATTERY_CHARGING_UPTIME,
                new EnumPrivacySetting<PSDeviceBatteryChargingUptimeEnum>(PSDeviceBatteryChargingUptimeEnum.class));
        registerPrivacySetting(EnergyConstants.PS_DEVICE_DATES, new EnumPrivacySetting<PSDeviceDatesEnum>(
                PSDeviceDatesEnum.class));
        registerPrivacySetting(EnergyConstants.PS_DEVICE_SCREEN, new BooleanPrivacySetting());
        registerPrivacySetting(EnergyConstants.PS_UPLOAD_DATA, new BooleanPrivacySetting());
        
        // Instantiate the broadcast receiver
        EnergyBroadcastReceiver ebr = new EnergyBroadcastReceiver();
        
        // Add broadcast receiver
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(EnergyConstants.ACTION_BATTERY_CHANGED);
        iFilter.addAction(EnergyConstants.ACTION_SCREEN_ON);
        iFilter.addAction(EnergyConstants.ACTION_SCREEN_OFF);
        
        registerReceiver(ebr, iFilter);
        
    }
    
}
