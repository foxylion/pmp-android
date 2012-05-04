package de.unistuttgart.ipvs.pmp.resourcegroups.energy.intenthandler;

import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.EnergyConstants;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.db.DBConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.event.BatteryEvent;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class BatteryHandler {
    
    public static void handle(Intent intent, Context context) {
        int level = intent.getIntExtra(EnergyConstants.EXTRA_LEVEL, 0);
        int healthIndicator = intent.getIntExtra(EnergyConstants.EXTRA_HEALTH, 0);
        int statusIndicator = intent.getIntExtra(EnergyConstants.EXTRA_STATUS, 0);
        int pluggedIndicator = intent.getIntExtra(EnergyConstants.EXTRA_PLUGGED, 0);
        boolean present = intent.getBooleanExtra(EnergyConstants.EXTRA_PRESENT, false);
        String technology = intent.getStringExtra(EnergyConstants.EXTRA_TECHNOLOGY);
        int temperature = intent.getIntExtra(EnergyConstants.EXTRA_TEMPERATURE, 0);
        int voltage = intent.getIntExtra(EnergyConstants.EXTRA_VOLTAGE, 0);
        
        /*
         * Get the health strings
         */
        String health;
        switch (healthIndicator) {
            case BatteryManager.BATTERY_HEALTH_DEAD:
                health = EnergyConstants.HEALTH_DEAD;
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                health = EnergyConstants.HEALTH_GOOD;
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                health = EnergyConstants.HEALTH_OVER_VOLTAGE;
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                health = EnergyConstants.HEALTH_OVERHEAT;
                break;
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                health = EnergyConstants.HEALTH_UNKNOWN;
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                health = EnergyConstants.HEALTH_UNSPECIFIED_FAILURE;
                break;
            default:
                health = EnergyConstants.HEALTH_UNKNOWN;
        }
        
        /*
         * Get the plugged strings
         */
        String plugged;
        switch (pluggedIndicator) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                plugged = EnergyConstants.PLUGGED_AC;
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                plugged = EnergyConstants.PLUGGED_USB;
                break;
            case 0:
                plugged = EnergyConstants.PLUGGED_NOT_PLUGGED;
                break;
            default:
                plugged = EnergyConstants.PLUGGED_NOT_PLUGGED;
        }
        
        /*
         * Get the status strings
         */
        String status;
        switch (statusIndicator) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                status = EnergyConstants.STATUS_CHARGING;
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                status = EnergyConstants.STATUS_DISCHARGING;
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                status = EnergyConstants.STATUS_FULL;
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                status = EnergyConstants.STATUS_NOT_CHARGING;
                break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                status = EnergyConstants.STATUS_UNKNOWN;
                break;
            default:
                status = EnergyConstants.STATUS_UNKNOWN;
        }
        
        /*
         * Store to database
         */
        BatteryEvent be = new BatteryEvent(-1, System.currentTimeMillis(), level, health, status, plugged, present,
                technology, temperature / 10, voltage);
        DBConnector.getInstance(context).storeBatteryEvent(be);
    }
    
}
