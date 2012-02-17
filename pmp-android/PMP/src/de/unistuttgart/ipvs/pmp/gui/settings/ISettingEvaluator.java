package de.unistuttgart.ipvs.pmp.gui.settings;

/**
 * Interface to define ways to get and set values of a {@link SettingAbstract}.
 * 
 * @author Tobias Kuhn
 * 
 * @param <T>
 *            the type to be stored
 */
public interface ISettingEvaluator<T> {
    
    /**
     * 
     * @return the value currently stored in the setting
     */
    public T getValue();
    
    
    /**
     * 
     * @param newValue
     *            the value to set the setting to
     */
    public void setValue(T newValue);
}
