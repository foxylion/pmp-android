package de.unistuttgart.ipvs.pmp.gui.views;

import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

/**
 * Creates the LayoutParams for the Views and the Layouts in the Activities
 * 
 * @author Alexander Wassiljew
 * 
 */
public class LayoutParamsCreator {
    
    /**
     * Creates the LayoutParams with - width: FILL_PARENT - height: FILL_PARENT
     * 
     * @return LayoutParams
     */
    public static LayoutParams createFPFP() {
        return new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    }
    
    
    /**
     * Creates the LayoutParams with - width: FILL_PARENT - height: WRAP_CONTENT
     * 
     * @return LayoutParams
     */
    public static LayoutParams createFPWC() {
        return new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
    }
    
    
    /**
     * Creates the LayoutParams with - width: WRAP_CONTENT - height: FILL_PARENT
     * 
     * @return LayoutParams
     */
    public static LayoutParams createWCFP() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
    }
    
    
    /**
     * Creates the LayoutParams with - width: WRAP_CONTENT - height: WRAP_CONTENT
     * 
     * @return LayoutParams
     */
    public static LayoutParams createWCWC() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }
    
    
    /**
     * Creates the LayoutParams with - width: FILL_PARENT - height: FILL_PARENT
     * 
     * @return LayoutParams
     */
    public static LayoutParams createFPFP(float weight) {
        return new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, weight);
    }
    
    
    /**
     * Creates the LayoutParams with - width: FILL_PARENT - height: WRAP_CONTENT
     * 
     * @return LayoutParams
     */
    public static LayoutParams createFPWC(float weight) {
        return new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, weight);
    }
    
    
    /**
     * Creates the LayoutParams with - width: WRAP_CONTENT - height: FILL_PARENT
     * 
     * 
     * @return LayoutParams
     */
    public static LayoutParams createWCFP(float weight) {
        return new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT, weight);
    }
    
    
    /**
     * Creates the LayoutParams with - width: WRAP_CONTENT - height: WRAP_CONTENT
     * 
     * @return LayoutParams
     */
    public static LayoutParams createWCWC(float weight) {
        return new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, weight);
    }
}
