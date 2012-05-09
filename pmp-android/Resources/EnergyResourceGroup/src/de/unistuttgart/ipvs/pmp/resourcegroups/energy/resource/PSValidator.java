package de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.AbstractPrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.EnumPrivacySetting;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.EnergyConstants;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.privacysettingenum.PSBatteryTemperatureEnum;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.privacysettingenum.PSDeviceBatteryChargingUptimeEnum;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.privacysettingenum.PSDeviceDatesEnum;

public class PSValidator {
    
    private ResourceGroup rg;
    private String appIdentifier;
    
    
    public PSValidator(ResourceGroup rg, String appIdentifier) {
        this.rg = rg;
    }
    
    
    public void validate(String psIdentifier, String requiredValue) {
        AbstractPrivacySetting<?> ps = this.rg.getPrivacySetting(psIdentifier);
        String grantedValue = this.rg.getPMPPrivacySettingValue(psIdentifier, this.appIdentifier);
        
        boolean failed = true;
        try {
            if (ps.permits(grantedValue, requiredValue)) {
                failed = false;
            }
        } catch (PrivacySettingValueException e) {
            logSomethingWentWrong(e);
        }
        
        if (failed) {
            throwException(psIdentifier, requiredValue);
        }
    }
    
    
    public void validate(PSBatteryTemperatureEnum reference) {
        @SuppressWarnings("unchecked")
        EnumPrivacySetting<PSBatteryTemperatureEnum> ps = (EnumPrivacySetting<PSBatteryTemperatureEnum>) this.rg
                .getPrivacySetting(EnergyConstants.PS_BATTERY_TEMPERATURE);
        boolean failed = true;
        try {
            if (ps.permits(this.appIdentifier, reference)) {
                failed = false;
            }
        } catch (PrivacySettingValueException e) {
            logSomethingWentWrong(e);
        }
        
        if (failed) {
            throwException(EnergyConstants.PS_BATTERY_TEMPERATURE, reference.name());
        }
    }
    
    
    public void validate(PSDeviceBatteryChargingUptimeEnum reference) {
        @SuppressWarnings("unchecked")
        EnumPrivacySetting<PSDeviceBatteryChargingUptimeEnum> ps = (EnumPrivacySetting<PSDeviceBatteryChargingUptimeEnum>) this.rg
                .getPrivacySetting(EnergyConstants.PS_DEVICE_BATTERY_CHARGING_UPTIME);
        boolean failed = true;
        try {
            if (ps.permits(this.appIdentifier, reference)) {
                failed = false;
            }
        } catch (PrivacySettingValueException e) {
            logSomethingWentWrong(e);
        }
        
        if (failed) {
            throwException(EnergyConstants.PS_DEVICE_BATTERY_CHARGING_UPTIME, reference.name());
        }
    }
    
    
    public void validate(PSDeviceDatesEnum reference) {
        @SuppressWarnings("unchecked")
        EnumPrivacySetting<PSDeviceDatesEnum> ps = (EnumPrivacySetting<PSDeviceDatesEnum>) this.rg
                .getPrivacySetting(EnergyConstants.PS_DEVICE_DATES);
        boolean failed = true;
        try {
            if (ps.permits(this.appIdentifier, reference)) {
                failed = false;
            }
        } catch (PrivacySettingValueException e) {
            logSomethingWentWrong(e);
        }
        
        if (failed) {
            throwException(EnergyConstants.PS_DEVICE_DATES, reference.name());
        }
    }
    
    
    private void logSomethingWentWrong(PrivacySettingValueException e) {
        Log.e(this, "Something went wrong while validating the permissions for Privacy Settings.", e);
    }
    
    
    private void throwException(String psName, String psValueRequired) {
        throw new SecurityException("The requested action requires at the PrivacySetting " + psName + " with at least "
                + psValueRequired + " as required value");
    }
    
}
