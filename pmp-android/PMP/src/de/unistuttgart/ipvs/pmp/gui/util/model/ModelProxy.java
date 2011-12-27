package de.unistuttgart.ipvs.pmp.gui.util.model;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.gui.util.model.mockup.MockupModel;
import de.unistuttgart.ipvs.pmp.model.IModel;
import de.unistuttgart.ipvs.pmp.model.Model;
import de.unistuttgart.ipvs.pmp.model.ipc.IPCProvider;

/**
 * Model proxy to easily switch between the real model and a mockup via ({@link ModelProxy#set(boolean, Context)}.
 * You can get the active model by {@link ModelProxy#get()}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class ModelProxy {
    
    protected static final IModel real = Model.getInstance();
    protected static final IModel mockup = MockupModel.instance;
    
    private static boolean isMockup = false;
    private static IModel instance = real;
    
    
    /**
     * 
     * @return the model set in the model proxy.
     */
    public static IModel get() {
        return instance;
    }
    
    
    /**
     * 
     * @param toMockup
     *            true for mockup, false for real
     * @param context
     */
    public static void set(boolean toMockup, Context activityContext) {
        isMockup = toMockup;
        if (toMockup) {
            IPCProvider.getInstance().startUpdate();
            instance = mockup;
            MockupControl.init(activityContext);
        } else {
            IPCProvider.getInstance().endUpdate();
            instance = real;
        }
    }
    
    
    public static boolean isMockup() {
        return isMockup;
    }
    
    
    private ModelProxy() {
    }
    
}
