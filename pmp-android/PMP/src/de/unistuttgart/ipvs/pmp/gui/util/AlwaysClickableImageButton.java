package de.unistuttgart.ipvs.pmp.gui.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

/**
 * For my fellow team members a {@link Button} that can handle clicks even when disabled. To react on such clicks use
 * {@link AlwaysClickableImageButton#setDisabledClickListener(OnClickListener)}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class AlwaysClickableImageButton extends ImageButton {
    
    /**
     * Click listener for handling clicks when this button is disabled.
     */
    protected OnClickListener disabledClickListener;
    
    
    /**
     * @see {@link ImageButton#ImageButton(Context)}
     */
    public AlwaysClickableImageButton(Context context) {
        super(context);
    }
    
    
    /**
     * @see {@link ImageButton#ImageButton(Context, AttributeSet)}
     */
    public AlwaysClickableImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    
    /**
     * @see {@link ImageButton#ImageButton(Context, AttributeSet, int)}
     */
    public AlwaysClickableImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    
    public OnClickListener getDisabledClickListener() {
        return this.disabledClickListener;
    }
    
    
    public void setOnDisabledClickListener(OnClickListener disabledClickListener) {
        this.disabledClickListener = disabledClickListener;
    }
    
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean handled = false;
        if (!isEnabled() && (event.getAction() == MotionEvent.ACTION_UP) && (this.disabledClickListener != null)) {
            this.disabledClickListener.onClick(this);
            handled = true;
        } else {
            handled = super.onTouchEvent(event);
        }
        return handled;
    }
    
}
