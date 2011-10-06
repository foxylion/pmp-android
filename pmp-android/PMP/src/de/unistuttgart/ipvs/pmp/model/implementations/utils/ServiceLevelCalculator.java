/*
 * Copyright 2011 pmp-android development team
 * Project: PMP
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
package de.unistuttgart.ipvs.pmp.model.implementations.utils;

import java.util.HashMap;
import java.util.Map;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.model.ModelConditions;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

/**
 * Calculates the appropriate service level.
 * 
 * @author Tobias Kuhn
 * 
 */
public class ServiceLevelCalculator {
    
    /**
     * Separator between RG and PL in {@link ServiceLevelCalculator#getUniquePLIdentifier(IPrivacyLevel)}.
     */
    private static final String RG_PL_SEPARATOR = "::";
    
    private IApp app;
    
    
    public ServiceLevelCalculator(IApp app) {
        ModelConditions.assertNotNull("app", app);
        
        this.app = app;
    }
    
    
    public int calculate() throws RemoteException {
        // we first need to know which are the best privacy levels availabel
        Map<String, String> bestValues = getBestValues();
        
        IServiceLevel[] appSL = this.app.getServiceLevels();
        
        // try the best first, then advance to the worst
        for (int serviceLevel = appSL.length - 1; serviceLevel > 0; serviceLevel--) {
            
            // we now try to confirm that all privacy levels are satisfied
            IServiceLevel testSL = appSL[serviceLevel];
            boolean confirmed = true;
            for (IPrivacyLevel toConfirmPL : testSL.getPrivacyLevels()) {
                if (!nullSafePermits(toConfirmPL, toConfirmPL.getValue(),
                        bestValues.get(getUniquePLIdentifier(toConfirmPL)))) {
                    confirmed = false;
                    continue;
                }
            }
            
            // if we we're successful, choose this one
            if (confirmed) {
                return serviceLevel;
            }
        } /* for service levels */
        
        // none found, default to zero
        return 0;
    }
    
    
    /**
     * Finds the best values of the presets of the app.
     * 
     * @return a map from {@link ServiceLevelCalculator#getUniquePLIdentifier(IPrivacyLevel)} to the best value assigned
     * @throws RemoteException
     */
    private Map<String, String> getBestValues() throws RemoteException {
        Map<String, String> bestValues = new HashMap<String, String>();
        
        // of all presets
        for (IPreset preset : this.app.getAssignedPresets()) {
            // with every value
            for (IPrivacyLevel value : preset.getUsedPrivacyLevels()) {
                // update the best one
                String currentBest = bestValues.get(getUniquePLIdentifier(value));
                
                if (currentBest == null) {
                    // set for the first time
                    bestValues.put(getUniquePLIdentifier(value), value.getValue());
                } else {
                    // check which one is equal or better
                    if (nullSafePermits(value, currentBest, value.getValue())) {
                        // value >= currentBest
                        bestValues.put(getUniquePLIdentifier(value), value.getValue());
                    }
                }
            } /* for privacy levels */
        } /* for presets */
        
        return bestValues;
    }
    
    
    /**
     * Performs pl.permits(), but assures no null values are passed to the {@link IPrivacyLevel}.
     * 
     * 
     * @see [@link IPrivacyLevel#permits(String, String)}
     */
    private boolean nullSafePermits(IPrivacyLevel pl, String reference, String value) throws RemoteException {
        if (value == null) {
            // if we don't need it, that passes
            return (reference == null);
            
        } else if (reference == null) {
            // value != null
            return true;
            
        } else {
            return pl.permits(reference, value);
        }
    }
    
    
    /**
     * 
     * @param privacyLevel
     *            the privacy level
     * @return a String uniquely identifying this privacy level in the system
     */
    private String getUniquePLIdentifier(IPrivacyLevel privacyLevel) {
        return privacyLevel.getResourceGroup().getIdentifier() + RG_PL_SEPARATOR + privacyLevel.getIdentifier();
    }
    
}
