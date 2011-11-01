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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;

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
     * The values that are stored in this privacy level for the apps.
     */
    private Map<String, T> values = new HashMap<String, T>();
    
    
    /**
     * 
     * @param locale
     *            the ISO-639 locale string available from {@link Locale#getLanguage()}
     * @return the name of this privacy level for the given locale
     */
    public abstract String getName(String locale);
    
    
    /**
     * 
     * @param locale
     *            the ISO-639 locale string available from {@link Locale#getLanguage()}
     * @return the description of this privacy level for the given locale
     */
    public abstract String getDescription(String locale);
    
    
    /**
     * 
     * @param locale
     *            the ISO-639 locale string available from {@link Locale#getLanguage()}
     * @return the human readable representation of the value for this privacy level for the given locale
     * @throws PrivacyLevelValueException
     *             if the supplied value does not match the format criteria.
     */
    public abstract String getHumanReadableValue(String locale, String value) throws PrivacyLevelValueException;
    
    
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
     */
    public boolean permits(String appIdentifier, T reference) {
        T value = this.values.get(appIdentifier);
        if (value == null) {
            return false;
        } else {
            return permits(value, reference);
        }
    }
    
    
    /**
     * Should create the representation of the string value for this {@link PrivacyLevel} based on a given String value.
     * 
     * @param value
     *            the value stored in PMP
     * @throws IllegalArgumentException
     *             if the supplied value does not match the format criteria.
     */
    public abstract T parseValue(String value) throws PrivacyLevelValueException;
    
    
    /**
     * Sets the values that are stored in this {@link PrivacyLevel} for the apps.
     * 
     * @param values
     *            Map from appIdentifier to actual value.
     * @throws PrivacyLevelValueException
     *             if any of the supplied values does not match the format criteria.
     */
    public void setValues(Map<String, String> values) throws PrivacyLevelValueException {
        this.values.clear();
        if (values != null) {
            for (Entry<String, String> entry : values.entrySet()) {
                this.values.put(entry.getKey(), parseValue(entry.getValue()));
            }
        }
    }
    
    
    /**
     * Gets the value for a specific app.
     * 
     * @param appIdentifier
     * @return
     */
    public final T getValue(String appIdentifier) {
        return this.values.get(appIdentifier);
    }
    
    
    /**
     * Should internally call an {@link Activity} which enables the user to change the oldValue to a new value.
     * 
     * (tk: I suppose the upper comment is wrong and the Activity should be called in ResourceGroup)
     * 
     * @param oldValue
     *            old value which should be initially displayed
     * @return Returns the new value which has been set.
     */
    public String changeValue(String oldValue) {
        return null;
    }
    
}
