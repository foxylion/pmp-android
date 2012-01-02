/*
 * Copyright 2011 pmp-android development team
 * Project: SwitchesResourceGroup
 * Project-Site: http://code.google.com/p/pmp-android/
 * 
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.resource.privacysetting;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

/**
 * {@link DefaultPrivacySetting} for {@link Boolean}.
 * 
 * @author Tobias Kuhn, Jakob Jarosch
 * 
 */
public class BooleanPrivacySetting extends DefaultPrivacySetting<Boolean> {
    
    @Override
    public Boolean parseValue(String value) throws PrivacySettingValueException {
        if (value == null) {
            return false;
        }
        
        boolean result = Boolean.valueOf(value);
        
        if (!result && !value.equalsIgnoreCase(Boolean.FALSE.toString())) {
            throw new PrivacySettingValueException();
        }
        
        return result;
    }
    
    
    @Override
    public View getView(Context context) {
        return new BooleanPrivacyLevelView(context);
    }
    
    
    @Override
    public String getViewValue(View view) {
        return ((BooleanPrivacyLevelView) view).getValue().toString();
    }
    
    
    @Override
    public void setViewValue(View view, Boolean value) {
        ((BooleanPrivacyLevelView) view).setValue(value);
    }
    
}

class BooleanPrivacyLevelView extends LinearLayout {
    
    private CheckBox checkBox;
    private Boolean value;
    
    
    public BooleanPrivacyLevelView(Context context) {
        super(context);
        
        this.checkBox = new CheckBox(context);
        
        addView(this.checkBox);
        
        this.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BooleanPrivacyLevelView.this.value = isChecked;
            }
        });
        
        setValue(false);
    }
    
    
    public void setValue(Boolean bool) {
        this.value = bool;
        this.checkBox.setChecked(bool);
    }
    
    
    public Boolean getValue() {
        return this.value;
    }
}
