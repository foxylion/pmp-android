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
 * This represents an {@link IApp} registered at PMP.<br>
 * 
 * You can identify an {@link IApp} by its identifier, use {@link IApp#getIdentifier()}. With only an identifier you can
 * get the {@link IApp} object from {@link IModel#getApp(String)}.
 * 
 * @author Jakob Jarosch
 */
public interface IApp {
    
    /**
     * @return Returns the unique identifier of the {@link IApp}.
     */
    public String getIdentifier();
    
    
    /**
     * @return Returns the localized name of the {@link IApp}.
     */
    public String getName();
    
    
    /**
     * @return Returns the localized description of the {@link IApp}.
     */
    public String getDescription();
    
    
    /**
     * @return Returns the service levels provided by the {@link IApp}.
     */
    public IServiceLevel[] getServiceLevels();
    
    
    /**
     * Returns a service level with exactly this {@link IServiceLevel}.
     * 
     * @param level
     *            level of the {@link IServiceLevel}
     * @return the to the level corresponding {@link IServiceLevel}
     */
    public IServiceLevel getServiceLevel(int level);
    
    
    /**
     * @return Returns the current active {@link IServiceLevel} set for the {@link IApp}.
     */
    public IServiceLevel getActiveServiceLevel();
    
    
    /**
     * Set a new {@link IServiceLevel} for the {@link IApp}. <i>This method removes the {@link IApp} from all Presets,
     * so use that with caution!</i><br>
     * You can only set a {@link IServiceLevel} level which is available, you can check that using
     * {@link IServiceLevel#isAvailable()}.
     * 
     * <p>
     * <b>This method is uses blocking access to other Services, you must call this method in another Thread than the
     * Main-Worker-Thread! Otherwise you will end in a deadlock</b>
     * </p>
     * 
     * <p>
     * If the method returns false that means the {@link IServiceLevel} could not be set, that happens when the
     * {@link IServiceLevel} is not available, use {@link IServiceLevel#isAvailable()} to check that).
     * </p>
     * 
     * @param level
     *            New {@link IServiceLevel}s level which should be set.
     * @return true if the {@link IServiceLevel} was set, false if not.
     */
    public boolean setActiveServiceLevelAsPreset(int level);
    
    
    /**
     * Verifies the service level in background and publishes the changed permissions to the {@link IApp} and the
     * corresponding {@link IResourceGroup}s.
     * 
     * <p>
     * <b> This method is running in background and will immediately terminate, that will not mean that the verification
     * has been finished, it runs in background. </b>
     * </p>
     */
    public void verifyServiceLevel();
    
    
    /**
     * @return Returns all to the {@link IApp} assigned {@link IPreset}s.
     */
    public IPreset[] getAssignedPresets();
    
    
    /**
     * @return Returns all {@link IResourceGroup}s which are used in one or more of the {@link IServiceLevel}s defined
     *         by this {@link IApp}.
     */
    public IResourceGroup[] getAllResourceGroupsUsedByServiceLevels();
    
    
    /**
     * Returns all {@link IPrivacyLevel}s of this {@link IApp}, which are currently in use, and does match the given
     * {@link IResourceGroup}.
     * 
     * @param resourceGroup
     *            The {@link IResourceGroup} which should be matched
     * @return Returns all {@link IPrivacyLevel}s of this {@link IApp}, which are currently in use, and does match the
     *         given {@link IResourceGroup}.
     */
    public IPrivacyLevel[] getAllPrivacyLevelsUsedByActiveServiceLevel(IResourceGroup resourceGroup);
}
