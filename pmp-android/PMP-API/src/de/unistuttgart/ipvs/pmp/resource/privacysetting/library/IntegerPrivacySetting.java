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
    
    private int worstValue;
    private int bestValue;
    
    
    public IntegerPrivacySetting(final int worstValue, final int bestValue) {
        super(new Comparator<Integer>() {
            
            @Override
            public int compare(Integer object1, Integer object2) {
                if (worstValue < bestValue) {
                    return object1.compareTo(object2);
                } else {
                    return object2.compareTo(object1);
                }
            }
        });
        
        this.worstValue = worstValue;
        this.bestValue = bestValue;
    }
    
    
    @Override
    public Integer parseValue(String value) throws PrivacySettingValueException {
        if (value == null || value.equals("")) {
            return this.worstValue;
        }
        return StringConverter.forIntegerSafe.valueOf(value);
    }
    
    
    @Override
    public String valueToString(Integer value) {
        return StringConverter.forIntegerSafe.toString(value);
    }
    
    
    @Override
    public IPrivacySettingView<Integer> makeView(Context context) {
        return new IntegerView(context);
    }
}
