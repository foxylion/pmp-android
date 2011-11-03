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
package de.unistuttgart.ipvs.pmp.model2.element.preset;

import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model2.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model2.element.privacylevel.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model2.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model2.element.servicelevel.IServiceLevel;

/**
 * {@link IPreset} is the chain link between the {@link IResourceGroup}, its {@link IPrivacyLevel}s and the {@link IApp}
 * . It gives the {@link IApp} access to the defined {@link IPrivacyLevel}s.
 * 
 * @author Jakob Jarosch
 */
public interface IPreset {
    
    /**
     * @return Returns the name of the {@link IPreset}.
     */
    public String getName();
    
    
    /**
     * @return Returns the {@link PMPComponentType} of the identifier which may be assigned.
     *         {@link PMPComponentType#NONE} will be returned when the identifier is null.
     */
    PMPComponentType getType();
    
    
    /**
     * @return Returns the identifier which created this {@link IPreset}, or null if this {@link IPreset} was generated
     *         manually.
     */
    public String getIdentifier();
    
    
    /**
     * @return Returns the description of the {@link IPreset}.
     */
    public String getDescription();
    
    
    /**
     * @return Returns the used {@link IPrivacyLevel}s by this preset.
     */
    public IPrivacyLevel[] getGrantedPrivacyLevels();
    
    
    /**
     * @param privacyLevel
     *            the privacy level in question
     * @return the granted minimum value of the {@link IPrivacyLevel} for this service level
     */
    public String getGrantedPrivacyLevelValue(IPrivacyLevel privacyLevel);
    
    
    /**
     * @return Returns the assigned {@link IApp}s to this {@link IPreset}.
     */
    public IApp[] getAssignedApps();
    
    
    /**
     * @return Returns true if the {@link IApp} is assigned, otherwise false.
     */
    public boolean isAppAssigned(IApp app);
    
    
    /**
     * Adds an {@link IApp} to the {@link IPreset}.
     * 
     * <p>
     * <i>Inside the {@link IApp#verifyServiceLevel()} will be called, what will maybe end up in a roll out of changed
     * {@link IServiceLevel} and {@link IResourceGroup} Accesses.</i> <b>So, if you have multiple changes on the
     * {@link IPreset} use the hidden variant of this method {@link IPreset#assignApp(IApp, boolean)}</b>
     * </p>
     * 
     * @param app
     *            The {@link IApp} which should be added.
     */
    public void assignApp(IApp app);
    
    
    /**
     * Adds an {@link IApp} to the {@link IPreset}.
     * 
     * <p>
     * <b>If you use the hidden variant you have to call {@link IApp#verifyServiceLevel()} manually, otherwise the
     * {@link IPreset} change will not be rolled out.</b>
     * </p>
     * 
     * @param app
     *            The {@link IApp} which should be added.
     * @param hidden
     *            if set to true {@link IApp#verifyServiceLevel()} will not be called.
     */
    @Deprecated
    public void assignApp(IApp app, boolean hidden);
    
    
    /**
     * Removes an {@link IApp} from the {@link IPreset}.
     * 
     * <p>
     * <i>Inside the {@link IApp#verifyServiceLevel()} will be called, what will maybe end up in a roll out of changed
     * {@link IServiceLevel} and {@link IResourceGroup} Accesses.</i> <b>So, if you have multiple changes on the
     * {@link IApp} use the hidden variant of this method {@link IPreset#removeApp(IApp, boolean)}</b>
     * </p>
     * 
     * @param app
     *            The {@link IApp} which should be removed.
     */
    public void removeApp(IApp app);
    
    
    /**
     * Removes an {@link IApp} from the {@link IPreset}.
     * 
     * <p>
     * <b>If you use the hidden variant you have to call {@link IApp#verifyServiceLevel()} manually, otherwise the
     * {@link IApp} remove from this {@link IPreset} will not be rolled out.</b>
     * </p>
     * 
     * @param app
     *            The {@link IApp} which should be removed.
     * @param hidden
     *            if set to true {@link IApp#verifyServiceLevel()} will not be called.
     */
    @Deprecated
    public void removeApp(IApp app, boolean hidden);
    
    
    /**
     * Sets an {@link IPrivacyLevel} to the {@link IPreset}.
     * 
     * <p>
     * <i>Inside the {@link IApp#verifyServiceLevel()} will be called for each {@link IApp}, what will maybe end up in a
     * roll out of changed {@link IServiceLevel}s and {@link IResourceGroup}s Accesses.</i> <b>So, if you have multiple
     * changes on the {@link IPreset} use the hidden variant of this method
     * {@link IPreset#assignPrivacyLevel(IPrivacyLevel, boolean)}</b>
     * </p>
     * 
     * @param privacyLevel
     *            The {@link IPrivacyLevel} which should be set.
     * @param the
     *            value of the privacy level
     */
    public void assignPrivacyLevel(IPrivacyLevel privacyLevel, String value);
    
    
    /**
     * Adds an {@link IPrivacyLevel} to the {@link IPreset}.
     * 
     * <p>
     * <b>If you use the hidden variant you have to call {@link IApp#verifyServiceLevel()} for each linked {@link IApp}
     * manually, otherwise the {@link IPreset} change will not be rolled out.</b>
     * </p>
     * 
     * @param privacyLevel
     *            The {@link IPrivacyLevel} which should be added.
     * @param hidden
     *            if set to true {@link IApp#verifyServiceLevel()} will not be called.
     */
    @Deprecated
    public void assignPrivacyLevel(IPrivacyLevel privacyLevel, String value, boolean hidden);
    
    
    /**
     * Removes an {@link IPrivacyLevel} from the {@link IPreset}.
     * 
     * <p>
     * <i>Inside the {@link IApp#verifyServiceLevel()} will be called for each {@link IApp}, what will maybe end up in a
     * roll out of changed {@link IServiceLevel}s and {@link IResourceGroup}s Accesses.</i> <b>So, if you have multiple
     * changes on the {@link IPreset} use the hidden variant of this method
     * {@link IPreset#removePrivacyLevel(IPrivacyLevel, boolean)}</b>
     * </p>
     * 
     * @param privacyLevel
     *            The {@link IPrivacyLevel} which should be removed.
     */
    public void removePrivacyLevel(IPrivacyLevel privacyLevel);
    
    
    /**
     * Removes an {@link IPrivacyLevel} from the {@link IPreset}.
     * 
     * <p>
     * <b>If you use the hidden variant you have to call {@link IApp#verifyServiceLevel()} for each linked {@link IApp}
     * manually, otherwise the {@link IPrivacyLevel} remove from this {@link IPreset} will not be rolled out.</b>
     * </p>
     * 
     * @param app
     *            The {@link IPrivacyLevel} which should be removed.
     * @param hidden
     *            if set to true {@link IApp#verifyServiceLevel()} will not be called.
     */
    @Deprecated
    public void removePrivacyLevel(IPrivacyLevel privacyLevel, boolean hidden);
    
    
    /**
     * @return true, if all {@link IResourceGroup}s that this preset requires are available in the system
     */
    public boolean isAvailable();
}
