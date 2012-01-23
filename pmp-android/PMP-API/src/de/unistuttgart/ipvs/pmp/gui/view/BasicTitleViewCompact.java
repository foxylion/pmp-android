package de.unistuttgart.ipvs.pmp.gui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.AttributeSetUtil;

/**
 * Compact version of the Basic Title for layouts.
 * 
 * @author Jakob Jarosch
 */
public class BasicTitleViewCompact extends BasicTitleView {
    
    public BasicTitleViewCompact(Context context) {
        super(context);
    }
    
    
    public BasicTitleViewCompact(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        this.borderColor = new AttributeSetUtil(context, attrs).getColor(AttributeSetUtil.ViewBasicTitle_borderColor,
                Color.parseColor("#777777"));
    }
    
    
    @Override
    protected void createLayout() {
        /* load the xml-layout. */
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(layoutInflater.inflate(R.layout.view_basictitle_compact, null));
    }
}
