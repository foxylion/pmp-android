package de.unistuttgart.ipvs.pmp.resource.privacysetting;

import android.content.Context;
import android.view.View;

/**
 * {@link DefaultPrivacySetting} for {@link Enum}s.
 * 
 * @author Tobias Kuhn, Jakob Jarosch
 * 
 */
public class EnumPrivacySetting<T extends Enum<T>> extends DefaultPrivacySetting<T> {
    
    private final Class<T> clazz;
    private EnumPrivacySettingView<T> view = null;
    
    
    public EnumPrivacySetting(Class<T> enumClass) {
        super();
        this.clazz = enumClass;
    }
    
    
    @Override
    public T parseValue(String value) throws PrivacySettingValueException {
        try {
            return Enum.valueOf(clazz, value);
        } catch (IllegalArgumentException iae) {
            throw new PrivacySettingValueException(iae);
        }
    }
    
    
    @Override
    public IPrivacySettingView<T> getView(Context context) {
        if (this.view == null) {
            this.view = new EnumPrivacySettingView<T>(context);
        }
        
        return this.view;
    }
    
}

class EnumPrivacySettingView<T extends Enum<T>> implements IPrivacySettingView<T> {
    
    public EnumPrivacySettingView(Context context) {
        // TODO Auto-generated constructor stub
    }
    
    
    @Override
    public View asView() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String getViewValue() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public void setViewValue(T value) throws PrivacySettingValueException {
        // TODO Auto-generated method stub
        
    }
    
}
