package de.unistuttgart.ipvs.pmp.gui.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * For my fellow team members a {@link Button} that can handle clicks even when disabled. To react on such clicks use
 * {@link AlwaysClickableButton#setDisabledClickListener(OnClickListener)}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class AlwaysClickableButton extends Button {
    
    /**
     * Click listener for handling clicks when this button is disabled.
     */
    protected OnClickListener disabledClickListener;
    
    
    /**
     * @see {@link Button#Button(Context)}
     */
    public AlwaysClickableButton(Context context) {
        super(context);
    }
    
    
    /**
     * @see {@link Button#Button(Context, AttributeSet)}
     */
    public AlwaysClickableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    
    /**
     * @see {@link Button#Button(Context, AttributeSet, int)}
     */
    public AlwaysClickableButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    
    public OnClickListener getDisabledClickListener() {
        return this.disabledClickListener;
    }
    
    
    public void setDisabledOnClickListener(OnClickListener disabledClickListener) {
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
