package de.unistuttgart.ipvs.pmp.resource.privacysetting.view;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.IPrivacySettingView;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;

public class EnumView<T extends Enum<T>> implements IPrivacySettingView<T> {
    
    private Spinner spinner;
    
    
    public EnumView(Context context, Class<T> clazz) {
        this.spinner = new Spinner(context);
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<T>(context, android.R.layout.simple_spinner_item,
                clazz.getEnumConstants());
        this.spinner.setAdapter(spinnerAdapter);
    }
    
    
    @Override
    public View asView() {
        return this.spinner;
    }
    
    
    @Override
    public void setViewValue(T value) throws PrivacySettingValueException {
        this.spinner.setSelection(value.ordinal());
    }
    
    
    @Override
    public T getViewValue() {
        return (T) this.spinner.getSelectedItem();
    }
    
}
