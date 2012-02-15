package de.unistuttgart.ipvs.pmp.resource.privacysetting.library;

import de.unistuttgart.ipvs.pmp.resource.privacysetting.DefaultPrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.IPrivacySettingView;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.view.EnumView;
import android.content.Context;

/**
 * {@link DefaultPrivacySetting} for {@link Enum}s.
 * 
 * @author Tobias Kuhn, Jakob Jarosch
 * 
 */
public class EnumPrivacySetting<T extends Enum<T>> extends DefaultPrivacySetting<T> {
    
    private final Class<T> clazz;
    private EnumView<T> view = null;
    
    
    public EnumPrivacySetting(Class<T> enumClass) {
        super();
        this.clazz = enumClass;
    }
    
    
    @Override
    public T parseValue(String value) throws PrivacySettingValueException {
        try {
            if (value == null) {
                value = clazz.getEnumConstants()[0].name();
            }
            return Enum.valueOf(clazz, value);
        } catch (IllegalArgumentException iae) {
            throw new PrivacySettingValueException(iae);
        }
    }
    
    
    @Override
    public IPrivacySettingView<T> getView(Context context) {
        if (this.view == null) {
            this.view = new EnumView<T>(context, clazz);
        }
        
        return this.view;
    }
}
