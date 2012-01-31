package de.unistuttgart.ipvs.pmp.model.plugin;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.model.Model;
import de.unistuttgart.ipvs.pmp.model.PresetController;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;

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
    
    
    /**
     * @see PMPConnectionInterface#getPrivacySettingValue(String, String, String)
     */
    private String getPrivacySettingValue(IResourceGroup rg, IPrivacySetting ps, IApp a) {
        try {
            return PresetController.findBestValue(a, ps);
            
        } catch (PrivacySettingValueException plve) {
            Log.e("Error while calculating privacy setting value.", plve);
            return null;
        }
    }
    
    
    @Override
    public Context getContext(String rgPackage) {
        // here it would be nice to have a security-wise adapter
        return PMPApplication.getContext();
    }
    
}
