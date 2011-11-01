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

import java.util.Comparator;

/**
 * {@link ComparablePrivacyLevel} that uses the basic methods of the type T.
 * 
 * @author Tobias Kuhn
 * 
 * @param <T>
 *            the type that is stored in this {@link DefaultPrivacyLevel}.
 */
public abstract class DefaultPrivacyLevel<T extends Comparable<T>> extends ComparablePrivacyLevel<T> {
    
    /**
     * Creates a {@link DefaultPrivacyLevel} using the {@link Comparable} implementation of T.
     */
    public DefaultPrivacyLevel() {
        super();
    }
    
    
    /**
     * Creates a {@link DefaultPrivacyLevel} using the {@link Comparator} implementation for T.
     * 
     * @param comparator
     *            Comparator to represent the "permit more or equal" partial order.
     */
    public DefaultPrivacyLevel(Comparator<T> comparator) {
        super(comparator);
    }
    
    
    @Override
    public String getHumanReadableValue(String locale, String value) throws PrivacyLevelValueException {
        return parseValue(value).toString();
    }
    
}
