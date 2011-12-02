/*
 * Copyright 2011 pmp-android development team
 * Project: SwitchesResourceGroup
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
package de.unistuttgart.ipvs.pmp.resource.privacylevel;

import android.view.View;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;

/**
 * An internal PrivacyLevel interface for a standard of accessing privacy levels. Note that this class saves the parsing
 * of the privacy levels. It's only needed implementing feature however is the {@link PrivacyLevel#parseValue(String)}
 * function.
 * 
 * @param <T>
 *            the type that is stored in this {@link PrivacyLevel}.
 */
public abstract class PrivacyLevel<T> {
    
    /**
     * The resource group that this resource is assigned to.
     */
    private ResourceGroup resourceGroup;
    
    /**
     * The identifier of this privacy setting in resource group.
     */
    private String identifier;
    
    
    /**
     * Assigns the resource group during registration.
     * 
     * <b>Do not call this method.</b>
     * 
     * @param resourceGroup
     */
    public final void assignResourceGroup(ResourceGroup resourceGroup, String identifier) {
        this.resourceGroup = resourceGroup;
        this.identifier = identifier;
    }
    
    
    /**
     * 
     * @return the associated {@link ResourceGroup}.
     */
    public final ResourceGroup getResourceGroup() {
        return this.resourceGroup;
    }
    
    
    /**
     * 
     * @return the human readable representation of the value for this privacy level for the given locale
     * @throws PrivacyLevelValueException
     *             if the supplied value does not match the format criteria.
     */
    public abstract String getHumanReadableValue(String value) throws PrivacyLevelValueException;
    
    
    /**
     * <p>
     * Should return true, if the tuple (value, reference) meets the "permits more or equal" partial order. That is
     * reference ">=" value where ">=" is your defined "permits more or equal" partial order (which needs not to match
     * the classical >= partial order on numbers).
     * </p>
     * <p>
     * Note that since this is a partial order it must meet the reflexivity, antisymmetry and transitivity property.
     * </p>
     * 
     * @param value
     *            the value that needs to permit more or equal than reference
     * @param reference
     *            the reference for the check
     * @return true, if value permits more or equal to reference
     */
    public abstract boolean permits(T value, T reference);
    
    
    /**
     * Convenience method for checking {@link PrivacyLevel#permits(Object, Object)} on String representations.
     * 
     * @see {@link PrivacyLevel#permits(Object, Object)}
     * @throws PrivacyLevelValueException
     *             if either reference or value do not meet the format criteria.
     */
    public boolean permits(String value, String reference) throws PrivacyLevelValueException {
        return permits(parseValue(value), parseValue(reference));
    }
    
    
    /**
     * Convenience method for checking {@link PrivacyLevel#permits(Object, Object)} for a specific app.
     * 
     * @param appIdentifier
     *            identifier of the app.
     * @param reference
     *            the reference instance of T for the check
     * @return true, if value permits more or equal to reference
     * @throws PrivacyLevelValueException
     */
    public boolean permits(String appIdentifier, T reference) throws PrivacyLevelValueException {
        T value = parseValue(getValue(appIdentifier));
        if (value == null) {
            return false;
        } else {
            return permits(value, reference);
        }
    }
    
    
    /**
     * Should create the representation of the string value for this {@link PrivacyLevel} based on a given String value.
     * If value is null, it should create an object that corresponds to "no privacy level value set".
     * 
     * @param value
     *            the value stored in PMP
     * @return an object corresponding to value, an object corresponding to "no privacy level value set", if value is
     *         null
     * @throws PrivacyLevelValueException
     *             if the supplied value does not match the format criteria.
     */
    public abstract T parseValue(String value) throws PrivacyLevelValueException;
    
    
    /**
     * Gets the value for this {@link PrivacyLevel} from PMP, if this {@link PrivacyLevel} is registered on a
     * {@link ResourceGroup}.
     * 
     * @param appIdentifier
     * @return the value as stored in PMP for this privacy setting for appIdentifier, or null, if none found or none set
     */
    public String getValue(String appIdentifier) {
        return getResourceGroup().getPMPPrivacyLevelValue(this.identifier, appIdentifier);
    }
    
    
    public abstract View getView();
    
    
    public abstract String getViewValue(View view);
    
    
    /**
     * 
     */
    public abstract void setViewValue(View view, T value) throws PrivacyLevelValueException;
    
    
    /**
     * Convenience method for {@link PrivacyLevel#setViewValue(View, Object)} if you only have a string.
     * 
     * @throws PrivacyLevelValueException
     */
    public void setViewValue(View view, String value) throws PrivacyLevelValueException {
        setViewValue(view, parseValue(value));
    }
}
