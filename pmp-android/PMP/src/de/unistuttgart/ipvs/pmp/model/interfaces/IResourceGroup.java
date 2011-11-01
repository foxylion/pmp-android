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
package de.unistuttgart.ipvs.pmp.model.interfaces;

/**
 * {@link IResourceGroup} represents a resource group known by PMP.
 * 
 * @author Jakob Jarosch
 */
public interface IResourceGroup {
    
    /**
     * @return Returns the unique identifier of the {@link IResourceGroup}.
     */
    public String getIdentifier();
    
    
    /**
     * @return Returns the localized name of the {@link IResourceGroup}.
     */
    public String getName();
    
    
    /**
     * @return Returns the localized description of the {@link IResourceGroup}.
     */
    public String getDescription();
    
    
    /**
     * @return Returns all usable {@link IPrivacyLevel}s of the {@link IResourceGroup}.
     */
    public IPrivacyLevel[] getPrivacyLevels();
    
    
    /**
     * Returns a {@link IPrivacyLevel} which matches the requested identifier.
     * 
     * @param privacyLevelIdentifier
     *            Identifier of the {@link IPrivacyLevel} which should be returned.
     * @return Returns the requested {@link IPrivacyLevel} or null if no {@link IPrivacyLevel} is available with that
     *         identifier.
     */
    public IPrivacyLevel getPrivacyLevel(String privacyLevelIdentifier);
    
    
    /**
     * This method return all {@link IApp}s which are currently using the {@link IResourceGroup}.<br>
     * Currently means that the {@link IApp} has an {@link IServiceLevel} set as active where one of the
     * {@link IResourceGroup}s {@link IPrivacyLevel} is referenced.
     * 
     * @return Returns all {@link IApp} which are <b>currently</b> using this {@link IResourceGroup}.
     */
    IApp[] getAllAppsUsingThisResourceGroup();
}
