package de.unistuttgart.ipvs.pmp.resource.privacysetting;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SetPrivacySetting<T> extends DefaultPrivacySetting {
    
    private T separator;
    
    
    public SetPrivacySetting(T separator) {
        this.separator = separator;
    }
    
    
    @Override
    public boolean permits(String value, String reference) throws PrivacySettingValueException {
        String[] values = parseValue(value);
        String[] references = parseValue(reference);
        
        for (String r : references) {
            boolean found = false;
            for (String v : values) {
                if (v.equals(r)) {
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                return false;
            }
        }
        
        return true;
    }
    
    
    @Override
    public String getHumanReadableValue(String value) throws PrivacySettingValueException {
        return value.toString();
    }
    
    
    @Override
    public String[] parseValue(String value) throws PrivacySettingValueException {
        return value.split(separator.toString());
    }
    
    
    @Override
    public IPrivacySettingView<String> getView(Context context) {
        return new SetPrivacySettingView(context, separator.toString());
    }
    
}

/**
 * {@link IPrivacySettingView} for {@link IntegerPrivacySetting}
 * 
 * @author Jakob Jarosch
 * 
 */
class SetPrivacySettingView extends LinearLayout implements IPrivacySettingView<String> {
    
    private EditText editText;
    
    
    public SetPrivacySettingView(Context context, String separator) {
        super(context);
        
        setOrientation(LinearLayout.VERTICAL);
        
        TextView description = new TextView(context);
        description.setText("Enter the entries separated by '" + separator + "'.");
        addView(description);
        
        this.editText = new EditText(context);
        this.editText.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(this.editText);
    }
    
    
    @Override
    public View asView() {
        return this;
    }
    
    
    @Override
    public void setViewValue(String value) throws PrivacySettingValueException {
        this.editText.setText(value);
    }
    
    
    @Override
    public String getViewValue() {
        return this.editText.getText().toString();
    }
    
}
