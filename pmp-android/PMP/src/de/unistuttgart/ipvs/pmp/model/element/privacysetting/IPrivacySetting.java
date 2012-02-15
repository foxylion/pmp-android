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
package de.unistuttgart.ipvs.pmp.model.element.privacysetting;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.model.element.IModelElement;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.IPrivacySettingView;
import de.unistuttgart.ipvs.pmp.resource.privacysetting.PrivacySettingValueException;

/**
 * The {@link IPrivacySetting} interface represents a PrivacySetting of a {@link IResourceGroup}. It can be fetched from
 * an {@link IServiceFeature}, {@link IPreset} or {@link IResourceGroup}.
 * 
 * @author Jakob Jarosch
 */
public interface IPrivacySetting extends IModelElement {
    
    /**
     * @return Returns the <b>unique</b> identifier of the {@link IPrivacySetting}.
     */
    @Override
    public String getIdentifier();
    
    
    /**
     * @return Returns the identifier of the {@link IPrivacySetting} in its {@link IResourceGroup}.
     */
    public String getLocalIdentifier();
    
    
    /**
     * @return Returns the {@link IResourceGroup} this {@link IPrivacySetting} belongs to.
     */
    public IResourceGroup getResourceGroup();
    
    
    /**
     * @return Returns the localized name of the {@link IPrivacySetting}.
     */
    public String getName();
    
    
    /**
     * @return Returns the localized description of the {@link IPrivacySetting}.
     */
    public String getDescription();
    
    
    /**
     * Returns true, if the privacy setting accepts this value for its internal processing.
     * 
     * @param value
     *            the value to be checked
     * @return true, if and only if the passed value is valid in this privacy setting.
     */
    public boolean isValueValid(String value);
    
    
    /**
     * Returns an human readable representation of the a given value.
     * 
     * @param value
     *            Value which should be returned as a human readable representation.
     * @return the human readable representation of the given value.
     * @throws PrivacySettingValueException
     *             if the given value is not a valid string for this privacy setting.
     */
    public String getHumanReadableValue(String value) throws PrivacySettingValueException;
    
    
    /**
     * Checks if a given value permits more or equal to the given reference, in general it means "value is better than
     * or equals reference".
     * 
     * 
     * @param reference
     *            The reference value which should be used to check permission.
     * @param value
     *            The value which should be checked against the reference value.
     * @return true, if and only if value permits reference, i.e. "value >= reference"
     * @throws PrivacySettingValueException
     *             if the given value is not a valid string for this privacy setting.
     */
    public boolean permits(String reference, String value) throws PrivacySettingValueException;
    
    
    /**
     * 
     * @param context
     *            context to use for the view
     * @return an @link{IPrivacySettingView} object to display and change the values of the privacy setting.
     */
    public IPrivacySettingView<?> getView(Context context);
    
    
    /**
     * Convenience method for {@link IPrivacySettingView#setViewValue(Object)} if you only have a string.
     * 
     * @param context
     *            context to use for the view
     * @param value
     *            the string to parse
     * @throws PrivacySettingValueException
     *             if the privacy setting rejected the value
     */
    public void setViewValue(Context context, String value) throws PrivacySettingValueException;
    
    
    /**
     * Converts a view value to its string representation.
     * 
     * @param value
     *            the value from the view
     * @return the string representation of this value
     */
    public String convertViewValue(Object value);
    
}
