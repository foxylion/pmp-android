package de.unistuttgart.ipvs.pmp.resource.privacysetting.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.IPrivacySettingView;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;

public class StringView extends TextView implements IPrivacySettingView<String> {
    
    public StringView(Context context) {
        super(context);
    }
    
    
    @Override
    public View asView() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String getViewValue() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public void setViewValue(String value) throws PrivacySettingValueException {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public String getViewValueObject() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
