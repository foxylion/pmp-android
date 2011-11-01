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

import java.util.Comparator;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.LocalizedDefaultPrivacyLevel;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.PrivacyLevel;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.PrivacyLevelValueException;

public class TestLocationPrivacyLevel implements IPrivacyLevel {
    
    /**
     * Bridge to RG-PrivacyLevel
     */
    private PrivacyLevel<LocationEnum> bridge;
    
    /**
     * Actual value
     */
    private String value;
    
    
    public TestLocationPrivacyLevel() {
        this.bridge = new LocalizedDefaultPrivacyLevel<LocationEnum>("Location", "Location in germany",
                new Comparator<LocationEnum>() {
                    
                    @Override
                    public int compare(LocationEnum object1, LocationEnum object2) {
                        
                        return (object1.getId() - object2.getId());
                    }
                }) {
            
            @Override
            public LocationEnum parseValue(String value) throws PrivacyLevelValueException {
                return LocationEnum.valueOf(value);
            }
            
        };
    }
    
    
    public TestLocationPrivacyLevel(String initValue) {
        this();
        setValue(initValue);
    }
    
    
    @Override
    public String getIdentifier() {
        return "de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestLocationRessourceGroup.TestLocationPrivacyLevel";
    }
    
    
    @Override
    public IResourceGroup getResourceGroup() {
        return new TestLocationResourceGroup();
    }
    
    
    @Override
    public String getName() {
        return this.bridge.getName(Constants.DEFAULT_LOCALE);
    }
    
    
    @Override
    public String getDescription() {
        return this.bridge.getDescription(Constants.DEFAULT_LOCALE);
    }
    
    
    @Override
    public String getValue() {
        return this.value;
    }
    
    
    public void setValue(String value) {
        this.value = value;
    }
    
    
    @Override
    public String getHumanReadableValue(String value) throws RemoteException {
        try {
            return this.bridge.getHumanReadableValue(Constants.DEFAULT_LOCALE, value);
        } catch (PrivacyLevelValueException e) {
            e.printStackTrace();
            return e.toString();
        }
    }
    
    
    @Override
    public boolean permits(String reference, String value) throws RemoteException {
        try {
            return this.bridge.permits(value, reference);
        } catch (PrivacyLevelValueException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public String getUniqueIdentifier() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
