package de.unistuttgart.ipvs.pmp.resource.privacysetting;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

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
            this.view = new EnumPrivacySettingView<T>(context, clazz);
        }
        
        return this.view;
    }
}

class EnumPrivacySettingView<T extends Enum<T>> implements IPrivacySettingView<T> {
    
    private Spinner spinner;
    
    
    public EnumPrivacySettingView(Context context, Class<T> clazz) {
        this.spinner = new Spinner(context);
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<T>(context, android.R.layout.simple_spinner_item,
                clazz.getEnumConstants());
        this.spinner.setAdapter(spinnerAdapter);
    }
    
    
    @Override
    public View asView() {
        return spinner;
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public String getViewValue() {
        return ((T) this.spinner.getSelectedItem()).name();
    }
    
    
    @Override
    public void setViewValue(T value) throws PrivacySettingValueException {
        this.spinner.setSelection(value.ordinal());
    }
    
}
