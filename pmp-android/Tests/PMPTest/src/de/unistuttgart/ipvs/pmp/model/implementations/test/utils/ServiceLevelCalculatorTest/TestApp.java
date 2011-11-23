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
package de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest;

import de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestServiceLevel.ServiceLevel0;
import de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestServiceLevel.ServiceLevel1;
import de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestServiceLevel.ServiceLevel2;
import de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestServiceLevel.ServiceLevel3;
import de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestServiceLevel.ServiceLevel4;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

public class TestApp implements IApp {
    
    private String identifier = "de.unistuttgart.ipvs.mode.implementations.test.utils.TestApp";
    private String name = "Test App";
    private String description = "This app is for testing ONLY";
    private IServiceLevel[] serviceLevels = createServiceLevels();
    private IPreset[] presets;
    
    
    @Override
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    @Override
    public String getName() {
        return this.name;
    }
    
    
    @Override
    public String getDescription() {
        return this.description;
    }
    
    
    @Override
    public IServiceLevel[] getServiceLevels() {
        return this.serviceLevels;
    }
    
    
    @Override
    public IServiceLevel getServiceLevel(int level) {
        return this.serviceLevels[level];
    }
    
    
    @Override
    public IServiceLevel getActiveServiceLevel() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public boolean setActiveServiceLevelAsPreset(int level) {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public void verifyServiceLevel() {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public IPreset[] getAssignedPresets() {
        return this.presets;
    }
    
    
    @Override
    public IResourceGroup[] getAllResourceGroupsUsedByServiceLevels() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public IPrivacyLevel[] getAllPrivacyLevelsUsedByActiveServiceLevel(IResourceGroup resourceGroup) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    /**
     * Creates the service level for the test app
     * 
     * @return {@link IServiceFeature} array
     */
    private IServiceLevel[] createServiceLevels() {
        IServiceLevel[] serviceLevels = new IServiceLevel[5];
        
        serviceLevels[0] = new ServiceLevel0();
        serviceLevels[1] = new ServiceLevel1();
        serviceLevels[2] = new ServiceLevel2();
        serviceLevels[3] = new ServiceLevel3();
        serviceLevels[4] = new ServiceLevel4();
        
        return serviceLevels;
    }
    
    
    /**
     * Sets the {@link IPreset}s of the app
     * 
     * @param presets
     *            {@link IPreset} array to set
     */
    public void setPresets(IPreset[] presets) {
        this.presets = presets;
    }
    
}
