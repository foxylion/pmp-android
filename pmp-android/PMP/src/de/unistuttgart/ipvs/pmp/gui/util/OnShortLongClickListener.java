package de.unistuttgart.ipvs.pmp.gui.util;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

/**
 * An abstract implementation of a on click listener which can act as {@link OnClickListener} and as a
 * {@link OnLongClickListener} at the same time.
 * 
 * @author Jakob Jarosch
 */
public abstract class OnShortLongClickListener implements OnClickListener, OnLongClickListener {
    
    /**
     * Auto registers the {@link OnShortLongClickListener} as the {@link OnClickListener} and
     * {@link OnLongClickListener} of the given {@link View}.
     * 
     * @param view
     */
    public void autoRegister(View view) {
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
    }
    
    
    @Override
    public final boolean onLongClick(View v) {
        onClick(v);
        
        return true;
    }
    
}
