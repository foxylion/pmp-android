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
package de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestLocationRessourceGroup;

import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;

public class TestLocationResourceGroup implements IResourceGroup {
    
    private String identifier = "de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestLocationRessourceGroup.TestLocationResourceGroup";
    
    
    @Override
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    @Override
    public String getName() {
        return null;
    }
    
    
    @Override
    public String getDescription() {
        return null;
    }
    
    
    @Override
    public IPrivacyLevel[] getPrivacyLevels() {
        return null;
    }
    
    
    @Override
    public IPrivacyLevel getPrivacyLevel(String privacyLevelIdentifier) {
        return null;
    }
    
    
    @Override
    public IApp[] getAllAppsUsingThisResourceGroup() {
        return null;
    }
    
}
