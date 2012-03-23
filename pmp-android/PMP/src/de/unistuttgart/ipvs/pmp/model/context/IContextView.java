package de.unistuttgart.ipvs.pmp.model.context;

import android.view.View;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidConditionException;

/**
 * Defines the displaying view for an {@link IContext}.
 * 
 * @author Tobias Kuhn
 * 
 */
public interface IContextView {
    
    /**
     * 
     * @return the actual {@link View} to display in Android
     */
    public View asView();
    
    
    /**
     * 
     * @return the currently displayed value as a string
     */
    public String getViewCondition();
    
    
    /**
     * 
     * @param condition
     *            the value to change the display to
     * @throws InvalidConditionException
     *             if the value was not supported by this {@link IContext}.
     */
    public void setViewCondition(String condition) throws InvalidConditionException;
    
    
    /**
     * 
     * @return a default condition string that contains the initial state of the {@link IContextView}
     */
    public String getDefaultCondition();
    
}
