package de.unistuttgart.ipvs.pmp.resource.privacysetting;

import android.view.View;

/**
 * Defines the displaying view for an {@link AbstractPrivacySetting}.
 * 
 * @author Tobias Kuhn
 * 
 * @param <T>
 *            the type of the value
 */
public interface IPrivacySettingView<T> {
    
    /**
     * 
     * @return the actual {@link View} to display in Android
     */
    public View asView();
    
    
    /**
     * 
     * @return the currently displayed value as a string readable by its {@link AbstractPrivacySetting}.
     */
    public String getViewValue();
    
    
    /**
     * 
     * @return the currently displayed value as an actual object, if it used in a container privacy setting.
     */
    public T getViewValueObject();
    
    
    /**
     * 
     * @param value
     *            the value to change the display to
     * @throws PrivacySettingValueException
     *             if the value was not supported by this {@link AbstractPrivacySetting}.
     */
    public void setViewValue(T value) throws PrivacySettingValueException;
    
}
