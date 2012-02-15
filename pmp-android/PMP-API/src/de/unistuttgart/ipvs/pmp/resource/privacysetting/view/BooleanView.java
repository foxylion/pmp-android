package de.unistuttgart.ipvs.pmp.resource.privacysetting.view;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.IPrivacySettingView;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.BooleanPrivacySetting;

/**
 * {@link IPrivacySettingView} for {@link BooleanPrivacySetting}
 * 
 * @author Jakob Jarosch
 * 
 */
public class BooleanView extends LinearLayout implements IPrivacySettingView<Boolean> {
    
    private CheckBox checkBox;
    
    
    public BooleanView(Context context) {
        super(context);
        this.checkBox = new CheckBox(context);
        addView(this.checkBox);
    }
    
    
    @Override
    public View asView() {
        return this;
    }
    
    
    @Override
    public void setViewValue(Boolean value) throws PrivacySettingValueException {
        this.checkBox.setChecked(value);
    }
    
    
    @Override
    public Boolean getViewValue() {
        return this.checkBox.isChecked();
    }
    
}
