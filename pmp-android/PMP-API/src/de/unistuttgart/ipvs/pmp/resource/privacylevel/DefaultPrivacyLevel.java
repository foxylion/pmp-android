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
public abstract class DefaultPrivacyLevel<T extends Comparable<T>> extends
	ComparablePrivacyLevel<T> {

    /**
     * Creates a {@link DefaultPrivacyLevel} using the {@link Comparable}
     * implementation of T.
     */
    public DefaultPrivacyLevel() {
	super();
    }

    /**
     * Creates a {@link DefaultPrivacyLevel} using the {@link Comparator}
     * implementation for T.
     * 
     * @param comparator
     *            Comparator to represent the "permit more or equal" partial
     *            order.
     */
    public DefaultPrivacyLevel(Comparator<T> comparator) {
	super(comparator);
    }

    @Override
    public String getHumanReadableValue(String locale, String value)
	    throws PrivacyLevelValueException {
	return parseValue(value).toString();
    }

}
