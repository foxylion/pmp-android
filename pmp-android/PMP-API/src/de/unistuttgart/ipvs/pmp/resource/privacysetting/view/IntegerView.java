package de.unistuttgart.ipvs.pmp.resource.privacysetting.view;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.IPrivacySettingView;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.IntegerPrivacySetting;

/**
 * {@link IPrivacySettingView} for {@link IntegerPrivacySetting}
 * 
 * @author Jakob Jarosch
 * 
 */
public class IntegerView extends LinearLayout implements IPrivacySettingView<Integer> {
    
    private EditText editText;
    
    
    public IntegerView(Context context) {
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
    public Integer getViewValue() {
        try {
            return Integer.valueOf(this.editText.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
}
