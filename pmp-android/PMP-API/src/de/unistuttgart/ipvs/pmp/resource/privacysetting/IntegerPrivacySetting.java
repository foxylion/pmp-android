package de.unistuttgart.ipvs.pmp.resource.privacysetting;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class IntegerPrivacySetting extends AbstractPrivacySetting<Integer> {
    
    private int multipliactor = 0;
    
    
    public IntegerPrivacySetting(boolean smallerIsBetter) {
        if (smallerIsBetter) {
            multipliactor = -1;
        } else {
            multipliactor = 1;
        }
    }
    
    
    @Override
    public String getHumanReadableValue(String value) throws PrivacySettingValueException {
        try {
            return "" + Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new PrivacySettingValueException();
        }
    }
    
    
    @Override
    public boolean permits(Integer value, Integer reference) {
        return ((value.compareTo(reference) * multipliactor) >= 0);
    }
    
    
    @Override
    public Integer parseValue(String value) throws PrivacySettingValueException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new PrivacySettingValueException();
        }
    }
    
    
    @Override
    public IPrivacySettingView<Integer> getView(Context context) {
        return new IntegerPrivacySettingView(context);
    }
}

/**
 * {@link IPrivacySettingView} for {@link IntegerPrivacySetting}
 * 
 * @author Jakob Jarosch
 * 
 */
class IntegerPrivacySettingView extends LinearLayout implements IPrivacySettingView<Integer> {
    
    private EditText editText;
    
    
    public IntegerPrivacySettingView(Context context) {
        super(context);
        this.editText = new EditText(context);
        this.editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        this.editText.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        addView(this.editText);
    }
    
    
    @Override
    public View asView() {
        return this;
    }
    
    
    @Override
    public void setViewValue(Integer value) throws PrivacySettingValueException {
        this.editText.setText("" + value.toString());
    }
    
    
    @Override
    public String getViewValue() {
        return this.editText.getText().toString();
    }
    
}
