package de.unistuttgart.ipvs.pmp.model.plugin;

import java.util.logging.Level;

import android.content.Context;
import android.test.mock.MockContext;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.model.Model;
import de.unistuttgart.ipvs.pmp.model.PersistenceConstants;
import de.unistuttgart.ipvs.pmp.model.PresetController;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.IPMPConnectionInterface;
import de.unistuttgart.ipvs.pmp.resource.RGMode;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;
import de.unistuttgart.ipvs.pmp.util.FileLog;

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
            String result = PresetController.findBestValue(a, ps);
            
            FileLog.get().logWithForward(this, null, FileLog.GRANULARITY_SETTING_REQUESTS, Level.FINE,
                    "%s requested privacy setting value for '%s' for app '%s', returned '%s'.", rg.getName(),
                    ps.getName(), a.getName(), result);
            
            return result;
            
        } catch (PrivacySettingValueException plve) {
            Log.e(this, "Error while calculating privacy setting value.", plve);
            return null;
        }
    }
    
    
    @Override
    @Deprecated
    public Context getContext(String rgPackage) {
        return getContext(rgPackage, "");
    }
    
    
    @Override
    public Context getContext(String rgPackage, String appPackage) {
        IResourceGroup rg = Model.getInstance().getResourceGroup(rgPackage);
        IApp app = Model.getInstance().getApp(appPackage);
        if (rg == null || app == null) {
            return new MockContext();
        } else {
            // if best == null
            RGMode mode = null;
            
            try {
                mode = RGMode.valueOf(PresetController.findBestValue(app,
                        rg.getPrivacySetting(PersistenceConstants.MODE_PRIVACY_SETTING)));
            } catch (PrivacySettingValueException psve) {
                psve.printStackTrace();
            }
            
            if (mode == null) {
                mode = RGMode.NORMAL;
            }
            
            return new SecurityContextAdapter(PMPApplication.getContext(), rg, app);
        }
    }
    
}
