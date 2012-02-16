package de.unistuttgart.ipvs.pmp.resource.privacysetting.view;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.IPrivacySettingView;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;

public class StringView extends LinearLayout implements IPrivacySettingView<String> {
    
    private EditText editText;
    
    
    public StringView(Context context) {
        super(context);
        this.editText = new EditText(context);
        setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        this.editText.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        addView(this.editText);
    }
    
    
    @Override
    public View asView() {
        return this;
    }
    
    
    @Override
    public String getViewValue() {
        return this.editText.getText().toString();
    }
    
    
    @Override
    public void setViewValue(String value) throws PrivacySettingValueException {
        this.editText.setText(value);
    }
    
}
