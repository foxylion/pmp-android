/*
 * Copyright 2011 pmp-android development team
 * Project: PMPTest
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
package de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestPresets;

import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;

public class TestPreset implements IPreset {
    
    private IPrivacyLevel[] pls;
    
    
    public TestPreset(IPrivacyLevel[] pls) {
        this.pls = pls;
    }
    
    
    @Override
    public String getName() {
        return null;
    }
    
    
    @Override
    public PMPComponentType getType() {
        return null;
    }
    
    
    @Override
    public String getIdentifier() {
        return null;
    }
    
    
    @Override
    public String getDescription() {
        return null;
    }
    
    
    @Override
    public IPrivacyLevel[] getUsedPrivacyLevels() {
        return this.pls;
    }
    
    
    @Override
    public IApp[] getAssignedApps() {
        return null;
    }
    
    
    @Override
    public boolean isAppAssigned(IApp app) {
        return false;
    }
    
    
    @Override
    public void addApp(IApp app) {
        
    }
    
    
    @Override
    public void addApp(IApp app, boolean hidden) {
        
    }
    
    
    @Override
    public void removeApp(IApp app) {
        
    }
    
    
    @Override
    public void removeApp(IApp app, boolean hidden) {
        
    }
    
    
    @Override
    public void setPrivacyLevel(IPrivacyLevel privacyLevel) {
        
    }
    
    
    @Override
    public void setPrivacyLevel(IPrivacyLevel privacyLevel, boolean hidden) {
        
    }
    
    
    @Override
    public void removePrivacyLevel(IPrivacyLevel privacyLevel) {
        
    }
    
    
    @Override
    public void removePrivacyLevel(IPrivacyLevel privacyLevel, boolean hidden) {
        
    }
    
}
