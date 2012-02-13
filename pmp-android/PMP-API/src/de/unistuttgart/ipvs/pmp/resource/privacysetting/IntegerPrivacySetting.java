package de.unistuttgart.ipvs.pmp.resource.privacysetting;

import java.util.Comparator;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * {@link DefaultPrivacySetting} for {@link Integer}.
 * 
 * @author Tobias Kuhn, Jakob Jarosch
 * 
 */
public class IntegerPrivacySetting extends DefaultPrivacySetting<Integer> {
    
    private IntegerPrivacySettingView view = null;
    
    
    public IntegerPrivacySetting() {
        this(false);
    }
    
    
    public IntegerPrivacySetting(final boolean smallerIsBetter) {
        super(new Comparator<Integer>() {
            
            @Override
            public int compare(Integer object1, Integer object2) {
                if (smallerIsBetter) {
                    return object1.compareTo(object2);
                } else {
                    return object2.compareTo(object1);
                }
            }
        });
    }
    
    
    @Override
    public IPrivacySettingView<Integer> getView(Context context) {
        if (this.view == null) {
            this.view = new IntegerPrivacySettingView(context);
        }
        return this.view;
    }
    
    
    @Override
    public Integer parseValue(String value) throws PrivacySettingValueException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            throw new PrivacySettingValueException();
        }
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
        this.editText.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
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
