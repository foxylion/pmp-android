package de.unistuttgart.ipvs.pmp.model.context.location;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.model.context.IContextView;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidConditionException;

/**
 * View component for the {@link LocationContext}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class LocationContextView extends LinearLayout implements IContextView {
    
    /**
     * Value currently in the view
     */
    private LocationContextCondition value;
    
    
    public LocationContextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }
    
    
    public LocationContextView(Context context) {
        super(context);
        setup(context);
    }
    
    
    private void setup(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        
        inflate(context, R.layout.contexts_time_view, this);
        
        // set fields
        addListeners();
    }
    
    
    private void addListeners() {
    }
    
    
    @Override
    public View asView() {
        return this;
    }
    
    
    @Override
    public String getViewCondition() {
        
        return this.value.toString();
    }
    
    
    @Override
    public void setViewCondition(String condition) throws InvalidConditionException {
        this.value = LocationContextCondition.parse(condition);
        
    }
}
