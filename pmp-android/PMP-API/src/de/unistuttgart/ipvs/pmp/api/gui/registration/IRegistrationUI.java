package de.unistuttgart.ipvs.pmp.api.gui.registration;

import android.content.Context;
import android.view.View;

public interface IRegistrationUI {
    
    public Context getContext();
    
    
    public View findViewById(int resource);
    
    
    public void invokeEvent(final RegistrationEventTypes eventType, final Object... parameters);
    
    
    public void close();
    
}
