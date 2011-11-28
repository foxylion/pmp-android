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

/**
 * This privacy level only exists to be faulty for system testing.
 * 
 * @author Tobias Kuhn
 * 
 */
public class FaultyPrivacyLevel extends PrivacyLevel<IllegalArgumentException> {
    
    private boolean throwExcep;
    
    
    public FaultyPrivacyLevel(boolean throwExcep) {
        this.throwExcep = throwExcep;
    }
    
    
    @Override
    public String getHumanReadableValue(String locale, String value) throws PrivacyLevelValueException {
        if (this.throwExcep) {
            throw new PrivacyLevelValueException();
        } else {
            return null;
        }
    }
    
    
    @Override
    public boolean permits(IllegalArgumentException value, IllegalArgumentException reference) {
        return true;
    }
    
    
    @Override
    public IllegalArgumentException parseValue(String value) throws PrivacyLevelValueException {
        if (this.throwExcep) {
            throw new PrivacyLevelValueException();
        } else {
            return null;
        }
    }
    
}
