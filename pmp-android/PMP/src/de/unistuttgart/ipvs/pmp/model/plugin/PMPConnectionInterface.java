package de.unistuttgart.ipvs.pmp.model.plugin;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.model.Model;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.PrivacyLevelValueException;

/**
 * A singleton for the PMP model to connect to the RG plugins.
 * 
 * @author Tobias Kuhn
 * 
 */
public class PMPConnectionInterface implements IPMPConnectionInterface {
    
    private static PMPConnectionInterface instance = new PMPConnectionInterface();
    
    
    public static PMPConnectionInterface getInstance() {
        return instance;
    }
    
    
    private PMPConnectionInterface() {
    }
    
    
    @Override
    public String getPrivacySettingValue(String rgPackage, String psIdentifier, String appPackage) {
        IResourceGroup rg = Model.getInstance().getResourceGroup(rgPackage);
        if (rg == null) {
            return null;
        } else {
            IPrivacySetting ps = rg.getPrivacySetting(psIdentifier);
            if (ps == null) {
                return null;
            } else {
                IApp a = Model.getInstance().getApp(appPackage);
                if (a == null) {
                    return null;
                } else {
                    return getPrivacySettingValue(rg, ps, a);
                }
            }
        }
    }
    
    
    private String getPrivacySettingValue(IResourceGroup rg, IPrivacySetting ps, IApp a) {
        String bestValue = null;
        
        try {
            for (IPreset p : a.getAssignedPresets()) {
                if (!p.isAvailable() || p.isDeleted()) {
                    continue;
                }
                
                String grantNow = p.getGrantedPrivacyLevelValue(ps);
                
                if (bestValue == null) {
                    bestValue = grantNow;
                } else {
                    if (ps.permits(bestValue, grantNow)) {
                        // grantNow allows more
                        bestValue = grantNow;
                    } /* else bestValue allows more, do nothing */
                }
            }
        } catch (PrivacyLevelValueException plve) {
            Log.e("Error while calculating privacy level value.", plve);
        }
        
        return bestValue;
    }
}
