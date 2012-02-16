package de.unistuttgart.ipvs.pmp.resource.privacysetting;

/**
 * 
 * @author Tobias Kuhn
 * 
 * @see IStringConverter
 * @param <T>
 */
public interface IStringConverterWithExceptions<T> {
    
    public T valueOf(String string) throws PrivacySettingValueException;
    
    
    public String toString(T value) throws PrivacySettingValueException;
}
