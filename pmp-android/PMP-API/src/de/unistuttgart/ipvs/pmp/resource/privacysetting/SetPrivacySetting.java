package de.unistuttgart.ipvs.pmp.resource.privacysetting;

import android.content.Context;

public class SetPrivacySetting<T> extends DefaultPrivacySetting {
    
    private T separator;
    
    
    public SetPrivacySetting(T separator) {
        this.separator = separator;
    }
    
    
    @Override
    public String getHumanReadableValue(String value) throws PrivacySettingValueException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public T parseValue(String value) throws PrivacySettingValueException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public IPrivacySettingView getView(Context context) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
