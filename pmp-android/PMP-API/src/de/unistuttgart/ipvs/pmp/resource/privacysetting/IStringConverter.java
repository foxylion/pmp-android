package de.unistuttgart.ipvs.pmp.resource.privacysetting;

/**
 * Interface for a converter for a type from string and to string.
 * 
 * @author Tobias Kuhn
 * @param <T>
 *            the type
 * 
 */
public interface IStringConverter<T> {
    
    public T valueOf(String string);
    
    
    public String toString(T value);
    
}
