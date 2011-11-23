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
package de.unistuttgart.ipvs.pmp.model.element.servicefeature;

import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;

/**
 * <p>
 * {@link IServiceFeature}s are collections of features for an {@link IApp}, containing all required
 * {@link IPrivacySetting}s for an according feature of the {@link IApp}.
 * </p>
 * 
 * <p>
 * It is possible that an {@link IServiceFeature} is not available (can be checked using
 * {@link IServiceFeature#isAvailable()}), that means the {@link IServiceFeature} is using at least one
 * {@link IPrivacySetting} of an {@link IResourceGroup} which is not registered at PMP.
 * </p>
 * 
 * @author Jakob Jarosch
 */
public interface IServiceFeature {
    
    /**
     * @return Returns the <b>unique</b> identifier of the {@link IServiceFeature}.
     */
    public String getIdentifier();
    
    
    /**
     * @return Returns the identifier of the {@link IServiceFeature} in its {@link IApp}.
     */
    public String getLocalIdentifier();
    
    
    /**
     * @return the {@link IApp} which defines this service feature
     */
    public IApp getApp();
    
    
    /**
     * @return Returns the localized name of the {@link IServiceFeature}.
     */
    public String getName();
    
    
    /**
     * @return Returns the localized description of the {@link IServiceFeature}.
     */
    public String getDescription();
    
    
    /**
     * @return Returns the required {@link IPrivacySetting}s for the {@link IServiceFeature}.
     */
    public IPrivacySetting[] getRequiredPrivacyLevels();
    
    
    /**
     * 
     * @return true, if and only if this service feature is currently active
     */
    public boolean isActive();
    
    
    /**
     * @param privacySetting
     *            the privacy level in question
     * @return the required minimum value of the {@link IPrivacySetting} for this service feature
     */
    public String getRequiredPrivacyLevelValue(IPrivacySetting privacySetting);
    
    
    /**
     * @return Returns true, if and only if this {@link IServiceFeature} can be used.
     */
    public boolean isAvailable();
    
}
