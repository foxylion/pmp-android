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
