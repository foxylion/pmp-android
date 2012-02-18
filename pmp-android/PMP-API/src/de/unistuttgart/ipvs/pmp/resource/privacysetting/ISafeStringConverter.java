package de.unistuttgart.ipvs.pmp.resource.privacysetting;

/**
 * Supposed to only throw {@link PrivacySettingValueException} exceptions and handle null values.
 * 
 * @author Tobias Kuhn
 * 
 * @see IStringConverter
 * @param <T>
 */
public interface ISafeStringConverter<T> {
    
    public T valueOf(String string) throws PrivacySettingValueException;
    
    
    public String toString(T value);
}
