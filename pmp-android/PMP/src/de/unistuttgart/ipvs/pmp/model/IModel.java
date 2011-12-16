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
package de.unistuttgart.ipvs.pmp.model;

import java.io.InputStream;

import de.unistuttgart.ipvs.pmp.model.element.IModelElement;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;

/**
 * The {@link IModel} provides all {@link IApp}s, {@link IPreset}s and {@link IResourceGroup}s known by PMP.
 * 
 * This interface is implemented by the {@link Model} class.
 * 
 * @author Jakob Jarosch
 */
public interface IModel {
    
    /**
     * @return Returns all {@link IApp}s known by PMP.
     */
    public IApp[] getApps();
    
    
    /**
     * Returns the corresponding {@link IApp} to an identifier of an {@link IApp}.
     * 
     * @param identifier
     *            Corresponding {@link IApp} identifier
     * @return Returns the requested {@link IApp} or null if the {@link IApp} does not exists in PMP.
     */
    public IApp getApp(String identifier);
    
    
    /**
     * Registers a new {@link IApp} at PMP.
     * 
     * <p>
     * <b>This method is executed asynchronously so the termination of this method will not mean the {@link IApp}
     * registration has succeeded.</b>
     * </p>
     * 
     * @param identifier
     *            The identifier for the {@link IApp} which should be registered.
     */
    public void registerApp(String identifier);
    
    
    /**
     * Removes the registration of a registered {@link IApp}.
     * 
     * @param identifier
     *            The identifier for the {@link IApp} which should be unregistered.
     * @return true, if and only if the app was found and removed
     */
    public boolean unregisterApp(String identifier);
    
    
    /**
     * @return Returns all {@link IResourceGroup}s known by PMP.
     */
    public IResourceGroup[] getResourceGroups();
    
    
    /**
     * Returns the corresponding {@link IResourceGroup} to an identifier of a {@link IResourceGroup}.
     * 
     * @param identifier
     *            Corresponding {@link IResourceGroup} identifier
     * @return Returns the requested {@link IResourceGroup} or null if the {@link IResourceGroup} does not exists in
     *         PMP.
     */
    public IResourceGroup getResourceGroup(String identifier);
    
    
    /**
     * Finds a list of {@link IResourceGroup}s for that search string.
     * 
     * <p>
     * <b>This method will cause a network connection to the resource group server.</b>
     * </p>
     * 
     * @param searchString
     *            the string for which shall be searched
     * @return some information about the results yet to be decided (TODO)
     */
    public String[] findResourceGroup(String searchString);
    
    
    /**
     * Installs a previously downloaded, new {@link IResourceGroup} at PMP.
     * 
     * <p>
     * <b>This method will cause a network connection to the resource group server.</b>
     * </p>
     * 
     * @param identifier
     *            The identifier for the {@link IResourceGroup} which should be registered.
     * @param input
     *            The input file which should be the jar or apk where the ResourceGroup API is stored.
     * @return true, if the installation was successful, false if an error occurred
     */
    public boolean installResourceGroup(String identifier);
    
    
    /**
     * Uninstalls an installed {@link IResourceGroup} at PMP.
     * 
     * @param identifier
     *            The identifier for the {@link IResourceGroup} which should be registered.
     * @return true, if the uninstallation was successful, false if an error occurred
     */
    public boolean uninstallResourceGroup(String identifier);
    
    
    /**
     * @return Returns all {@link IPreset}s known by PMP.
     */
    public IPreset[] getPresets();
    
    
    /**
     * @param creator
     *            null, if the user created this preset, the {@link IApp} or {@link IResourceGroup} if the
     *            {@link IPreset} is bundled.
     * @return Returns all {@link IPreset}s which were created by creator or null, if none found
     */
    public IPreset[] getPresets(ModelElement creator);
    
    
    /**
     * Returns a specific existing {@link IPreset}.
     * 
     * @param creator
     *            null, if the user created this preset, the {@link IApp} or {@link IResourceGroup} if the
     *            {@link IPreset} is bundled.
     * @param identifier
     *            a unique (for creator) identifier for this preset
     * @return the corresponding {@link IPreset} or null, if none found
     */
    public IPreset getPreset(IModelElement creator, String identifier);
    
    
    /**
     * Adds a new {@link IPreset} to PMP.
     * 
     * @param creator
     *            null, if the user created this preset, the {@link IApp} or {@link IResourceGroup} if the
     *            {@link IPreset} is bundled.
     * @param identifier
     *            a unique (for creator) identifier for this preset
     * @param name
     *            The name of the {@link IPreset}.
     * @param description
     *            The description of the {@link IPreset}.
     * @return the {@link IPreset} that was created
     */
    public IPreset addPreset(IModelElement creator, String identifier, String name, String description);
    
    
    /**
     * Convenience function to simply add a new preset created by a user. This function guarantees the preset will get
     * an identifier that is not yet taken, based on the name of the preset.
     * 
     * @param name
     *            The name of the {@link IPreset}.
     * @param description
     *            The description of the {@link IPreset}.
     * @return the {@link IPreset} that was created
     */
    public IPreset addUserPreset(String name, String description);
    
    
    /**
     * Removes an existing {@link IPreset} ultimately from PMP. This cannot be undone. Consider using
     * {@link IPreset#setDeleted(boolean)} alternatively.
     * 
     * @param creator
     *            null, if the user created this preset, the {@link IApp} or {@link IResourceGroup} if the
     *            {@link IPreset} is bundled.
     * @param identifier
     *            a unique identifier for this preset
     * @return true, if and only if the preset was found and removed
     */
    public boolean removePreset(IModelElement creator, String identifier);
    
    
    /**
     * Will clear the complete model on both data and persistence layer.
     */
    public void clearAll();
    
}
