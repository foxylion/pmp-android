package de.unistuttgart.ipvs.pmp.resource.privacysetting.library;

import java.util.Comparator;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.DefaultPrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.IPrivacySettingView;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.view.IntegerView;

/**
 * {@link DefaultPrivacySetting} for {@link Integer}.
 * 
 * @author Tobias Kuhn, Jakob Jarosch
 * 
 */
public class IntegerPrivacySetting extends DefaultPrivacySetting<Integer> {
    
    public IntegerPrivacySetting() {
        this(false);
    }
    
    
    public IntegerPrivacySetting(final boolean smallerIsBetter) {
        super(new Comparator<Integer>() {
            
            @Override
            public int compare(Integer object1, Integer object2) {
                if (!smallerIsBetter) {
                    return object1.compareTo(object2);
                } else {
                    return object2.compareTo(object1);
                }
            }
        });
    }
    
    
    @Override
    public Integer parseValue(String value) throws PrivacySettingValueException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            throw new PrivacySettingValueException();
        }
    }
    
    
    @Override
    public String valueToString(Object value) {
        if (value == null || !(value instanceof Integer)) {
            return null;
        }
        Integer i = (Integer) value;
        return i.toString();
    }
    
    
    @Override
    public IPrivacySettingView<Integer> makeView(Context context) {
        return new IntegerView(context);
    }
}
