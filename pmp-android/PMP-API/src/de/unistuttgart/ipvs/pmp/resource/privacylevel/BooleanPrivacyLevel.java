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
package de.unistuttgart.ipvs.pmp.resource.privacylevel;

import java.util.Map;

/**
 * {@link LocalizedDefaultPrivacyLevel} for {@link Boolean}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class BooleanPrivacyLevel extends LocalizedDefaultPrivacyLevel<Boolean> {
    
    /**
     * @see {@link LocalizedDefaultPrivacyLevel#LocalizedDefaultPrivacyLevel(Map, Map)}
     */
    public BooleanPrivacyLevel(Map<String, String> names, Map<String, String> descriptions) {
        super(names, descriptions);
    }
    
    
    /**
     * @see {@link LocalizedDefaultPrivacyLevel#LocalizedDefaultPrivacyLevel(String, String)}
     */
    public BooleanPrivacyLevel(String defaultName, String defaultDescription) {
        super(defaultName, defaultDescription);
    }
    
    
    @Override
    public Boolean parseValue(String value) throws PrivacyLevelValueException {
        boolean result = Boolean.valueOf(value);
        
        if (!result && !value.equalsIgnoreCase(Boolean.FALSE.toString())) {
            throw new PrivacyLevelValueException();
        }
        
        return result;
    }
    
}
