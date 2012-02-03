package de.unistuttgart.ipvs.pmp.model.context;

import android.content.Context;
import android.graphics.drawable.Drawable;
import de.unistuttgart.ipvs.pmp.service.PMPService;

/**
 * Interface to describe a context that can switch its activity state based on its surrounding conditions (hence the
 * term "Context"). Has nothing to do with the Android {@link Context}.
 * 
 * @author Tobias Kuhn
 * 
 */
public interface IContext {
    
    /**
     * @return the localized name of the context
     */
    public String getName();
    
    
    /**
     * @return the localized description of the context
     */
    public String getDescription();
    
    
    /**
     * @return the icon of the context
     */
    public Drawable getIcon();
    
    
    /**
     * 
     * @param context
     *            the {@link Context} to use for this {@link IContextView} (don't confuse these!)
     * @return the {@link IContextView} for this {@link IContext}
     */
    public IContextView getView(Context context);
    
    
    /**
     * The main state update called by the {@link PMPService} in a different thread.
     * 
     * @return the time in {@link System#currentTimeMillis()} domain to perform the next update on
     */
    public long update();
    
    
    /**
     * Validates the last state against condition.
     * 
     * @param condition
     *            the condition String for this to check
     * @return true, if and only if condition was true after the last state update
     */
    public boolean getLastState(String condition);
}
