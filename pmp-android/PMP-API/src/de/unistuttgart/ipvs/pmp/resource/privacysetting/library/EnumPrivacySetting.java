package de.unistuttgart.ipvs.pmp.resource.privacysetting.library;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.DefaultPrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.IPrivacySettingView;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.view.EnumView;

/**
 * {@link DefaultPrivacySetting} for {@link Enum}s.
 * 
 * @author Tobias Kuhn, Jakob Jarosch
 * 
 */
public class EnumPrivacySetting<T extends Enum<T>> extends DefaultPrivacySetting<T> {
    
    private final Class<T> clazz;
    
    
    public EnumPrivacySetting(Class<T> enumClass) {
        super();
        this.clazz = enumClass;
    }
    
    
    @Override
    public T parseValue(String value) throws PrivacySettingValueException {
        try {
            if (value == null) {
                value = this.clazz.getEnumConstants()[0].name();
            }
            return Enum.valueOf(this.clazz, value);
        } catch (IllegalArgumentException iae) {
            throw new PrivacySettingValueException(iae);
        }
    }
    
    
    @Override
    public String valueToString(T value) {
        if (value == null) {
            return null;
        }
        return value.name();
    }
    
    
    @Override
    public IPrivacySettingView<T> makeView(Context context) {
        return new EnumView<T>(context, this.clazz);
    }
    
}
