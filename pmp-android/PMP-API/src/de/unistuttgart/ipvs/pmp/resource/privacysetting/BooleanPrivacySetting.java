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

import android.view.View;

/**
 * {@link DefaultPrivacySetting} for {@link Boolean}.
 * 
 * @author Tobias Kuhn
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
    public View getView() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String getViewValue(View view) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public void setViewValue(View view, Boolean value) {
        // TODO Auto-generated method stub
        
    }
    
}
