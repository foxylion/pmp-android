package de.unistuttgart.ipvs.pmp.gui.util;

import de.unistuttgart.ipvs.pmp.model.IModel;
import de.unistuttgart.ipvs.pmp.model.Model;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;

public class PresetMananger {
    
    public static IModel model = Model.getInstance();
    
    
    public static void enableServiceFeature(IServiceFeature serviceFeature) {
        IPreset preset = getPreset(serviceFeature);
        
        preset.startUpdate();
        
        preset.assignApp(serviceFeature.getApp());
        
        for (IPrivacySetting ps : serviceFeature.getRequiredPrivacySettings()) {
            preset.assignPrivacyLevel(ps, serviceFeature.getRequiredPrivacySettingValue(ps));
        }
        
        preset.endUpdate();
    }
    
    
    public static void disableServiceFeature(IServiceFeature serviceFeature) {
        IPreset preset = getPreset(serviceFeature);
        model.removePreset(null, preset.getIdentifier());
    }
    
    
    private static IPreset getPreset(IServiceFeature serviceFeature) {
        IPreset preset = model.getPreset(null, serviceFeature.getIdentifier());
        
        if (preset == null) {
            preset = model.addPreset(null, serviceFeature.getIdentifier(), serviceFeature.getName(),
                    serviceFeature.getDescription());
        }
        
        return preset;
    }
    
}
