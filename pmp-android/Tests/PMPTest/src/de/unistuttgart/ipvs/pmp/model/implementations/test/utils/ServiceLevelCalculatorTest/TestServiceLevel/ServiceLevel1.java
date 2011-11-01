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
package de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestServiceLevel;

import de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestAccuracyRessourceGroup.TestAccuracyPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

public class ServiceLevel1 implements IServiceLevel {
    
    @Override
    public boolean isAvailable() {
        return true;
    }
    
    
    @Override
    public IPrivacyLevel[] getPrivacyLevels() {
        IPrivacyLevel[] pls = new IPrivacyLevel[1];
        pls[0] = new TestAccuracyPrivacyLevel("50");
        return pls;
    }
    
    
    @Override
    public String getName() {
        return "Level 1";
    }
    
    
    @Override
    public int getLevel() {
        return 1;
    }
    
    
    @Override
    public String getDescription() {
        return "The first service Level";
    }


    @Override
    public String getUniqueIdentifier() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public IApp getApp() {
        // TODO Auto-generated method stub
        return null;
    }
}
