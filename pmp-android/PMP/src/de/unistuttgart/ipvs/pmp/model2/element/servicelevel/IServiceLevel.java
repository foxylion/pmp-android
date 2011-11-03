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
package de.unistuttgart.ipvs.pmp.model2.element.servicelevel;

import de.unistuttgart.ipvs.pmp.model2.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model2.element.privacylevel.IPrivacyLevel;

/**
 * {@link IServiceLevel} is returned by an {@link IApp}, containing all required {@link IPrivacyLevel}s for an
 * accordingly service of the {@link IApp}.
 * 
 * <p>
 * It is possible that an {@link IServiceLevel} is not available (can be checked using
 * {@link IServiceLevel#isAvailable()}), that means the {@link IServiceLevel} is using a {@link IPrivacyLevel} of an
 * {@link IResourceGroup} which are not registered at PMP or does not have this {@link IPrivacyLevel} as a choice. They
 * will be made available when {@link IResourceGroup} is registered at PMP and does offer all the required
 * {@link IPrivacyLevel}s.
 * 
 * @author Jakob Jarosch
 */
public interface IServiceLevel {
    
    /**
     * @return a system-wide unique identifier for this privacy level
     */
    public String getUniqueIdentifier();
    
    
    /**
     * @return returns the unique level of the {@link IServiceLevel}.
     */
    public int getLevel();
    
    
    /**
     * @return the {@link IApp} which uses this service level
     */
    public IApp getApp();
    
    
    /**
     * @return Returns the localized name of the {@link IServiceLevel}.
     */
    public String getName();
    
    
    /**
     * @return Returns the localized description of the {@link IServiceLevel}.
     */
    public String getDescription();
    
    
    /**
     * @return Returns the required {@link IPrivacyLevel}s for the {@link IServiceLevel}.
     */
    public IPrivacyLevel[] getPrivacyLevels();
    
    
    /**
     * @param privacyLevel
     *            the privacy level in question
     * @return the required minimum value of the {@link IPrivacyLevel} for this service level
     */
    public String getRequiredPrivacyLevelValue(IPrivacyLevel privacyLevel);
    
    
    /**
     * @return Returns if a {@link IServiceLevel} is available for setting or not. (more details can be found in JavaDoc
     *         of {@link IServiceLevel}).
     */
    public boolean isAvailable();
    
}
