package de.unistuttgart.ipvs.pmp.resource.privacysetting.library;

/**
 * Interface for a converter for a type from string and to string.
 * 
 * @author Tobias Kuhn
 * 
 */
public interface IStringConverter<T> {
    
    public T valueOf(String string);
    
    
    public String toString(T value);
    
}
