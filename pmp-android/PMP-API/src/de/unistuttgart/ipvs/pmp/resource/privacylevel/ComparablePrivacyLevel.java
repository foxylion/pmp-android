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
 * <p>
 * A {@link PrivacyLevel} which has the "permits more or equal" partial order built into the generic type which must
 * extend Comparable.
 * </p>
 * <p>
 * If the present {@link Comparable} implementation does not represent the "permits more or equal" it is possible to
 * supply an optional {@link Comparator} which represents the "permits more or equal" partial order.
 * </p>
 * 
 * 
 * @author Tobias Kuhn
 * 
 * @param <T>
 *            the type that is stored in this {@link ComparablePrivacyLevel}.
 */
public abstract class ComparablePrivacyLevel<T extends Comparable<T>> extends PrivacyLevel<T> {
    
    /**
     * Additional comparator, if necessary
     */
    private Comparator<T> comparator;
    
    
    /**
     * Creates a {@link ComparablePrivacyLevel} using the {@link Comparable} implementation of T.
     */
    public ComparablePrivacyLevel() {
        this.comparator = null;
    }
    
    
    /**
     * Creates a {@link ComparablePrivacyLevel} using the {@link Comparator} implementation for T.
     * 
     * @param comparator
     *            Comparator to represent the "permit more or equal" partial order.
     */
    public ComparablePrivacyLevel(Comparator<T> comparator) {
        this.comparator = comparator;
    }
    
    
    @Override
    public boolean permits(T value, T reference) {
        if (this.comparator == null) {
            return value.compareTo(reference) >= 0;
        } else {
            return this.comparator.compare(value, reference) >= 0;
        }
    }
    
}
