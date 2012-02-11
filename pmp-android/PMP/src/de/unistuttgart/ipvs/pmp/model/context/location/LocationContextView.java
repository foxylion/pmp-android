package de.unistuttgart.ipvs.pmp.model.context.location;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.model.context.IContextView;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidConditionException;
import de.unistuttgart.ipvs.pmp.util.MapsAPIKeyAsset;

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
    
    /**
     * The {@link MapView}
     */
    private MapView map;
    
    
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
        
        String mapKey = MapsAPIKeyAsset.getKey(context);
        if (mapKey == null) {
            // if not existing, I'm taking you down with me!
            throw new IllegalAccessError();
        }
        
        this.map = new MapView(context, mapKey);
        addView(this.map);
        inflate(context, R.layout.contexts_location_view, this);
        
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
